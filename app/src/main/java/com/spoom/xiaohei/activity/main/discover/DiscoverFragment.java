package com.spoom.xiaohei.activity.main.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.spoom.base.widget.ImagePanel;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.activity.moment.MomentActivity;

/**
 * package com.lan.ichat.activity.main.discover
 *
 * @author lanzongxiao
 * @date 03/12/2017
 */

public class DiscoverFragment extends Fragment {
    private ImagePanel ipMoment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, null);
        ipMoment = view.findViewById(R.id.ip_moment);
        ipMoment.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MomentActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
