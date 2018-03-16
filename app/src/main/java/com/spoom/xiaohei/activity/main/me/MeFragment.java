package com.spoom.xiaohei.activity.main.me;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.spoom.base.widget.ImagePanel;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.XiaoheiApp;
import com.spoom.xiaohei.manager.LocalUserManager;

/**
 * package com.lan.ichat.activity.main.profile
 *
 * @author lanzongxiao
 * @date 03/12/2017
 */

public class MeFragment extends Fragment {
    private ImageView ivAvatar;
    private TextView tvUsername, tvNickname;

    private ImagePanel ipCard;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvUsername = view.findViewById(R.id.tv_username);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_avatar);
        // TODO: 11/03/2018 缓存图片，手动保存到手机中，和contact部分一样的逻辑
        Glide.with(this)
                .load(LocalUserManager.getInstance().getAvatar())
                .apply(options)
                .into(ivAvatar);
        tvUsername.setText(String.format(getString(R.string.username), LocalUserManager.getInstance().getUsername()));
        tvNickname.setText(LocalUserManager.getInstance().getNickname());

        ipCard = view.findViewById(R.id.ip_card);
        Dialog dialog = XiaoheiApp.getInstance().createLoadingDialog(getContext(), "傻逼");
        ipCard.setOnClickListener(v -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            } else {
                dialog.show();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
