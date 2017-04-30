package com.xnf.henghenghui.logic;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/4/6.
 */
public class CourseManager extends BaseManager{
    private static final String TAG = CourseManager.class.getName();

    /**
     * 课堂广告
     */
    public static void requestBanner(String userId){
        //String userId = "67890113";//LoginUserBean.getInstance().getLoginUserid();
        Log.d(TAG, "requestBanner userid:"+userId);
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("userId", userId);
        }catch(Exception e){
            e.printStackTrace();
        }
        String jsonString = jsonObj.toString();
        L.d("csy", "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CLASS_BANNER)
                .tag(Urls.ACTION_CLASS_BANNER)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_BANNER;
                        msg.obj=(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }
    /**
     * 课堂视频列表
     * param1:视频类型
     * param2:起始行号
     * param3:结束行号
     */
    public static void requestCourseList(String userId,String type,String start,String end){
        //String userId = "67890113";
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("userId", userId);
            jsonObj.put("videoType",type);
            jsonObj.put("startRowNum",start);
            jsonObj.put("endRowNum",end);
        }catch(Exception e){
            e.printStackTrace();
        }
        final int videoType = Integer.valueOf(type);
        String jsonString = jsonObj.toString();
        L.d("csy", "courselist jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CLASS_VIDEO_LIST)
                .tag(Urls.ACTION_CLASS_VIDEO_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>(){
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","courselist onResponse:" + s);
                        Message msg = new Message();
                        if(videoType == 1){
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_FREEVIDEO_LIST;
                        }else if(videoType == 2){
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_HOTVIDEO_LIST;
                        }else if(videoType == 3){
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_DISCOUNTVIDEO_LIST;
                        }
                        msg.obj =(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 课程视频详情
     * @param vId
     */
    public static void requesetCourseDetail(String userId ,String vId){
        if(vId == null || "".equals(vId))return;
        //String userId = "67890113";
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("userId", userId);
            jsonObj.put("vId",vId);
        }catch(Exception e){
            e.printStackTrace();
        }

        String jsonString = jsonObj.toString();
        L.d("csy", "courselist jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CLASS_VIDEO_DETAIL)
                .tag(Urls.ACTION_CLASS_VIDEO_DETAIL)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","videodetail onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_VIDEO_DETAIL;
                        msg.obj=(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 查询是否收藏
     */
    public static void requestFavCount(String userId,String startNum,String endNum,String keyType){
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("userId", userId);
            jsonObj.put("startRowNum",startNum);
            jsonObj.put("endRowNum",endNum);
            jsonObj.put("keyFlag",keyType);
        }catch(Exception e){
            e.printStackTrace();
        }

        String jsonString = jsonObj.toString();
        L.d("csy", "favlist jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GET_FAVORITELIST)
                .tag(Urls.ACTION_GET_FAVORITELIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>(){
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","favlist onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_VIDEO;
                        msg.obj =(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 查询点赞总数
     * @param Id
     */
    public static void requestPraiseCount(String userId ,String Id){
        if(Id == null || "".equals(Id))return;
        //String userId = "13971457654";
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("userId",userId);
            jsonObj.put("keyId",Id);
        }catch(Exception e){
            e.printStackTrace();
        }

        String jsonStr = jsonObj.toString();
        L.d("csy","praiseCount jsonStr:"+ "{" + "\"request\"" + ":" + jsonStr + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_PRAISE_REPLY_COUNT)
                .tag(Urls.ACTION_PRAISE_REPLY_COUNT)
                .postJson(getRequestBody(jsonStr))
                .execute(new MyJsonCallBack<String>(){
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","praiseCount onResponse:"+s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_PRAISE_REPLY_COUNT;
                        msg.obj =(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 点赞操作
     * @param Id
     */
    public static void requestGivePraise(String userId ,String Id,String optFlag){
        if(Id == null || "".equals(Id))return;
        //String userId = "13971457654";
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("userId",userId);
            jsonObj.put("keyId",Id);
            jsonObj.put("optFlag",optFlag);
        }catch(Exception e){
            e.printStackTrace();
        }

        String jsonStr = jsonObj.toString();
        L.d("csy","give Praise jsonStr:"+ "{" + "\"request\"" + ":" + jsonStr + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GIVE_PRAISE)
                .tag(Urls.ACTION_GIVE_PRAISE)
                .postJson(getRequestBody(jsonStr))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","give praise onResponse:"+s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_GIVE_PRAISE;
                        msg.obj =(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    /**
     * 课程评论列表接口
     */
    public static void requestCommentList(String userId ,String bId,final Handler handler){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("bId", bId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObj.toString();
        L.d("csy", "Course CommentList jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CONFERENCE_COMMENT_LIST)
                .tag(Urls.ACTION_CONFERENCE_COMMENT_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","Course comment list onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_COMMENT_LIST;
                        msg.arg1 = 0;
                        msg.obj =(String)s;
                        handler.sendMessage(msg);
                    }
                });
    }
    /**
     * 发布课程评论接口
     */
    public static void requestAddComment(String userId,String bId,String typeId,String content){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("bId", bId);
            jsonObj.put("typeId",typeId);
            jsonObj.put("content",content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObj.toString();
        L.d("csy", "add comment jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CONFERENCE_ADD_COMMENT)
                .tag(Urls.ACTION_CONFERENCE_ADD_COMMENT)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","add course comment onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_ADD_COMMENT;
                        msg.obj =(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }
    /**
     * 课程收藏操作接口
     */
    public static void requestFavOpt(String userId,String keyId,String optFlag){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("keyId", keyId);
            jsonObj.put("optFlag",optFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObj.toString();
        L.d("csy", "course fav jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_COURSE_FAV_OPT)
                .tag(Urls.ACTION_COURSE_FAV_OPT)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String resp) {
                        L.d("csy","fav course opt onResponse:" + resp);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_COURSE_FAV_OPT;
                        msg.obj =(String)resp;
                        mHandler.sendMessage(msg);
                    }
                });
    }
    /**
     * 专家详情接口
     */
    public static void requestCourseExpert(String userId ,String mMasterId,final Handler handler){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("expertsId", mMasterId);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_EXPERTS_INFO)
                .tag(Urls.ACTION_EXPERTS_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_DETAIL_MASTER;
                        msg.obj = s;
                        handler.sendMessage(msg);
                    }
                });
    }
}
