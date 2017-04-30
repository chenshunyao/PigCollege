package com.xnf.henghenghui.logic;

import android.net.Uri;
import android.os.Message;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.PostRequest;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import java.io.File;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/22.
 */
public class MainPageManager extends BaseManager {

    private static final String TAG = MainPageManager.class.getSimpleName();

    public static void checkAppUpdate(int currentVersion) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("currentVersion", String.valueOf(currentVersion));
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CHECK_UPDATE)
                .tag(Urls.ACTION_CHECK_UPDATE)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CHECK_UPDATE;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取首页的广告
     * @param UserID
     */
    public static void getIndexBanner(String UserID,String adType) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",UserID);
            jsonObj.put("adType",adType);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_INDEXBANNER)
                .tag(Urls.ACTION_INDEXBANNER)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_INDEX_BANNER;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }


    /**
     * 获取首页推荐的专家面对面
     * @param UserID
     */
    public static void getF2fHotList(String UserID) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",UserID);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_EXPERTS_INFO_LIST)
                .tag(Urls.ACTION_EXPERTS_INFO_LIST)
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
     * 获取首页推荐的专家面对面
     * @param UserID
     */
    public static void getTopicHotList(String UserID) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",UserID);
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
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取首页推荐的热门文章
     * @param UserID
     */
    public static void getTopicHotArticleList(String UserID) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",UserID);
            jsonObj.put("columnCode","1002");
            jsonObj.put("keyWord","");
            jsonObj.put("startRowNum","");
            jsonObj.put("endRowNum","");
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_HOT_ARTICLE_LIST)
                .tag(Urls.ACTION_TOPIC_ARTICLE_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_TOPIC_ARTICLE_LIST;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取首页推荐的热门文章
     * @param UserID
     */
    public static void getTopicHotQtTpList(String UserID,int questionCount,int topicCount) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",UserID);
            jsonObj.put("questionCount",String.valueOf(questionCount));
            jsonObj.put("topicCount",String.valueOf(topicCount));
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_HOT_QT_TP_LIST)
                .tag(Urls.ACTION_HOT_QT_TP_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_HOTPT_HOTTP;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }


    /**
     * 获取点赞数，评论数，收藏数
     * @param UserID
     */
    public static void getPraiseBrowseReplyCount(String UserID,String keyId,String keyType) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",UserID);
            jsonObj.put("keyId",keyId);
            jsonObj.put("keyType",keyType);
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_PRAISE_REPLY_COUNT)
                .tag(Urls.ACTION_PRAISE_REPLY_COUNT)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_PRAISE_BROWSER_REPLAY_COUNT;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    public static void upLoadImage(String imagePath,String imageName){
        PostRequest request = OkHttpUtils.post(Urls.SERVER_URL_ + Urls.ACTION_UPLOAD_IMAGE)
                .tag(Urls.ACTION_UPLOAD_IMAGE);
       request.params(imageName,new File(Uri.parse(imagePath).getPath()), imageName + ".png");

        request.execute(new MyJsonCallBack<String>() {
            @Override
            public void onResponse(String s) {
                L.d(TAG, "onResponse:" + s);

            }
        });
    }
}
