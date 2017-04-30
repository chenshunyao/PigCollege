package com.xnf.henghenghui.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.L;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.BaseManager;
import com.xnf.henghenghui.logic.Face2FaceManager;
import com.xnf.henghenghui.model.TopicListResponse;
import com.xnf.henghenghui.model.TopicsItemModel;
import com.xnf.henghenghui.ui.activities.F2FListActivity;
import com.xnf.henghenghui.ui.activities.TopicDetailActivity;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.EmptyLayout;
import com.xnf.henghenghui.ui.view.PullListView;
import com.xnf.henghenghui.util.CodeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TopicsListFragment extends BaseFragment2 {
    private PullListView mListView;

    private List<TopicsItemModel> mTopicList;

    private int mTotalTopics;

    private int mIndex = 0;
    private int mRefreshIndex = 0;

    private MyAdapter mTopicListAdapter;

    private int mStartIndex= 0;
    private int mRequestNum =10;

    private String mCurTopicName;

    private EmptyLayout emptyLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView = (PullListView) view.findViewById(R.id.topics_list);
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
        emptyLayout = (EmptyLayout)view.findViewById(R.id.error_layout);

    }

    @Override
    protected void setUIHandler() {
        super.setUIHandler();
        //Face2FaceManager.setHandler(mHandler);
    }

    @Override
    public void initData() {
        super.initData();
         mTopicList = new ArrayList<TopicsItemModel>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_topicslist3, container, false);
        Face2FaceManager.setHandler(mHandler);
        initView(view);
        initData();

        mTopicListAdapter = new MyAdapter(getActivity());
        mListView.setAdapter(mTopicListAdapter);
        mListView.performRefresh();
        mListView.setNoMore();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
            return mTopicList.size();
        }

        @Override
        public Object getItem(int position) {
            return mTopicList.get(position);
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

                convertView  = mInflater.inflate(R.layout.topics_list_item, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.topic_listitem_icon);
                holder.topicName =(TextView)convertView.findViewById(R.id.topic_listitem_name);
                holder.topicDesc =(TextView)convertView.findViewById(R.id.topic_listitem_desc);
                holder.participantsNum =(TextView)convertView.findViewById(R.id.topic_listitem_num);
                convertView.setTag(holder);

            }else {
                holder = (MyViewHolder)convertView.getTag();
            }
            holder.icon.setImageResource(R.drawable.topic_list_icon);
            holder.topicName.setText("话题："+mTopicList.get(position).getTopicName());
            holder.topicDesc.setText("说明："+mTopicList.get(position).getTopicDesc());
            holder.participantsNum.setText("参与讨论："+ Html.fromHtml("<font color='#ff0000'>"+mTopicList.get(position).getParticipantsNum()+"</font>")+"人");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: 这里这一期屏蔽，暂时不做
//                    Toast.makeText(getActivity(),"Item:"+position,Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(), TopicDetailActivity.class);
//                    intent.putExtra("TOPIC_ID",mTopicList.get(position).getTopicId());
//                    intent.putExtra("TOPIC_NAME",mTopicList.get(position).getTopicName());
//                    startActivity(intent);
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

        private ImageView icon;
        private TextView topicName;
        private TextView topicDesc;
        private TextView participantsNum;
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
            }, 50);
        }
    }

    public void refreshComplate() {
        mTopicListAdapter.notifyDataSetChanged();
        mListView.refreshComplete();
    }

    public void loadMoreComplate() {
        mTopicListAdapter.notifyDataSetChanged();
        mListView.getMoreComplete();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mListView.performRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 添加数据
     */
    private void addData() {
//        for (int i = 0; i < 13; i++) {
//            TopicsItemModel  topic1 = new TopicsItemModel();
//            topic1.setTopicIcon("icon path");
//            topic1.setTopicName("种猪");
//            topic1.setTopicDesc("关于种猪相关的提问");
//            topic1.setParticipantsNum(100);
//            mTopicList.add(topic1);
//        }

        mStartIndex = mStartIndex +mRequestNum;
        mRequestNum = mStartIndex+mRequestNum;
        if(mRequestNum >= mTotalTopics){
            Toast.makeText(getActivity(),"没有更多内容了",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Face2FaceManager.getTopicsList(LoginUserBean.getInstance().getLoginUserid(),mStartIndex,mRequestNum,mHandler);
        }
    }

    public void newData() {
        //mTopicList = new ArrayList<TopicsItemModel>();
        mTopicList.clear();
//        for (int i = 0; i < 13; i++) {
//            TopicsItemModel  topic1 = new TopicsItemModel();
//            topic1.setTopicIcon("icon path");
//            topic1.setTopicName("种猪");
//            topic1.setTopicDesc("关于种猪相关的提问");
//            topic1.setParticipantsNum(100);
//            mTopicList.add(topic1);
//        }
        mStartIndex =1;
        mRequestNum = 10;
        Face2FaceManager.getTopicsList(LoginUserBean.getInstance().getLoginUserid(),mStartIndex,mRequestNum,mHandler);
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_TOPICLIST: {
                String response = (String)msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    TopicListResponse topiclistResponse = gson.fromJson(response,TopicListResponse.class);
                    mTotalTopics = topiclistResponse.getResponse().getTotalRow();
                    for (int i = 0; i < topiclistResponse.getResponse().getContent().size(); i++) {
                        TopicsItemModel  topic1 = new TopicsItemModel();
                        topic1.setTopicIcon(topiclistResponse.getResponse().getContent().get(i).getTopicCover());
                        topic1.setTopicName(topiclistResponse.getResponse().getContent().get(i).getTopicTitle());
                        topic1.setTopicDesc(topiclistResponse.getResponse().getContent().get(i).getTopicDesc());
                        topic1.setParticipantsNum(Integer.parseInt(topiclistResponse.getResponse().getContent().get(i).getCommentCount()));
                        topic1.setTopicId(topiclistResponse.getResponse().getContent().get(i).getTopicId());
                        mTopicList.add(topic1);
                    }
                    if(mTotalTopics >= BaseManager.sRequestNum){
                        mListView.setHasMore();
                    }else{
                        mListView.setNoMore();
                    }
                    if(mTopicList!=null && mTopicList.size()== 0){
                        emptyLayout.setVisibility(View.VISIBLE);
                        emptyLayout.setNoDataContent("暂时无内容");
                    }else{
                        emptyLayout.setVisibility(View.GONE);
                    }
                }else{
                    if(mTopicList!=null && mTopicList.size()== 0){
                        emptyLayout.setVisibility(View.VISIBLE);
                        emptyLayout.setNoDataContent("暂时无内容");
                    }else{
                        emptyLayout.setVisibility(View.GONE);
                    }
                    mListView.setNoMore();
                }

            }
            break;
            default:
                break;
        }
        return false;
    }
}
