package com.spoom.base.picker;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.spoom.base.picker.ui.PickerActivity;

/**
 * package com.spoom.base.picker
 *
 * @author spoomlan
 * @date 02/03/2018
 */

public final class PickerBuilder {
    private final Picker picker;
    private final PickerSpec pickerSpec;

    public PickerBuilder(Picker picker, int type) {
        this.picker = picker;
        pickerSpec = PickerSpec.getInstance().reset();
        pickerSpec.type = type;
    }

    /**
     * Maximum selectable count.
     *
     * @param maxSelect Maximum selectable count. Default is 1.
     * @return {@link PickerBuilder}
     */
    public PickerBuilder maxSelect(int maxSelect) {
        if (maxSelect < 1)
            throw new IllegalArgumentException("maxSelect must be bigger than or equal to one");
        pickerSpec.maxSelect = maxSelect;
        return this;
    }

    /**
     * Grid column in {@link PickerActivity}
     *
     * @param column grid column
     * @return {@link PickerBuilder}
     */
    public PickerBuilder gridColumnCount(int column) {
        if (column < 1 || column > 6) {
            throw new IllegalArgumentException(("grid column error"));
        }
        pickerSpec.gridColumnCount = column;
        return this;
    }

    /**
     * Image thumbnail scale in {@link PickerActivity}
     *
     * @param scale image thumbnail scale
     * @return {@link PickerBuilder}
     */
    public PickerBuilder thumbscale(float scale) {
        if (scale <= 0f || scale > 1f)
            throw new IllegalArgumentException("Thumbnail scale must be between (0.0, 1.0]");
        pickerSpec.thumbScale = scale;
        return this;
    }

    /**
     * Start to select media and wait for result.
     *
     * @param requestCode Identity of the request Activity or Fragment.
     */
    public void forResult(int requestCode) {
        Activity activity = picker.getActivity();
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, PickerActivity.class);

        Fragment fragment = picker.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }
}
