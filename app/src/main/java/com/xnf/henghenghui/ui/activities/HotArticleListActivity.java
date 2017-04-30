package com.xnf.henghenghui.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
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
import com.xnf.henghenghui.logic.MainPageManager;
import com.xnf.henghenghui.model.HotArticleModel;
import com.xnf.henghenghui.model.HotSubjectModel;
import com.xnf.henghenghui.model.HttpArtilcleListResponse;
import com.xnf.henghenghui.model.HttpMasterListResponse;
import com.xnf.henghenghui.ui.adapter.HotArticleAdapter;
import com.xnf.henghenghui.ui.adapter.HotSubjectAdapter;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.HotViewPager;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 哼哼热门文正列表
 */
public class HotArticleListActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,HotViewPager.BannerViewClick,HotViewPager.bannerDataCallback{

    private static final String TAG = "HotArticleListActivity";
    private RelativeLayout mLoading_layout;
    private ListView mListView;
    private ImageView mLoading_iv;
    private Context mContext;
    private ListView mLV = null;
    private ArrayList<HotArticleModel> mArticleList;
    private List<ImageView> views;
    private LinearLayout mLinearLayout;
    private HotArticleAdapter mArticleListAdapter;

    private TextView mBackBtn;
    //假数据
    private int[] drawables = new int[]{R.drawable.ex1,R.drawable.ex2,R.drawable.ex3,R.drawable.ex4};
    private int[] picdrawables = new int[]{R.drawable.p1,R.drawable.p2,R.drawable.p3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {
        MainPageManager.setHandler(mHandler);
        mArticleList = new ArrayList<HotArticleModel>();
        //mArticleList = getHotNews();
        //获取热门文章
        MainPageManager.getTopicHotArticleList(LoginUserBean.getInstance().getLoginUserid());
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_hotarticle_list);
        mLinearLayout = (LinearLayout) findViewById(R.id.activity_layout);

        mLoading_layout = (RelativeLayout)findViewById(R.id.loading_layout);
        mLoading_iv = (ImageView) findViewById(R.id.loading_img);
        mListView = (ListView) findViewById(R.id.listview);
        mArticleListAdapter = new HotArticleAdapter();
        mListView.setAdapter(mArticleListAdapter);
        mListView.setOnItemClickListener(this);
        //setListViewHeightBasedOnChildren(mListView);
        mBackBtn = (TextView)findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);

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
            case R.id.back:
                L.d(TAG,"onSubject1Click SubjectActivity");
                onBackPressed();
                break;
            case R.id.subject2:
                startActivity(new Intent(this, SubjectActivity.class));
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

    private ArrayList<HotArticleModel> getHotNews(){
        ArrayList<HotArticleModel> list = new ArrayList<HotArticleModel>();
        for(int i=0;i<14;i++){
            HotArticleModel article = new HotArticleModel();
            article.setTitle("习近平节后首场调研");
            article.setDesc("中国人常说，“形势比人强”，办好事必须顺势而为。新闻报道一直与时间在赛跑，传播形势与格局的变迁，对岛叔这样的新闻人来说，更加敏感。");
            list.add(article);
        }
        return list;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(this== null){
            return false;
        }
        switch (msg.what) {case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_TOPIC_ARTICLE_LIST:
        {
            String response = (String)msg.obj;
            if (Utils.getRequestStatus(response) == 1) {
                Gson gson = new Gson();
                HttpArtilcleListResponse httpArtilcleListResponse = gson.fromJson(response, HttpArtilcleListResponse.class);
                ArrayList<HttpArtilcleListResponse.Content> datas = new ArrayList<HttpArtilcleListResponse.Content>();
                datas  = httpArtilcleListResponse.getResponse().getContent();
                for(int i = 0;i<datas.size();i++){
                    HotArticleModel model = new HotArticleModel();
                    model.setArticleId(datas.get(i).getArtId());
                    model.setTitle(datas.get(i).getArtTitle());
                    model.setDesc(datas.get(i).getrtDesc());
                    model.setImage(datas.get(i).getArtPhoto());
                    model.setCommentNum(datas.get(i).getCommentCount());
                    model.setTime(datas.get(i).getArtDateTime());
                    //model.setmUrl(datas.get(i).);
                    model.setZuanNum(datas.get(i).getPraiseCount());
                    mArticleList.add(model);
                }
                mArticleListAdapter.setData(mArticleList);
            }else{

            }
            break;
        }
            default:
                break;
        }
        return false;
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
