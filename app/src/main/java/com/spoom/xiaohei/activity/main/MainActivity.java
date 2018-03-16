package com.spoom.xiaohei.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.activity.BaseActivity;
import com.spoom.xiaohei.activity.main.contacts.ContactsFragment;
import com.spoom.xiaohei.activity.main.conversation.ConversationFragment;
import com.spoom.xiaohei.activity.main.discover.DiscoverFragment;
import com.spoom.xiaohei.activity.main.me.MeFragment;

/**
 * package com.lan.ichat.activity.main
 *
 * @author lanzongxiao
 * @date 03/12/2017
 */

public class MainActivity extends BaseActivity implements MainView {
    private MainPresenter mainPresenter;

    private TabLayout mTablayout;
    private NoAnimViewPager viewPager;

    private int[] mTitles;
    private int[] drawables = new int[]{R.drawable.tab_conversation_bg, R.drawable.tab_contacts_bg, R.drawable.tab_discover_bg, R.drawable.tab_me_bg};
    private TabLayout.Tab[] tabs;
    private Fragment[] fragments;
    //新消息角标
    private TextView unreadLabel;
    // 新好友申请消息角标
    public TextView unreadInviteLabel;
    //朋友圈通知
    public TextView unreadFriendLabel;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_main);
        initTitleBar();
        setTitle("微信");
        mainPresenter = new MainPresenter(this);
        mTitles = new int[]{R.string.chat, R.string.contacts, R.string.discover, R.string.me};
        initViews();
    }

    private void initViews() {
        mTablayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        // 保持4个fragment不被销毁
        viewPager.setOffscreenPageLimit(4);
        fragments = new Fragment[]{new ConversationFragment(), new ContactsFragment(), new DiscoverFragment(), new MeFragment()};
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        // 将viewPager绑定到TabLayout
        mTablayout.setupWithViewPager(viewPager);
        tabs = new TabLayout.Tab[mTitles.length];
        for (int i = 0; i < mTitles.length; i++) {
            tabs[i] = mTablayout.getTabAt(i);
            tabs[i].setCustomView(getBottomView(i, drawables[i]));
        }
    }

    /**
     * 根据index设置tab的icon & title
     *
     * @param index       tab的index
     * @param drawableRes icon的res id
     * @return tab的view
     */
    private View getBottomView(int index, int drawableRes) {
        View view = getLayoutInflater().inflate(R.layout.item_main_button, null);
        Button button = view.findViewById(R.id.main_button);
        button.setCompoundDrawablesWithIntrinsicBounds(0, drawableRes, 0, 0);
        button.setText(getResources().getString(mTitles[index]));
        button.setOnClickListener(v -> {
            if (tabs[index] != null) {
                tabs[index].select();
            }
        });
        TextView textView = view.findViewById(R.id.unread_msg_number);
        if (index == 0) {
            unreadLabel = textView;
        } else if (index == 1) {
            unreadInviteLabel = textView;
        } else if (index == 2) {
            unreadFriendLabel = textView;
        }
        return view;
    }

    @Override
    public void setPresenter(MainPresenter presenter) {
        this.mainPresenter = presenter;
    }

    @Override
    public Activity getBaseActivity() {
        return this;
    }

    @Override
    public void unreadMessagesChanged(int count) {
        if (count > 0) {
            setTitle(String.format("微信(%s)", count));
        } else {
            setTitle("微信");
        }
    }
}
