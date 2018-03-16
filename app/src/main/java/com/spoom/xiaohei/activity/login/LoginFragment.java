package com.spoom.xiaohei.activity.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.XiaoheiApp;
import com.spoom.xiaohei.util.CommonUtils;

/**
 * package com.lan.ichat.activity.login
 *
 * @author spoomlan
 * @date 02/01/2018
 */

public class LoginFragment extends Fragment implements LoginContract.View {
    private LoginContract.Presenter presenter;
    private EditText etTelephone, etPassword;
    private TextView tvCountry, tvCountryCode, tvFindPassword;
    private Button btnLogin, btnReg;
    private RelativeLayout rlCountry;
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = XiaoheiApp.getInstance().createLoadingDialog(getActivity(), getString(R.string.logging));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        etTelephone = root.findViewById(R.id.et_telephone);
        etPassword = root.findViewById(R.id.et_password);
        tvCountry = root.findViewById(R.id.tv_country);
        tvCountryCode = root.findViewById(R.id.tv_country_code);
        tvFindPassword = root.findViewById(R.id.tv_find_password);
        btnLogin = root.findViewById(R.id.btn_login);
        btnReg = root.findViewById(R.id.btn_register);
        rlCountry = root.findViewById(R.id.rl_country);

        btnLogin.setOnClickListener(v -> {
            if (TextUtils.isEmpty(getUsername())) {
                CommonUtils.showToastShort(getActivity(), R.string.tel_can_not_null);
            }
            if (TextUtils.isEmpty(getPassword())) {
                CommonUtils.showToastShort(getActivity(), R.string.psw_can_not_null);
            }
            presenter.requestServer(getUsername(), getPassword(), false);
        });

        return root;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Context getBaseContext() {
        return getContext();
    }

    @Override
    public Activity getBaseActivity() {
        return getActivity();
    }

    @Override
    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    public void cancelDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public String getUsername() {
        return etTelephone.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString().trim();
    }

    @Override
    public void setButtonEnable() {
        btnLogin.setEnabled(true);
    }

    @Override
    public void setButtonDisable() {
        btnLogin.setEnabled(false);
    }

    @Override
    public String getCountryName() {
        return tvCountry.getText().toString().trim();
    }

    @Override
    public String getCountryCode() {
        return tvCountryCode.getText().toString().trim();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }
}
