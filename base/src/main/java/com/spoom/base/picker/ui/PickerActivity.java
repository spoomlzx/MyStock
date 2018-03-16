package com.spoom.base.picker.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.spoom.base.R;
import com.spoom.base.picker.PickerSpec;
import com.spoom.base.picker.adapter.FolderAdapter;
import com.spoom.base.picker.adapter.ImageAdapter;
import com.spoom.base.picker.entry.Folder;
import com.spoom.base.picker.entry.Image;
import com.spoom.base.picker.entry.LoadImage;
import com.spoom.base.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * package com.spoom.base.picker.ui
 *
 * @author spoomlan
 * @date 03/03/2018
 */

public class PickerActivity extends AppCompatActivity {
    public static final String EXTRA_RESULT_SELECTED_PATH = "extra_result_selected_path";
    private static final int PERMISSION_REQUEST_CODE = 0X00000011;
    private PickerSpec spec;

    private ImageAdapter imageAdapter;
    private GridLayoutManager layoutManager;
    private List<Folder> folders;
    private Folder recentFolder;
    private boolean goSettings = false;
    private boolean isOpenFolder;
    private boolean isShowTime;
    private boolean isInitFolder;

    private TextView tvTime, tvFolderName, tvPreview;
    private Button btnConfirm;
    private RecyclerView rvImage, rvFolder;
    private View masking;

