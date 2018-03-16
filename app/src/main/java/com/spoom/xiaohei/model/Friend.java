package com.spoom.xiaohei.model;

import android.text.TextUtils;

/**
 * package com.lan.ichat.model
 *
 * @author spoomlan
 * @date 13/01/2018
 */

public class Friend {
    private String username;
    private String nickname;
    private Integer type;
    private String telephone;
    private String motto;
    private String avatar;
    private Integer gender;
    private String region;
    private String remark;

    private String localAvatar;
    private String bigAvatar;

    private String headChar;
    private String sortStr;

    public String getAvatarToLoad() {
        if (localAvatar == null) {
            return avatar;
        } else {
            return localAvatar;
        }
    }

    public String getName() {
        return TextUtils.isEmpty(remark) ? (TextUtils.isEmpty(nickname) ? username : nickname) : remark;
    }

    public String getHeadChar() {
        return headChar;
    }

    public void setHeadChar(String headChar) {
        this.headChar = headChar;
    }

    public String getSortStr() {
        return sortStr;
    }

    public void setSortStr(String sortStr) {
        this.sortStr = sortStr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocalAvatar() {
        return localAvatar;
    }

    public void setLocalAvatar(String localAvatar) {
        this.localAvatar = localAvatar;
    }

    public String getBigAvatar() {
        return bigAvatar;
    }

    public void setBigAvatar(String bigAvatar) {
        this.bigAvatar = bigAvatar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean equals(Friend friend) {
        return (username == null ? friend.getUsername() == null : username.equals(friend.getUsername()))
                && (nickname == null ? friend.getNickname() == null : nickname.equals(friend.getNickname()))
                && (type == null ? friend.getType() == null : type.equals(friend.getType()))
                && (telephone == null ? friend.getTelephone() == null : telephone.equals(friend.getTelephone()))
                && (motto == null ? friend.getMotto() == null : motto.equals(friend.getMotto()))
                && (avatar == null ? friend.getAvatar() == null : avatar.equals(friend.getAvatar()))
                && (gender == null ? friend.getGender() == null : gender.equals(friend.getGender()))
                && (region == null ? friend.getRegion() == null : region.equals(friend.getRegion()))
                && (remark == null ? friend.getRemark() == null : remark.equals(friend.getRemark()));
    }
}
