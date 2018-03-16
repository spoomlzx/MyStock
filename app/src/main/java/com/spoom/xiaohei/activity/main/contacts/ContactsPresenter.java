package com.spoom.xiaohei.activity.main.contacts;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * package com.lan.ichat.activity.main.contacts
 *
 * @author spoomlan
 * @date 06/01/2018
 */

public class ContactsPresenter implements ContactsContract.Presenter {
    private ContactsContract.View view;
    private List<NimUserInfo> friends;

    public ContactsPresenter(ContactsContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public List<NimUserInfo> getContactsListInDb() {
        //List<Friend> friends = new ArrayList<>(ContactManager.getInstance().getContactsList().values());
        List<String> friends = NIMClient.getService(FriendService.class).getFriendAccounts();
        List<NimUserInfo> userInfos = NIMClient.getService(UserService.class).getUserInfoList(friends);
        this.friends = sortList(userInfos);
        return this.friends;
    }

    @Override
    public void deleteFriend(String username) {
        //Friend friend=ContactManager.getInstance().getContactsList().get(username);
        // post api返回success之后再删除本地DB和缓存中的数据
    }

    @Override
    public List<NimUserInfo> sortList(List<NimUserInfo> friends) {
        PinyinComparator comparator = new PinyinComparator();
        Collections.sort(friends, comparator);
        return friends;
    }

    public class PinyinComparator implements Comparator<NimUserInfo> {
        @Override
        public int compare(NimUserInfo o1, NimUserInfo o2) {
            String py1 = o1.getName();
            String py2 = o2.getName();
            if (py1.equals(py2)) {
                return o1.getAccount().compareTo(o2.getAccount());
            } else {
                if ("#".equals(py1)) {
                    return 1;
                } else if ("#".equals(py2)) {
                    return -1;
                }
                return py1.compareTo(py2);
            }
        }
    }

    @Override
    public void refreshFriendsFromServer() {
        List<String> friends = NIMClient.getService(FriendService.class).getFriendAccounts();
        NIMClient.getService(UserService.class).fetchUserInfo(friends)
                .setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {
                    @Override
                    public void onResult(int code, List<NimUserInfo> result, Throwable exception) {
                        if (200 == code) {
                            //                            for (NimUserInfo user : result) {
                            //                                Friend friend = new Friend();
                            //                                friend.setUsername(user.getAccount());
                            //                                friend.setNickname(user.getName());
                            //                                friend.setAvatar(user.getAvatar());
                            //                                friend.setGender(user.getGenderEnum().getValue());
                            //                                friend.setMotto(user.getSignature());
                            //                                friend.setTelephone(user.getMobile());
                            //                                friend.setType(3);
                            //                                ContactManager.getInstance().saveFriend(friend);
                            //                            }
                            refreshFriendsFromLocal();
                        }
                    }
                });
    }

    @Override
    public int getFriendsCount() {
        return friends.size();
    }

    @Override
    public void refreshFriendsFromLocal() {
        friends.clear();
        friends.addAll(getContactsListInDb());
        view.refresh();
    }

    @Override
    public void start() {

    }
}
