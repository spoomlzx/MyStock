package com.spoom.xiaohei.activity.login;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.spoom.base.utils.SizeUtil;
import com.spoom.base.utils.log.Logger;
import com.spoom.xiaohei.activity.main.MainActivity;
import com.spoom.xiaohei.manager.DatabaseManager;
import com.spoom.xiaohei.manager.LocalUserManager;

import java.util.List;

/**
 * package com.lan.ichat.activity.login
 *
 * @author spoomlan
 * @date 05/01/2018
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        SizeUtil.observeSoftKeyboard(view.getBaseActivity(), (softKeyboardHeight, visible, onGlobalLayoutListener) -> {
            if (visible && softKeyboardHeight > 0) {
                LocalUserManager.getInstance().setKeyboardHeight(softKeyboardHeight);
                view.getBaseActivity().getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
            }
        });
    }

    @Override
    public void requestServer(String username, String password, boolean isAuth) {
        view.showDialog();
        LoginInfo info = new LoginInfo(username, password);
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        LocalUserManager.getInstance().setUsername(username);
                        LocalUserManager.getInstance().setToken(password);
                        DatabaseManager.init();

                        List<String> friends = NIMClient.getService(FriendService.class).getFriendAccounts();
                        getUserInfo(username, friends);
                    }

                    @Override
                    public void onFailed(int code) {
                        Logger.e("error");
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
    }

    private void getUserInfo(String username, List<String> userList) {
        userList.add(username);
        NIMClient.getService(UserService.class).fetchUserInfo(userList)
                .setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {
                    @Override
                    public void onResult(int code, List<NimUserInfo> result, Throwable exception) {
                        if (200 == code) {
                            for (NimUserInfo user : result) {
                                if (username.equals(user.getAccount())) {
                                    LocalUserManager.getInstance().setNickname(user.getName());
                                    LocalUserManager.getInstance().setAvatar(user.getAvatar());
                                    LocalUserManager.getInstance().setGender(user.getGenderEnum().getValue());
                                    LocalUserManager.getInstance().setMotto(user.getSignature());
                                    LocalUserManager.getInstance().setTelephone(user.getMobile());
                                    view.cancelDialog();
                                    Intent intent = new Intent(view.getBaseContext(), MainActivity.class);
                                    view.getBaseContext().startActivity(intent);
                                    view.getBaseActivity().finish();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void chooseCountry(Context context, TextView tvCountryName, TextView tvCountryCode) {

    }

    @Override
    public void register() {

    }
}
