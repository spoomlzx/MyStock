package com.spoom.xiaohei.activity.main.conversation;

import android.content.Context;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * package com.lan.ichat.activity.main.conversation
 *
 * @author spoomlan
 * @date 29/01/2018
 */

public class ConversationPresenter implements ConversationContract.Presenter {
    private ConversationContract.View view;
    private Context context;

    private List<RecentContact> conversations;

    public ConversationPresenter(ConversationContract.View view) {
        this.view = view;
        view.setPresenter(this);
        this.context = view.getBaseContext();
        this.conversations = loadConversationList();
    }

    @Override
    public void start() {

    }

    @Override
    public List<RecentContact> getAllConversations() {
        return conversations;
    }

    @Override
    public int getUnreadMessageCount() {
        int total = 0;
        if (conversations != null && conversations.size() != 0) {
            for (int i = 0; i < conversations.size(); i++) {
                total += conversations.get(i).getUnreadCount();
            }
        }
        return total;
    }

    @Override
    public void refreshConversations() {
        conversations.clear();
        conversations.addAll(loadConversationList());
    }

    private List<RecentContact> loadConversationList() {
        return NIMClient.getService(MsgService.class).queryRecentContactsBlock();
    }
}
