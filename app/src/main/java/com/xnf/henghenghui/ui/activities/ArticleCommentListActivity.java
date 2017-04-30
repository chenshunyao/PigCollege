package com.xnf.henghenghui.ui.activities;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.BaseManager;
import com.xnf.henghenghui.logic.CourseManager;
import com.xnf.henghenghui.logic.MeetingManager;
import com.xnf.henghenghui.model.CommentModel;
import com.xnf.henghenghui.model.ConferenceCommentListModel;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.PullListView;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import java.util.ArrayList;
import java.util.List;

public class ArticleCommentListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ArticleCommentListActivity";
    private static final String TYPE = "ArticleCommentListActivity";
    private PullListView mCommentsListView;
    private CommentListAdapter mAdapter;
    private ImageView mBackImg;
    private TextView mTitle;
    private List<CommentModel> mCommentList;
    //视频id
    private String mArticleId;
    private int mTotalComments;
    private int mStartIndex= 1;
    private int mRequestNum =10;

    private EditText mEditComment;
    private Button mBtnComment;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_comment_list);
        mBackImg =(ImageView)findViewById(R.id.img_back);
        mTitle =(TextView)findViewById(R.id.txt_title);
        mCommentsListView = (PullListView)findViewById(R.id.course_comment_list);
        setOnRefreshListener();
        mTitle.setText(R.string.article_comment);
        mBackImg.setVisibility(View.VISIBLE);

        bindOnClickLister(this, mBackImg);
        mAdapter = new CommentListAdapter(ArticleCommentListActivity.this);
        mCommentsListView.setAdapter(mAdapter);
        mCommentsListView.performRefresh();
        mCommentsListView.setNoMore();
        mCommentsListView.setClickable(false);

        mEditComment = (EditText) findViewById(R.id.meeting_comment_text);
        mBtnComment = (Button) findViewById(R.id.meeting_comment_add);
        mBtnComment.setOnClickListener(this);

        L.d(TAG,"initView");

    }

    private void setOnRefreshListener() {
        mCommentsListView.setOnRefreshListener(new PullListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                L.d(TAG, "onRefreshListener onRefresh!!!");
                new Thread(new MyRunnable(true)).start();
            }
        });

        mCommentsListView.setOnGetMoreListener(new PullListView.OnGetMoreListener() {

            @Override
            public void onGetMore() {
                new Thread(new MyRunnable(false)).start();
            }
        });
    }

    @Override
    protected void initData() {
        if(getIntent() == null)return;
        mArticleId = getIntent().getStringExtra("ARTICLE_ID");
        L.d(TAG,"mVid:"+mArticleId);
        if(mArticleId == null)return;
        mCommentList = new ArrayList<CommentModel>();
        MeetingManager.setHandler(mHandler);
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
        switch (msg.what){
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_COMMENT_LIST:
                mCommentList.clear();
                String resp2  =(String)msg.obj;
                L.d(TAG, "detail comment Response:" + resp2);
                if(Utils.getRequestStatus(resp2) == 1){
                    Gson gson = new Gson();
                    ConferenceCommentListModel responseModel = gson.fromJson(resp2, ConferenceCommentListModel.class);
                    mTotalComments = responseModel.getReponse().getTotalRow();
                    L.d(TAG,"handler msg mTotalComments:"+mTotalComments);
                    List<ConferenceCommentListModel.ConferenceCommentContent> contents = responseModel.getReponse().getContent();
                    for(int i=0;i<contents.size();i++){
                        ConferenceCommentListModel.ConferenceCommentContent content = contents.get(i);
                        CommentModel comment = new CommentModel();
                        comment.setComtId(content.getComtId());
                        comment.setCommentDesc(content.getContent());
                        comment.setCommentName(content.getComtNikeName());
                        comment.setComtUserId(content.getComtUserId());
                        comment.setComtDate(content.getComtDate());
                        comment.setUserPhoto(content.getUserPhoto());
                        comment.setCommentFrom("湖北养户");
                        mCommentList.add(comment);
                    }
//                    if(mTotalComments >= BaseManager.sRequestNum){
//                        mCommentsListView.setHasMore();
//                    }else{
//                        mCommentsListView.setNoMore();
//                    }
                    mCommentsListView.setNoMore();
//                    mAdapter.notifyDataSetChanged();
                    refreshComplate();
//                    if(msg.arg1 ==0 ){
//                        refreshComplate();
//                    }else{
//                        loadMoreComplate();
//                    }
                }else{
                    L.d(TAG,"comment list interface request fail!");
                }
                break;

            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_ADD_COMMENT: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Toast.makeText(this, R.string.comment_success, Toast.LENGTH_SHORT).show();
                    MeetingManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(), mArticleId,mHandler);
                } else {
                    Toast.makeText(this, R.string.comment_fail, Toast.LENGTH_SHORT).show();
                }
            }
            break;

            default:
                break;
        }
        return false;
    }

    class MyRunnable implements Runnable {

        boolean isRefresh;

        public MyRunnable(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        @Override
        public void run() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRefresh) {
                        L.d(TAG,"new Data");
                        newData();
                        refreshComplate();
                    } else {
                        addData();
                        loadMoreComplate();
                    }
                }
            }, 2000);
        }
    }

    public void newData() {
        //mTopicList = new ArrayList<TopicsItemModel>();
        mCommentList.clear();
        mStartIndex = 0;
        mRequestNum = 10;
        L.d("csy", "new data");
        //根据startIndex和requestNum来查询评论列表
        //CourseManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(),mStartIndex,mRequestNum,mHandler);
        L.d(TAG,"requestCommentList...");
        MeetingManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(),mArticleId,mHandler);
    }

    private void addData() {
        mStartIndex = mStartIndex + mRequestNum;
        mRequestNum = mStartIndex + mRequestNum;
        if(mRequestNum >= mTotalComments){
            Toast.makeText(ArticleCommentListActivity.this, "没有更多内容了", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            //CourseManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(),mStartIndex,mRequestNum,mHandler);
            MeetingManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(), mArticleId,mHandler);
        }
    }

    public void refreshComplate() {
        mAdapter.notifyDataSetChanged();
        mCommentsListView.refreshComplete();
    }

    public void loadMoreComplate() {
        mAdapter.notifyDataSetChanged();
        mCommentsListView.getMoreComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.meeting_comment_add:{
                String content = mEditComment.getText().toString();
                L.d(TAG, "onclick content:" + content);
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(this, R.string.add_comment_hint, Toast.LENGTH_SHORT).show();
                    return;
                }
                CourseManager.requestAddComment(LoginUserBean.getInstance().getLoginUserid(), mArticleId, TYPE, content);
                mEditComment.getText().clear();
                break;
            }
            default:
                break;
        }
    }

    private class CommentListAdapter extends BaseAdapter{
        private LayoutInflater mLayoutInflater;
        //private List<CommentModel> mComments;

        public CommentListAdapter(Context context){
            mLayoutInflater = LayoutInflater.from(context);
            //mComments = datas;
        }

        @Override
        public int getCount() {
            return mCommentList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCommentList.get(position);
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
                viewHolder.icon = (RoundImageView) convertView.findViewById(R.id.comment_icon);
                viewHolder.name = (TextView) convertView.findViewById(R.id.comment_name);
                viewHolder.from = (TextView) convertView.findViewById(R.id.comment_from);
                viewHolder.content = (TextView) convertView.findViewById(R.id.comment_content);
                viewHolder.comment_time = (TextView)convertView.findViewById(R.id.comment_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (CommentViewHolder) convertView.getTag();
            }
            CommentModel comment = mCommentList.get(position);
            if(comment.getUserPhoto()!=null && !comment.getUserPhoto().isEmpty()){
                Picasso.with(ArticleCommentListActivity.this).load(comment.getUserPhoto()).error(R.drawable.index_zhuanti).
                        placeholder(R.drawable.index_zhuanti).into(viewHolder.icon);
            }else{
                viewHolder.icon.setImageResource(R.drawable.index_zhuanti);
            }

            if (!TextUtils.isEmpty(comment.getCommentName())) {
                viewHolder.name.setText(comment.getCommentName());
            } else {
                viewHolder.name.setText(R.string.noname_user);
            }
            viewHolder.comment_time.setText(comment.getComtDate());
            viewHolder.from.setText(comment.getCommentFrom());
            viewHolder.content.setText(comment.getCommentDesc());
            return convertView;
        }
    }

    class CommentViewHolder{
        private RoundImageView icon;
        private TextView name;
        private TextView from;
        private TextView content;
        private TextView comment_time;
    }

}
