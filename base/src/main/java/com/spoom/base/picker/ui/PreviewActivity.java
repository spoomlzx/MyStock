package com.spoom.base.picker.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.spoom.base.R;
import com.spoom.base.picker.PickerSpec;
import com.spoom.base.picker.adapter.ImagePagerAdapter;
import com.spoom.base.picker.entry.Image;
import com.spoom.base.picker.widget.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

import static android.animation.ObjectAnimator.ofFloat;

/**
 * package com.spoom.base.picker.ui
 *
 * @author spoomlan
 * @date 03/03/2018
 */

public class PreviewActivity extends AppCompatActivity {
    public static final int RESULT_CODE = 0X00000012;
    public static final String IS_CONFIRM = "is_confirm";
    private static final String POSITION = "position";

    private PickerSpec spec;
    private boolean isShowBar = true;
    private boolean isConfirm = false;
    private List<Image> selectedImages = new ArrayList<>();
    private Image curImage;

    private PhotoViewPager vpImage;
    private TextView tvIndicator;
    private Button btnConfirm;
    private TextView tvSelect;
    private RelativeLayout rlTopBar;
    private RelativeLayout rlBottomBar;

    public static void openActivity(Activity activity, int position) {
        Intent intent = new Intent(activity, PreviewActivity.class);
        intent.putExtra(POSITION, position);
        activity.startActivityForResult(intent, RESULT_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        setStatusBarVisible(true);

        spec = PickerSpec.getInstance();
        this.selectedImages.clear();
        this.selectedImages.addAll(spec.selectedImages);

        initView();
        initListener();
        initViewPager();

        tvIndicator.setText(1 + "/" + spec.images.size());
        changeSelect(spec.images.get(0));
        Intent intent = getIntent();
        int pos = intent.getIntExtra(POSITION, 0);
        vpImage.setCurrentItem(pos);
    }

    private void initView() {
        vpImage = findViewById(R.id.vp_image);
        tvIndicator = findViewById(R.id.tv_indicator);
        btnConfirm = findViewById(R.id.btn_confirm);
        tvSelect = findViewById(R.id.tv_select);
        rlTopBar = findViewById(R.id.rl_top_bar);
        rlBottomBar = findViewById(R.id.rl_bottom_bar);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rlTopBar.getLayoutParams();
        lp.topMargin = getStatusBarHeight(this);
        rlTopBar.setLayoutParams(lp);
    }

    private void initListener() {
        findViewById(R.id.ib_back).setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> {
            isConfirm = true;
            if (selectedImages.size() == 0) {
                selectedImages.add(curImage);
            }
            finish();
        });
        tvSelect.setOnClickListener(v -> clickSelect());
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, spec.images);
        vpImage.setAdapter(adapter);
        adapter.setItemClickListener((position, image) -> {
            if (isShowBar) {
                hideBar();
            } else {
                showBar();
            }
        });
        vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvIndicator.setText((position + 1) + "/" + spec.images.size());
                changeSelect(spec.images.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setStatusBarVisible(boolean show) {
        if (show) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    /**
     * 显示头部和尾部栏
     */
    private void showBar() {
        isShowBar = true;
        setStatusBarVisible(true);
        //添加延时，保证StatusBar完全显示后再进行动画。
        rlTopBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (rlTopBar != null) {
                    ObjectAnimator animator = ofFloat(rlTopBar, "translationY",
                            rlTopBar.getTranslationY(), 0).setDuration(300);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            if (rlTopBar != null) {
                                rlTopBar.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    animator.start();
                    ofFloat(rlBottomBar, "translationY", rlBottomBar.getTranslationY(), 0)
                            .setDuration(300).start();
                }
            }
        }, 100);
    }

    /**
     * 隐藏头部和尾部栏
     */
    private void hideBar() {
        isShowBar = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(rlTopBar, "translationY",
                0, -rlTopBar.getHeight()).setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (rlTopBar != null) {
                    rlTopBar.setVisibility(View.GONE);
                    //添加延时，保证rlTopBar完全隐藏后再隐藏StatusBar。
                    rlTopBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setStatusBarVisible(false);
                        }
                    }, 5);
                }
            }
        });
        animator.start();
        ofFloat(rlBottomBar, "translationY", 0, rlBottomBar.getHeight())
                .setDuration(300).start();
    }

    private void clickSelect() {
        int position = vpImage.getCurrentItem();
        if (spec.images != null && spec.images.size() > position) {
            Image image = spec.images.get(position);
            if (selectedImages.contains(image)) {
                selectedImages.remove(image);
            } else if (spec.maxSelect == 1) {
                selectedImages.clear();
                selectedImages.add(image);
            } else if (spec.maxSelect <= 0 || selectedImages.size() < spec.maxSelect) {
                selectedImages.add(image);
            }
            changeSelect(image);
        }
    }

    private void changeSelect(Image image) {
        int resId = selectedImages.contains(image) ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_check_box_outline_blank_black_24dp;
        tvSelect.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        setSelectImageCount(selectedImages.size());
        curImage = image;
    }

    private void setSelectImageCount(int count) {
        if (count == 0) {
            btnConfirm.setText("发送");
        } else {
            btnConfirm.setEnabled(true);
            if (spec.maxSelect == 1) {
                btnConfirm.setText("发送");
            } else if (spec.maxSelect > 0) {
                btnConfirm.setText("发送(" + count + "/" + spec.maxSelect + ")");
            } else {
                btnConfirm.setText("发送(" + count + ")");
            }
        }
    }

    /**
     * Activity finish 时，把是否是确认发送回传给{@link PickerActivity}
     */
    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(IS_CONFIRM, isConfirm);
        setResult(RESULT_CODE, intent);
        spec.selectedImages.clear();
        spec.selectedImages.addAll(this.selectedImages);
        super.finish();
    }
}
