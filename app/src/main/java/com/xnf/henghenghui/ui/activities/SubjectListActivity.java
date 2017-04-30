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
import com.xnf.henghenghui.logic.SubjectManager;
import com.xnf.henghenghui.model.HotSubjectModel;
import com.xnf.henghenghui.model.HttpSubjectListResponse;
import com.xnf.henghenghui.ui.adapter.HotSubjectAdapter;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.HotImageView;
import com.xnf.henghenghui.ui.view.HotViewPager;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 哼哼热点首页，对应着广告，搜索以及推荐的热点入口，点击首页的哼哼热点即可进入
 */
public class SubjectListActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,HotViewPager.BannerViewClick,HotViewPager.bannerDataCallback{

    private static final String TAG = "SubjectListActivity";
    private RelativeLayout mLoading_layout;
    private ListView mListView;
    private ImageView mLoading_iv;
    private Context mContext;
    private ListView mLV = null;
    private ArrayList<HotSubjectModel> mSubjetcList;
    private List<ImageView> views;
    private LinearLayout mLinearLayout;
    private HotSubjectAdapter mSubjectListAdapter;
    private boolean ismIsGetAllCommned = false;
    private String mKeyword = "";
    private boolean mIsGetAll = false;

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
       // mNews = getHotNews();


    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_subject_list);
        mLinearLayout = (LinearLayout) findViewById(R.id.activity_layout);

        mLoading_layout = (RelativeLayout)findViewById(R.id.loading_layout);
        mLoading_iv = (ImageView) findViewById(R.id.loading_img);
        mListView = (ListView) findViewById(R.id.listview);
        mSubjectListAdapter = new HotSubjectAdapter();
        mListView.setAdapter(mSubjectListAdapter);
        //setListViewHeightBasedOnChildren(mListView);
        mListView.setOnItemClickListener(this);
        mBackBtn = (TextView)findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);

        mIsGetAll = getIntent().getBooleanExtra("isGetAll",false);
        ismIsGetAllCommned = getIntent().getBooleanExtra("GET_ALL_COMMEND",false);
        mKeyword = getIntent().getStringExtra("KEYWORD");
        SubjectManager.setHandler(mHandler);

        mSubjetcList = new ArrayList<HotSubjectModel>();
        if(mIsGetAll){
            SubjectManager.getSubjectList(LoginUserBean.getInstance().getLoginUserid(),"","","","","");
        }else if(mKeyword!=null && !mKeyword.isEmpty()){
            SubjectManager.getSubjectList(LoginUserBean.getInstance().getLoginUserid(),"",mKeyword,"","","");
        }else if(ismIsGetAllCommned){
            SubjectManager.getSubjectList(LoginUserBean.getInstance().getLoginUserid(),"","","1","","");
        }else{
            SubjectManager.getSubjectList(LoginUserBean.getInstance().getLoginUserid(),"","","","","");
        }
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
        Intent intent = new Intent(this, SubjectActivity.class);
        intent.putExtra("SUBJECT_ID",mSubjetcList.get(position).getSubjectId());
        Utils.start_Activity(this, intent);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
         
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_LIST:{
                String response = (String) msg.obj;
                L.e(TAG, "Reponse:" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    HttpSubjectListResponse responseModel = gson.fromJson(response, HttpSubjectListResponse.class);
                    ArrayList<HttpSubjectListResponse.Content> contents = responseModel.getResponse().getContent();
                    //mDatas = new ArrayList<HotImageView>();
                    for (int i = 0; i < contents.size(); i++) {
                        HttpSubjectListResponse.Content content = contents.get(i);
                        HotSubjectModel subject = new HotSubjectModel();
                        subject.setSubjectId(content.getTopicid());
                        subject.setTitle(content.getTopictitle());
                        subject.setmUrl(content.getTopicphoto());
                        subject.setDesc(content.getTopicdesc());
                        subject.setZuanNum(content.getPraisecount());
                        subject.setTime(content.getTopicdatetime());
                        subject.setImage(content.getTopicphoto());
                        mSubjetcList.add(subject);
                    }
                    mSubjectListAdapter.setData(mSubjetcList);

                } else {
                    L.d(TAG, "Reponse banner fail");
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
