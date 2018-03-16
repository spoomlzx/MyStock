package com.spoom.base.picker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.spoom.base.R;
import com.spoom.base.picker.PickerSpec;
import com.spoom.base.picker.entry.Image;

import java.io.File;
import java.util.ArrayList;

/**
 * package com.spoom.base.picker.adapter
 *
 * @author spoomlan
 * @date 03/03/2018
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Image> images;
    //保存选中的图片
    private ArrayList<Image> selectedImages = new ArrayList<>();
    private PickerSpec spec;

    private OnImageSelectListener imageSelectListener;
    private OnItemClickListener itemClickListener;

    public ImageAdapter(Context context, PickerSpec spec) {
        this.context = context;
        this.spec = spec;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.picker_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Image image = images.get(position);
        Glide.with(context)
                .load(new File(image.getPath()))
                .into(holder.ivImage);
        setItemSelect(holder, selectedImages.contains(image));
        holder.ivSelect.setOnClickListener(v -> {
            if (selectedImages.contains(image)) {
                //如果图片已经选中，就取消选中
                unSelectImage(image);
                setItemSelect(holder, false);
            } else if (spec.maxSelect == 1) {
                //如果是单选，就先清空已经选中的图片，再选中当前图片
                clearImageSelect();
                selectImage(image);
                setItemSelect(holder, true);
            } else if (spec.maxSelect <= 0 || selectedImages.size() < spec.maxSelect) {
                //如果不限制图片的选中数量，或者图片的选中数量
                // 还没有达到最大限制，就直接选中当前图片。
                selectImage(image);
                setItemSelect(holder, true);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.OnItemClick(image, holder.getAdapterPosition());
            }
        });
    }

    /**
     * 选中图片
     *
     * @param image
     */
    private void selectImage(Image image) {
        selectedImages.add(image);
        if (imageSelectListener != null) {
            imageSelectListener.OnImageSelect(image, true, selectedImages.size());
        }
    }

    /**
     * 取消选中图片
     *
     * @param image
     */
    private void unSelectImage(Image image) {
        selectedImages.remove(image);
        if (imageSelectListener != null) {
            imageSelectListener.OnImageSelect(image, false, selectedImages.size());
        }
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public ArrayList<Image> getData() {
        return images;
    }

    public void refresh(ArrayList<Image> data) {
        images = data;
        notifyDataSetChanged();
    }

    /**
     * 设置图片选中和未选中的效果
     */
    private void setItemSelect(ViewHolder holder, boolean isSelect) {
        if (isSelect) {
            holder.ivSelect.setImageResource(R.drawable.ic_check_box_black_24dp);
            holder.ivMasking.setAlpha(0.5f);
        } else {
            holder.ivSelect.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
            holder.ivMasking.setAlpha(0.2f);
        }
    }

    private void clearImageSelect() {
        if (images != null && selectedImages.size() == 1) {
            int index = images.indexOf(selectedImages.get(0));
            if (index != -1) {
                selectedImages.clear();
                notifyItemChanged(index);
            }
        }
    }

    public ArrayList<Image> getSelectImages() {
        return selectedImages;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivMasking;
        ImageView ivSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivMasking = itemView.findViewById(R.id.iv_masking);
            ivSelect = itemView.findViewById(R.id.iv_select);
        }
    }

    public void setImageSelectListener(OnImageSelectListener imageSelectListener) {
        this.imageSelectListener = imageSelectListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnImageSelectListener {
        void OnImageSelect(Image image, boolean isSelect, int selectCount);
    }

    public interface OnItemClickListener {
        void OnItemClick(Image image, int position);
    }
}
