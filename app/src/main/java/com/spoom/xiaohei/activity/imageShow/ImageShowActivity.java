package com.spoom.xiaohei.activity.imageShow;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.spoom.base.utils.SizeUtil;
import com.spoom.xiaohei.R;

import static com.spoom.base.utils.ImageUtil.setBitmap;

/**
 * package com.spoom.xiaohei.activity.imageShow
 *
 * @author spoomlan
 * @date 11/03/2018
 */

public class ImageShowActivity extends AppCompatActivity {
    private PhotoView pvImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        pvImage = findViewById(R.id.pv_image);
        String localPath = getIntent().getStringExtra("localPath");
        Glide.with(this).asBitmap()
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(localPath).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                int bw = resource.getWidth();
                int bh = resource.getHeight();
                if (bw > 8192 || bh > 8192) {
                    Bitmap bitmap = SizeUtil.zoomBitmap(resource, 8192, 8192);
                    setBitmap(pvImage, bitmap);
                } else {
                    setBitmap(pvImage, resource);
                }
            }
        });
    }
}
