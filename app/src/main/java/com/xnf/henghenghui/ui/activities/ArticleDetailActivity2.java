package com.xnf.henghenghui.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.CourseManager;
import com.xnf.henghenghui.logic.MainPageManager;
import com.xnf.henghenghui.logic.MeetingManager;
import com.xnf.henghenghui.model.CommentModel;
import com.xnf.henghenghui.model.HttpArticleResponse;
import com.xnf.henghenghui.model.HttpCommitQaResponse;
import com.xnf.henghenghui.model.PraiseCountResponseModel;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.ui.view.ShareDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.ToastUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * 多个模块公用
 */
public class ArticleDetailActivity2 extends BaseActivity implements View.OnClickListener {

    private static final String TYPE = "ArticleDetailActivity2";
    private String mId;
    private EditText mEditComment;
    private Button mBtnComment;
    ProgressDialog mDialog;
    private boolean mProgressShow;
    private TextView mBackBtn;
    private WebView mWebView;
    private String mArticleId = null;
    private ProgressDialog dialog;
    private Context mContext;

    private ImageView mZhan;
    private ImageView mCollect;
    private TextView mCollectTxt;
    private LinearLayout mFav;
    private LinearLayout mGivePraise;
    private TextView mPraiseCount;
    private LinearLayout mShareBtn;

    private TextView mCommentNum;
    private String mPraiseNum = "0";

    private boolean mIsFav = false;

    private static final String ADD_FAV_OPT = "1";
    private static final String CANCEL_FAV_OPT = "0";

    private static final String GIVE_PRAISE_TYPE = "1";

    private HttpArticleResponse mHttpArticleResponse;

    private LinearLayout mMorePopMenu;
    private MorePopWindow morePopWindow;

    private FrameLayout mCommentNumLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void initData() {
        mArticleId = getIntent().getStringExtra("ARTICLE_ID");
        initloadingDialog();
        lazyLoad();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_article3);
        mEditComment = (EditText) findViewById(R.id.meeting_comment_text);
        mBtnComment = (Button) findViewById(R.id.meeting_comment_add);
        mBtnComment.setOnClickListener(this);
        mBackBtn = (TextView) findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);

