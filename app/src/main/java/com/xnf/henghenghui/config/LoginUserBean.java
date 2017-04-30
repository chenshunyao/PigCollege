package com.xnf.henghenghui.config;

/**
 * Created by Administrator on 2016/4/5.
 */
public class LoginUserBean {

    /**
     * 登录成功
     */
    public static final int LOGIN_OK = 1;

    /**
     * 登录失败
     */
    public static final int LOGIN_FAILED = 2;
    /**
     * 退出登录
     */
    public static final int LOGIN_OUT = 3;
    /**
     * 普通用户
     */
    public static final int NORMAL_USER = 1;

    /**
     * 专家用户
     */
    public static final int EXPERT_USER = 2;

    /**
     * 登录状态
     */
    private int loginStatus;
    /**
     * 当前登录的用户id
     */
    private String loginUserid;

    /**
     * 当前登录的用户类型
     */
    private int loginUserType;

    private static  LoginUserBean sInstance;

    public static boolean isInfoModified = false;

    public  static LoginUserBean getInstance(){
        if (sInstance==null){
            sInstance = new LoginUserBean();
        }
        return sInstance;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getLoginUserid() {
        return loginUserid;
    }

    public void setLoginUserid(String loginUserid) {
        this.loginUserid = loginUserid;
    }

    public int getLoginUserType() {
        return loginUserType;
    }

    public void setLoginUserType(int loginUserType) {
        this.loginUserType = loginUserType;
    }
}
