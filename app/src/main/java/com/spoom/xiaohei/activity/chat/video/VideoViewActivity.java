package com.spoom.xiaohei.activity.chat.video;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.VideoView;
import com.spoom.xiaohei.R;

/**
 * package com.lan.ichat.activity.chat.video
 *
 * @author spoomlan
 * @date 23/12/2017
 */

public class VideoViewActivity extends AppCompatActivity {
    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        Button play = findViewById(R.id.play);
        videoView = findViewById(R.id.video_view);
        videoView.setVideoPath(Environment.getExternalStorageDirectory() + "/Movies/aa.mp4");
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.suspend();
        }
    }
}
