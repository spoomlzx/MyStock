package com.spoom.xiaohei.manager;

import com.spoom.xiaohei.model.Friend;
import com.spoom.xiaohei.util.CommonUtils;

import java.util.List;
import java.util.Map;

/**
 * package com.lan.ichat.manager
 *
 * @author spoomlan
 * @date 13/01/2018
 */

public class ContactManager {
    private static ContactManager contactManager;
    private Map<String, Friend> friends;

    public static synchronized void init() {
        if (contactManager == null) {
            contactManager = new ContactManager();
        }
    }

    public ContactManager() {
        this.friends = DatabaseManager.getInstance().getFriendList();
    }

    public static ContactManager getInstance() {
        if (contactManager == null) {
            throw new RuntimeException("init first");
        }
        return contactManager;
    }

    public Friend getFriend(String username) {
        return friends.get(username);
    }

    public Map<String, Friend> getContactsList() {
        if (friends == null || friends.isEmpty()) {
            friends = DatabaseManager.getInstance().getFriendList();
        }
        return friends;
    }

    public void saveFriendList(List<Friend> friendList) {
        DatabaseManager.getInstance().saveFriends(friendList);
    }

    public void saveFriend(Friend friend) {
        CommonUtils.getSortStr(friend);
        friends.put(friend.getUsername(), friend);
        DatabaseManager.getInstance().saveFriend(friend);
    }

    public void updateFriendLocalAvatar(Friend friend) {
        friends.put(friend.getUsername(), friend);
        DatabaseManager.getInstance().updateFriendLocalAvatar(friend.getUsername(), friend.getLocalAvatar());
    }

    public void updateFriendBigAvatar(Friend friend) {
        friends.put(friend.getUsername(), friend);
        DatabaseManager.getInstance().updateFriendBigAvatar(friend.getUsername(), friend.getBigAvatar());
    }

    public void deleteFriend(String username) {
        friends.remove(username);
        DatabaseManager.getInstance().deleteFriend(username);
    }
}
