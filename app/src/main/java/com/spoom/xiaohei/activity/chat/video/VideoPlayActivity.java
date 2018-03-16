package com.spoom.xiaohei.activity.chat.video;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import com.spoom.xiaohei.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * package com.lan.ichat.activity.chat.video
 *
 * @author spoomlan
 * @date 21/12/2017
 */

public class VideoPlayActivity extends AppCompatActivity {
    private JZVideoPlayerStandard myVideoView;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉头部title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_play);

        //        String uri = getIntent().getStringExtra(MediaRecorderActivity.VIDEO_URI);
        //        String shot = getIntent().getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
        //        initView(uri, shot);
        String uri = Environment.getExternalStorageDirectory() + "/Movies/aa.mp4";
        File file = new File(Environment.getExternalStorageDirectory() + "/Movies/" + System.currentTimeMillis() + ".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            Bitmap thumbBitmap = createVideoThumbnail(uri, 1L);
            if (thumbBitmap != null) {
                thumbBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initView(uri, file.getPath());
    }

    private void initView(String uri, String shot) {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        myVideoView = findViewById(R.id.vv_play);
        /*
        所有路径使用本地uri，如果传入的消息内不包含localPath，则从服务器加载，保存到本地后进行播放，
        并将localPath保存到message中
         */
        myVideoView.setUp(uri, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        myVideoView.thumbImageView.setImageURI(Uri.parse(shot));
        myVideoView.backButton.setVisibility(View.GONE);
        myVideoView.startButton.performClick();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.closeButton:
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 为一个视频生成缩略图
     *
     * @param filePath 视频文件路径
     * @param time     缩略图帧所在的时间点
     * @return bitmap
     */
    public static Bitmap createVideoThumbnail(String filePath, long time) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(time);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (bitmap == null) {
            return null;
        }
        return bitmap;
    }
}
