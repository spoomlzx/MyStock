package com.spoom.xiaohei.activity.main;

import android.content.Context;

/**
 * package com.lan.ichat.activity.main
 *
 * @author spoomlan
 * @date 24/12/2017
 */

public class MainPresenter {
    private MainView mainView;
    private Context context;

    public MainPresenter(MainView view) {
        this.mainView = view;
        view.setPresenter(this);
        this.context = view.getBaseActivity();
    }
}
