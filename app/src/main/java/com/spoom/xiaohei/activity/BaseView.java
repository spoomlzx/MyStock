package com.spoom.xiaohei.activity;

import android.app.Activity;
import android.content.Context;

/**
 * package com.spoom.xiaohei.activity
 *
 * @author lanzongxiao
 * @date 03/12/2017
 */

public interface BaseView<T> {
    /**
     * get reference to presenter
     *
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * used to get Context in presenter
     *
     * @return Context
     */
    Context getBaseContext();

    /**
     * used to get Activity in presenter
     *
     * @return
     */
    Activity getBaseActivity();
}
