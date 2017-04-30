package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.VoiceCallActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpMasterAnwserQAListResponse;
import com.xnf.henghenghui.model.HttpMasterArtListResponse;
import com.xnf.henghenghui.model.HttpMasterDetailResponse;
import com.xnf.henghenghui.model.HttpMasterListResponse;
import com.xnf.henghenghui.model.HttpMasterQAListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.MD5Calculator;
import com.xnf.henghenghui.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.MemoryHandler;

public class MasterDetailActivity extends BaseActivity implements View.OnClickListener {

    public static boolean DATA_CHANGE = false;

    private ImageView mBackImg;
    private TextView mTitle;
    private ImageView mRightImg;
    private View mLayoutChat;
    private View mLayoutPhone;
    private RoundImageView mMasterImage;
    private TextView mMasterName;
    private TextView mMasterDuty;
    private TextView mMasterAnswer;
    private TextView mMasterCompany;
    private TextView mMasterHot;
    private TextView mMasterLevel;
    private View mMasterSign;
    private TextView mDetailTitle;
    private TextView mDetailDetail1;
    private TextView mDetailDetail2;
    private View mDetailOpen;
    private View mDetailClose;
    private ViewGroup mLayoutQaMain;
    private View mMoreQA;
    private ViewGroup mLayoutArtMain;
    private View mMoreArt;

    private ProgressDialog dialog;

    private String mMasterId;
    private HttpMasterDetailResponse mHttpMasterDetailResponse;
    private HttpMasterAnwserQAListResponse mHttpMasterAnwserQAListResponse;
    private HttpMasterArtListResponse mHttpMasterArtListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DATA_CHANGE = false;
    }

    @Override
    protected void initData() {
        initDialog();
    }

    private void initDialog(){
        dialog = new ProgressDialog(MasterDetailActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        dialog.setMessage(getString(R.string.progress_doing));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_master_detail);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.hengheng_master_detail_title);
        mRightImg = (ImageView)findViewById(R.id.img_right);
        mRightImg.setImageResource(R.drawable.master_detail_right);
        mRightImg.setVisibility(View.VISIBLE);

        mLayoutChat = findViewById(R.id.layout_start_chat);
        mLayoutPhone = findViewById(R.id.layout_start_phone);

        mMasterImage = (RoundImageView)findViewById(R.id.master_image);
        mMasterName = (TextView)findViewById(R.id.master_name);
        mMasterDuty = (TextView)findViewById(R.id.master_duty);
        mMasterAnswer = (TextView)findViewById(R.id. master_answer);
        mMasterCompany = (TextView)findViewById(R.id.master_company);
        mMasterHot = (TextView)findViewById(R.id.master_hot);
        mMasterLevel = (TextView)findViewById(R.id.master_level);
        mMasterSign = findViewById(R.id.master_sign);
        mDetailTitle = (TextView)findViewById(R.id.detail_title);
        mDetailDetail1 = (TextView)findViewById(R.id.detail_detail_1);
        mDetailDetail2 = (TextView)findViewById(R.id.detail_detail_2);
        mDetailOpen = findViewById(R.id.detail_open);
        mDetailClose = findViewById(R.id.detail_close);

        mLayoutQaMain = (ViewGroup)findViewById(R.id.layout_qa_main);
        mMoreQA = findViewById(R.id.more_qa);

        mLayoutArtMain = (ViewGroup)findViewById(R.id.layout_art_main);
        mMoreArt = findViewById(R.id.more_art);

//        View view1 = getLayoutInflater().inflate(R.layout.master_detail_qa_item,null);
//        ((TextView)view1.findViewById(R.id.master_qa_item)).setText("医师诊断猪患了心脏病变，建议增加维生素E的用量，有何建议？");
//        mLayoutQaMain.addView(view1);
//        View view2 = getLayoutInflater().inflate(R.layout.master_detail_qa_item,null);
//        ((TextView)view2.findViewById(R.id.master_qa_item)).setText("医师诊断猪患了心脏病变，建议增加维生素E的用量，有何建议？");
//        mLayoutQaMain.addView(view2);

        mMasterId = getIntent().getStringExtra("masterid");

        bindOnClickLister(this, mBackImg, mRightImg, mLayoutChat, mLayoutPhone, mDetailOpen, mDetailClose, mMoreQA, mMoreArt);

        initMaster(true);
