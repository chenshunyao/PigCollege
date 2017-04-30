package com.xnf.henghenghui.logic;

import android.os.Handler;
import android.os.Message;

import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/10.
 */
public class MeetingManager extends BaseManager {
    private static final String TAG = MeetingManager.class.getName();

    /**
     * 会评列表接口
     */
    public static void requestMeetingList(String userId ,String beginTime, String title,String state ,final Handler handler) {
        //String userId = "67890113";
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("beginTime", beginTime);
            jsonObj.put("title", title);
            jsonObj.put("state",state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObj.toString();
        L.d("csy", "meetinglist jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_MEETING_INFO_LIST)
                .tag(Urls.ACTION_MEETING_INFO_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","meetinglist onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_MEETING_LIST;
                        msg.obj =(String)s;
                        handler.sendMessage(msg);
                    }
                });
    }
    /**
     * 会评详情接口
     */
    public static void requestMeetingDetail(String userId ,String cId){
        //String userId = "67890113";
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("cId", cId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObj.toString();
        L.d("csy", "meetingDetail jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        L.d("csy","requestMeetingDetail mHandler:"+mHandler);
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_MEETING_INFO_DETAIL)
                .tag(Urls.ACTION_MEETING_INFO_DETAIL)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","meetingdetail onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_MEETING_DETAIL;
                        msg.obj =(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }
    /**
     * 直播室列表接口
     */
    public static void requestLiveMeetingList(String userId ,String cId, String startRowNo,String endRowNo,final Handler handler){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("cId",cId);
            jsonObj.put("startRowNum", startRowNo);
            jsonObj.put("endRowNum", endRowNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObj.toString();
        L.d("csy", "liveConferenceList jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        L.d("csy","requestLiveMeetingList mHandler:"+mHandler);
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_LIVE_CONFERENCE_LIST)
                .tag(Urls.ACTION_LIVE_CONFERENCE_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","liveconference list onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_LIVECONFERENCE_LIST;
                        msg.obj =(String)s;
                        handler.sendMessage(msg);
                    }
                });
    }
    /**
     * 听课笔记接口
     */
    public static void requestNoteList(String userId ,String cId){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("cId", cId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObj.toString();
        L.d("csy", "NoteList jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CONFERENCE_NOTE_LIST)
                .tag(Urls.ACTION_CONFERENCE_NOTE_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","conference note list onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_NOTE_LIST;
                        msg.obj =(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }
    /**
     * 会评笔记接口
     */
    public static void requestNoteInfo(String userId ,String cId){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("cId", cId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObj.toString();
        L.d("csy", "NoteInfo jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CONFERENCE_NOTE_INFO)
                .tag(Urls.ACTION_CONFERENCE_NOTE_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","conference note info onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_NOTE_INFO;
                        msg.obj =(String)s;
                        mHandler.sendMessage(msg);
                    }
                });
    }
    /**
     * 会评评论接口
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
        L.d("csy", "CommentList jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CONFERENCE_COMMENT_LIST)
                .tag(Urls.ACTION_CONFERENCE_COMMENT_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","conference comment list onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_COMMENT_LIST;
                        msg.obj =(String)s;
                        handler.sendMessage(msg);
                    }
                });
    }
    /**
     * 发布会评评论接口
     */
    public static void requestAddComment(String userId,String bId,String typeId,String content,final Handler handler){
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
                        L.d("csy","add comment onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_ADD_COMMENT;
                        msg.obj =(String)s;
                        handler.sendMessage(msg);
                    }
                });
    }
    /**
     * 点赞操作
     * @param Id
     */
    public static void requestGivePraise(String userId ,String Id,String optFlag,final Handler handler){
        if(Id == null || "".equals(Id))return;
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
                        L.d("csy","meeting give praise onResponse:"+s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_GIVE_PRAISE;
                        msg.obj =(String)s;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 关注操作
     * @param Id
     */
    public static void requestConcern(String userId ,String Id,String optFlag,final Handler handler){
        if(Id == null || "".equals(Id))return;
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("userId",userId);
            jsonObj.put("keyId",Id);
            jsonObj.put("optFlag",optFlag);
        }catch(Exception e){
            e.printStackTrace();
        }

        String jsonStr = jsonObj.toString();
        L.d("csy","give attentation jsonStr:"+ "{" + "\"request\"" + ":" + jsonStr + "}");
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CONCERN)
                .tag(Urls.ACTION_CONCERN)
                .postJson(getRequestBody(jsonStr))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d("csy","meeting give praise onResponse:"+s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONCERN;
                        msg.obj =(String)s;
                        handler.sendMessage(msg);
                    }
                });
    }
}
