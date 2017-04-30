package com.xnf.henghenghui.logic;

import android.os.Handler;
import android.os.Message;

import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.ExpertUserInfo;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/22.
 * 获取专家面对面分类的Action
 * 获取专家面对面的问题列表
 * 获取话题分类的Action
 * 获取话题分类列表的Action
 * 发表话题
 */
public class Face2FaceManager extends BaseManager {

    private static final String TAG = Face2FaceManager.class.getSimpleName();

    public static final int LOGIN_OK = 1001;
    public static final int NO_LOGIN = 1002;

    /**
     * 获取专家面对面分类
     */
    public static void getF2FCatergory(String userId, final Handler handler) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_EXPERTFACETOFACE)
                .tag(Urls.ACTION_EXPERTFACETOFACE)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_F2F_CATEGORY;
                        msg.obj = (String) s;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取专家面对面的问题列表
     */
    public static void getF2FQsList(String userId, String qtCategoryId, final Handler handler) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("qtCategoryId", qtCategoryId);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_FACETOFACE_QUESTION_LISY)
                .tag(Urls.ACTION_FACETOFACE_QUESTION_LISY)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_F2F_LIST;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取专家面对面的问题列表
     * TODO:这个接口需要调整为公共接口，传递不同的参数获取不同的评论数据
     */
    public static void getF2FQsCommentList(String userId, String bId, final Handler handler) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("qtId", bId);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GET_COMMENT_LIST)
                .tag(Urls.ACTION_GET_COMMENT_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_F2F_QS_COMMENTLIST;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取话题列表
     *
     */
    public static void getTopicsList(String UserId, final int startRowNum, int endRow, final Handler handler) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", UserId);
            jsonObj.put("startRowNum", String.valueOf(startRowNum));
            jsonObj.put("endRowNum", String.valueOf(endRow));
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_TOPIC_LIST)
                .tag(Urls.ACTION_TOPIC_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_TOPICLIST;
                        msg.obj = (String) s;
                        if(startRowNum==0){
                            msg.arg1 = 0;
                        }else{
                            msg.arg1 = 1;
                        }
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取专家面对面问题详情
     *
     * @param qtId
     */
    public static void getF2fQSDetails(String UserId, String qtId,String qtBeginTime,
                                       String qtEndTime,String startRowNum,String endRowNum) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", UserId);
            jsonObj.put("qtId", qtId);
            jsonObj.put("qtBeginTime", qtBeginTime);
            jsonObj.put("qtEndTime", qtEndTime);
            jsonObj.put("startRowNum", startRowNum);
            jsonObj.put("endRowNum", endRowNum);

        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GET_F2FQS_DETAIL_INFO)
                .tag(Urls.ACTION_GET_F2FQS_DETAIL_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_QUESTIONV2_INFO;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取专家面对面问题详情页的回答列表
     *
     * @param qtId
     */
    public static void getF2fQSDetailsReplyList(String UserId, String qtId,
                                                String startTime, String endTime, String startRow, String endRow, final Handler handler) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", UserId);
            jsonObj.put("qtId", qtId);
            jsonObj.put("qtBeginTime", startTime);
            jsonObj.put("qtEndTime", endTime);
            jsonObj.put("startRowNum", startRow);
            jsonObj.put("endRowNum", endRow);

        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GET_F2FQS_DETAIL_REPLY_LIST)
                .tag(Urls.ACTION_GET_F2FQS_DETAIL_REPLY_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_ANSWER_QUESTION_INFO;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取话题详情
     *
     * @param topicId
     */
    public static void getTopicsDetails(String UserId, String topicId) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", UserId);
            jsonObj.put("topicId", topicId);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_TOPIC_DETAIL)
                .tag(Urls.ACTION_TOPIC_DETAIL)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_TOPICLIST;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取话题详情
     *
     * @param topicId
     */
    public static void publishTopic(String UserId,String topicTitle, String topicId,String topicContent,String opicPhoto) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", UserId);
            jsonObj.put("topicTitle", topicTitle);
            jsonObj.put("topicId", topicId);
            jsonObj.put("topicContent", topicId);
            jsonObj.put("topicId", topicId);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_LOGIN)
                .tag(Urls.ACTION_LOGIN)
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
                });

    }


}