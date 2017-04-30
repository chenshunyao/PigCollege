package com.xnf.henghenghui.ui.activities;

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

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.ExpertQsItemModel;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.view.PullListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TopicDetailActivity extends BaseActivity {

    private PullListView mListView;

    private List<ExpertQsItemModel> mExpertQsList;
    private Handler mHandler = new Handler();

    private int mIndex = 0;
    private int mRefreshIndex = 0;

    private MyAdapter mExpertQsListAdapter;

    private ImageView mBackImg;

    private TextView mTitleTextView;

    private TextView mPublishTopicBtn;

    private String mTopicName;

    private String mTopicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

        Date date = new Date();
        mExpertQsList = new ArrayList<ExpertQsItemModel>();
        ExpertQsItemModel qs1 = new ExpertQsItemModel();
        //qs1.setExpertQsName("关于种猪相关的提问");
        qs1.setExpertQsNum("100");
        qs1.setExpertQsLastestTime(date.toString());
        mExpertQsList.add(qs1);

        ExpertQsItemModel qs2 = new ExpertQsItemModel();
        //qs2.setExpertQsName("关于种猪相关的提问");
        qs2.setExpertQsNum("100");
        qs2.setExpertQsLastestTime(date.toString());
        mExpertQsList.add(qs2);

        ExpertQsItemModel qs3 = new ExpertQsItemModel();
        //qs3.setExpertQsName("关于种猪相关的提问");
        qs3.setExpertQsNum("100");
        qs3.setExpertQsLastestTime(date.toString());
        mExpertQsList.add(qs3);

        ExpertQsItemModel qs4 = new ExpertQsItemModel();
        //qs4.setExpertQsName("关于种猪相关的提问");
        qs4.setExpertQsNum("100");
        qs4.setExpertQsLastestTime(date.toString());
        mExpertQsList.add(qs4);

        ExpertQsItemModel qs5 = new ExpertQsItemModel();
        //qs5.setExpertQsName("关于种猪相关的提问");
        qs5.setExpertQsNum("100");
        qs5.setExpertQsLastestTime(date.toString());
        mExpertQsList.add(qs5);

        ExpertQsItemModel qs6 = new ExpertQsItemModel();
       // qs6.setExpertQsName("关于种猪相关的提问");
        qs6.setExpertQsNum("100");
        qs6.setExpertQsLastestTime(date.toString());
        mExpertQsList.add(qs6);

        ExpertQsItemModel qs7 = new ExpertQsItemModel();
        //qs7.setExpertQsName("关于种猪相关的提问");
        qs7.setExpertQsNum("100");
        qs7.setExpertQsLastestTime(date.toString());
        mExpertQsList.add(qs7);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_topic_detail);

        mTopicName = getIntent().getStringExtra("TOPIC_NAME");
        mTopicId = getIntent().getStringExtra("TOPIC_ID");
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
        mTitleTextView =(TextView)findViewById(R.id.txt_title);
        if(!mTopicName.isEmpty()){
            mTitleTextView.setText(mTopicName);
        }
        mExpertQsListAdapter = new MyAdapter(this);
        mListView.setAdapter(mExpertQsListAdapter);
        mListView.performRefresh();

        mPublishTopicBtn = (TextView)findViewById(R.id.publish_topic_btn);
        mPublishTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TopicDetailActivity.this, PublishToipcActivity.class);
                startActivity(intent);
            }
        });
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
            }, 2000);
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
        for (int i = 0; i < 13; i++) {
            ExpertQsItemModel  qs5 = new ExpertQsItemModel();
            //qs5.setExpertQsName("关于种猪相关的提问");
            qs5.setExpertQsNum("100");
            qs5.setExpertQsLastestTime(date.toString());
            mExpertQsList.add(qs5);
        }
    }

    public void newData() {
        mExpertQsList.clear();
        Date date = new Date();
        for (int i = 0; i < 13; i++) {
            ExpertQsItemModel  qs5 = new ExpertQsItemModel();
            //qs5.setExpertQsName("关于种猪相关的提问");
            qs5.setExpertQsNum("100");
            qs5.setExpertQsLastestTime(date.toString());
            mExpertQsList.add(qs5);
        }
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
        return false;
    }

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

                convertView  = mInflater.inflate(R.layout.expert_f2f_list_item, null);
                holder.expertQsDes =(TextView)convertView.findViewById(R.id.expert_f2f_listitem_qs_desc);
                holder.expertQsNum =(TextView)convertView.findViewById(R.id.expert_f2f_listitem_qs_num);
                holder.expertLastestReplyTime =(TextView)convertView.findViewById(R.id.expert_f2f_listitem_lasest_reply);
                holder.toDetail = (TextView)convertView.findViewById(R.id.expert_f2f_listitem_detail);
                holder.askBtn =(TextView)convertView.findViewById(R.id.expert_f2f_listitem_ask);
                convertView.setTag(holder);

            }else {
                holder = (MyViewHolder)convertView.getTag();
            }
            holder.expertQsDes.setText(mExpertQsList.get(position).getQsDescription());
            holder.expertQsNum.setText("参与讨论专家人数："+mExpertQsList.get(position).getExpertQsNum());
            holder.expertLastestReplyTime.setText("最新回复："+mExpertQsList.get(position).getExpertQsLastestTime());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TopicDetailActivity.this,"Item:"+position,Toast.LENGTH_SHORT).show();
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

        private TextView expertQsDes;
        private TextView expertQsNum;
        private TextView expertLastestReplyTime;
        private TextView toDetail;
        private TextView askBtn;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
