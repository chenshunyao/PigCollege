package com.xnf.henghenghui.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.CourseManager;
import com.xnf.henghenghui.logic.SubjectManager;
import com.xnf.henghenghui.model.HotArticleModel;
import com.xnf.henghenghui.model.HotSubjectModel;
import com.xnf.henghenghui.model.HttpCommitQaResponse;
import com.xnf.henghenghui.model.HttpSubjectArticleListResponse;
import com.xnf.henghenghui.model.HttpSubjectDetailResponse;
import com.xnf.henghenghui.model.PraiseCountResponseModel;
import com.xnf.henghenghui.ui.adapter.HotArticleAdapter;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.HotImageView;
import com.xnf.henghenghui.ui.view.HotViewPager;
import com.xnf.henghenghui.ui.view.ShareDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 哼哼热点首页，对应着广告，搜索以及推荐的热点入口，点击首页的哼哼热点即可进入
 */
public class SubjectActivityBak extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,HotViewPager.BannerViewClick,HotViewPager.bannerDataCallback{

    private static final String TAG = "HotFragment";
    private RelativeLayout mLoading_layout;
    //HotPullToRefreshView replace ListView
    private ListView mListView;
    private ImageView mLoading_iv;
    private Context mContext;
    private ListView mLV = null;
    private List<HotImageView> mDatas;
    private ArrayList<HotArticleModel> mArticleList;
    private List<ImageView> views;
    private LinearLayout mLinearLayout;
    private HotArticleAdapter mHotArticleAdapter;

    private TextView mSubjectDesc;
    private TextView mExpandDesBtn;
    //假数据
    private int[] drawables = new int[]{R.drawable.ex1,R.drawable.ex2,R.drawable.ex3,R.drawable.ex4};
    private int[] picdrawables = new int[]{R.drawable.p1,R.drawable.p2,R.drawable.p3};

    private static final int CONTENT_DESC_MAX_LINE = 3;// 默认展示最大行数3行  
    private static final int SHOW_CONTENT_NONE_STATE = 0;// 扩充  
    private static final int SHRINK_UP_STATE = 1;// 收起状态  
    private static final int SPREAD_STATE = 2;// 展开状态  
    private static int mState = SHRINK_UP_STATE;//默认收起状态 
    private TextView mBackBtn;
    private String mSubjectId="";
	
	private ImageView mZhan;
    private ImageView mCollect;
    private TextView mCollectTxt;
    private LinearLayout mFav;
    private LinearLayout mGivePraise;
    private TextView mPraiseCount;
    private TextView mTvOne;
    private LinearLayout mShareBtn;

    private TextView mSubjectTitle;
    private ImageView mSubjectImageView;

    private boolean mIsFav = false;

    private static final String ADD_FAV_OPT = "1";
    private static final String CANCEL_FAV_OPT = "0";

    private android.view.animation.Animation mAnimation;

    private static final String GIVE_PRAISE_TYPE = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {
        mDatas = getAdData();
        //mArticleList = getHotNews();
        mSubjectId = getIntent().getStringExtra("SUBJECT_ID");
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    private List<HotImageView> getAdData(){
       List<HotImageView> list = new ArrayList<HotImageView>();
       for(int i=0;i<4;i++){
           HotImageView image = new HotImageView(this);
           //设置默认图片
           image.setDefaultImageResId(drawables[i]);
           image.setScaleType(ImageView.ScaleType.FIT_XY);
           image.setId(i);
           list.add(image);
       }
       return list;
   }

    @Override
    protected void onStart() {
        super.onStart();
        //查询点赞数,业务记录Id
        CourseManager.requestPraiseCount(LoginUserBean.getInstance().getLoginUserid(),mSubjectId);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_subject);
        mLinearLayout = (LinearLayout) findViewById(R.id.activity_layout);

        mSubjectTitle = (TextView)findViewById(R.id.subject_title);
        mSubjectDesc =(TextView)findViewById(R.id.subject_desc);
        mSubjectImageView = (ImageView) findViewById(R.id.subject_imageview);

        mExpandDesBtn =(TextView)findViewById(R.id.subject_expand);
        mExpandDesBtn.setOnClickListener(this);
        mLoading_layout = (RelativeLayout)findViewById(R.id.loading_layout);
        mLoading_iv = (ImageView) findViewById(R.id.loading_img);
        mListView = (ListView) findViewById(R.id.listview);
        mHotArticleAdapter = new HotArticleAdapter();
        mListView.setAdapter(mHotArticleAdapter);
        //setListViewHeightBasedOnChildren(mListView);
        mListView.setOnItemClickListener(this);
        mBackBtn =(TextView) findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);

