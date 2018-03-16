package com.spoom.xiaohei.activity.main.contacts;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.spoom.xiaohei.activity.BasePresenter;
import com.spoom.xiaohei.activity.BaseView;

import java.util.List;

/**
 * package com.lan.ichat.activity.main.contacts
 *
 * @author spoomlan
 * @date 06/01/2018
 */

public interface ContactsContract {
    interface View extends BaseView<Presenter> {
        /**
         * display how many contacts in total
         *
         * @param count
         */
        void showContactsCount(int count);

        /**
         * refresh the view after load contacts data from db or remote
         */
        void refresh();
    }

    interface Presenter extends BasePresenter {
        /**
         * get contacts list from database
         *
         * @return
         */
        List<NimUserInfo> getContactsListInDb();

        /**
         * delete Friend by username
         *
         * @param username
         */
        void deleteFriend(String username);

        //void moveUserToBlack(String userId);

        /**
         * sort the friend list by first pinyin character
         *
         * @param friends
         * @return
         */
        List<NimUserInfo> sortList(List<NimUserInfo> friends);

        /**
         * get friend list from server
         */
        void refreshFriendsFromServer();

        // int getInvitionCount();

        /**
         * get friend count
         *
         * @return
         */
        int getFriendsCount();

        // void clearInvitionCount();

        /**
         * get friend list from local
         */
        void refreshFriendsFromLocal();
    }
}
