package com.xnf.henghenghui.logic;

/**
 * Created by Administrator on 2016/4/16.
 */
public class HhhErrorCode {

    public static final String SUCCESS_REQUEST ="\"succeed\":1";

    public static final String FAILED_RET ="\"succeed\":0";

    public static final String FAILED_USER_EXIST ="\"errorCode\":\"20002\"";

    /**
     * 系统操作出现异常
     */
    public static final int ERROR_CODE_10000 =10000;
    /**
     * 数据库异常
     */
    public static final int ERROR_CODE_10001 =10001;
    /**
     * 解密失败
     */
    public static final int ERROR_CODE_10002 =10002;
    /**
     * 系统忙请稍后再试
     */
    public static final int ERROR_CODE_10003 =10003;
    /**
     * 参数错误
     */
    public static final int ERROR_CODE_10004 =10004;
    /**
     * 查询无相关数据
     */
    public static final int ERROR_CODE_20000 =20000;
    /**
     * 用户名或密码错误
     */
    public static final int ERROR_CODE_20001 =20001;
    /**
     * 用户名已存在，不能重复注册
     */
    public static final int ERROR_CODE_20002 =20002;
    /**
     * 用户名已存在，不能重复注册
     */
    public static final int ERROR_CODE_20005 =20005;

}
