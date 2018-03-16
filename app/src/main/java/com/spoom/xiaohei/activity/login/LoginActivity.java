package com.spoom.xiaohei.activity.login;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import com.spoom.base.widget.TitleBar;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.activity.BaseActivity;
import com.spoom.xiaohei.runtimepermissions.PermissionsManager;
import com.spoom.xiaohei.runtimepermissions.PermissionsResultAction;

/**
 * package com.spoom.xiaohei.activity.login
 *
 * @author spoomlan
 * @date 19/12/2017
 */

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initTitleBar();
        setTitle(R.string.login_by_mobile);
        // TODO: 05/01/2018 如果是登录状态下访问login界面进行账号切换，是否要退出当前用户
        setLeftButton(TitleBar.LeftButtonType.NONE);
        requestPermissions();
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.contentFrame, loginFragment);
            fragmentTransaction.commit();
        }
        new LoginPresenter(loginFragment);
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(String permission) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
