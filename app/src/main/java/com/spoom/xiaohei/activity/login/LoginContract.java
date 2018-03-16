package com.spoom.xiaohei.activity.login;

import android.content.Context;
import android.widget.TextView;
import com.spoom.xiaohei.activity.BasePresenter;
import com.spoom.xiaohei.activity.BaseView;

/**
 * package com.lan.ichat.activity.login
 *
 * @author spoomlan
 * @date 05/01/2018
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showDialog();

        void cancelDialog();

        String getUsername();

        String getPassword();

        void setButtonEnable();

        void setButtonDisable();

        String getCountryName();

        String getCountryCode();
    }

    interface Presenter extends BasePresenter {
        void requestServer(String username, String password, boolean isAuth);

        void chooseCountry(Context context, TextView tvCountryName, TextView tvCountryCode);

        void register();
    }
}
