package com.xnf.henghenghui.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.L;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.BaseManager;
import com.xnf.henghenghui.logic.Face2FaceManager;
import com.xnf.henghenghui.model.Base;
import com.xnf.henghenghui.model.ExpertQsItemModel;
import com.xnf.henghenghui.model.F2FCategoryResponse;
import com.xnf.henghenghui.model.F2FListResponse;
import com.xnf.henghenghui.model.QACategory;
import com.xnf.henghenghui.model.TopicsItemModel;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.HandlerInterface;
import com.xnf.henghenghui.ui.utils.HenghenghuiHandler;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.EmptyLayout;
import com.xnf.henghenghui.ui.view.PullListView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class F2FListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private PullListView mListView;

    private List<ExpertQsItemModel> mExpertQsList;

    private int mIndex = 0;
    private int mRefreshIndex = 0;

    private MyAdapter mExpertQsListAdapter;

    private ImageView mBackImg;

    private TextView mTitleTextView;

    private String mCategoryName;

    private String mCategoryId;

//    private EmptyLayout emptyLayout;
    private LinearLayout emptyLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {

        Date date = new Date();
        mExpertQsList = new ArrayList<ExpertQsItemModel>();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_f2f_list);

        mCategoryName = getIntent().getStringExtra("CATEGORY_NAME");
        mCategoryId = getIntent().getStringExtra("CATEGORY_ID");

        //mUIHandler = new HenghenghuiHandler(this);
        Face2FaceManager.setHandler(mHandler);
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

        mListView.setOnItemClickListener(this);

        View emptyView = findViewById(R.id.empty);
        mListView.setEmptyView(emptyView);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        mBackImg.setVisibility(View.VISIBLE);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTitleTextView =(TextView)findViewById(R.id.txt_title);
        mTitleTextView.setText(mCategoryName);
        mExpertQsListAdapter = new MyAdapter(this);
        mListView.setAdapter(mExpertQsListAdapter);
        mListView.setCanRefresh(false);
        mListView.performRefresh();
        mListView.setNoMore();

        emptyLayout = (LinearLayout)findViewById(R.id.empty_layout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, AnswerDetailActivity.class);
        startActivity(intent);
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
       // Date date = new Date();
//        for (int i = 0; i < 13; i++) {
//            ExpertQsItemModel  qs = new ExpertQsItemModel();
//            qs.setExpertQsName("关于种猪相关的提问");
//            qs.setQsDescription("我家的猪，最近食欲不是很好，天天在猪圈里面乱叫，我怀疑是不是有点不正常，麻烦解答一下，医生！最近食欲不是很好，天天在猪圈里面乱叫，我怀疑是不是有点不正常，麻烦解答一下，医生！");
//            qs.setExpertQsNum("100");
//            qs.setExpertQsLastestTime(StringUtils.getDateString(date));
//            mExpertQsList.add(qs);
//        }
        //Face2FaceManager.getF2FQsList(LoginUserBean.getInstance().getLoginUserid(),mCategoryId,mHandler);
    }

    public void newData() {
        mExpertQsList.clear();
        Date date = new Date();
//        for (int i = 0; i < 13; i++) {
//            ExpertQsItemModel  qs5 = new ExpertQsItemModel();
//            qs5.setExpertQsName("关于种猪相关的提问");
//            qs5.setExpertQsNum("100");
//            qs5.setExpertQsLastestTime(date.toString());
//            mExpertQsList.add(qs5);
//        }
        Face2FaceManager.getF2FQsList(LoginUserBean.getInstance().getLoginUserid(),mCategoryId,mHandler);
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

                convertView  = mInflater.inflate(R.layout.expert_f2f_list_item2, null);
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

            String num=mExpertQsList.get(position).getExpertQsNum();
            holder.expertQsNum.setText("参与讨论专家数："+ Html.fromHtml("<font color=\"#ff0000\">"+num));
            //holder.expertQsNum.setText("参与讨论专家数："+mExpertQsList.get(position).getExpertQsNum());
            holder.expertLastestReplyTime.setText("最新回复："+mExpertQsList.get(position).getExpertQsLastestTime());
            holder.toDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
//                    intent.setClass(F2FListActivity.this, ExpertQSDetailActivity.class);
//                    intent.putExtra("questionId",(mExpertQsList.get(position).getExpertQsId()));
//                    intent.putExtra("questionDes",(mExpertQsList.get(position).getQsDescription()));
//                    intent.putExtra("qsExpertNum",(mExpertQsList.get(position).getExpertQsNum()));
//                    intent.putExtra("qsLastestTime",(mExpertQsList.get(position).getExpertQsLastestTime()));
                    //TODO 2
                    intent.setClass(F2FListActivity.this, ExpertQSDetailActivity2.class);
                    intent.putExtra("qtid",(mExpertQsList.get(position).getExpertQsId()));
                    startActivity(intent);
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

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_F2F_LIST: {
                String response = (String)msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    F2FListResponse f2FListResponse = gson.fromJson(response,F2FListResponse.class);
                    int size = f2FListResponse.getResponse().getContent().size();
                    for (int i = 0; i < f2FListResponse.getResponse().getContent().size(); i++) {
                        ExpertQsItemModel  qs = new ExpertQsItemModel();
                        qs.setExpertQsId(f2FListResponse.getResponse().getContent().get(i).getQtId());
                        qs.setQsDescription(f2FListResponse.getResponse().getContent().get(i).getQtTitle());
                        //qs.setQsDescription("我家的猪，最近食欲不是很好，天天在猪圈里面乱叫，我怀疑是不是有点不正常，麻烦解答一下，医生！最近食欲不是很好，天天在猪圈里面乱叫，我怀疑是不是有点不正常，麻烦解答一下，医生！");
                        qs.setExpertQsNum(f2FListResponse.getResponse().getContent().get(i).getAnswerExpertCount());
                        qs.setExpertQsLastestTime(f2FListResponse.getResponse().getContent().get(i).getLastReplyTime());
                        mExpertQsList.add(qs);
                    }
                    mExpertQsListAdapter.notifyDataSetChanged();
                    mListView.refreshComplete();
                    if(size >= BaseManager.sRequestNum){
                        mListView.setHasMore();
                    }
                    if(mExpertQsList.size()== 0){
                        emptyLayout.setVisibility(View.VISIBLE);
//                        emptyLayout.setNoDataContent("暂时无内容");
                        mListView.setVisibility(View.GONE);
                    }else{
                        emptyLayout.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }

                }else{
                    if(mExpertQsList!=null && mExpertQsList.size()== 0){
                        emptyLayout.setVisibility(View.VISIBLE);
//                        emptyLayout.setNoDataContent("暂时无内容");
                        mListView.setVisibility(View.GONE);
                    }else{
                        emptyLayout.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }

                }


            }
            break;
            default:
                break;
        }
        return false;
    }
}
