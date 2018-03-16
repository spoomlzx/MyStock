package com.spoom.xiaohei.activity.main.conversation;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.spoom.xiaohei.activity.BasePresenter;
import com.spoom.xiaohei.activity.BaseView;

import java.util.List;

/**
 * package com.lan.ichat.activity.main.conversation
 *
 * @author spoomlan
 * @date 29/01/2018
 */

public interface ConversationContract {
    interface View extends BaseView<Presenter> {
        /**
         * refresh the view
         */
        void refresh();
    }

    interface Presenter extends BasePresenter {
        List<RecentContact> getAllConversations();

        int getUnreadMessageCount();

        void refreshConversations();
    }
}
