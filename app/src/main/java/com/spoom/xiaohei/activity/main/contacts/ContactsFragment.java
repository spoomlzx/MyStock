package com.spoom.xiaohei.activity.main.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.activity.main.detail.UserDetailActivity;
import com.spoom.xiaohei.util.CommonUtils;

/**
 * package com.lan.ichat.activity.main.contacts
 *
 * @author lanzongxiao
 * @date 03/12/2017
 */

public class ContactsFragment extends Fragment implements ContactsContract.View {
    private ContactsContract.Presenter presenter;
    private ContactsAdapter adapter;
    private ListView listView;

    private TextView tvTotal;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new ContactsPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        listView = view.findViewById(R.id.lv_friends);

        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_contacts_list_header, null);
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_contacts_list_footer, null);
        listView.addHeaderView(headerView);
        listView.addFooterView(footerView);

        tvTotal = footerView.findViewById(R.id.tv_total);
        headerView.findViewById(R.id.ip_new_friends).setOnClickListener(v -> {
            CommonUtils.showToastShort(getContext(), "new friend");
        });
        headerView.findViewById(R.id.ip_group_chat).setOnClickListener(v -> {
            CommonUtils.showToastShort(getContext(), "group chat");
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new ContactsAdapter(getActivity(), presenter.getContactsListInDb());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position > 0 && position <= adapter.getCount()) {
                NimUserInfo friend = adapter.getItem(position - 1);
                startActivity(new Intent(getContext(), UserDetailActivity.class).putExtra("username", friend.getAccount()));
            }
        });
        // 注册长按弹出菜单
        registerForContextMenu(listView);
        //presenter.refreshFriendsFromServer();
        presenter.refreshFriendsFromLocal();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1, 1, 1, "设置备注");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            CommonUtils.showToastShort(getContext(), "设置备注");
        }
        return true;
    }

    @Override
    public void setPresenter(ContactsContract.Presenter presenter) {
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
    public void showContactsCount(int count) {
        String total = count + "个联系人";
        tvTotal.setText(total);
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
        showContactsCount(presenter.getFriendsCount());
    }
}
