package com.spoom.base.picker;

import com.spoom.base.picker.entry.Image;

import java.util.List;

/**
 * package com.spoom.base.picker
 *
 * @author spoomlan
 * @date 03/03/2018
 */

public final class PickerSpec {
    public static final int TYPE_ALL = 3;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_VIDEO = 1;

    public int type;
    public int maxSelect;
    public int gridColumnCount;
    public float thumbScale;

    public List<Image> images;
    public List<Image> selectedImages;

    private static PickerSpec instance = new PickerSpec();

    private PickerSpec() {
    }

    public static PickerSpec getInstance() {
        if (instance == null) {
            instance = new PickerSpec();
        }
        return instance;
    }

    public PickerSpec reset() {
        type = TYPE_ALL;
        maxSelect = 1;
        gridColumnCount = 4;
        thumbScale = 1;
        return this;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setSelectedImages(List<Image> selectedImages) {
        this.selectedImages = selectedImages;
    }
}