        mTvOne =(TextView)findViewById(R.id.tv_one);

        mGivePraise =(LinearLayout)findViewById(R.id.course_give_praise);
        mCollect =(ImageView)findViewById(R.id.course_collect);
        mFav =(LinearLayout)findViewById(R.id.fav_course_opt);
        mCollectTxt =(TextView)findViewById(R.id.course_detail_correct);
        mCollectTxt.setText(R.string.add_favorite);
        mZhan =(ImageView)findViewById(R.id.course_give_zhan);
        mAnimation = AnimationUtils.loadAnimation(SubjectActivityBak.this, R.anim.nn);

        mShareBtn = (LinearLayout)findViewById(R.id.fav_course_share);
        mPraiseCount =(TextView)findViewById(R.id.course_detail_zhan);
        bindOnClickLister(this,mGivePraise,mFav,mShareBtn);


        SubjectManager.setHandler(mHandler);
        mArticleList = new ArrayList<HotArticleModel>();
        SubjectManager.getSubjectDetail(LoginUserBean.getInstance().getLoginUserid(),mSubjectId);
        SubjectManager.getSubjectArticleList(LoginUserBean.getInstance().getLoginUserid(),mSubjectId,"","","","");
        
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.subject_expand:{
                L.d(TAG,"onSubject1Click SubjectActivity");
                if(mState==SPREAD_STATE){
                    mSubjectDesc.setMaxLines(CONTENT_DESC_MAX_LINE);
                    mSubjectDesc.requestLayout();
                    mExpandDesBtn.setText("展开");
                    mState=SHRINK_UP_STATE;
                }else if(mState==SHRINK_UP_STATE){
                    mSubjectDesc.setMaxLines(Integer.MAX_VALUE);
                    mSubjectDesc.requestLayout();
                    mState=SPREAD_STATE;
                    mExpandDesBtn.setText("收缩");
                }
            }
                break;
            case R.id.back:
                onBackPressed();
                break;
				 case R.id.course_give_praise:
            {
                CourseManager.requestGivePraise(LoginUserBean.getInstance().getLoginUserid() ,mSubjectId,GIVE_PRAISE_TYPE);
                mTvOne.setVisibility(View.VISIBLE);
                mTvOne.startAnimation(mAnimation);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTvOne.setVisibility(View.GONE);
                    }
                },700);
            }
                break;
            case R.id.fav_course_opt:
                if(!mIsFav){
                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid() ,mSubjectId, ADD_FAV_OPT);
                    //mIsFav = true;
                }else{
                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid() ,mSubjectId, CANCEL_FAV_OPT);
                    //mIsFav = false;
                }
                break;
            case R.id.fav_course_share: {
                handleShare();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void subViewClickListener(View paramView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ArticleDetailActivity2.class);
        intent.putExtra("ARTICLE_ID",mArticleList.get(position).getArticleId());
        Utils.start_Activity(this, intent);
    }

    private ArrayList<HotSubjectModel> getHotNews(){
        ArrayList<HotSubjectModel> list = new ArrayList<HotSubjectModel>();
        for(int i=0;i<4;i++){
            HotSubjectModel article = new HotSubjectModel();
            article.setTitle("习近平节后首场调研");
            article.setDesc("中国人常说，“形势比人强”，办好事必须顺势而为。新闻报道一直与时间在赛跑，传播形势与格局的变迁，对岛叔这样的新闻人来说，更加敏感。");
            list.add(article);
        }
        return list;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
         
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_ARTICLE_LIST:{
                String response = (String) msg.obj;
                L.e(TAG, "Reponse:" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    L.d(TAG, "Reponse banner success");
                    Gson gson = new Gson();
                    HttpSubjectArticleListResponse responseModel = gson.fromJson(response, HttpSubjectArticleListResponse.class);
                    ArrayList<HttpSubjectArticleListResponse.Content> contents = responseModel.getResponse().getContent();
                    //mDatas = new ArrayList<HotImageView>();
                    for (int i = 0; i < contents.size(); i++) {
                        HttpSubjectArticleListResponse.Content content = contents.get(i);
                        HotArticleModel article = new HotArticleModel();
                        article.setArticleId(content.getArtid());
                        article.setTitle(content.getArttitle());
                        article.setmUrl(content.getArtphoto());
                        article.setDesc(content.getArtdesc());
                        article.setZuanNum(content.getPraisecount());
                        article.setCommentNum(content.getCommentcount());
                        article.setTime(content.getArtdatetime());
                        mArticleList.add(article);
                    }
                    mHotArticleAdapter.setData(mArticleList);

                } else {
                    L.d(TAG, "Reponse banner fail");
                }
                break;
            }
          case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_COURSE_FAV_OPT:{
                String response1  =(String)msg.obj;
                L.d(TAG, "fav course opt Response:" + response1);
                if(Utils.getRequestStatus(response1) == 1) {
                    Gson gson = new Gson();
                    HttpCommitQaResponse httpCommitQaResponse = gson.fromJson(response1, HttpCommitQaResponse.class);
                    if (httpCommitQaResponse.getResponse().getSucceed() == 1) {
                        ToastUtil.showToast(SubjectActivityBak.this,getString(R.string.op_success));
                        if (!mIsFav) {
                            mIsFav = true;
                            mCollectTxt.setText(R.string.cancel_favorite);
                            mCollect.setImageResource(R.drawable.course_detail_collect);
                        } else {
                            mIsFav = false;
                            mCollectTxt.setText(R.string.add_favorite);
                            mCollect.setImageResource(R.drawable.course_detail_nocollect);
                        }
                    }else{
                        ToastUtil.showToast(SubjectActivityBak.this,getString(R.string.op_failed,
                                httpCommitQaResponse.getResponse().getErrorCode(),
                                httpCommitQaResponse.getResponse().getErrorInfo()));
                    }
                }
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
                    mPraiseCount.setText(String.format(getResources().getString(R.string.praise_count),Integer.valueOf(content.getPraiseCount())));
                }else{
                    L.d(TAG,"resp praise fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_GIVE_PRAISE:{
                String res = (String)msg.obj;
                L.d(TAG, "give praise Response:" + res);
                if(Utils.getRequestStatus(res) == 1) {
                    L.d(TAG, "resp give praise success");
                    CourseManager.requestPraiseCount(LoginUserBean.getInstance().getLoginUserid(),mSubjectId);
                    mZhan.setImageResource(R.drawable.course_detail_zhan);
                    //mGivePraise.setEnabled(false);
                    mGivePraise.setClickable(false);
                }else{
                    L.d(TAG,"resp give praise fail");
                }
            }
                break;
            case  CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_DETAIL:{
                String response = (String)msg.obj;
                L.d(TAG, "MSG_GET_SUBJECT_DETAIL Response:" + response);
                if(Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    HttpSubjectDetailResponse subjectDetailResponse = gson.fromJson(response, HttpSubjectDetailResponse.class);
                    HttpSubjectDetailResponse.Content content = subjectDetailResponse.getResponse().getContent();
                    mSubjectTitle.setText(content.getTopicName());
                    mSubjectDesc.setText(content.getTopicContent());
//                    if (content.getPhotos()!= null &&  !content.getPhotos().isEmpty()) {
//                        mImageLoader.displayImage(content.getPhotos(), mSubjectImageView);
//                    } else {
//                        mSubjectImageView.setImageResource(R.drawable.banaer_test);
//                    }
                }else{
                    L.d(TAG,"resp give praise fail");
                }
                break;
            }
            default:
                break;
        }
        return false;
    }

 // 分享
    public void handleShare() {
        final ShareDialog dialog = new ShareDialog(this,this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);
        dialog.setShareInfo("Title", "ShareContent", "http://www.baidu.com");
        dialog.show();
    }


    @Override
    public boolean handleNotifyMessage(Message msg) {
        return false;
    }

    @Override
    public boolean handleUIMessage(Message msg) {
        return false;
    }
}
