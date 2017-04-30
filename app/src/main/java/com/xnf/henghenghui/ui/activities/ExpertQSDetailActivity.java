package com.xnf.henghenghui.ui.activities;

/**
 * 专家面对面中的问题详情页面，该页面为点击专家面对面类别列表点击后的页面
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.BaseManager;
import com.xnf.henghenghui.logic.Face2FaceManager;
import com.xnf.henghenghui.model.ExpertQsItemModel;
import com.xnf.henghenghui.model.ExpertReplyItemModel;
import com.xnf.henghenghui.model.F2FListResponse;
import com.xnf.henghenghui.model.F2fDetailReplyContent;
import com.xnf.henghenghui.model.HttpF2FDetailReplyListResponse;
import com.xnf.henghenghui.model.HttpF2FDetailResponse;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.image.multi_image_selector.bean.Image;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.PullListView;
import com.xnf.henghenghui.ui.view.ShareDialog;
import com.xnf.henghenghui.util.CodeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpertQSDetailActivity extends BaseActivity implements View.OnClickListener {

    private PullListView mListView;

    private List<F2fDetailReplyContent> mExpertQsList;

    private int mIndex = 0;
    private int mRefreshIndex = 0;

    private MyAdapter mExpertQsListAdapter;

    private ImageView mBackImg;

    private TextView mQsDesTextView;
    private TextView mAskBtn;
    private TextView mLastestReplyTextView;
    private TextView mReplyNumTextView;

    private ImageView mShareBtn;

    private String mQsId;
    private String mQsDes;
    private String mQsLastestTime;
    private String mReplyNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

        Date date = new Date();
        mExpertQsList = new ArrayList<F2fDetailReplyContent>();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_expert_qs_detail);
        mQsId = getIntent().getStringExtra("questionId");
        mQsDes = getIntent().getStringExtra("questionDes");
        mQsLastestTime = getIntent().getStringExtra("qsLastestTime");
        mReplyNum = getIntent().getStringExtra("qsExpertNum");

        mListView = (PullListView) findViewById(R.id.topics_list);
        mListView.setOnRefreshListener(new PullListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread(new MyRunnable(true)).start();
            }
        });

        mListView.setOnGetMoreListener(new PullListView.OnGetMoreListener() {

            @Override
            public void onGetMore() {
                new Thread(new MyRunnable(false)).start();
            }
        });

        mBackImg = (ImageView)findViewById(R.id.img_back);
        mBackImg.setVisibility(View.VISIBLE);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQsDesTextView =(TextView)findViewById(R.id.expert_qs_desc);
        mQsDesTextView.setText("描述："+mQsDes);

        mAskBtn = (TextView)findViewById(R.id.expert_f2f_ask);
        mAskBtn.setOnClickListener(this);

        mReplyNumTextView = (TextView)findViewById(R.id.expert_reply_num);
        mReplyNumTextView.setText("参与讨论专家数："+mReplyNum);
        mLastestReplyTextView =(TextView)findViewById(R.id.expert_lasest_reply);
        mLastestReplyTextView.setText("最新回复："+mQsLastestTime);

        mExpertQsListAdapter = new MyAdapter(this);
        mListView.setAdapter(mExpertQsListAdapter);
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        emptyView.setText("This appears when the list is empty");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)mListView.getParent()).addView(emptyView);
        mListView.setEmptyView(emptyView);

        mShareBtn = (ImageView)findViewById(R.id.txt_right);
        mShareBtn.setOnClickListener(this);
        mListView.performRefresh();
        mListView.setCanRefresh(false);
        mListView.setNoMore();
        Face2FaceManager.setHandler(mHandler);
        Face2FaceManager.getF2fQSDetails(LoginUserBean.getInstance().getLoginUserid(),mQsId,"","","","");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_right:
                handleShare();
                break;
            case R.id.expert_f2f_ask:
                Intent intent = new Intent(this, QAActivity.class);
                Utils.start_Activity(this, intent);
                break;
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
        if(mQsDesTextView!=null){

            dialog.setShareInfo("哼哼会",
                    mQsDesTextView.getText().toString(), Urls.SHARE_ARTICLE_URL+mQsId);
        }else{
            dialog.setShareInfo("哼哼会",
                    "", Urls.SHARE_ARTICLE_URL+mQsId);
        }
        dialog.show();
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
                        newData();
                        refreshComplate();
                    } else {
                        addData();
                        loadMoreComplate();
                    }
                }
            }, 100);
        }
    }

    public void refreshComplate() {
        mExpertQsListAdapter.notifyDataSetChanged();
        mListView.refreshComplete();
    }

    public void loadMoreComplate() {
        mExpertQsListAdapter.notifyDataSetChanged();
        mListView.getMoreComplete();
    }

    /**
     * 添加数据
     */
    private void addData() {
        Date date = new Date();
//        for (int i = 0; i < 2; i++) {
//            ExpertReplyItemModel qs6 = new ExpertReplyItemModel();
//            qs6.setExpertImg("关于种猪相关的提问");
//            qs6.setQsDescription("我家的猪，最近食欲不是很好，天天在猪圈里面乱叫，我怀疑是不是有点不正常，麻烦解答一下，医生！最近食欲不是很好，天天在猪圈里面乱叫，我怀疑是不是有点不正常，麻烦解答一下，医生！");
//            qs6.setExpertReplyComentNum("100");
//            qs6.setExpertReplyLikeNum("50");
//            mExpertQsList.add(qs6);
//        }
    }

    public void newData() {
        mExpertQsList.clear();
        Face2FaceManager.getF2fQSDetailsReplyList(LoginUserBean.getInstance().getLoginUserid(),mQsId,"","","","",null);
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_ANSWER_QUESTION_INFO:{
                String response = (String)msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    HttpF2FDetailReplyListResponse replyListResponse = gson.fromJson(response,HttpF2FDetailReplyListResponse.class);
                    int size = replyListResponse.getResponse().getContent().size();
                    for (int i = 0; i < replyListResponse.getResponse().getContent().size(); i++) {
                        mExpertQsList.add(replyListResponse.getResponse().getContent().get(i));
                    }
                    mExpertQsListAdapter.notifyDataSetChanged();
                    mListView.refreshComplete();
                    if(size >= BaseManager.sRequestNum){
                        mListView.setHasMore();
                    }else{
                        mListView.setNoMore();
                    }
                    if(mExpertQsList.size()== 0){
//                        emptyLayout.setVisibility(View.VISIBLE);
//                        emptyLayout.setNoDataContent("暂时无内容");
                    }else{
//                        emptyLayout.setVisibility(View.GONE);
                    }

                }else{
                    if(mExpertQsList!=null && mExpertQsList.size()== 0){
//                        emptyLayout.setVisibility(View.VISIBLE);
//                        emptyLayout.setNoDataContent("暂时无内容");
                    }else{
//                        emptyLayout.setVisibility(View.GONE);
                    }
                    mListView.setNoMore();
                }
            }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_QUESTIONV2_INFO:{
                String response = (String)msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    HttpF2FDetailResponse detailResponse = gson.fromJson(response,HttpF2FDetailResponse.class);
                    mQsDesTextView.setText("描述："+detailResponse.getResponse().getContent().get(0).getQtDesc());

                }else{

                }
                break;
            }
            default:
                break;
        }
        return false;
    }

    //TODO adapter 需要进行调整
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public int getCount() {
            return mExpertQsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mExpertQsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyViewHolder holder = null;
            if (convertView == null) {

                holder=new MyViewHolder();

                convertView  = mInflater.inflate(R.layout.expert_reply_list_item, null);
                holder.expertImg = (ImageView)convertView.findViewById(R.id.expert_image);
                holder.expertQsDes =(TextView)convertView.findViewById(R.id.expert_qs_desc);
                holder.expertQsCommentNum =(TextView)convertView.findViewById(R.id.comment_num);
                holder.expertQsLikeNum =(TextView)convertView.findViewById(R.id.like_num);
                holder.recommend = (TextView)convertView.findViewById(R.id.expert_recommend);
                convertView.setTag(holder);

            }else {
                holder = (MyViewHolder)convertView.getTag();
            }
//            holder.expertImg.setImageDrawable();
            if(mExpertQsList.get(position).getAqPhoto() == null || mExpertQsList.get(position).getAqPhoto().isEmpty()){
                holder.expertImg.setImageResource(R.drawable.shouyi2);
            }else{
                Picasso.with(ExpertQSDetailActivity.this).load(mExpertQsList.get(position).getAqPhoto()).error(R.drawable.shouyi2).
                        placeholder(R.drawable.shouyi2).into(holder.expertImg);
            }

            holder.expertQsDes.setText(mExpertQsList.get(position).getAqContent());
            holder.expertQsCommentNum.setText("评论("+mExpertQsList.get(position).getPraiseCount()+")");
            holder.expertQsLikeNum.setText("赞("+mExpertQsList.get(position).getPraiseCount()+")");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(ExpertQSDetailActivity.this, AnswerDetailActivity.class);
//                    it.putExtra("keyword", editQaSearch.getText().toString().trim());
                    Utils.start_Activity(ExpertQSDetailActivity.this, it);
                }
            });
            return convertView;

        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }


    private final class MyViewHolder{

        private ImageView expertImg;
        private TextView expertQsDes;
        private TextView expertQsCommentNum;
        private TextView expertQsLikeNum;
        private TextView recommend;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
