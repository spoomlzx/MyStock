package com.spoom.xiaohei.activity.main.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.spoom.base.widget.BasePopupWindow;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.activity.BaseActivity;
import com.spoom.xiaohei.activity.chat.video.VideoPlayActivity;

/**
 * package com.lan.ichat.activity.main.detail
 * intent中只包含userId，则从好友中查找，（并试图从网络上更新数据？）
 * intent中包含了name和avatar的信息，则直接加载信息显示
 *
 * @author spoomlan
 * @date 09/01/2018
 */

public class UserDetailActivity extends BaseActivity {
    private BasePopupWindow popupWindow;
    private LinearLayout llRoot;
    private LinearLayout setRemark, setStar, setMoment, sendCard, delete;
    private TextView tvSetStar;

    private NimUserInfo friend;
    private int hasStar;

    private UserDetailFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initTitleBar();
        setTitle(getString(R.string.user_detail));
        initView();
    }

    private void initView() {
        String username = getIntent().getStringExtra("username");
        if (!TextUtils.isEmpty(username)) {
            if (NIMClient.getService(FriendService.class).isMyFriend(username)) {
                this.friend = NIMClient.getService(UserService.class).getUserInfo(username);
                initPopupWindow();
                llRoot = findViewById(R.id.ll_root);
                showRightButton(R.drawable.svg_more_white_24dp, v -> {
                    popupWindow.showAtLocation(llRoot, Gravity.BOTTOM, 0, 0);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.6f;
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp);
                });
            } else {
                //finish();
                // 如果是自己，则没有菜单，如果是群好友，菜单应该不同，而不是没有菜单
                //return;
            }
            initFragment(this.friend);
        } else {
//            String userInfo = getIntent().getStringExtra("userinfo");
//            if (userInfo == null) {
//                finish();
//            } else {
//                Gson gson = new Gson();
//                this.friend = gson.fromJson(userInfo, Friend.class);
//                initFragment(this.friend);
//            }
        }

    }

    private void initFragment(NimUserInfo friend) {
        fragment = (UserDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = new UserDetailFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, fragment);
            transaction.commit();
        }
        new UserDetailPresenter(fragment, friend);
    }

    private void initPopupWindow() {
        popupWindow = new BasePopupWindow(this, R.layout.popup_contact_detail, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                setRemark = view.findViewById(R.id.ll_set_remark);
                setStar = view.findViewById(R.id.ll_set_star);
                setMoment = view.findViewById(R.id.ll_set_moment);
                sendCard = view.findViewById(R.id.ll_send_contact);
                delete = view.findViewById(R.id.ll_delete);
                tvSetStar = setStar.findViewById(R.id.tv_set_star);
                //hasStar = (friend.getType() >> 6) & 1;
                //if (1 == hasStar) {
                    tvSetStar.setText(getString(R.string.cancel_set_star));
                //}
            }

            @Override
            protected void initEvent() {
                setRemark.setOnClickListener(v -> startActivity(new Intent(UserDetailActivity.this, VideoPlayActivity.class)));
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                instance.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp);
                });
                instance.setAnimationStyle(R.style.animTranslate);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }
}
