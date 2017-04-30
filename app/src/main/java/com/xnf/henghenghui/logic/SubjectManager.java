package com.xnf.henghenghui.logic;

import android.net.Uri;
import android.os.Message;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.PostRequest;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2016/1/22.
 */
public class SubjectManager extends BaseManager {

    private static final String TAG = SubjectManager.class.getSimpleName();

    /**
     * 获取首页推荐的热门文章
     * @param UserID
     */
    public static void getSubjectList(String UserID,String topicId,String keyWord,
                                      String commend,String startRow,String endRow) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",UserID);
            jsonObj.put("topicId",topicId);
            jsonObj.put("keyWord",keyWord);
            jsonObj.put("commend",commend);
            jsonObj.put("startRowNum",startRow);
            jsonObj.put("endRowNum",endRow);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_SUBJECT_LIST)
                .tag(Urls.ACTION_SUBJECT_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_LIST;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取首页推荐的热门文章
     * @param UserID
     */
    public static void getSubjectDetail(String UserID,String subjectId) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",UserID);
            jsonObj.put("topicId",subjectId);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_SUBJECT_DETAIL)
                .tag(Urls.ACTION_SUBJECT_DETAIL)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_DETAIL;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }



    /**
     * 获取首页推荐的热门文章
     * @param UserID
     */
    public static void getSubjectArticleList(String UserID,String topicId,String keyWord,
                                      String commend,String startRow,String endRow) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",UserID);
            jsonObj.put("topicId",topicId);
            jsonObj.put("keyWord",keyWord);
            jsonObj.put("commend",commend);
            jsonObj.put("startRowNum",startRow);
            jsonObj.put("endRowNum",endRow);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_SUBJECT_ARTICLE_LIST)
                .tag(Urls.ACTION_SUBJECT_ARTICLE_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_ARTICLE_LIST;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

}
