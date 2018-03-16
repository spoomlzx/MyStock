package com.spoom.xiaohei.activity.main.detail;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.spoom.xiaohei.manager.LocalUserManager;

/**
 * package com.lan.ichat.activity.main.detail
 *
 * @author spoomlan
 * @date 09/01/2018
 */

public class UserDetailPresenter implements UserDetailContract.Presenter {
    private UserDetailContract.View view;

    private NimUserInfo friend;

    public UserDetailPresenter(UserDetailContract.View view, NimUserInfo friend) {
        this.view = view;
        this.view.setPresenter(this);
        this.friend = friend;
    }

    @Override
    public void start() {

    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    @Override
    public boolean isMe() {
        return LocalUserManager.getInstance().getUsername().equals(friend.getAccount());
    }

    @Override
    public NimUserInfo getFriend() {
        return this.friend;
    }
}
