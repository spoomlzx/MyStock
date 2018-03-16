package com.spoom.xiaohei.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.spoom.base.widget.TitleBar;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.XiaoheiApp;

/**
 * package com.spoom.xiaohei
 *
 * @author lanzongxiao
 * @date 03/12/2017
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private TitleBar titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XiaoheiApp.getInstance().saveActivity(this);
    }

    /**
     * init TitleBar,
     * should be called after {@link AppCompatActivity#setContentView(int)} and before using the TitleBar
     */
    protected void initTitleBar() {
        titleBar = findViewById(R.id.title_bar);
        titleBar.setLeftButtonClickListener(v -> finish());
    }

    @Override
    public void setTitle(int title) {
        titleBar.setTitle(title);
    }

    public void setTitle(String title) {
        titleBar.setTitle(title);
    }

    public void setLeftButton(int type) {
        titleBar.setLeftButton(type);
    }

    public void showSearchButton(View.OnClickListener listener) {
        titleBar.setSearchButtonClickListener(listener);
    }

    public void showRightButton(int res, View.OnClickListener onClickListener) {
        titleBar.setRightButton(res, onClickListener);
    }

    public void showRightTextButton(int res, View.OnClickListener onClickListener) {
        titleBar.setRightTextButton(res, onClickListener);
    }

    public void showRightTextButton(String res, View.OnClickListener onClickListener) {
        titleBar.setRightTextButton(res, onClickListener);
    }

    protected boolean isCompatible(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }
}
