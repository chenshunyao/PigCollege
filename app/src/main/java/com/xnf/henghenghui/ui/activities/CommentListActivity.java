package com.xnf.henghenghui.ui.activities;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.BaseManager;
import com.xnf.henghenghui.logic.CourseManager;
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

public class CommentListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "csy_CommentListActivity";
    private PullListView mCommentsList;
    private CommentListAdapter mAdapter;
    private ImageView mBackImg;
    private TextView mTitle;
    private EditText mCommentEdit;
    private Button mCommentBtn;
    private List<CommentModel> mCommentList;
    //视频id
    private String mVid;
    private int mTotalComments;
    private int mStartIndex= 1;
    private int mRequestNum =10;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_comment_list);
        mBackImg =(ImageView)findViewById(R.id.img_back);
        mTitle =(TextView)findViewById(R.id.txt_title);
        mCommentEdit =(EditText)findViewById(R.id.meeting_comment_text);
        mCommentBtn =(Button)findViewById(R.id.meeting_comment_add);
        mCommentsList = (PullListView)findViewById(R.id.course_comment_list);
        setOnRefreshListener();
        mTitle.setText(R.string.course_comment);
        mBackImg.setVisibility(View.VISIBLE);
        bindOnClickLister(this, mBackImg,mCommentBtn);
        mAdapter = new CommentListAdapter(CommentListActivity.this);
        mCommentsList.setAdapter(mAdapter);
        mCommentsList.performRefresh();
        mCommentsList.setNoMore();
        L.d(TAG,"initView");

    }

    private void setOnRefreshListener() {
        mCommentsList.setOnRefreshListener(new PullListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                L.d(TAG, "onRefreshListener onRefresh!!!");
                new Thread(new MyRunnable(true)).start();
            }
        });

        mCommentsList.setOnGetMoreListener(new PullListView.OnGetMoreListener() {

            @Override
            public void onGetMore() {
                new Thread(new MyRunnable(false)).start();
            }
        });
    }

    @Override
    protected void initData() {
        if(getIntent() == null)return;
        mVid = getIntent().getStringExtra("vId");
        L.d(TAG,"mVid:"+mVid);
        if(mVid == null)return;
        mCommentList = new ArrayList<CommentModel>();
        CourseManager.setHandler(mHandler);
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
                String resp2  =(String)msg.obj;
                L.d(TAG, "detail comment Response:" + resp2);
                if(Utils.getRequestStatus(resp2) == 1){
                    if(mCommentList != null && mCommentList.size() > 0)mCommentList.clear();
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
                        comment.setComtUserId(content.getComtUserId());
                        comment.setComtDate(content.getComtDate());
                        comment.setCommentFrom("湖北养户");
                        mCommentList.add(comment);
                    }
                    if(mTotalComments >= BaseManager.sRequestNum){
                        mCommentsList.setHasMore();
                    }else{
                        mCommentsList.setNoMore();
                    }
                    if(msg.arg1 ==0 ){
                        refreshComplate();
                    }else{
                        loadMoreComplate();
                    }
                }else{
                    L.d(TAG,"comment list interface request fail!");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_ADD_COMMENT:
                String response  =(String)msg.obj;
                L.d(TAG, "add course comment Response:" + response);
                if(Utils.getRequestStatus(response) == 1){
                    Toast.makeText(getBaseContext(),R.string.comment_success,Toast.LENGTH_SHORT).show();
                    CourseManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(), mVid,mHandler);
                }else{
                    Toast.makeText(getBaseContext(),R.string.comment_fail,Toast.LENGTH_SHORT).show();
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
            }, 50);
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
        CourseManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(),mVid , mHandler);
    }

    private void addData() {
        mStartIndex = mStartIndex + mRequestNum;
        mRequestNum = mStartIndex + mRequestNum;
        if(mRequestNum >= mTotalComments){
            Toast.makeText(CommentListActivity.this, "没有更多内容了", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            //CourseManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(),mStartIndex,mRequestNum,mHandler);
            CourseManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(), mVid, mHandler);
        }
    }

    public void refreshComplate() {
        mAdapter.notifyDataSetChanged();
        mCommentsList.refreshComplete();
    }

    public void loadMoreComplate() {
        mAdapter.notifyDataSetChanged();
        mCommentsList.getMoreComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.meeting_comment_add:
                String content = mCommentEdit.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(getBaseContext(),R.string.add_comment_hint,Toast.LENGTH_SHORT).show();
                    return;
                }
                CourseManager.requestAddComment(LoginUserBean.getInstance().getLoginUserid(),mVid,"",content);
                mCommentEdit.getText().clear();
                break;
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
                Picasso.with(CommentListActivity.this).load(comment.getUserPhoto()).error(R.drawable.index_zhuanti).
                        placeholder(R.drawable.index_zhuanti).into(viewHolder.icon);
            }else{
                viewHolder.icon.setImageResource(R.drawable.index_zhuanti);
            }
            viewHolder.comment_time.setText(comment.getComtDate());
            viewHolder.name.setText(comment.getComtUserId());
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
