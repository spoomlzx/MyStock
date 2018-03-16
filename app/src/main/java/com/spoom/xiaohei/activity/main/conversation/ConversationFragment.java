package com.spoom.xiaohei.activity.main.conversation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.spoom.base.utils.log.Logger;
import com.spoom.base.widget.DividerDecoration;
import com.spoom.xiaohei.LocalAction;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.activity.chat.ChatActivity;

/**
 * package com.lan.ichat.activity.main.conversation
 *
 * @author lanzongxiao
 * @date 03/12/2017
 */

public class ConversationFragment extends Fragment implements ConversationContract.View {
    private ConversationContract.Presenter presenter;

    private LinearLayout llRoot;
    private RecyclerView recyclerView;
    private RelativeLayout errorItem;
    private TextView errorText;

    private ConversationAdapter adapter;
    private NewMessageListener newMessageListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new ConversationPresenter(this);
        if (context instanceof NewMessageListener) {
            newMessageListener = (NewMessageListener) context;
            newMessageListener.unreadMessagesChanged(presenter.getUnreadMessageCount());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        llRoot = view.findViewById(R.id.ll_root);
        recyclerView = view.findViewById(R.id.rv_conversation);
        errorItem = view.findViewById(R.id.item_chat_error);
        errorText = errorItem.findViewById(R.id.tv_connect_error_msg);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ConversationAdapter(getActivity(), presenter.getAllConversations(), new ConversationAdapter.OnClickListener() {
            @Override
            public void onItemClick(RecentContact conversation) {
                NIMClient.getService(MsgService.class).setChattingAccount(conversation.getContactId(), conversation.getSessionType());
                Intent intent = new Intent(getActivity(), ChatActivity.class)
                        .putExtra("chatType", conversation.getSessionType().getValue())
                        .putExtra("username", conversation.getContactId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view) {

                return false;
            }
        });

        recyclerView.setAdapter(adapter);
        DividerDecoration dividerDecoration = new DividerDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerDecoration);
        registerConversationBroadcastReceiver();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (LocalAction.ACTION_DISCONNECT.equals(action)) {

            } else if (LocalAction.ACTION_CONVERSATION_UPDATE.equals(action)) {
                refresh();
                Logger.d("update");
            } else if (LocalAction.ACTION_NEW_MESSAGE.equals(action)) {
                refresh();
            }
        }
    };

    private void registerConversationBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocalAction.ACTION_DISCONNECT);
        intentFilter.addAction(LocalAction.ACTION_CONVERSATION_UPDATE);
        intentFilter.addAction(LocalAction.ACTION_NEW_MESSAGE);
        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void setPresenter(ConversationContract.Presenter presenter) {
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
    public void refresh() {
        presenter.refreshConversations();
        adapter.notifyDataSetChanged();
        newMessageListener.unreadMessagesChanged(presenter.getUnreadMessageCount());
    }

    public interface NewMessageListener {
        /**
         * {@link com.spoom.xiaohei.activity.main.MainActivity} implements this interface<br>
         * tell MainActivity how many unread messages remain now
         *
         * @param count
         */
        void unreadMessagesChanged(int count);
    }
}