//        mShareBtn = (LinearLayout) findViewById(R.id.fav_course_share);

        mCommentNum = (TextView) findViewById(R.id.comment_num);
        mCommentNumLayout =(FrameLayout)findViewById(R.id.comment_num_layout);
        bindOnClickLister(this, mShareBtn, mCommentNum,mCommentNumLayout);

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
        MainPageManager.setHandler(mHandler);

        mMorePopMenu = (LinearLayout) findViewById(R.id.more_pop_menu);
        mMorePopMenu.setOnClickListener(this);
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        //查询点赞数,业务记录Id
        CourseManager.requestPraiseCount(LoginUserBean.getInstance().getLoginUserid(), mArticleId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainPageManager.getPraiseBrowseReplyCount(LoginUserBean.getInstance().getLoginUserid(),mArticleId,"replyCount");
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
                L.d(TAG, "response:" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpArticleResponse = gson.fromJson(response, HttpArticleResponse.class);
                    try {
                        L.d(TAG, "getArtcontent()" + mHttpArticleResponse.getResponse().getContent().getArtcontent());
//                        mWebView.loadData(URLEncoder.encode(mHttpArticleReponse.getResponse().getContent().getEntryContent(), "gb2312"), "text/html", "gb2312");

                        StringBuilder artContent = new StringBuilder();
                        // 添加title
                        artContent.append(String.format("<div class='title' align=\"left\" style='margin-top:2px;font-weight:bold;font-size:20px'>%s</div>", mHttpArticleResponse.getResponse().getContent().getArttitle()));
                        // 添加作者和时间
                        String time = mHttpArticleResponse.getResponse().getContent().getPublishdate();
                        String author = String.format("<a class='author' style='color:#a9a9a9;margin-top:8px;font-size:14px'>%s</a>",mHttpArticleResponse.getResponse().getContent().getAuthorname());
                        artContent.append(String.format("<div class='authortime'  style='color:#a9a9a9;margin-top:8px;font-size:14px'>%s&nbsp;&nbsp;%s</div>", author, time));
                        artContent.append(String.format("<hr style=\"height:0.2px;border:none;border-top:1px solid #E6E6E6;\"/>"));
                        artContent.append(mHttpArticleResponse.getResponse().getContent().getArtcontent());
                        if(mHttpArticleResponse.getResponse().getContent().getIsFavorite().equals("1")){
                            mIsFav =true;
                        }else{
                            mIsFav =false;
                        }

                        mWebView.loadData(getHtmlData(artContent.toString()), "text/html; charset=utf-8", null);
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_COURSE_FAV_OPT: {
                String response1 = (String) msg.obj;
                L.d(TAG, "fav course opt Response:" + response1);
                if (Utils.getRequestStatus(response1) == 1) {
                    Gson gson = new Gson();
                    HttpCommitQaResponse httpCommitQaResponse = gson.fromJson(response1, HttpCommitQaResponse.class);
                    if (httpCommitQaResponse.getResponse().getSucceed() == 1) {
                        if (!mIsFav) {
                            mIsFav = true;
                        } else {
                            mIsFav = false;
                        }
                        if (mIsFav) {
                            mCollectTxt.setText(R.string.cancel_favorite);
                            mCollect.setImageResource(R.drawable.course_detail_collect);
                            Toast.makeText(ArticleDetailActivity2.this, getString(R.string.op_fav_success),Toast.LENGTH_SHORT).show();
                        } else {
                            mCollectTxt.setText(R.string.add_favorite);
                            mCollect.setImageResource(R.drawable.course_detail_nocollect);
                            Toast.makeText(ArticleDetailActivity2.this, getString(R.string.op_cancel_fav_success),Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ArticleDetailActivity2.this, getString(R.string.op_failed,
                                httpCommitQaResponse.getResponse().getErrorCode(),
                                httpCommitQaResponse.getResponse().getErrorInfo()),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_PRAISE_REPLY_COUNT:
                String resp1 = (String) msg.obj;
                L.d(TAG, "praisecount Response:" + resp1);
                if (Utils.getRequestStatus(resp1) == 1) {
                    L.d(TAG, "resp praise success");
                    Gson gson = new Gson();
                    PraiseCountResponseModel praiseCountModel = gson.fromJson(resp1, PraiseCountResponseModel.class);
                    PraiseCountResponseModel.Content content = praiseCountModel.getReponse().getContent();

                    if(morePopWindow!=null){
                        morePopWindow.setPriaseNum(content.getPraiseCount());
                        mPraiseNum = content.getPraiseCount();
                    }

                    // mPraiseCount.setText(String.format(getResources().getString(R.string.praise_count),Integer.valueOf(content.getPraiseCount())));
                } else {
                    L.d(TAG, "resp praise fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_GIVE_PRAISE:{
                String res = (String) msg.obj;
                L.d(TAG, "give praise Response:" + res);
                if (Utils.getRequestStatus(res) == 1) {
                    L.d(TAG, "resp give praise success");
                    CourseManager.requestPraiseCount(LoginUserBean.getInstance().getLoginUserid(), mArticleId);
                    // mZhan.setImageResource(R.drawable.course_detail_zhan);
                    //mGivePraise.setEnabled(false);
                    if(morePopWindow!=null){
                        morePopWindow.setPriaseBtnClickable(false);
                    }
                } else {
                    L.d(TAG, "resp give praise fail");
                }
            }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_PRAISE_BROWSER_REPLAY_COUNT:
            {
                String res = (String) msg.obj;
                L.d(TAG,"MSG_GET_PRAISE_BROWSER_REPLAY_COUNT Res:"+res);
                if (Utils.getRequestStatus(res) == 1) {
                    Gson gson = new Gson();
                    PraiseCountResponseModel praiseCountModel = gson.fromJson(res, PraiseCountResponseModel.class);
                    PraiseCountResponseModel.Content content = praiseCountModel.getReponse().getContent();

                   if(mCommentNum!=null){
                       if(Integer.parseInt(content.getReplyCount())>10000){
                           mCommentNum.setText("10000+");
                       }else{
                           mCommentNum.setText(content.getReplyCount());
                       }
                   }
                } else {
                    L.d(TAG, "MSG_GET_PRAISE_BROWSER_REPLAY_COUNT fail");
                }
                break;
            }
            default:
                break;
        }
        return false;
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{width:100%;max-width: 100%; height: auto;align =middle;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "<br><br><br></body></html>";
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
            case R.id.course_give_praise: {
                CourseManager.requestGivePraise(LoginUserBean.getInstance().getLoginUserid(), mArticleId, GIVE_PRAISE_TYPE);
            }
            break;
            case R.id.fav_course_opt:
//                if (!mIsFav) {
//                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid(), mArticleId, ADD_FAV_OPT);
//                    mIsFav = true;
//                } else {
//                    CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid(), mArticleId, CANCEL_FAV_OPT);
//                    mIsFav = false;
//                }
                break;
            case R.id.fav_course_share: {
                handleShare();
                break;
            }
            case R.id.comment_num_layout: {
                Intent intent = new Intent(this, ArticleCommentListActivity.class);
                intent.putExtra("ARTICLE_ID", mArticleId);
                Utils.start_Activity(this, intent);
                break;
            }
            case R.id.more_pop_menu: {
                morePopWindow= new MorePopWindow(ArticleDetailActivity2.this);
                morePopWindow.showPopupWindow(mMorePopMenu);

                break;
            }
            default:
                break;
        }
    }

    // 分享
    public void handleShare() {
        final ShareDialog dialog = new ShareDialog(this, this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);
        if(mHttpArticleResponse!=null){

            dialog.setShareInfo(mHttpArticleResponse.getResponse().getContent().getArttitle(),
                    "哼哼会，一个知识性，学术性平台，方便养猪产业的人士学习交流和提升。", Urls.SHARE_ARTICLE_URL+mArticleId);
        }else{
            dialog.setShareInfo("哼哼会",
                    "哼哼会，一个知识性，学术性平台，方便养猪产业的人士学习交流和提升。", Urls.SHARE_ARTICLE_URL+mArticleId);
        }

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
                holderView.commenterImg = (RoundImageView) convertView.findViewById(R.id.meeting_comment_icon);
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
        RoundImageView commenterImg;
        TextView name;
        TextView from;
        TextView commentContent;
    }

    public class MorePopWindow extends PopupWindow {
        private View conentView;

        public MorePopWindow(final Activity context) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.article_more_popmenu, null);
            int h = context.getWindowManager().getDefaultDisplay().getHeight();
            int w = context.getWindowManager().getDefaultDisplay().getWidth();
            this.setContentView(conentView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            // 刷新状态
            this.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0000000000);
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            this.setBackgroundDrawable(dw);
            // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimationPreview);

            mShareBtn = (LinearLayout) conentView
                    .findViewById(R.id.fav_course_share);

            mGivePraise = (LinearLayout) conentView.findViewById(R.id.course_give_praise);
            mCollect = (ImageView) conentView.findViewById(R.id.course_collect);
            mFav = (LinearLayout) conentView.findViewById(R.id.fav_course_opt);
            mCollectTxt = (TextView) conentView.findViewById(R.id.course_detail_correct);
            mCollectTxt.setText(R.string.add_favorite);
            mZhan = (ImageView) conentView.findViewById(R.id.course_give_zhan);
            mPraiseCount = (TextView) conentView.findViewById(R.id.course_detail_zhan);
            // 设置SelectPicPopupWindow的View

            mPraiseCount.setText(String.format(getResources().getString(R.string.praise_count),Integer.valueOf(mPraiseNum)));
            mGivePraise.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    MorePopWindow.this.dismiss();
                    CourseManager.requestGivePraise(LoginUserBean.getInstance().getLoginUserid(), mArticleId, GIVE_PRAISE_TYPE);
                }
            });

            if (mIsFav) {
                mCollectTxt.setText(R.string.cancel_favorite);
                mCollect.setImageResource(R.drawable.course_detail_collect);
            } else {
                mCollectTxt.setText(R.string.add_favorite);
                mCollect.setImageResource(R.drawable.course_detail_nocollect);
            }

            mFav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MorePopWindow.this.dismiss();
                    if (!mIsFav) {
                        CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid(), mArticleId, ADD_FAV_OPT);
                    } else {
                        CourseManager.requestFavOpt(LoginUserBean.getInstance().getLoginUserid(), mArticleId, CANCEL_FAV_OPT);
                    }
                }
            });

            mShareBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MorePopWindow.this.dismiss();
                    handleShare();
                }
            });

        }

        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
            } else {
                this.dismiss();
            }
        }

        public void setPriaseNum(String num) {
            mPraiseCount.setText(String.format(getResources().getString(R.string.praise_count),Integer.valueOf(num)));
        }

        public void setPriaseBtnClickable(boolean clickable) {
            mGivePraise.setClickable(clickable);
        }
    }

}
