package com.spoom.xiaohei.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * package com.lan.ichat.manager
 *
 * @author lanzongxiao
 * @date 30/11/2017
 */

public class LocalUserManager {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static LocalUserManager instance;

    private final static String PREFERENCE_NAME = "USER_INFO";
    private final String KEY_TOKEN = "KEY_TOKEN";

    private final String KEY_NICKNAME = "KEY_NICKNAME";
    private final String KEY_TELEPHONE = "KEY_TELEPHONE";
    private final String KEY_MOTTO = "KEY_MOTTO";
    private final String KEY_AVATAR = "KEY_AVATAR";
    private final String KEY_GENDER = "KEY_GENDER";
    private final String KEY_REGION = "KEY_REGION";
    private final String KEY_USERNAME = "KEY_USERNAME";

    private final String KEY_KEYBOARD_HEIGHT = "KEY_KEYBOARD_HEIGHT";

    private LocalUserManager(Context context) {
        super();
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void init(Context context) {
        instance = new LocalUserManager(context);
    }

    public static LocalUserManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("Init first!");
        }
        return instance;
    }

    public void logout() {
        editor.remove(KEY_TOKEN)
                .remove(KEY_NICKNAME)
                .remove(KEY_TELEPHONE)
                .remove(KEY_MOTTO)
                .remove(KEY_AVATAR)
                .remove(KEY_GENDER)
                .remove(KEY_REGION)
                .remove(KEY_USERNAME).apply();
    }

    public void setKeyboardHeight(int height) {
        editor.putInt(KEY_KEYBOARD_HEIGHT, height).apply();
    }

    public Integer getKeyboardHeight() {
        return preferences.getInt(KEY_KEYBOARD_HEIGHT, 0);
    }

    public void setUsername(String username) {
        editor.putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, null);
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public void setNickname(String nickname) {
        editor.putString(KEY_NICKNAME, nickname).apply();
    }

    public String getNickname() {
        return preferences.getString(KEY_NICKNAME, null);
    }

    public void setTelephone(String telephone) {
        editor.putString(KEY_TELEPHONE, telephone).apply();
    }

    public String getTelephone() {
        return preferences.getString(KEY_TELEPHONE, null);
    }

    public void setMotto(String motto) {
        editor.putString(KEY_MOTTO, motto).apply();
    }

    public String getMotto() {
        return preferences.getString(KEY_MOTTO, null);
    }

    public void setAvatar(String avatar) {
        editor.putString(KEY_AVATAR, avatar).apply();
    }

    public String getAvatar() {
        return preferences.getString(KEY_AVATAR, null);
    }

    public void setGender(Integer gender) {
        editor.putInt(KEY_GENDER, gender).apply();
    }

    public Integer getGender() {
        return preferences.getInt(KEY_GENDER, 0);
    }

    public void setRegion(String region) {
        editor.putString(KEY_REGION, region).apply();
    }

    public String getRegion() {
        return preferences.getString(KEY_REGION, null);
    }
}
