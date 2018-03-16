package com.spoom.xiaohei.activity.main.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.activity.chat.ChatActivity;
import com.spoom.xiaohei.activity.chat.video.VideoPlayActivity;

/**
 * package com.lan.ichat.activity.main.detail
 *
 * @author spoomlan
 * @date 09/01/2018
 */

public class UserDetailFragment extends Fragment implements UserDetailContract.View, View.OnClickListener {
    private UserDetailContract.Presenter presenter;
    private ImageView ivAvatar, ivGender;
    private ImageView ivHideHis, ivHideMy, ivStar;
    private TextView tvName, tvUsername, tvNickname, tvSetRemark, tvTelephone, tvRegion;
    private Button btnSendMessage, btnAddFriend;
    private RelativeLayout rlSetRemark;

    private String toUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initView(view);
        showDetail(presenter.getFriend());
        setListener();
        return view;
    }

    private void showDetail(NimUserInfo friend) {
        toUsername = friend.getAccount();
        String avatar = friend.getAvatar();
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_avatar);
        Glide.with(this)
                .load(avatar)
                .apply(options)
                .into(ivAvatar);
        String name = friend.getName();
        tvName.setText(name);
        if (GenderEnum.FEMALE == friend.getGenderEnum()) {
            ivGender.setImageResource(R.drawable.icon_female);
        } else {
            ivGender.setImageResource(R.drawable.icon_male);
        }
        int hasStar = 1;
        if (0 == hasStar) {
            ivStar.setVisibility(View.GONE);
        } else {
            ivStar.setVisibility(View.VISIBLE);
        }
        int hideHisMM = 1;
        if (0 == hideHisMM) {
            ivHideHis.setVisibility(View.GONE);
        } else {
            ivHideHis.setVisibility(View.VISIBLE);
        }
        int hideMyMM = 1;
        if (0 == hideMyMM) {
            ivHideMy.setVisibility(View.GONE);
        } else {
            ivHideMy.setVisibility(View.VISIBLE);
        }
        tvUsername.setText(String.format(getString(R.string.username), friend.getAccount()));
        if (TextUtils.isEmpty(friend.getName())) {
            tvNickname.setVisibility(View.GONE);
        } else {
            tvSetRemark.setText("标签");
            tvNickname.setVisibility(View.VISIBLE);
            tvNickname.setText(String.format(getString(R.string.nickname), friend.getName()));
        }
        if (TextUtils.isEmpty(friend.getMobile())) {
            tvTelephone.setVisibility(View.GONE);
        } else {
            tvTelephone.setVisibility(View.VISIBLE);
            tvTelephone.setText(friend.getMobile());
        }
        tvRegion.setText("浙江 杭州");
        if (presenter.isMe()) {
            btnSendMessage.setVisibility(View.GONE);
            btnAddFriend.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        ivAvatar = view.findViewById(R.id.iv_avatar);
        ivGender = view.findViewById(R.id.iv_gender);
        ivHideHis = view.findViewById(R.id.iv_hide_his);
        ivHideMy = view.findViewById(R.id.iv_hide_my);
        ivStar = view.findViewById(R.id.iv_star);
        tvName = view.findViewById(R.id.tv_name);
        tvUsername = view.findViewById(R.id.tv_username);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvSetRemark = view.findViewById(R.id.tv_set_remark);
        tvTelephone = view.findViewById(R.id.tv_telephone);
        tvRegion = view.findViewById(R.id.tv_region);
        btnSendMessage = view.findViewById(R.id.btn_send_message);
        btnAddFriend = view.findViewById(R.id.btn_add_friend);
        rlSetRemark = view.findViewById(R.id.rl_set_remark);
    }

    private void setListener() {
        rlSetRemark.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);
    }

    @Override
    public void setPresenter(UserDetailContract.Presenter presenter) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_set_remark:
                startActivity(new Intent(getContext(), VideoPlayActivity.class));
                break;
            case R.id.btn_send_message:
                //NIMClient.getService(MsgService.class).setChattingAccount(conversation.getContactId(), conversation.getSessionType());
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("username", toUsername);
                intent.putExtra("chatType", SessionTypeEnum.P2P.getValue());
                startActivity(intent);
                break;
            default:
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void triggerStar() {
        if (ivStar.getVisibility() == View.VISIBLE) {
            ivStar.setVisibility(View.GONE);
        } else {
            ivStar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void triggerHideHis() {
        if (ivHideHis.getVisibility() == View.VISIBLE) {
            ivHideHis.setVisibility(View.GONE);
        } else {
            ivHideHis.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void triggerHideMy() {
        if (ivHideMy.getVisibility() == View.VISIBLE) {
            ivHideMy.setVisibility(View.GONE);
        } else {
            ivHideMy.setVisibility(View.VISIBLE);
        }
    }
}
