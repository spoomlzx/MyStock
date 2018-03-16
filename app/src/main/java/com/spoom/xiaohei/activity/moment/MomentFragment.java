package com.spoom.xiaohei.activity.moment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.spoom.xiaohei.R;

/**
 * package com.lan.ichat.activity.moment
 *
 * @author spoomlan
 * @date 17/01/2018
 */

public class MomentFragment extends Fragment {
    private int mOffset = 0;
    private int mScrollY = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moment, container, false);
        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);

        final View parallax = view.findViewById(R.id.parallax);
        refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshlayout.finishLoadmore(2000));
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
            }
        });
        return view;
    }
}
