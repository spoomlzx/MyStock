package com.spoom.base.picker;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.spoom.base.picker.ui.PickerActivity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * package com.spoom.base.picker
 *
 * @author spoomlan
 * @date 02/03/2018
 */

public final class Picker {
    private final WeakReference<Activity> mContext;
    private final WeakReference<Fragment> mFragment;

    private Picker(Activity activity) {
        this(activity, null);
    }

    private Picker(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private Picker(Activity activity, Fragment fragment) {
        mContext = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    /**
     * Start Picker from activity
     * <p>
     * The activity's {@link Activity#onActivityResult(int, int, Intent)} will be called after user finish select
     * </p>
     *
     * @param activity activity context.
     * @return Picker instance.
     */
    public static Picker from(Activity activity) {
        return new Picker(activity);
    }

    /**
     * Start Picker from fragment
     * <p>
     * The fragment's {@link Fragment#onActivityResult(int, int, Intent)} will be called after user finish select
     * </p>
     *
     * @param fragment fragment.
     * @return Picker instance
     */
    public static Picker from(Fragment fragment) {
        return new Picker(fragment);
    }

    /**
     * Set pick type
     *
     * @param type type user can choose
     * @return {@link PickerBuilder} to build pick specifications.
     * @see PickerSpec#TYPE_ALL
     * @see PickerSpec#TYPE_IMAGE
     * @see PickerSpec#TYPE_VIDEO
     */
    public PickerBuilder choose(int type) {
        return new PickerBuilder(this, type);
    }

    /**
     * Get user selected media path list in the starting Activity or Fragment.
     *
     * @param data Intent passed by {@link Activity#onActivityResult(int, int, Intent)} or
     *             {@link Fragment#onActivityResult(int, int, Intent)}.
     * @return selected media path list.
     */
    public static List<String> getPathResult(Intent data) {
        if (data == null) {
            return null;
        }
        return data.getStringArrayListExtra(PickerActivity.EXTRA_RESULT_SELECTED_PATH);
    }

    @Nullable
    Activity getActivity() {
        return mContext.get();
    }

    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }
}
