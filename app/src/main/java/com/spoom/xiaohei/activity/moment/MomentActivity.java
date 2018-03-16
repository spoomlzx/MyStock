package com.spoom.xiaohei.activity.moment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import com.spoom.base.utils.log.Logger;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.activity.BaseActivity;

/**
 * package com.lan.ichat.activity.moment
 *
 * @author spoomlan
 * @date 17/01/2018
 */

public class MomentActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initTitleBar();
        setTitle("朋友圈");
        showRightButton(R.drawable.icon_take_photo, v -> Logger.d("take photo"));
        MomentFragment momentFragment = (MomentFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (momentFragment == null) {
            momentFragment = new MomentFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, momentFragment);
            transaction.commit();
        }
    }
}
