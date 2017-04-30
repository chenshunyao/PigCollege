package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/9/18.
 */

public class LoginUserModel {
    private String userId;
    private String passWord;
    private String imei;
    private String platform;

    public LoginUserModel() {
    }

    public LoginUserModel(String imei, String passWord, String platform, String userId) {
        this.imei = imei;
        this.passWord = passWord;
        this.platform = platform;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LoginUserModel{" +
                "imei='" + imei + '\'' +
                ", userId='" + userId + '\'' +
                ", passWord='" + passWord + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
