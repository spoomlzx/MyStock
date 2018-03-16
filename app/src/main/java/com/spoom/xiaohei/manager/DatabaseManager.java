package com.spoom.xiaohei.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.spoom.base.utils.log.Logger;
import com.spoom.xiaohei.XiaoheiApp;
import com.spoom.xiaohei.model.Friend;
import com.spoom.xiaohei.util.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package com.lan.ichat.manager.user
 *
 * @author spoomlan
 * @date 19/12/2017
 */

public class DatabaseManager {
    private static final String TAG = "spoom.im";
    private IMDatabaseHelper databaseHelper;

    private static DatabaseManager databaseManager;

    public DatabaseManager() {
        String databaseName = LocalUserManager.getInstance().getUsername() + "_app.db";
        databaseHelper = new IMDatabaseHelper(XiaoheiApp.getInstance().getApplicationContext(), databaseName, null);
    }

    public static DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public static void init() {
        databaseManager = new DatabaseManager();
    }

    /**
     * 获取数据库中的所有friend
     *
     * @return
     */
    public synchronized Map<String, Friend> getFriendList() {
        Map<String, Friend> friends = new HashMap<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from friend where type>=3", null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                Integer type = cursor.getInt(cursor.getColumnIndex("type"));
                String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                String motto = cursor.getString(cursor.getColumnIndex("motto"));
                String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                Integer gender = cursor.getInt(cursor.getColumnIndex("gender"));
                String region = cursor.getString(cursor.getColumnIndex("region"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                String localAvatar = cursor.getString(cursor.getColumnIndex("localAvatar"));
                String bigAvatar = cursor.getString(cursor.getColumnIndex("bigAvatar"));
                Friend friend = new Friend();
                friend.setUsername(username);
                friend.setNickname(nickname);
                friend.setType(type);
                friend.setTelephone(telephone);
                friend.setMotto(motto);
                friend.setAvatar(avatar);
                friend.setGender(gender);
                friend.setRegion(region);
                friend.setRemark(remark);
                friend.setLocalAvatar(localAvatar);
                friend.setBigAvatar(bigAvatar);
                CommonUtils.getSortStr(friend);
                // TODO: 14/01/2018 从type中取出所有信息
                friends.put(username, friend);
            }
            cursor.close();
        }
        return friends;
    }

    /**
     * 将http返回的friend list保存到db
     *
     * @param friends
     */
    public void saveFriends(List<Friend> friends) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (db.isOpen()) {
            // 更新好友db
            for (Friend friend : friends) {
                ContentValues values = new ContentValues();
                values.put("type", friend.getType());
                if (0 == friend.getType()) {
                    db.update("friend", values, "username = ?", new String[]{friend.getUsername()});
                    continue;
                }
                values.put("username", friend.getUsername());
                if (friend.getNickname() != null) {
                    values.put("nickname", friend.getNickname());
                }
                if (friend.getTelephone() != null) {
                    values.put("telephone", friend.getTelephone());
                }
                if (friend.getMotto() != null) {
                    values.put("motto", friend.getMotto());
                }
                if (friend.getAvatar() != null) {
                    values.put("avatar", friend.getAvatar());
                    values.put("localAvatar", "");
                    values.put("bigAvatar", "");
                }
                if (friend.getGender() != null) {
                    values.put("gender", friend.getGender());
                }
                if (friend.getRegion() != null) {
                    values.put("region", friend.getRegion());
                }
                if (friend.getRemark() != null) {
                    values.put("remark", friend.getRemark());
                }
                db.replace("friend", null, values);
            }
            Logger.d("updated " + friends.size() + " friends");
        }
    }

    /**
     * 将单个friend保存到db
     *
     * @param friend
     */
    public synchronized void saveFriend(Friend friend) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("username", friend.getUsername());
            values.put("nickname", friend.getNickname());
            values.put("type", friend.getType());
            if (friend.getTelephone() != null) {
                values.put("telephone", friend.getTelephone());
            }
            if (friend.getMotto() != null) {
                values.put("motto", friend.getMotto());
            }
            if (friend.getAvatar() != null) {
                values.put("avatar", friend.getAvatar());
            }
            if (friend.getGender() != null) {
                values.put("gender", friend.getGender());
            }
            if (friend.getRegion() != null) {
                values.put("region", friend.getRegion());
            }
            if (friend.getRemark() != null) {
                values.put("remark", friend.getRemark());
            }
            db.replace("friend", null, values);
        }
    }

    /**
     * update friend information
     *
     * @param friend the object of the information to update
     */
    public synchronized void updateFriend(Friend friend) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            if (friend.getNickname() != null) {
                values.put("nickname", friend.getNickname());
            }
            if (friend.getType() != null) {
                values.put("type", friend.getType());
            }
            if (friend.getTelephone() != null) {
                values.put("telephone", friend.getTelephone());
            }
            if (friend.getMotto() != null) {
                values.put("motto", friend.getMotto());
            }
            if (friend.getAvatar() != null) {
                values.put("avatar", friend.getAvatar());
            }
            if (friend.getGender() != null) {
                values.put("gender", friend.getGender());
            }
            if (friend.getRegion() != null) {
                values.put("region", friend.getRegion());
            }
            if (friend.getRemark() != null) {
                values.put("remark", friend.getRemark());
            }
            db.update("friend", values, "username = ?", new String[]{friend.getUsername()});
        }
    }

    public synchronized void updateFriendLocalAvatar(String username, String localAvatar) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("localAvatar", localAvatar);
            db.update("friend", values, "username = ?", new String[]{username});
        }
    }

    public synchronized void updateFriendBigAvatar(String username, String bigAvatar) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("bigAvatar", bigAvatar);
            db.update("friend", values, "username = ?", new String[]{username});
        }
    }

    /**
     * 根据chatId删除对应的friend
     *
     * @param username
     */
    public synchronized void deleteFriend(String username) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete("friend", "username = ?", new String[]{username});
        }
    }

    public synchronized void closeDB() {
        if (databaseHelper != null) {
            try {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                database.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
