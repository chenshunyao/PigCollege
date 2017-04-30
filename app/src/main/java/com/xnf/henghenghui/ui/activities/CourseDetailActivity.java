package com.xnf.henghenghui.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.CourseManager;
import com.xnf.henghenghui.model.CommentModel;
import com.xnf.henghenghui.model.ConferenceCommentListModel;
import com.xnf.henghenghui.model.CourseModel;
import com.xnf.henghenghui.model.FavCountResponseModel;
import com.xnf.henghenghui.model.GivePraiseModel;
import com.xnf.henghenghui.model.HttpCommitQaResponse;
import com.xnf.henghenghui.model.HttpMasterDetailResponse;
import com.xnf.henghenghui.model.PraiseCountResponseModel;
import com.xnf.henghenghui.model.VideoDetailResponseModel;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.CustomListView;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.ToastUtil;
/*import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;*/
import com.youku.cloud.module.PlayerErrorInfo;
import com.youku.cloud.player.PlayerListener;
import com.youku.cloud.player.VideoDefinition;
import com.youku.cloud.player.YoukuPlayerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class CourseDetailActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private static final String TAG = "csy_CourseDetailActivity";
    private ImageView mBackImg;
    private ImageView mShareImg;
    private TextView mTitle;
    private CustomListView mListView;
    private CustomListView mRecommendList;
    private RelativeLayout mTitleLayout;
    private LinearLayout mExpendMore;
    private RelativeLayout mMasterDetail;
    private LinearLayout mSendComment;
    private LinearLayout mGivePraise;
    private ImageView mImageSpread;
    private ImageView mImageShrinkUp;
    //private TextView mCourseDetail;
    private ImageView mZhan;
    private ImageView mCollect;
    private TextView mCollectTxt;
    private LinearLayout mFav;
    //课程名称
    private TextView mCourseTitle;
    private TextView mCoursePrice;
    private TextView mCourseCount;
    //private TextView mCourseDesc;
    private TextView mCourseTime;
    private TextView mPraiseCount;
    private TextView mTvOne;
    //评论
    private LinearLayout mCommentLinear;
    private TextView mCommentTitle;
    private TextView mCommentMore;
    private EditText mEditComment;
    private Button mAddComment;
    private android.view.animation.Animation mAnimation;
    //课程专家Id
    private ImageView mExpertIcon;
    private TextView mExpertName;
    private TextView mExpertOffice;
    private TextView mExpertPosition;
    private HttpMasterDetailResponse mHttpMasterDetailResponse;
    private String mExpertId;
    private CourseModel mCourse;
    //private YoukuBasePlayerManager basePlayerManager;
    // 播放器控件
    private YoukuPlayerView mYoukuPlayerView;
    // YoukuPlayer实例，进行视频播放控制
    //private YoukuPlayer youkuPlayer;
    // 需要播放的视频id
    private String vid;
    private int mType;
    private List<CommentModel> datas;
    private List<CourseModel> mReList;
    //推荐视频
    private TextView mReVidoeMore;
    private ArrayList<CourseModel> cList;
    private boolean mIsFullScreen = false;
    private CommentAdapter mCommentAdapter;
    private RecommendAdapter mReAdapter;
    private static final int VIDEO_CONTENT_DESC_MAX_LINE = 2;
    private static final int SHRINK_UP_STATE = 1;// 收起状态
    private static final int SPREAD_STATE = 2;// 展开状态
    private static int mState = SHRINK_UP_STATE;//默认收起状态
    private static final int LOAD_DATA_FINISH = -1;
    private static final String GIVE_PRAISE_TYPE = "1";
    private static final String CANCEL_PRAISE_TYPE = "2";
    private static final String ADD_FAV_OPT = "1";
    private static final String CANCEL_FAV_OPT = "0";
    private boolean mIsFav = false;
    @Override
    protected void initData() {
        getIntentData(getIntent());
        datas = new ArrayList<CommentModel>();
        //获取评论列表
        //datas = getCourseComments();
        CourseManager.setHandler(mHandler);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_course_detail);
        mYoukuPlayerView = (YoukuPlayerView)findViewById(R.id.course_player);
        // 初始化播放器
        mYoukuPlayerView.attachActivity(this);
        mYoukuPlayerView.setPreferVideoDefinition(VideoDefinition.VIDEO_HD);
        mYoukuPlayerView.setShowBackBtn(true);
        mYoukuPlayerView.setPlayerListener(new MyPlayerListener());
        mListView =(CustomListView)findViewById(R.id.detail_comment_list);
        mRecommendList =(CustomListView)findViewById(R.id.detail_recommend_list);
        if(mReList != null){
            mReAdapter = new RecommendAdapter(CourseDetailActivity.this,mReList);
            mRecommendList.setAdapter(mReAdapter);
        }
        mRecommendList.setOnItemClickListener(this);
        //mCourseDetail =(TextView)findViewById(R.id.course_detail_desc);
        mCourseTitle =(TextView)findViewById(R.id.course_detail_title);
        mCoursePrice =(TextView)findViewById(R.id.course_detail_price);
        mCourseCount = (TextView)findViewById(R.id.course_detail_time);
        //mCourseDesc =(TextView)findViewById(R.id.course_detail_desc);
        mCourseTime =(TextView)findViewById(R.id.course_detail_duration);
        mPraiseCount =(TextView)findViewById(R.id.course_detail_zhan);
        mTvOne =(TextView)findViewById(R.id.tv_one);
        mTitleLayout =(RelativeLayout)findViewById(R.id.course_title_layout);
        mExpendMore =(LinearLayout)findViewById(R.id.course_detail_expend);
        mMasterDetail =(RelativeLayout)findViewById(R.id.course_master);
        mCommentLinear = (LinearLayout)findViewById(R.id.course_comment);
        mCommentTitle =(TextView)findViewById(R.id.course_detail_comment);
        mEditComment =(EditText)findViewById(R.id.course_comment_text);
        mAddComment =(Button)findViewById(R.id.course_comment_add);
        //mAddComment.setOnClickListener(this);
        mSendComment =(LinearLayout)findViewById(R.id.send_comment_layout);
        mGivePraise =(LinearLayout)findViewById(R.id.course_give_praise);
        mCollect =(ImageView)findViewById(R.id.course_collect);
        mFav =(LinearLayout)findViewById(R.id.fav_course_opt);
        mCollectTxt =(TextView)findViewById(R.id.course_detail_correct);
        mCollectTxt.setText(R.string.add_favorite);
        //mGivePraise.setOnClickListener(this);
        mZhan =(ImageView)findViewById(R.id.course_give_zhan);
        mAnimation = AnimationUtils.loadAnimation(CourseDetailActivity.this, R.anim.nn);
        mImageSpread =(ImageView)findViewById(R.id.course_expend_icon);
        mImageShrinkUp =(ImageView)findViewById(R.id.course_shrinkup_icon);
        mShareImg = (ImageView)findViewById(R.id.img_right);
        //mCommentAdapter = new CommentAdapter(getBaseContext(),datas);
        //mListView.setAdapter(mCommentAdapter);
        //setListViewHeightBasedOnChildren(mListView);
        /*mListView.setonLoadListener(new CustomListView.OnLoadListener() {
            @Override
            public void onLoad() {
                //TODO 加载更多
                L.e(TAG, "onLoad");
                loadData();
            }
        });*/
        mExpertIcon =(ImageView)findViewById(R.id.teacher_icon);
        mExpertName =(TextView)findViewById(R.id.teacher_name);
        mExpertOffice =(TextView)findViewById(R.id.teacher_office);
        mExpertPosition =(TextView)findViewById(R.id.teacher_position);
        mBackImg =(ImageView)findViewById(R.id.img_back);
        mShareImg.setImageResource(R.drawable.share_icon);
        mShareImg.setVisibility(View.INVISIBLE);
        mTitle =(TextView)findViewById(R.id.txt_title);
        //课程标题
        //mTitle.setText(getString(R.string.course_detail));
        mTitle.setText(mCourse.getcTitle());
        mBackImg.setVisibility(View.VISIBLE);
        mReVidoeMore =(TextView)findViewById(R.id.recommend_more_course);
        mCommentMore =(TextView)findViewById(R.id.comment_more_course);
        bindOnClickLister(this, mBackImg, mShareImg,mExpendMore,mMasterDetail,mAddComment,mGivePraise,mFav,mReVidoeMore,mCommentMore);
        //initPlay();
        goPlay();
    }

    @Override
    protected void onResume() {
        mYoukuPlayerView.onResume();
        //L.d(TAG,"onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        mYoukuPlayerView.onPause();
        //L.d(TAG,"onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mYoukuPlayerView.onDestroy();
        //L.d(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_right:
                Log.d("csy", "share click!");
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "哼哼课堂");
                intent.putExtra(Intent.EXTRA_TEXT, mCourse.getcDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent,"课程分享"));
                break;
            case R.id.course_detail_expend:
                /*if (mState == SPREAD_STATE) {
                    mCourseDetail.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mCourseDetail.requestLayout();
                    mImageShrinkUp.setVisibility(View.GONE);
                    mImageSpread.setVisibility(View.VISIBLE);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    mCourseDetail.setMaxLines(Integer.MAX_VALUE);
                    mCourseDetail.requestLayout();
                    mImageShrinkUp.setVisibility(View.VISIBLE);
                    mImageSpread.setVisibility(View.GONE);
                    mState = SPREAD_STATE;
                }*/
                break;
            case R.id.course_master:
                Intent intent1 = new Intent(this, MasterDetailActivity.class);
                intent1.putExtra("masterid",mExpertId);
                Utils.start_Activity(this, intent1);
                break;
            case R.id.course_give_praise:
                CourseManager.requestGivePraise(LoginUserBean.getInstance().getLoginUserid() ,vid,GIVE_PRAISE_TYPE);
                mTvOne.setVisibility(View.VISIBLE);
                mTvOne.startAnimation(mAnimation);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTvOne.setVisibility(View.GONE);
                    }
                },700);
                break;
            case R.id.fav_course_opt:
                if(!mIsFav){
                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid() ,vid, ADD_FAV_OPT);
                    //mIsFav = true;
                }else{
                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid() ,vid, CANCEL_FAV_OPT);
                    //mIsFav = false;
                }
                break;
            case R.id.course_comment_add:
                String content = mEditComment.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(getBaseContext(),R.string.add_comment_hint,Toast.LENGTH_SHORT).show();
                    return;
                }
                CourseManager.requestAddComment(LoginUserBean.getInstance().getLoginUserid(),vid,"",content);
                mEditComment.getText().clear();
                break;
            case R.id.recommend_more_course:
                Intent intent2 = new Intent(CourseDetailActivity.this,CourseListActivity.class);
                intent2.putExtra("type",mType == 0 ? 3 : mType);
                Utils.start_Activity(CourseDetailActivity.this,intent2);
                break;
            case R.id.comment_more_course:
                Intent intent3 = new Intent(CourseDetailActivity.this,CommentListActivity.class);
                intent3.putExtra("vId",vid);
                Utils.start_Activity(CourseDetailActivity.this, intent3);
                break;
            default:
                break;
        }
    }

    // 添加播放器的监听器
    private class MyPlayerListener extends PlayerListener {
        @Override
        public void onComplete() {
            // TODO Auto-generated method stub
            super.onComplete();
        }

        @Override
        public void onError(int code, PlayerErrorInfo info) {
            // TODO Auto-generated method stub
            //txt1.setText(info.getDesc());
        }

        @Override
        public void OnCurrentPositionChanged(int msec) {
            // TODO Auto-generated method stub
            super.OnCurrentPositionChanged(msec);
        }

        @Override
        public void onVideoNeedPassword(int code) {
            // TODO Auto-generated method stub
            super.onVideoNeedPassword(code);
        }

        @Override
        public void onVideoSizeChanged(int width, int height) {
            // TODO Auto-generated method stub
            super.onVideoSizeChanged(width, height);
            //L.d(TAG,"onVideoSizeChanged");
        }
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void goPlay(){
        if(vid == null || "".equals(vid))return;
        //L.d(TAG,"goPlay vid:"+vid);
        mYoukuPlayerView.playYoukuVideo(vid);
    }

    private void getIntentData(Intent intent){
        if(intent == null) return;
        mCourse = (CourseModel)intent.getSerializableExtra("course");
        if(mCourse == null)return;
        //Log.d("csy", "initdata mCourse:" + mCourse);
        mType = mCourse.getcType();
        if(mType == 1){
            cList = intent.getParcelableArrayListExtra("freecourse");
        }else if(mType == 2){
            cList = intent.getParcelableArrayListExtra("hotcourse");
        }else if(mType == 3){
            cList = intent.getParcelableArrayListExtra("discourse");
        }else{
            cList = intent.getParcelableArrayListExtra("favcourse");
        }
        if(cList == null || cList.size() == 0)return;
        mReList = recommendCourseList(cList,mCourse);
        vid = mCourse.getcVid();
        //Log.d("csy", "initdata type:" + mType + ";vid:" + vid);
    }

    private List<CourseModel> recommendCourseList(List<CourseModel> models,CourseModel course){
        List<CourseModel> lists = models;
        List<CourseModel> datas = new ArrayList<CourseModel>();
        int size = lists.size();
        if(size == 1){
            datas.add(lists.get(0));
        }else {
            for(int i =0;i<2;i++){
                datas.add(lists.get(i));
            }
        }
        return datas;
    }

    private void requestLayout(boolean flag){
        //L.d("csy","requestLayout mIsFullScreen:"+mIsFullScreen);
        if(!mIsFullScreen && flag){
            //L.d("csy","set Gone");
            mTitleLayout.setVisibility(View.GONE);
            mSendComment.setVisibility(View.GONE);
            mIsFullScreen = true;
        }else if(mIsFullScreen && !flag){
            mTitleLayout.setVisibility(View.VISIBLE);
            mSendComment.setVisibility(View.VISIBLE);
            mIsFullScreen = false;
        }
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        CourseModel cour = mReList.get(position);
        if (mType == 1) {
            bundle.putParcelableArrayList("freecourse",cList);
        }else if(mType == 2){
            bundle.putParcelableArrayList("hotcourse",cList);
        }else if(mType == 3){
            bundle.putParcelableArrayList("discourse",cList);
        }else{
            bundle.putParcelableArrayList("favcourse",cList);
        }
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d("csy", "@onitemclickcourse course:"+cour);
        bundle.putSerializable("course", cour);

        intent.putExtras(bundle);
        Utils.start_Activity(this,intent);
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_VIDEO_DETAIL:
                String resp  =(String)msg.obj;
                L.d(TAG, "detail Response:" + resp);
                if(Utils.getRequestStatus(resp) == 1){
                    L.d(TAG,"resp detail success");
                    Gson gson = new Gson();
                    VideoDetailResponseModel videoModel = gson.fromJson(resp, VideoDetailResponseModel.class);
                    VideoDetailResponseModel.VideoContent content = videoModel.getReponse().getContent();
                    L.d("csy", "videodetail content:" + content);
                    //vid = content.getvId();
                    mExpertId = content.getTeacherId();
                    L.d(TAG,"video_title:"+content.getVideoTitle());
                    mCourseTitle.setText(content.getVideoTitle());
                    if(!TextUtils.isEmpty(content.getTeacherId())){
                        CourseManager.requestCourseExpert(LoginUserBean.getInstance().getLoginUserid(),content.getTeacherId(),mHandler);
                    }
                    String price = content.getPrice();
                    if(price == null || price.equals("null") || price.equals("")) {
                        price = "0.0";
                    }
                    L.d(TAG,"video_price:"+price+";playcount:"+content.getPlayCount()+";time:"+content.getClassTimeLong());
                    mCoursePrice.setText(String.format(getResources().getString(R.string.course_price), Float.valueOf(price)));
                    mCourseCount.setText(String.format(getResources().getString(R.string.course_play_count), Integer.valueOf(content.getPlayCount())));
                    //L.d(TAG,"desc length:"+content.getClassDesc().length());
                    /*if(content.getClassDesc().length() < 50){
                        mExpendMore.setVisibility(View.GONE);
                    }else{
                        mExpendMore.setVisibility(View.VISIBLE);
                    }*/
                    //mCourseDesc.setText(content.getClassDesc());
                    if(content.getClassTimeLong() == null || "null".equalsIgnoreCase(content.getClassTimeLong())){
                        mCourseTime.setText(String.format(getResources().getString(R.string.course_time_duration), 0));
                    }else{
                        mCourseTime.setText(String.format(getResources().getString(R.string.course_time_duration), Integer.valueOf(content.getClassTimeLong())));
                    }

                }else{
                    L.d(TAG,"resp videolist fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_VIDEO:
                String respFav  =(String)msg.obj;
                L.d(TAG, "favcount Response:" + respFav);
                if(Utils.getRequestStatus(respFav) == 1){
                    L.d(TAG,"resp fav success");
                    Gson gson = new Gson();
                    FavCountResponseModel praiseCountModel = gson.fromJson(respFav, FavCountResponseModel.class);
                    List<FavCountResponseModel.Content> content = praiseCountModel.getReponse().getContent();
                    if(isFav(content)){
                        mCollectTxt.setText(R.string.cancel_favorite);
                        mCollectTxt.setTextColor(getResources().getColor(R.color.tab_text_color_checked));
                        mCollect.setImageResource(R.drawable.course_detail_collect);
                    }
                }else{
                    L.d(TAG,"resp fav fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_PRAISE_REPLY_COUNT:
                String resp1  =(String)msg.obj;
                L.d(TAG, "praisecount Response:" + resp1);
                if(Utils.getRequestStatus(resp1) == 1){
                    L.d(TAG,"resp praise success");
                    Gson gson = new Gson();
                    PraiseCountResponseModel praiseCountModel = gson.fromJson(resp1, PraiseCountResponseModel.class);
                    PraiseCountResponseModel.Content content = praiseCountModel.getReponse().getContent();
                    mZhan.setImageResource(R.drawable.course_detail_zhan);
                    mPraiseCount.setText(String.format(getResources().getString(R.string.praise_count),Integer.valueOf(content.getPraiseCount())));
                    mPraiseCount.setTextColor(getResources().getColor(R.color.tab_text_color_checked));
                }else{
                    L.d(TAG,"resp praise fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_GIVE_PRAISE:
                String res = (String)msg.obj;
                L.d(TAG, "give praise Response:" + res);
                if(Utils.getRequestStatus(res) == 1) {
                    L.d(TAG, "resp give praise success");
                    CourseManager.requestPraiseCount(LoginUserBean.getInstance().getLoginUserid(),vid);
                    mZhan.setImageResource(R.drawable.course_detail_zhan);
                    mPraiseCount.setTextColor(getResources().getColor(R.color.tab_text_color_checked));
                    //mGivePraise.setEnabled(false);
                    mGivePraise.setClickable(false);
                }else{
                    L.d(TAG,"resp give praise fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_COMMENT_LIST:
                String resp2  =(String)msg.obj;
                L.d(TAG, "detail comment Response:" + resp2);
                if(Utils.getRequestStatus(resp2) == 1){
                    if(datas != null && datas.size() > 0){
                        datas.clear();
                    }
                    Gson gson = new Gson();
                    ConferenceCommentListModel responseModel = gson.fromJson(resp2,ConferenceCommentListModel.class);
                    List<ConferenceCommentListModel.ConferenceCommentContent> contents = responseModel.getReponse().getContent();
                    for(int i=0;i<contents.size();i++){
                        ConferenceCommentListModel.ConferenceCommentContent content = contents.get(i);
                        CommentModel comment = new CommentModel();
                        comment.setComtId(content.getComtId());
                        comment.setCommentDesc(content.getContent());
                        comment.setComtUserId(content.getComtUserId());
                        comment.setPhotos(content.getUserPhoto());
                        comment.setCommentName(content.getComtNikeName());
                        comment.setComtDate(content.getComtDate());
                        comment.setCommentFrom("湖北养户");
                        datas.add(comment);
                    }
                    if(datas != null && datas.size() > 0){
                        //mCommentTitle.setVisibility(View.VISIBLE);
                        mCommentLinear.setVisibility(View.VISIBLE);
                        L.d(TAG,"comment_list count:"+ datas.size());
                        mCommentTitle.setText(String.format(getResources().getString(R.string.comment_count),datas.size()));
                        if(mCommentAdapter == null){
                            mCommentAdapter = new CommentAdapter(getBaseContext(),datas);
                            mListView.setAdapter(mCommentAdapter);
                            setListViewHeightBasedOnChildren(mListView);
                        }else{
                            mCommentAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(mListView);
                        }
                    }
                }else{

                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_ADD_COMMENT:
                String response  =(String)msg.obj;
                L.d(TAG, "add course comment Response:" + response);
                if(Utils.getRequestStatus(response) == 1){
                    Toast.makeText(getBaseContext(),R.string.comment_success,Toast.LENGTH_SHORT).show();
                    CourseManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(), vid,mHandler);
                }else{
                    Toast.makeText(getBaseContext(),R.string.comment_fail,Toast.LENGTH_SHORT).show();
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_COURSE_FAV_OPT:
                String response1  =(String)msg.obj;
                L.d(TAG, "fav course opt Response:" + response1);
                if(Utils.getRequestStatus(response1) == 1) {
                    Gson gson = new Gson();
                    HttpCommitQaResponse httpCommitQaResponse = gson.fromJson(response1, HttpCommitQaResponse.class);
                    if (httpCommitQaResponse.getResponse().getSucceed() == 1) {
                        ToastUtil.showToast(CourseDetailActivity.this,getString(R.string.op_success));
                        if (!mIsFav) {
                            mIsFav = true;
                            mCollectTxt.setText(R.string.cancel_favorite);
                            mCollectTxt.setTextColor(getResources().getColor(R.color.tab_text_color_checked));
                            mCollect.setImageResource(R.drawable.course_detail_collect);
                        } else {
                            mIsFav = false;
                            mCollectTxt.setText(R.string.add_favorite);
                            mCollectTxt.setTextColor(getResources().getColor(R.color.image_text_light_grey));
                            mCollect.setImageResource(R.drawable.course_detail_nocollect);
                        }
                    }else{
                        ToastUtil.showToast(CourseDetailActivity.this,getString(R.string.op_failed,
                                httpCommitQaResponse.getResponse().getErrorCode(),
                                httpCommitQaResponse.getResponse().getErrorInfo()));
                    }
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_DETAIL_MASTER:
                //返回专家信息
                String response2 = (String) msg.obj;
                if (Utils.getRequestStatus(response2) == 1) {
                    Gson gson = new Gson();
                    mHttpMasterDetailResponse = gson.fromJson(response2, HttpMasterDetailResponse.class);
                    if(mHttpMasterDetailResponse == null){
                        return false;
                    }
                    mImageLoader.displayImage(mHttpMasterDetailResponse.getResponse().getContent().getPhoto(),mExpertIcon,mOptions1);
                    mExpertName.setText(mHttpMasterDetailResponse.getResponse().getContent().getUserName());
                    mExpertOffice.setText(mHttpMasterDetailResponse.getResponse().getContent().getCompany());
                    mExpertPosition.setText(mHttpMasterDetailResponse.getResponse().getContent().getTitles());
                } else {
                    //ToastUtil.showToast(this, getString(R.string.load_master_detail_failed));
                    L.d(TAG,"获取专家信息失败!");
                }
                break;
            case LOAD_DATA_FINISH:
                if(mCommentAdapter != null){
                    mCommentAdapter.notifyDataSetChanged();
                }
                mListView.onLoadComplete();
                setListViewHeightBasedOnChildren(mListView);
                break;
            default:
                break;
        }
        return false;
    }

    private boolean isFav(List<FavCountResponseModel.Content> content){
        L.d(TAG,"isFav vid:"+vid+";content-size:"+content.size());
        if(content != null && content.size() > 0){
            List<FavCountResponseModel.KeyData> keyDatas = content.get(0).getKeyDataList();
            if(keyDatas != null && keyDatas.size() > 0){
                for(int i=0;i<keyDatas.size();i++){
                    FavCountResponseModel.KeyData data = keyDatas.get(i);
                    String keyId = data.getKeyId();
                    String keyString = data.getKeyType();
                    L.d(TAG,"isFav keyId:"+keyId+";keyString:"+keyString);
                    if(keyId.equalsIgnoreCase(vid) && keyString.equalsIgnoreCase("XM"))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 即使响应为竖屏，也强制设置为横屏，这样可以始终保持横屏
            //L.d(TAG,"onConfigurationChanged portrait");
            requestLayout(false);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //L.d(TAG,"onConfigurationChanged landscape");
            requestLayout(true);
        }
    }



   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean managerKeyDown = basePlayerManager.onKeyDown(keyCode, event);
        if (basePlayerManager.shouldCallSuperKeyDown()) {
            return super.onKeyDown(keyCode, event);
        } else {
            return managerKeyDown;
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        //basePlayerManager.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CourseManager.requesetCourseDetail(LoginUserBean.getInstance().getLoginUserid(), vid);
                CourseManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(),vid,mHandler);
                //查询点赞数,业务记录Id
                CourseManager.requestPraiseCount(LoginUserBean.getInstance().getLoginUserid(),vid);
                CourseManager.requestFavCount(LoginUserBean.getInstance().getLoginUserid(),"","",CodeUtil.FAV_FLAG_TYPE);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (mYoukuPlayerView.isFullScreen()) {
            mYoukuPlayerView.goSmallScreen();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        // 通过Intent获取播放需要的相关参数
        getIntentData(intent);
        //L.d(TAG,"onNewIntent goPlay");
        // 进行播放
        goPlay();
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
*/
    /**
     * 推荐视频列表
     */
    private class RecommendAdapter extends BaseAdapter{
        private LayoutInflater mLayoutInflater;
        private List<CourseModel> mCourses;

        public RecommendAdapter(Context context,List<CourseModel> datas){
            mLayoutInflater = LayoutInflater.from(context);
            mCourses = datas;
        }
        @Override
        public int getCount() {
            return mCourses.size();
        }

        @Override
        public Object getItem(int position) {
            return mCourses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RecommendViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new RecommendViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.recommend_course_item,parent,false);
                viewHolder.icon =(ImageView)convertView.findViewById(R.id.course_icon);
                viewHolder.title =(TextView)convertView.findViewById(R.id.re_course_title);
                viewHolder.duration =(TextView)convertView.findViewById(R.id.re_course_duration);
                viewHolder.count = (TextView)convertView.findViewById(R.id.re_course_count);
                //viewHolder.arrow =(ImageView)convertView.findViewById(R.id.re_course_arrow);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (RecommendViewHolder)convertView.getTag();
            }
            final CourseModel data = mCourses.get(position);
            mImageLoader.displayImage(data.getcImageUrl(),viewHolder.icon,mOptions);
            viewHolder.title.setText(getString(R.string.recommend_course_title, data.getcTitle()));
            viewHolder.duration.setText(getString(R.string.recommend_course_time,String.valueOf(data.getcDuration())));
            viewHolder.count.setText(getString(R.string.recommend_course_count, String.valueOf(data.getcPlayTime())));
            return convertView;
        }
    }
    /**
     * 评论列表
     */
    private class CommentAdapter extends BaseAdapter{

        private LayoutInflater mLayoutInflater;
        private List<CommentModel> mComments;

        public CommentAdapter(Context context,List<CommentModel> datas){
            mLayoutInflater = LayoutInflater.from(context);
            mComments = datas;
        }

        @Override
        public int getCount() {
            return mComments.size();
        }

        @Override
        public Object getItem(int position) {
            return mComments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CommentViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new CommentViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.article_comment_item,parent,false);
                RoundImageView innerImage = (RoundImageView) convertView.findViewById(R.id.comment_icon);
                innerImage.setType(RoundImageView.TYPE_CIRCLE);
                viewHolder.icon = innerImage;
                viewHolder.name = (TextView) convertView.findViewById(R.id.comment_name);
                viewHolder.from = (TextView) convertView.findViewById(R.id.comment_from);
                viewHolder.content = (TextView) convertView.findViewById(R.id.comment_content);
                viewHolder.comment_time = (TextView)convertView.findViewById(R.id.comment_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (CommentViewHolder) convertView.getTag();
            }
            CommentModel comment = mComments.get(position);
            if(comment.getPhotos() != null && !comment.getPhotos().isEmpty()){
                Picasso.with(CourseDetailActivity.this).load(comment.getPhotos()).error(R.drawable.index_zhuanti).
                        placeholder(R.drawable.index_zhuanti).into(viewHolder.icon);
            }else{
                viewHolder.icon.setImageResource(R.drawable.index_zhuanti);
            }
            viewHolder.comment_time.setText(comment.getComtDate());
            if(!TextUtils.isEmpty(comment.getCommentName())){
                viewHolder.name.setText(comment.getCommentName());
            }else{
                viewHolder.name.setText(R.string.noname_user);
            }
            viewHolder.from.setText(comment.getCommentFrom());
            viewHolder.content.setText(comment.getCommentDesc());
            return convertView;
        }
    }

    class CommentViewHolder{
        private ImageView icon;
        private TextView name;
        private TextView from;
        private TextView content;
        private TextView comment_time;
    }

    class RecommendViewHolder{
        private ImageView icon;
        private TextView title;
        private TextView duration;
        private TextView count;
        //private ImageView arrow;
    }

}
