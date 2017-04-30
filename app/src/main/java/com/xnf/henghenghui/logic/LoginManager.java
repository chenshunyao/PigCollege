package com.xnf.henghenghui.logic;

import android.os.Message;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.ExpertUserInfo;
import com.xnf.henghenghui.model.NormalUserInfo;
import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/1/22.
 */
public class LoginManager extends BaseManager {

    private static final String TAG = LoginManager.class.getSimpleName();

    public static final int LOGIN_OK = 1001;
    public static final int NO_LOGIN = 1002;

    /**
     * 登录
     * @param userInfo
     */
    public static void login(UserInfo userInfo) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userInfo.getUserId());
            jsonObj.put("passWord", userInfo.getPassWord());
            jsonObj.put("imei", userInfo.getImei());
            jsonObj.put("platform", userInfo.getPlatform());
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_LOGIN)
                .tag(Urls.ACTION_LOGIN)
                .connTimeOut(15000)      // 设置当前请求的连接超时时间
                .readTimeOut(15000)      // 设置当前请求的读取超时时间
                .writeTimeOut(15000)     // 设置当前请求的写入超时时间
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_LOGIN;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(call, response, e);
                        L.d(TAG, "Error Response:");
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_LOGIN_FAILED;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 注册用户信息
     * @param userInfo
     */
    public static void register(UserInfo userInfo) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userInfo.getUserId());
            jsonObj.put("passWord",  userInfo.getPassWord());
            jsonObj.put("imei", userInfo.getImei());
            jsonObj.put("regType", userInfo.getRegType());
            jsonObj.put("userType", userInfo.getUserType());
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_REGISTER)
                .tag(Urls.ACTION_REGISTER)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_REGISTER;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 注册用户信息
     * @param userInfo
     */
    public static void inviteRegister(UserInfo userInfo,String inviteCode) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userInfo.getUserId());
            jsonObj.put("passWord",  userInfo.getPassWord());
            jsonObj.put("imei", userInfo.getImei());
            jsonObj.put("regType", userInfo.getRegType());
            jsonObj.put("userType", userInfo.getUserType());
            jsonObj.put("inviteCode",inviteCode);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_REGISTER)
                .tag(Urls.ACTION_REGISTER)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_REGISTER;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 获取验证码
     *
     * @param mobile      手机号
     * @param vfyCodeType 验证码类型 ,login-登录验证码, register-注册验证码，modifyPwd-修改密码，其他-待扩展
     * @param imei
     */
    public static void getVerifyCode(String mobile, String vfyCodeType, String imei) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("mobile", mobile);
            jsonObj.put("vfyCodeType", vfyCodeType);
            jsonObj.put("imei", imei);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GETVERIFYCODE)
                .tag(Urls.ACTION_GETVERIFYCODE)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_VERIFY_CODE;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 校验服务器返回的验证码是否正确
     *
     * @param mobile
     * @param vfyCodeType 验证码类型 ,login-登录验证码, register-注册验证码，modifyPwd-修改密码,其他-待扩展
     * @param verifyCode
     * @param imei
     */
    public static void checkVerifyCode(String mobile, String vfyCodeType, String verifyCode, String imei) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("mobile", mobile);
            jsonObj.put("vfyCodeType", vfyCodeType);
            jsonObj.put("verifyCode", verifyCode);
            jsonObj.put("imei", imei);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CHECKVERIFYCODE)
                .tag(Urls.ACTION_CHECKVERIFYCODE)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CHECK_CODE;
                        msg.obj = (String) s;
                        mHandler.sendMessageAtTime(msg,1500);
                    }
                });
    }

    /**
     * 修改用户密码
     * @param userId
     * @param imei
     * @param mobile
     * @param verifyCode
     * @param verifyType
     * @param newPassword
     */
    public static void modifyPassWd(String userId, String imei,String mobile,
                                    String verifyCode, String verifyType , String newPassword) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",userId);
            jsonObj.put("imei",imei);
            jsonObj.put("mobile", mobile);
            jsonObj.put("verifyCode", verifyCode);
            jsonObj.put("verifyType", verifyType);
            jsonObj.put("newPassword",  newPassword.substring(0,newPassword.length()-1));
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_MODIFYPASSWORD)
                .tag(Urls.ACTION_MODIFYPASSWORD)
                .postJson(getRequestBody(jsonString))
                .connTimeOut(3000)
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_MODIFY_PASSWD;
                        msg.obj=(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 修改养殖户个人信息
     * @param normalUserInfo
     */
    public static void modifyBreederInfo(NormalUserInfo normalUserInfo) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", normalUserInfo.getUserId());
            jsonObj.put("userName", normalUserInfo.getUserName());
            jsonObj.put("nikeName", normalUserInfo.getNikeName());
            jsonObj.put("mobile", normalUserInfo.getMobile());
            jsonObj.put("email", normalUserInfo.getEmail());
//            jsonObj.put("city", normalUserInfo.getCity());
            jsonObj.put("address", normalUserInfo.getAddress());
//            jsonObj.put("species", normalUserInfo.getSpecies());
//            jsonObj.put("businessType", normalUserInfo.getBusinessType());
            jsonObj.put("company", normalUserInfo.getCompany());
            jsonObj.put("breedScope", normalUserInfo.getBreedScope());
            jsonObj.put("fileMappingId", normalUserInfo.getFileMappingId());
            jsonObj.put("farmName",normalUserInfo.getFarmName());
            jsonObj.put("farmAddress",normalUserInfo.getFarmAddress());
            jsonObj.put("title",normalUserInfo.getDuites());
            jsonObj.put("photo", normalUserInfo.getPhoto());
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_MODIFY_NORMALUSER_INFO)
                .tag(Urls.ACTION_MODIFY_NORMALUSER_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_MODIFY_NORMALUSER_INFO;
                        msg.obj=(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }


    /**
     * 修改专家个人信息
     * @param expertUserInfo
     */
    public static void modifyExpertInfo(ExpertUserInfo expertUserInfo) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", expertUserInfo.getUserId());
            jsonObj.put("userName", expertUserInfo.getUserName());
            jsonObj.put("mobile", expertUserInfo.getMobile());
            jsonObj.put("email", expertUserInfo.getEmail());
            jsonObj.put("address", expertUserInfo.getAddress());
            jsonObj.put("isRect", expertUserInfo.getIsRect());
            jsonObj.put("certType", expertUserInfo.getCertType());
            jsonObj.put("company", expertUserInfo.getCompany());
            jsonObj.put("titles", expertUserInfo.getTitles());
            jsonObj.put("desc", expertUserInfo.getDesc());
            jsonObj.put("professional", expertUserInfo.getProfessional());
            jsonObj.put("otherContact", expertUserInfo.getOtherContact());
            jsonObj.put("fileMappingId", expertUserInfo.getFileMappingId());
            jsonObj.put("photo", expertUserInfo.getPhoto());
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_MODIFY_EXPERTUSER_INFO)
                .tag(Urls.ACTION_MODIFYPASSWORD)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_MODIFY_EXPERTUSER_INFO;
                        msg.obj=(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 校验服务器返回的验证码是否正确
     *
     * */
    public static void getAuthStatus(String userId, String type) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("userType", type);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GET_AUTH_STATUS)
                .tag(Urls.ACTION_GET_AUTH_STATUS)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_AUTH_STATUS;
                        msg.obj = (String) s;
                        mHandler.sendMessageAtTime(msg,1000);
                    }
                });
    }


}