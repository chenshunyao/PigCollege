package com.xnf.henghenghui.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.CourseManager;
import com.xnf.henghenghui.logic.MeetingManager;
import com.xnf.henghenghui.model.HttpConcernResponse;
import com.xnf.henghenghui.model.MeetingDetailResponseModel;
import com.xnf.henghenghui.ui.activities.MeetingDetailActivity;
import com.xnf.henghenghui.ui.base.BaseFragment3;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.ShareDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.StringUtils;
import com.xnf.henghenghui.util.ToastUtil;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MeetingDetailFragment extends BaseFragment3 implements View.OnClickListener {

    private static final String TAG = MeetingDetailFragment.class.getName();
    private LinearLayout mShareLayout;
    private LinearLayout mExpendMore;
    private LinearLayout mAttentation;
    private TextView mMeetingDetail;
    private TextView mMeetingTitle;
    private TextView mMeetingTime;
    private TextView mMeetingEndTime;
    private TextView mMeetingAddress;
    private TextView mMeetingStatus;
    private TextView mMeetingOnlineNum;
    private TextView mAttentationTitle;
    private ImageView mMeetingImage;
    private ImageView mImageSpread;
    private ImageView mImageShrinkUp;
    private static final int MEETING_CONTENT_DESC_MAX_LINE = 3;
    private static final int SHRINK_UP_STATE = 1;// 收起状态
    private static final int SPREAD_STATE = 2;// 展开状态
    private static int mState = SHRINK_UP_STATE;//默认收起状态
    /*private static final String NO_START = "0";
    private static final String LIVING_CONFERENCE = "1";
    private static final String CONFERENCE_END = "2";*/
    private String mId;
    private String mUrl;
    private MeetingDetailThread mDetailThread;
    ProgressDialog mDialog;
    private boolean mProgressShow;
    private static final String OPT_CONCERN_YES = "1";
    private static final String OPT_CONCERN_CANCEL = "0";
    private boolean mIsConcern = false;
    private String mMeetingUrl;
    private String mMeetingName;
    private String mMeetingContent;

    private static final String ADD_FAV_OPT = "1";
    private static final String CANCEL_FAV_OPT = "0";


    public static MeetingDetailFragment getInstance(Bundle bundle) {
        MeetingDetailFragment meetingFragment = new MeetingDetailFragment();
        meetingFragment.setArguments(bundle);
        return meetingFragment;
    }

    @Override
    protected String setFragmentTag() {
        return null;
    }

    @Override
    protected void initData(Bundle bundle) {
        mId = getActivity().getIntent().getStringExtra("cId");
        L.d("csy", "initData meetingdetailFragment initview mId:" + mId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initloadingDialog();
        L.d("csy", "@ MeetingDetailFragment onCreate setHandler mHandler:" + mHandler);
        L.d("csy", "MeetingDetailFragment onCreate setHandler");
        //MeetingManager.setHandler(mHandler);
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d("csy", "MeetingDetailFragment onStart");
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                MeetingManager.requestMeetingDetail(LoginUserBean.getInstance().getLoginUserid(),mId);
            }
        }).start();
        if(mDetailThread == null){
            mDetailThread = new MeetingDetailThread();
            mDetailThread.start();
        }*/
    }

    @Override
    protected void lazyLoad() {
        if (Utils.hasNoNetwork(getActivity())) {
            showSettingInternet();
            return;
        }
        MeetingManager.setHandler(mHandler);
        L.d("csy", "MeetingDetailFragment lazyload");
        if(mDetailThread == null){
            L.d("csy","MeetingDetailFragment lazyload getdata");
            mDialog.show();
            mDetailThread = new MeetingDetailThread();
            mDetailThread.start();
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_meeting_detail, container,
                false);
        mShareLayout = (LinearLayout)view.findViewById(R.id.meeting_detail_share);
        mExpendMore =(LinearLayout)view.findViewById(R.id.meeting_detail_extend);
        mAttentation =(LinearLayout)view.findViewById(R.id.meeting_detail_attentation1);
        mAttentation.setOnClickListener(this);
        mMeetingStatus = (TextView)view.findViewById(R.id.meeting_detail_status);
        mMeetingOnlineNum =(TextView)view.findViewById(R.id.meeting_detail_join);
        mMeetingTitle = (TextView)view.findViewById(R.id.meeting_detail_title1);
        mMeetingTime = (TextView)view.findViewById(R.id.meeting_detail_time);
        mMeetingEndTime = (TextView)view.findViewById(R.id.meeting_detail_endtime);
        mMeetingAddress =(TextView)view.findViewById(R.id.meeting_detail_place);
        mMeetingDetail =(TextView)view.findViewById(R.id.meeting_detail_desc);
        mAttentationTitle =(TextView)view.findViewById(R.id.meeting_detail_attentation);
        mMeetingImage =(ImageView)view.findViewById(R.id.meeting_detail_live);
        mImageSpread =(ImageView)view.findViewById(R.id.meeting_expend_icon);
        mImageShrinkUp =(ImageView)view.findViewById(R.id.meeting_shrinkup_icon);
        mShareLayout.setOnClickListener(this);
        mExpendMore.setOnClickListener(this);
        //mId = getActivity().getIntent().getStringExtra("cId");
        //L.d("csy", "meetingdetailFragment initview mId:" + mId);
        return view;
    }

    private void initloadingDialog(){
        mProgressShow = true;
        mDialog = new ProgressDialog(getContext());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mProgressShow = false;
            }
        });
        mDialog.setMessage(getString(R.string.dl_waiting));
    }

    class MeetingDetailThread extends Thread{
        public volatile boolean exit = false;
        @Override
        public void run() {
            L.d("csy","MeetingDetailThread run exit:"+exit+";mId:"+mId);
            if(!exit) {
                MeetingManager.requestMeetingDetail(LoginUserBean.getInstance().getLoginUserid(), mId);
            }
        }
    }

    private void showSettingInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String ok = getString(R.string.internet_setting);
        String cancle = getString(R.string.cancel);
        builder.setTitle(R.string.info)
                .setMessage(R.string.network_error_em)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                        startActivity(wifiSettingsIntent);
                    }
                })
                .setNegativeButton(cancle, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void setVisible(boolean flag){
        if(mDetailThread == null){
            mDetailThread = new MeetingDetailThread();
        }
        if(flag){
            if(mDetailThread.isAlive()){
                return;
            }
            mDetailThread.exit = false;
            mDetailThread.start();
        }else{
            mDetailThread.exit = true;
        }
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
        switch(msg.what){
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_MEETING_DETAIL:
                String resp  =(String)msg.obj;
                L.d(TAG, "detail Response:" + resp);
                if(mDialog != null && mDialog.isShowing()){
                    mDialog.dismiss();
                }
                if(Utils.getRequestStatus(resp) == 1){
                    L.d(TAG,"resp detail success");
                    Gson gson = new Gson();
                    MeetingDetailResponseModel meetingModel = gson.fromJson(resp, MeetingDetailResponseModel.class);
                    MeetingDetailResponseModel.MeetingContent content = meetingModel.getReponse().getContent();
                    L.d("csy", "meeting detail content:" + content);
                    //vid = content.getvId();
                    mMeetingUrl = Urls.SHARE_MEETING_URL+content.getcId();
                    if(mMeetingImage != null){
                        mImageLoader.displayImage(content.getThumbnailURL(),mMeetingImage,mOptions);
                    }
                    if(mMeetingTitle != null){
                        mMeetingName = content.getTitle();
                        mMeetingTitle.setText(content.getTitle());
                    }
                    if(mMeetingTime != null){
                        mMeetingTime.setText(getActivity().getString(R.string.meeting_time, StringUtils.getShortTime(content.getBeginTime())));
                    }
                    if(mMeetingEndTime != null){
                        mMeetingEndTime.setText(getActivity().getString(R.string.meeting_end_time,StringUtils.getShortTime(content.getEndTime())));
                    }
                    if(mMeetingAddress != null){
                        mMeetingAddress.setText(getActivity().getString(R.string.meeting_place,content.getAddress()));
                    }
                    String cont = content.getContent();
                    mMeetingContent =cont;
                    if(mMeetingDetail != null){
                        if(!TextUtils.isEmpty(cont)){
                            mMeetingDetail.setText(cont);
                        }else{
                            mMeetingDetail.setText(getString(R.string.error_view_no_data));
                        }
                    }
                    mUrl = content.getcURL();
                    if(mMeetingStatus != null){
                        String status = content.getStatus();
                        //L.d("csy_"+TAG,"status:"+status);
                        if(!TextUtils.isEmpty(status)){
                            //0:未开始；1：直播中；2：已结束
                            if(CodeUtil.NO_START.equals(status)){
                                //L.d("csy_"+TAG,"0 mMeetingStatus setText:"+status);
                                mMeetingStatus.setText(getString(R.string.meeting_status0));
                            }else if(CodeUtil.LIVING_CONFERENCE.equals(status)){
                                //L.d("csy_"+TAG,"1 mMeetingStatus setText:"+status);
                                mMeetingStatus.setText(getString(R.string.meeting_status1));
                            }else if(CodeUtil.CONFERENCE_END.equals(status)){
                                //L.d("csy_"+TAG,"2 mMeetingStatus setText:"+status);
                                mMeetingStatus.setText(getString(R.string.meeting_status2));
                            }
                            //L.d("csy_"+TAG,"mMeetingStatus text:"+mMeetingStatus.getText().toString());
                        }
                    }
                    if(mMeetingOnlineNum != null && !TextUtils.isEmpty(content.getOnlineNum())){
                        int onlineNum = Integer.parseInt(content.getOnlineNum());
                        if(onlineNum > 0) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("online_num", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("MEETING_ONLINE_NUM",content.getOnlineNum());
                            editor.apply();
                            mMeetingOnlineNum.setText(getString(R.string.conference_online_num, content.getOnlineNum()));
                        }
                    }
                    if(!TextUtils.isEmpty(content.getIsConcern())){
                        int isConcern = Integer.parseInt(content.getIsConcern());
                        if(isConcern==1){
                            mIsConcern=true;
                            mAttentationTitle.setText(getString(R.string.meeting_attention1,1));
                            mAttentationTitle.setTextColor(getResources().getColor(R.color.tab_text_color_checked));
                        }
                        else{
                            mIsConcern=true;
                            mAttentationTitle.setText(getString(R.string.meeting_attention,0));
                            mAttentationTitle.setTextColor(getResources().getColor(R.color.image_text_light_grey));
                        }
                    }

                }else{
                    L.d(TAG,"resp meeting detail fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_COURSE_FAV_OPT:
                String response1  =(String)msg.obj;
                L.d(TAG, "concern opt Response:" + response1);
                if(Utils.getRequestStatus(response1) == 1) {
                    Gson gson = new Gson();
                    HttpConcernResponse httpCommitQaResponse = gson.fromJson(response1, HttpConcernResponse.class);
                    if (httpCommitQaResponse.getResponse().getSucceed() == 1) {

                        if (!mIsConcern) {
                            mIsConcern = true;
                            Toast.makeText(getActivity(), getString(R.string.op_guanzhu_success),Toast.LENGTH_SHORT).show();
                            mAttentationTitle.setText(getString(R.string.meeting_attention1,1));
                            mAttentationTitle.setTextColor(getResources().getColor(R.color.tab_text_color_checked));
                        } else {
                            mIsConcern = false;
                            mAttentationTitle.setText(getString(R.string.meeting_attention,0));
                            Toast.makeText(getActivity(),getString(R.string.op_cancel_guanzhu_success),Toast.LENGTH_SHORT).show();
                            mAttentationTitle.setTextColor(getResources().getColor(R.color.image_text_light_grey));
                        }
                    }else{
                        ToastUtil.showToast(getActivity(),getString(R.string.op_failed,httpCommitQaResponse.getResponse().getErrorCode(),httpCommitQaResponse.getResponse().getErrorInfo()));
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.meeting_detail_share:
                handleShare();
                break;
            case R.id.meeting_detail_extend:
                if (mState == SPREAD_STATE) {
                    mMeetingDetail.setMaxLines(MEETING_CONTENT_DESC_MAX_LINE);
                    mMeetingDetail.requestLayout();
                    mImageShrinkUp.setVisibility(View.GONE);
                    mImageSpread.setVisibility(View.VISIBLE);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    mMeetingDetail.setMaxLines(Integer.MAX_VALUE);
                    mMeetingDetail.requestLayout();
                    mImageShrinkUp.setVisibility(View.VISIBLE);
                    mImageSpread.setVisibility(View.GONE);
                    mState = SPREAD_STATE;
                }
                break;
            case R.id.meeting_detail_attentation1:
                if (!mIsConcern) {
                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid(), mId, ADD_FAV_OPT);
                } else {
                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid(), mId, CANCEL_FAV_OPT);
                }
                //MeetingManager.requestConcern(LoginUserBean.getInstance().getLoginUserid(), mId,OPT_CONCERN_YES,mHandler);
                break;
            default:
                break;
        }
    }

    public void handleShare() {
        final ShareDialog dialog = new ShareDialog(getActivity(), getActivity());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);

        if(!mMeetingUrl.isEmpty()){
            dialog.setShareInfo(mMeetingName, mMeetingContent, mMeetingUrl);
        }
        dialog.show();
    }
}
