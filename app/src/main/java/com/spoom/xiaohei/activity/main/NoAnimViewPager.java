package com.spoom.xiaohei.activity.main;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * package com.spoom.xiaohei.activity.main
 *
 * @author lanzongxiao
 * @date 03/12/2017
 */

public class NoAnimViewPager extends ViewPager {
    public NoAnimViewPager(Context context) {
        super(context);
    }

    public NoAnimViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 去除点击下方按钮切换界面时的滑动效果，例如在chat时，点击me，如果有滑动效果，则会中途出现contacts和discover
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
}
