package com.spoom.base.picker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.spoom.base.picker.entry.Image;
import com.spoom.base.utils.SizeUtil;

import java.io.File;
import java.util.List;

import static com.spoom.base.utils.ImageUtil.setBitmap;

/**
 * package com.spoom.base.picker.adapter
 *
 * @author spoomlan
 * @date 11/03/2018
 */

public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<Image> imageList;
    private OnItemClickListener itemClickListener;

    public ImagePagerAdapter(Context context, List<Image> imgList) {
        this.context = context;
        this.imageList = imgList;
    }

    @Override
    public int getCount() {
        return imageList == null ? 0 : imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof PhotoView) {
            PhotoView view = (PhotoView) object;
            view.setImageDrawable(null);
            container.removeView(view);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final Image image = imageList.get(position);
        PhotoView photoView = new PhotoView(context);
        photoView.setAdjustViewBounds(true);
        container.addView(photoView);
        // 不能使用缓存，不然浏览大量图片时产生大量无用的缓存
        Glide.with(context).asBitmap()
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(new File(image.getPath())).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                int bw = resource.getWidth();
                int bh = resource.getHeight();
                if (bw > 8192 || bh > 8192) {
                    Bitmap bitmap = SizeUtil.zoomBitmap(resource, 8192, 8192);
                    setBitmap(photoView, bitmap);
                } else {
                    setBitmap(photoView, resource);
                }
            }
        });
        photoView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position, image);
            }
        });
        return photoView;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Image image);
    }
}