    private Handler mHideHandler = new Handler();
    private Runnable mHide = this::hideTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        spec = PickerSpec.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        initView();
        initListener();
        initImageList();
        checkPermissionAndLoadImages();
        hideFolderList();
        setSelectImageCount(0);
    }

    private void initView() {
        tvTime = findViewById(R.id.tv_time);
        tvFolderName = findViewById(R.id.tv_folder_name);
        tvPreview = findViewById(R.id.tv_preview);
        btnConfirm = findViewById(R.id.btn_confirm);
        rvImage = findViewById(R.id.rv_image);
        rvFolder = findViewById(R.id.rv_folder);
        masking = findViewById(R.id.masking);
    }

    private void initListener() {
        findViewById(R.id.ib_back).setOnClickListener(v -> finish());

        tvPreview.setOnClickListener(v -> toPreviewActivity(imageAdapter.getSelectImages(), 0));

        btnConfirm.setOnClickListener(v -> confirm());

        findViewById(R.id.rl_folder).setOnClickListener(v -> {
            if (isInitFolder) {
                if (isOpenFolder) {
                    closeFolder();
                } else {
                    openFolder();
                }
            }
        });

        masking.setOnClickListener(v -> closeFolder());

        rvImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                changeTime();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                changeTime();
            }
        });
    }

    private void initImageList() {
        layoutManager = new GridLayoutManager(this, spec.gridColumnCount);
        rvImage.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(this, spec);
        rvImage.setAdapter(imageAdapter);

        if (folders != null && !folders.isEmpty()) {
            setFolder(folders.get(0));
        }

        imageAdapter.setImageSelectListener((image, isSelect, selectCount) -> setSelectImageCount(selectCount));
        imageAdapter.setItemClickListener((image, position) -> toPreviewActivity(imageAdapter.getData(), position));
    }

    /**
     * 初始化图片文件夹列表
     */
    private void initFolderList() {
        if (folders != null && !folders.isEmpty()) {
            isInitFolder = true;
            rvFolder.setLayoutManager(new LinearLayoutManager(PickerActivity.this));
            FolderAdapter adapter = new FolderAdapter(PickerActivity.this, folders);
            adapter.setOnFolderSelectListener(folder -> {
                setFolder(folder);
                closeFolder();
            });
            rvFolder.setAdapter(adapter);
        }
    }

    private void hideFolderList() {
        rvFolder.post(() -> {
            rvFolder.setTranslationY(rvFolder.getHeight());
            rvFolder.setVisibility(View.GONE);
        });
    }

    /**
     * 设置选中的文件夹，同时刷新图片列表
     *
     * @param folder
     */
    private void setFolder(Folder folder) {
        if (folder != null && imageAdapter != null && !folder.equals(recentFolder)) {
            this.recentFolder = folder;
            tvFolderName.setText(folder.getName());
            rvImage.scrollToPosition(0);
            imageAdapter.refresh(folder.getImages());
        }
    }

    private void setSelectImageCount(int count) {
        if (count == 0) {
            btnConfirm.setEnabled(false);
            tvPreview.setEnabled(false);
            btnConfirm.setText("确定");
            tvPreview.setText("预览");
        } else {
            btnConfirm.setEnabled(true);
            tvPreview.setEnabled(true);
            tvPreview.setText("预览(" + count + ")");
            if (spec.maxSelect == 1) {
                btnConfirm.setText("确定");
            } else if (spec.maxSelect > 0) {
                btnConfirm.setText("确定(" + count + "/" + spec.maxSelect + ")");
            } else {
                btnConfirm.setText("确定(" + count + ")");
            }
        }
    }

    /**
     * 检查权限并加载SD卡里的图片。
     */
    private void checkPermissionAndLoadImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，加载图片。
            loadImageForSDCard();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(PickerActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，加载图片。
                loadImageForSDCard();
            } else {
                //拒绝权限，弹出提示框。
                showExceptionDialog();
            }
        }
    }

    /**
     * 发生没有权限等异常时，显示一个提示dialog.
     */
    private void showExceptionDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("使用相册需要赋予访问存储的权限，请到“设置”>“应用”>“权限”中配置权限。")
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.cancel();
                    finish();
                }).setPositiveButton("确定", (dialog, which) -> {
                    dialog.cancel();
                    startAppSettings();
                    goSettings = true;
                }
        ).show();
    }

    /**
     * start application details setting
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void loadImageForSDCard() {
        LoadImage.loadImageFromSdcard(getBaseContext(), spec.type, folderList -> {
            folders = folderList;
            runOnUiThread(() -> {
                if (folders != null && !folders.isEmpty()) {
                    initFolderList();
                    setFolder(folders.get(0));
                }
            });
        });
    }

    private void toPreviewActivity(ArrayList<Image> images, int position) {
        spec.setImages(images);
        spec.setSelectedImages(imageAdapter.getSelectImages());
        if (images != null && !images.isEmpty()) {
            PreviewActivity.openActivity(this, position);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (goSettings) {
            goSettings = false;
            checkPermissionAndLoadImages();
        }
    }

    /**
     * 处理图片预览页返回的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PreviewActivity.RESULT_CODE) {
            if (data != null && data.getBooleanExtra(PreviewActivity.IS_CONFIRM, false)) {
                //如果用户在预览页点击了确定，就直接把用户选中的图片返回给用户。
                confirm();
            } else {
                //否则，就刷新当前页面。
                imageAdapter.notifyDataSetChanged();
                setSelectImageCount(imageAdapter.getSelectImages().size());
            }
        }
    }

    private void confirm() {
        if (imageAdapter == null) {
            return;
        }
        ArrayList<Image> selectImages = imageAdapter.getSelectImages();
        ArrayList<String> images = new ArrayList<>();
        for (Image image : selectImages) {
            images.add(image.getPath());
        }
        //点击确定，把选中的图片通过Intent传给上一个Activity。
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_RESULT_SELECTED_PATH, images);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 改变时间条显示的时间（显示图片列表中的第一个可见图片的时间）
     */
    private void changeTime() {
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < imageAdapter.getData().size()) {
            Image image = imageAdapter.getData().get(firstVisibleItemPosition);
            String time = DateUtil.getImageTime(image.getTime() * 1000);
            tvTime.setText(time);
            showTime();
            mHideHandler.removeCallbacks(mHide);
            mHideHandler.postDelayed(mHide, 1500);
        }
    }

    /**
     * 隐藏时间条
     */
    private void hideTime() {
        if (isShowTime) {
            ObjectAnimator.ofFloat(tvTime, "alpha", 1, 0).setDuration(300).start();
            isShowTime = false;
        }
    }

    /**
     * 显示时间条
     */
    private void showTime() {
        if (!isShowTime) {
            ObjectAnimator.ofFloat(tvTime, "alpha", 0, 1).setDuration(300).start();
            isShowTime = true;
        }
    }

    private void openFolder() {
        if (!isOpenFolder) {
            masking.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(rvFolder, "translationY",
                    rvFolder.getHeight(), 0).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    rvFolder.setVisibility(View.VISIBLE);
                }
            });
            animator.start();
            isOpenFolder = true;
        }
    }

    private void closeFolder() {
        if (isOpenFolder) {
            masking.setVisibility(View.GONE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(rvFolder, "translationY",
                    0, rvFolder.getHeight()).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rvFolder.setVisibility(View.GONE);
                }
            });
            animator.start();
            isOpenFolder = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && isOpenFolder) {
            closeFolder();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
