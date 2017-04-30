package com.xnf.henghenghui.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.CourseManager;
import com.xnf.henghenghui.logic.MainPageManager;
import com.xnf.henghenghui.logic.SubjectManager;
import com.xnf.henghenghui.model.BannerResponseModel;
import com.xnf.henghenghui.model.HotSubjectModel;
import com.xnf.henghenghui.model.HttpSubjectListResponse;
import com.xnf.henghenghui.model.MainBannerResponseModel;
import com.xnf.henghenghui.ui.adapter.HotSubjectAdapter;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.FixedListView;
import com.xnf.henghenghui.ui.view.HotImageView;
import com.xnf.henghenghui.ui.view.HotViewPager;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 哼哼热点首页，对应着广告，搜索以及推荐的热点入口，点击首页的哼哼热点即可进入
 */
public class HotSubjectActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,HotViewPager.BannerViewClick,HotViewPager.bannerDataCallback{

    private static final String TAG = "HotSubjectActivity";
    private RelativeLayout mLoading_layout;
    //HotPullToRefreshView replace ListView
    private FixedListView mListView;
    private HotViewPager mHvp;
    private ImageView mLoading_iv;
    private Context mContext;
    private ListView mLV = null;
    private List<HotImageView> mConvers;
    private ArrayList<HotSubjectModel> mSubjetcList;
    private List<ImageView> views;
    private LinearLayout mLinearLayout;
    private HotSubjectAdapter mHotSubjectAdapter;

    private TextView mAllSubject;
    private ImageView mSearchBtn;

    private EditText editQaSearch;
    private TextView editQaSearchHint;

    private LinearLayout mLoadMoreBtn;

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
        //mDatas = getAdData();
        //mSubjetcList = getHotNews();
        mSubjetcList = new ArrayList<HotSubjectModel>();
        SubjectManager.getSubjectList(LoginUserBean.getInstance().getLoginUserid(),"","","1","","");
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_hot_subject);
        mHvp = (HotViewPager) findViewById(R.id.hotviewpager);
        mHvp.init(this);
        mHvp.setBaseAdapterData(mConvers, this);
        //mHvp.setSubOnClickListener(this);
        mLinearLayout = (LinearLayout) findViewById(R.id.activity_layout);

        mLoading_layout = (RelativeLayout)findViewById(R.id.loading_layout);
        mLoading_iv = (ImageView) findViewById(R.id.loading_img);
        mListView = (FixedListView) findViewById(R.id.listview);
        mHotSubjectAdapter = new HotSubjectAdapter();
        //mHotSubjectAdapter.setData(mSubjetcList);
        mListView.setAdapter(mHotSubjectAdapter);
//        setListViewHeightBasedOnChildren(mListView);
        mListView.setOnItemClickListener(this);

        mAllSubject = (TextView)findViewById(R.id.all_btn);
        mAllSubject.setOnClickListener(this);
        mSearchBtn = (ImageView)findViewById(R.id.btn_search);
        mSearchBtn.setOnClickListener(this);

        editQaSearch = (EditText)findViewById(R.id.edit_qa_search);
        editQaSearchHint = (TextView)findViewById(R.id.edit_qa_search_hint);
        editQaSearch.setFocusable(false);
        editQaSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    editQaSearchHint.setVisibility(View.GONE);
                }else{
                    editQaSearchHint.setVisibility(View.VISIBLE);
                }
            }
        });
        editQaSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    Intent it = new Intent(HotSubjectActivity.this, SubjectListActivity.class);
                    it.putExtra("keyword", editQaSearch.getText().toString().trim());
                    Utils.start_Activity(HotSubjectActivity.this, it);
                    return true;
                }
                return false;
            }
        });

        mLoadMoreBtn =(LinearLayout) findViewById(R.id.subject_loadmore);
        mLoadMoreBtn.setOnClickListener(this);

        CourseManager.setHandler(mHandler);
        SubjectManager.setHandler(mHandler);

        mBackBtn =(TextView)findViewById(R.id.back);
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
            case R.id.all_btn:{
                L.d(TAG,"onSubject1Click SubjectActivity");
                Intent intent = new Intent(this, SubjectListActivity.class);
                intent.putExtra("isGetAll",true);
                Utils.start_Activity(this, intent);
            }
                break;
            case R.id.btn_search:
            {
                L.d(TAG,"onSubject1Click SubjectActivity");
                if(editQaSearch.getText().toString().isEmpty()){
                    Toast.makeText(this,"请输入关键字",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this, SubjectListActivity.class);
                intent.putExtra("KEYWORD",editQaSearch.getText().toString());
                Utils.start_Activity(this, intent);
            }
                break;
            case R.id.subject_loadmore:{
                L.d(TAG,"onSubject1Click SubjectActivity");
                Intent intent = new Intent(this, SubjectListActivity.class);
                intent.putExtra("GET_ALL_COMMEND",true);
                Utils.start_Activity(this, intent);
            }

                break;
            case R.id.back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainPageManager.getIndexBanner(LoginUserBean.getInstance().getLoginUserid(),"topic");
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_INDEX_BANNER:{
                String response = (String)msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    MainBannerResponseModel bannerResponseModel = gson.fromJson(response,MainBannerResponseModel.class);
                    List<MainBannerResponseModel.BannerContent> contents = bannerResponseModel.getReponse().getContents();
                    mConvers = new ArrayList<HotImageView>();
                    for (int i = 0; i < contents.size(); i++) {
                        MainBannerResponseModel.BannerContent content = contents.get(i);
//                        HotImageView image = new HotImageView(getContext());
//                        L.d(TAG,"contents:"+content.getBannerImgURL());
//                        //将数据填充到datas中
//                        image.setImageUrl(content.getBannerImgURL(),mImageLoader,mOptions);

                        HotImageView img = new HotImageView(this);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                        //mImageLoader.displayImage(content.getBannerImgURL(), img);
                        if(content.getBannerImgURL()==null || content.getBannerImgURL().isEmpty()){
                            img.setImageResource(R.drawable.banner_default);
                        }else{
                            Picasso.with(this).load(content.getBannerImgURL()).error(R.drawable.banner_default).placeholder(R.drawable.banner_default).into(img);
                        }
                        mConvers.add(img);

                    }
                    if(mConvers != null && mConvers.size() > 0){
                        mHvp.setBaseAdapterData(mConvers, this);
                    }

                }else{

                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_LIST:{
                String response = (String) msg.obj;
                L.e(TAG, "Reponse:" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    L.d(TAG, "Reponse banner success");
                    Gson gson = new Gson();
                    HttpSubjectListResponse responseModel = gson.fromJson(response, HttpSubjectListResponse.class);
                    ArrayList<HttpSubjectListResponse.Content> contents = responseModel.getResponse().getContent();
                    //mDatas = new ArrayList<HotImageView>();
                    for (int i = 0; i < contents.size(); i++) {
                        HttpSubjectListResponse.Content content = contents.get(i);
                        HotSubjectModel subject = new HotSubjectModel();
                        subject.setSubjectId(content.getTopicid());
                        subject.setTitle(content.getTopictitle());
                        subject.setImage(content.getTopicphoto());
                        subject.setDesc(content.getTopicdesc());
                        subject.setZuanNum(content.getPraisecount());
                        subject.setTime(content.getTopicdatetime());
                        mSubjetcList.add(subject);
                    }
                    mHotSubjectAdapter.setData(mSubjetcList);

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
