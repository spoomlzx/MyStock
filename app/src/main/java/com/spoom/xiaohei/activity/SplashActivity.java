package com.spoom.xiaohei.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.XiaoheiApp;
import com.spoom.xiaohei.activity.login.LoginActivity;
import com.spoom.xiaohei.activity.main.MainActivity;

import java.util.List;

/**
 * package com.spoom.xiaohei.activity
 *
 * @author lanzongxiao
 * @date 19/11/2017
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(2000);
        RelativeLayout relativeLayout = findViewById(R.id.splash_layout);
        relativeLayout.startAnimation(animation);
        LoginInfo info = XiaoheiApp.getInstance().loginInfo();
        if (info != null) {
            NIMClient.getService(AuthService.class).login(info)
                    .setCallback(new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            List<String> friends = NIMClient.getService(FriendService.class).getFriendAccounts();
                            NIMClient.getService(UserService.class).fetchUserInfo(friends);
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailed(int code) {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
