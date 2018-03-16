package com.spoom.xiaohei.activity.main.detail;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.spoom.xiaohei.activity.BasePresenter;
import com.spoom.xiaohei.activity.BaseView;

/**
 * package com.lan.ichat.activity.main.detail
 *
 * @author spoomlan
 * @date 09/01/2018
 */

public interface UserDetailContract {
    interface View extends BaseView<Presenter> {
        void triggerStar();
        void triggerHideHis();
        void triggerHideMy();
    }

    interface Presenter extends BasePresenter {
        /**
         * destroy the view
         */
        void onDestroy();

        /**
         * check whether the user to shown is the app user
         *
         * @return
         */
        boolean isMe();

        NimUserInfo getFriend();
    }
}
