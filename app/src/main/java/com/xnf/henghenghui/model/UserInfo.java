package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/6/4.
 */
public class UserInfo {
    private  String userId;
    private String passWord;
    private String imei;
    private  String platform;
    private String regType;
    private String userType;

    public UserInfo() {
    }

    public UserInfo(String imei, String passWord, String platform, String regType, String userId, String userType) {
        this.imei = imei;
        this.passWord = passWord;
        this.platform = platform;
        this.regType = regType;
        this.userId = userId;
        this.userType = userType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "imei='" + imei + '\'' +
                ", userId='" + userId + '\'' +
                ", passWord='" + passWord + '\'' +
                ", platform='" + platform + '\'' +
                ", regType='" + regType + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}