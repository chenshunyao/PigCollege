package com.xnf.henghenghui.logic;

import android.os.Message;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.ExpertUserInfo;
import com.xnf.henghenghui.model.NormalUserInfo;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/9.
 */
public class PersonalnfoManager  extends BaseManager{
    private static final String TAG = PersonalnfoManager.class.getSimpleName();

    /**
     * 获取普通用户的个人信息
     * @param userID
     */
    public static void getNormalUserInfo(String userID){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",userID);
        } catch (Exception e) {

        }

        String jsonString = jsonObj.toString();
        L.d(TAG,"jsonString:"+"{"+"\"request\""+":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GET_NORMALUSER_INFO)
                .tag(Urls.ACTION_GET_NORMALUSER_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG,"onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_NORAMLUSER_INFO;
                        msg.obj=(String)s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取普通用户的个人信息
     * @param userID
     */
    public static void getExpertUserInfo(String userID){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",userID);
        } catch (Exception e) {

        }

        String jsonString = jsonObj.toString();
        L.d(TAG,"jsonString:"+"{"+"\"request\""+":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GET_EXPERTUSER_INFO)
                .tag(Urls.ACTION_GET_EXPERTUSER_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG,"onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_EXPERTUSER_INFO;
                        msg.obj=(String)s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 完善/修改普通用户的个人信息
     */
    public static void implementNormalUserPersonInfo(NormalUserInfo normalUserInfo){
        Gson userInfo = new Gson();
        String jsonString = userInfo.toJson(normalUserInfo);
        L.d(TAG,"jsonString:"+"{"+"\"request\""+":" + jsonString + "}");
        //需要调整
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_MODIFY_NORMALUSER_INFO)
                .tag(Urls.ACTION_MODIFY_NORMALUSER_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG,"onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_MODIFY_NORMALUSER_INFO;
                        msg.obj=(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 完善/修改专家的个人信息
     */
    public static void implementExpertPersonInfo(ExpertUserInfo expertUserInfo){
        Gson userInfo = new Gson();
        String jsonString = userInfo.toJson(expertUserInfo);
        L.d(TAG,"jsonString:"+"{"+"\"request\""+":" + jsonString + "}");
        //需要调整
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_MODIFY_EXPERTUSER_INFO)
                .tag(Urls.ACTION_MODIFY_EXPERTUSER_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG,"onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_MODIFY_EXPERTUSER_INFO;
                        msg.obj=(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 获取用户提问的问题
     * @param userID
     * @param beginTime
     * @param endTime
     * @param qtUserId
     * @param qtTitle
     * @param startRowNum
     * @param endRowNum
     */
    public static void getUserAskQuestions(String userID,String beginTime,String endTime,String qtUserId ,String qtTitle,
                                           String startRowNum,String endRowNum){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",userID);
            jsonObj.put("beginTime",beginTime);
            jsonObj.put("endTime",endTime);
            jsonObj.put("qtUserId",qtUserId);
            jsonObj.put("qtTitle",qtTitle);
            jsonObj.put("startRowNum",startRowNum);
            jsonObj.put("endRowNum",endRowNum);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG,"jsonString:"+"{"+"\"request\""+":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_USER_ASK_QUESTION)
                .tag(Urls.ACTION_USER_ASK_QUESTION)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG,"onResponse:" + s);
                    }
                });

    }

    /**
     * 获取用户解答的问题
     * @param userID
     * @param beginTime
     * @param endTime
     * @param aqUserId
     * @param startRowNum
     * @param endRowNum
     */
    public static void getUserAnsweredQuestions(String userID,String beginTime,String endTime,String aqUserId ,
                                           String startRowNum,String endRowNum){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",userID);
            jsonObj.put("beginTime",beginTime);
            jsonObj.put("endTime",endTime);
            jsonObj.put("aqUserId",aqUserId);
            jsonObj.put("startRowNum",startRowNum);
            jsonObj.put("endRowNum",endRowNum);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG,"jsonString:"+"{"+"\"request\""+":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_USER_ANSWER_QUESTION)
                .tag(Urls.ACTION_USER_ANSWER_QUESTION)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG,"onResponse:" + s);
                    }
                });
    }

    /**
     * 获取用户解答的问题
     * @param userID
     * @param feedBackContent
     */
    public static void feedBack(String userID,String feedBackContent){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",userID);
            jsonObj.put("comment",feedBackContent);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG,"jsonString:"+"{"+"\"request\""+":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_FEEDBACK)
                .tag(Urls.ACTION_FEEDBACK)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG,"onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_FEEDBACK;
                        msg.obj=(String)s;
                        mHandler.sendMessageDelayed(msg,1500);
                    }
                });
    }
}