//        loadQa();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(DATA_CHANGE){
            initMaster(true);
        }
    }

    private void initMaster(boolean sp){
        if(sp){
            if(dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
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
                        mHandler.sendMessage(msg);
                    }
                });
    }

    private void loadQa(){
        mLayoutQaMain.removeAllViews();
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("beginTime", "");
            jsonObj.put("endTime", "");
            jsonObj.put("aqUserId", mHttpMasterDetailResponse.getResponse().getContent().getUserId());
            jsonObj.put("startRowNum", "1");
            jsonObj.put("endRowNum", "4");
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_EXPERTS_ANSWER_QUESTION)
                .tag(Urls.ACTION_EXPERTS_ANSWER_QUESTION)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_MASTER_ANSWER_QUESTION;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    private void loadArt(){
        mLayoutArtMain.removeAllViews();
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("topicId", "");
            jsonObj.put("expertId", mHttpMasterDetailResponse.getResponse().getContent().getUserId());
            jsonObj.put("keyWord", "");
            jsonObj.put("commend", "");
            jsonObj.put("startRowNum", "1");
            jsonObj.put("endRowNum", "3");
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_SUBJECT_ARTICLE_LIST)
                .tag(Urls.ACTION_SUBJECT_ARTICLE_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_ARTICLE_LIST;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_right:
                ;
                break;
            case R.id.layout_start_chat:
                if(mHttpMasterDetailResponse != null) {
                    String username = mHttpMasterDetailResponse.getResponse().getContent().getUserId();
                    String chatuserid = MD5Calculator.calculateMD5(username);
                    if(chatuserid.equals(EMClient.getInstance().getCurrentUser())){
                        Toast.makeText(this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(this, ChatActivity.class).putExtra("userId", chatuserid));
                    }
                }
                break;
            case R.id.layout_start_phone:
                if (!EMClient.getInstance().isConnected()) {
                    Toast.makeText(this, R.string.not_connect_to_server, Toast.LENGTH_LONG).show();
                } else {
                    String username = mHttpMasterDetailResponse.getResponse().getContent().getUserId();
                    String chatuserid = MD5Calculator.calculateMD5(username);
                    if(chatuserid.equals(EMClient.getInstance().getCurrentUser())){
                        Toast.makeText(this, R.string.Cant_phone_with_yourself, Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(this, VoiceCallActivity.class).putExtra("username", chatuserid)
                                .putExtra("isComingCall", false));
                    }
                }

//                Intent it = new Intent();
//                it.setAction(Intent.ACTION_CALL);
//                it.setData(Uri.parse("tel:" + mHttpMasterDetailResponse.getResponse().getContent().getMobile()));
//                startActivity(it);
//
//                JSONObject jsonObj = new JSONObject();
//                try {
//                    jsonObj.put("userId", mHttpMasterDetailResponse.getResponse().getContent().getMobile());
//                    jsonObj.put("callBeginTime", "2016-3-4 23:23:34");
//                    jsonObj.put("callEndTime", "2016-3-4 23:23:34");
//                    jsonObj.put("callLong", "10");
//                    jsonObj.put("score", "3");
//                } catch (Exception e) {
//                }
//                String jsonString = jsonObj.toString();
//                OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_RECORD_CALL_HISTORY)
//                        .tag(Urls.ACTION_RECORD_CALL_HISTORY)
//                        .postJson(getRequestBody(jsonString))
//                        .execute(new MyJsonCallBack<String>() {
//                            @Override
//                            public void onResponse(String s) {
//                                L.d(TAG, "onResponse:" + s);
//                            }
//                        });

                break;
            case R.id.detail_open:
                mDetailDetail1.setVisibility(View.GONE);
                mDetailOpen.setVisibility(View.GONE);
                mDetailDetail2.setVisibility(View.VISIBLE);
                mDetailClose.setVisibility(View.VISIBLE);
                break;
            case R.id.detail_close:
                mDetailDetail1.setVisibility(View.VISIBLE);
                mDetailOpen.setVisibility(View.VISIBLE);
                mDetailDetail2.setVisibility(View.GONE);
                mDetailClose.setVisibility(View.GONE);
                break;
            case R.id.more_qa: {
                    Intent intent = new Intent(this, WhoAnwserQuestionActivity.class);
                    intent.putExtra("userid", mHttpMasterDetailResponse.getResponse().getContent().getUserId());
                    intent.putExtra("username", mHttpMasterDetailResponse.getResponse().getContent().getUserName());
                    Utils.start_Activity(this, intent);
                }
                break;
            case R.id.more_art: {
                    Intent intent = new Intent(this, WhoArtActivity.class);
                    intent.putExtra("userid", mHttpMasterDetailResponse.getResponse().getContent().getUserId());
                    intent.putExtra("username", mHttpMasterDetailResponse.getResponse().getContent().getUserName());
                    Utils.start_Activity(this, intent);
                }
                break;
            default:
                break;
        }
        //onPrepareOptionsMenu(menu);
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    public boolean handleNotifyMessage(Message msg) {
        return false;
    }

    @Override
    public boolean handleUIMessage(Message msg) {
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_DETAIL_MASTER: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpMasterDetailResponse = gson.fromJson(response, HttpMasterDetailResponse.class);
                    freshMasterUI();
                    loadQa();
                    loadArt();
                } else {
                    ToastUtil.showToast(this,getString(R.string.load_master_detail_failed));
                }
                if(dialog != null){
                    dialog.dismiss();
                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_MASTER_ANSWER_QUESTION:{
                String response = (String) msg.obj;
                mLayoutQaMain.removeAllViews();
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpMasterAnwserQAListResponse = gson.fromJson(response, HttpMasterAnwserQAListResponse.class);
                    for(final HttpMasterAnwserQAListResponse.Content content : mHttpMasterAnwserQAListResponse.getResponse().getContent()){
                        View view = getLayoutInflater().inflate(R.layout.master_detail_qa_item,null);
                        ((TextView)view.findViewById(R.id.master_qa_item)).setText(content.getQtDesc());
                        mLayoutQaMain.addView(view);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MasterDetailActivity.this, QuestionDetailActivity.class);
                                intent.putExtra("qtid", content.getQtId());
                                Utils.start_Activity(MasterDetailActivity.this, intent);
                            }
                        });
                    }
                } else {
                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_ARTICLE_LIST:{
                String response = (String) msg.obj;
                mLayoutArtMain.removeAllViews();
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpMasterArtListResponse = gson.fromJson(response, HttpMasterArtListResponse.class);
                    for(final HttpMasterArtListResponse.Content content : mHttpMasterArtListResponse.getResponse().getContent()){
                        View view = getLayoutInflater().inflate(R.layout.master_detail_qa_item,null);
                        ((TextView)view.findViewById(R.id.master_qa_item)).setText(content.getArtTitle());
                        mLayoutArtMain.addView(view);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Intent intent = new Intent(MasterDetailActivity.this, QuestionDetailActivity.class);
//                                intent.putExtra("qtid", content.getQtId());
//                                Utils.start_Activity(MasterDetailActivity.this, intent);
                                Intent intent = new Intent(MasterDetailActivity.this, ArticleDetailActivity2.class);
                                intent.putExtra("ARTICLE_ID",content.getArtId());
                                Utils.start_Activity(MasterDetailActivity.this, intent);
                            }
                        });
                    }
                } else {
                }
                break;
            }
        }
        return false;
    }

    private void freshMasterUI(){
        if(mHttpMasterDetailResponse == null){
            return;
        }
        if(mHttpMasterDetailResponse.getResponse().getContent().getPhoto()==null ||
                mHttpMasterDetailResponse.getResponse().getContent().getPhoto().isEmpty()){
            mMasterImage.setImageResource(R.drawable.shouyi2);
        }else{
            mImageLoader.displayImage(mHttpMasterDetailResponse.getResponse().getContent().getPhoto(), mMasterImage);
        }
//        mMasterImage.setImageResource(R.drawable.shouyi2);
        mMasterName.setText(mHttpMasterDetailResponse.getResponse().getContent().getUserName());
        mMasterDuty.setText(mHttpMasterDetailResponse.getResponse().getContent().getTitles());
        mMasterAnswer.setText(getString(R.string.master_detail_answer, mHttpMasterDetailResponse.getResponse().getContent().getAnswerQtNum()));
        mMasterCompany.setText(mHttpMasterDetailResponse.getResponse().getContent().getCompany());
        mMasterHot.setText(getString(R.string.master_detail_hot, mHttpMasterDetailResponse.getResponse().getContent().getFocus()));
        mMasterLevel.setText(getString(R.string.master_detail_level, mHttpMasterDetailResponse.getResponse().getContent().getGrade()));
        if("1".equals(mHttpMasterDetailResponse.getResponse().getContent().getCert())) {
            mMasterSign.setVisibility(View.VISIBLE);
        }else{
            mMasterSign.setVisibility(View.GONE);
        }
        mDetailTitle.setText(getString(R.string.master_detail_professional, mHttpMasterDetailResponse.getResponse().getContent().getProfessional()));
        freshMasterDetail();
    }

    private void freshMasterDetail(){
        if(mHttpMasterDetailResponse == null){
            return;
        }
        String detailtxt = mHttpMasterDetailResponse.getResponse().getContent().getDesc();
        if(detailtxt == null){
            detailtxt = "";
        }
//        detailtxt = "abcdefghigklmnabcdefghigklmnabcdefghigklmnabcdefghigklmn";
        int max = 100;
        if(detailtxt.length() > max) {
            String simpledetailtxt = detailtxt.substring(0, max);
            mDetailDetail1.setText(simpledetailtxt);
            mDetailDetail2.setText(detailtxt);
            mDetailDetail1.setVisibility(View.VISIBLE);
            mDetailOpen.setVisibility(View.VISIBLE);
            mDetailDetail2.setVisibility(View.GONE);
            mDetailClose.setVisibility(View.GONE);
        }else{
            mDetailDetail1.setText(detailtxt);
            mDetailDetail2.setText(detailtxt);
            mDetailDetail1.setVisibility(View.VISIBLE);
            mDetailOpen.setVisibility(View.GONE);
            mDetailDetail2.setVisibility(View.GONE);
            mDetailClose.setVisibility(View.GONE);
        }
    }

}
