package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.CourseManager;
import com.xnf.henghenghui.logic.MeetingManager;
import com.xnf.henghenghui.model.CommentModel;
import com.xnf.henghenghui.model.ConferenceCommentListModel;
import com.xnf.henghenghui.model.HotArticleModel;
import com.xnf.henghenghui.model.HttpArticleResponse;
import com.xnf.henghenghui.model.HttpBaikeInfoResponse;
import com.xnf.henghenghui.model.HttpCommitQaResponse;
import com.xnf.henghenghui.model.PraiseCountResponseModel;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.fragments.MeetingCommentFragment;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.ShareDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 多个模块公用
 */
public class ArticleDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TYPE = "CF";
    private ListView mListView;
    private List<CommentModel> mComments;
    private String mId;
    private CommentAdapter mCommentAdapter;
    private ConferenceCommentThread mCommentThread;
    private EditText mEditComment;
    private Button mBtnComment;
    ProgressDialog mDialog;
    private boolean mProgressShow;
    private TextView mBackBtn;
    private WebView mWebView;
    private String mArticleId = null;
    private ProgressDialog dialog;

    private ImageView mZhan;
    private ImageView mCollect;
    private TextView mCollectTxt;
    private LinearLayout mFav;
    private LinearLayout mGivePraise;
    private TextView mPraiseCount;
    private TextView mTvOne;
    private LinearLayout mShareBtn;

    private boolean mIsFav = false;

    private static final String ADD_FAV_OPT = "1";
    private static final String CANCEL_FAV_OPT = "0";

    private android.view.animation.Animation mAnimation;

    private static final String GIVE_PRAISE_TYPE = "1";

    private HttpArticleResponse mHttpArticleResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {
        mArticleId = getIntent().getStringExtra("ARTICLE_ID");
        initloadingDialog();
        mComments = new ArrayList<CommentModel>();
        lazyLoad();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_article2);
        mListView = (ListView) findViewById(R.id.meeting_comment_list);
        mEditComment = (EditText) findViewById(R.id.meeting_comment_text);
        mBtnComment = (Button) findViewById(R.id.meeting_comment_add);
        mBtnComment.setOnClickListener(this);
        mBackBtn = (TextView) findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);
        mTvOne =(TextView)findViewById(R.id.tv_one);

        mGivePraise =(LinearLayout)findViewById(R.id.course_give_praise);
        mCollect =(ImageView)findViewById(R.id.course_collect);
        mFav =(LinearLayout)findViewById(R.id.fav_course_opt);
        mCollectTxt =(TextView)findViewById(R.id.course_detail_correct);
        mCollectTxt.setText(R.string.add_favorite);
        mZhan =(ImageView)findViewById(R.id.course_give_zhan);
        mAnimation = AnimationUtils.loadAnimation(ArticleDetailActivity.this, R.anim.nn);

        mShareBtn = (LinearLayout)findViewById(R.id.fav_course_share);
        mPraiseCount =(TextView)findViewById(R.id.course_detail_zhan);
        bindOnClickLister(this,mGivePraise,mFav,mShareBtn);

        mWebView = (WebView) findViewById(R.id.web_view);
        WebSettings wsettings = mWebView.getSettings();
        wsettings.setJavaScriptEnabled(false);
        wsettings.setJavaScriptCanOpenWindowsAutomatically(false);
        wsettings.setAppCacheEnabled(true);
        wsettings.setBuiltInZoomControls(false);
        wsettings.setDisplayZoomControls(false);
        wsettings.setSupportZoom(false);
        wsettings.setLoadsImagesAutomatically(true);
        wsettings.setDefaultTextEncodingName("utf-8");

        loadArticleDetail(true);
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        //查询点赞数,业务记录Id
        CourseManager.requestPraiseCount(LoginUserBean.getInstance().getLoginUserid(),mArticleId);
    }

    private void loadArticleDetail(boolean sp) {
        if (sp) {
            if (dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("artId", mArticleId);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_ARTICLE_DETAIL_INFO)
                .tag(Urls.ACTION_ARTICLE_DETAIL_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_ARTICLE_DETAIL_INFO;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    protected void lazyLoad() {
        //TODO
        mId = mArticleId;
        MeetingManager.setHandler(mHandler);
        L.d("csy", "MeetingCommentFragment lazyload");
        if (mCommentThread == null) {
            L.d("csy", "MeetingCommentFragment lazyload getdata");
            mDialog.show();
            mCommentThread = new ConferenceCommentThread();
            mCommentThread.start();
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
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_COMMENT_LIST:
                String resp = (String) msg.obj;
                L.d(TAG, "comment list Response:" + resp);
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                if (Utils.getRequestStatus(resp) == 1) {
                    if (mComments != null && mComments.size() > 0) {
                        mComments.clear();
                    }
                    Gson gson = new Gson();
                    ConferenceCommentListModel responseModel = gson.fromJson(resp, ConferenceCommentListModel.class);
                    List<ConferenceCommentListModel.ConferenceCommentContent> contents = responseModel.getReponse().getContent();
                    for (int i = 0; i < contents.size(); i++) {
                        ConferenceCommentListModel.ConferenceCommentContent content = contents.get(i);
                        CommentModel comment = new CommentModel();
                        comment.setComtId(content.getComtId());
                        comment.setCommentDesc(content.getContent());
                        comment.setComtUserId(content.getComtUserId());
                        comment.setComtDate(content.getComtDate());
                        comment.setCommentName(content.getComtNikeName());
                        comment.setCommentFrom(content.getComtNikeName());
                        mComments.add(comment);
                    }
                    if (mComments != null && mComments.size() > 0) {
                        if (mCommentAdapter == null) {
                            mCommentAdapter = new CommentAdapter(this, mComments);
                            mListView.setAdapter(mCommentAdapter);
                        } else {
                            mCommentAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    L.d(TAG, "resp comment conference list fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_ADD_COMMENT: {
                String response = (String) msg.obj;
                L.d(TAG, "add comment Response:" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    Toast.makeText(this, R.string.comment_success, Toast.LENGTH_SHORT).show();
                    MeetingManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(), mId,mHandler);
                } else {
                    Toast.makeText(this, R.string.comment_fail, Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_ARTICLE_DETAIL_INFO: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpArticleResponse = gson.fromJson(response, HttpArticleResponse.class);
                    try {
//                        mWebView.loadData(URLEncoder.encode(mHttpArticleReponse.getResponse().getContent().getEntryContent(), "gb2312"), "text/html", "gb2312");
                        mWebView.loadData(mHttpArticleResponse.getResponse().getContent().getArtcontent(), "text/html; charset=utf-8", null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
//                        mWebView.loadData(URLEncoder.encode(getString(R.string.hengheng_baike_detail_failed), "gb2312"), "text/html", "gb2312");
                        mWebView.loadData(getString(R.string.hengheng_baike_detail_failed), "text/html; charset=utf-8", null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (dialog != null) {
                    dialog.dismiss();
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
                        ToastUtil.showToast(ArticleDetailActivity.this,getString(R.string.op_success));
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
                        ToastUtil.showToast(ArticleDetailActivity.this,getString(R.string.op_failed,
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_GIVE_PRAISE:
                String res = (String)msg.obj;
                L.d(TAG, "give praise Response:" + res);
                if(Utils.getRequestStatus(res) == 1) {
                    L.d(TAG, "resp give praise success");
                    CourseManager.requestPraiseCount(LoginUserBean.getInstance().getLoginUserid(),mArticleId);
                    mZhan.setImageResource(R.drawable.course_detail_zhan);
                    //mGivePraise.setEnabled(false);
                    mGivePraise.setClickable(false);
                }else{
                    L.d(TAG,"resp give praise fail");
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.meeting_comment_add:
                String content = mEditComment.getText().toString();
                L.d(TAG, "onclick content:" + content);
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(this, R.string.add_comment_hint, Toast.LENGTH_SHORT).show();
                    return;
                }
                MeetingManager.requestAddComment(LoginUserBean.getInstance().getLoginUserid(), mId, TYPE, content,mHandler);
                mEditComment.getText().clear();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.course_give_praise:
            {
                CourseManager.requestGivePraise(LoginUserBean.getInstance().getLoginUserid() ,mArticleId,GIVE_PRAISE_TYPE);
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
            case R.id.favorite_btn: {
                break;
            }
            case R.id.fav_course_opt:
                if(!mIsFav){
                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid() ,mArticleId, ADD_FAV_OPT);
                    //mIsFav = true;
                }else{
                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid() ,mArticleId, CANCEL_FAV_OPT);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    class ConferenceCommentThread extends Thread {
        public volatile boolean exit = false;

        @Override
        public void run() {
            L.d("csy", "ConferenceCommentThread run exit:" + exit + ";mId:" + mId);
            if (!exit) {
                MeetingManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(), mId,mHandler);
            }
        }
    }

    private void initloadingDialog() {
        mProgressShow = true;
        mDialog = new ProgressDialog(this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mProgressShow = false;
            }
        });
        mDialog.setMessage(getString(R.string.dl_waiting));
    }

    class CommentAdapter extends BaseAdapter {
        List<CommentModel> mDatas;
        LayoutInflater mLayoutInflater;

        public CommentAdapter(Context context, List<CommentModel> datas) {
            mDatas = datas;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CommentHolderView holderView;
            if (convertView == null) {
                holderView = new CommentHolderView();
                convertView = mLayoutInflater.inflate(R.layout.meeting_comment_item, parent, false);
                holderView.commenterImg = (ImageView) convertView.findViewById(R.id.meeting_comment_icon);
                holderView.name = (TextView) convertView.findViewById(R.id.meeting_comment_name);
                holderView.from = (TextView) convertView.findViewById(R.id.meeting_comment_from);
                holderView.commentContent = (TextView) convertView.findViewById(R.id.meeting_comment_content);
                convertView.setTag(holderView);
            } else {
                holderView = (CommentHolderView) convertView.getTag();
            }
            CommentModel comment = mDatas.get(position);
            holderView.commenterImg.setImageResource(R.drawable.ic_sex_male);
            if (!TextUtils.isEmpty(comment.getCommentName())) {
                holderView.name.setText(comment.getCommentName());
            } else {
                holderView.name.setText(comment.getComtUserId());
            }
            holderView.from.setText(comment.getCommentFrom());
            holderView.commentContent.setText(comment.getCommentDesc());
            return convertView;
        }
    }

    static class CommentHolderView {
        ImageView commenterImg;
        TextView name;
        TextView from;
        TextView commentContent;
    }

}
