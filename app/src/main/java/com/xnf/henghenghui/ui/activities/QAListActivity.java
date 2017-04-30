package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpHotKeyResponse;
import com.xnf.henghenghui.model.HttpMasterListResponse;
import com.xnf.henghenghui.model.HttpQaListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.adapter.ViewPageFragmentAdapter;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.fragments.QAListFragment;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.EmptyLayout;
import com.xnf.henghenghui.ui.view.PagerSlidingTabStrip;
import com.xnf.henghenghui.ui.widget.AutoLineGroup;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import java.util.ArrayList;

public class QAListActivity extends BaseActivity implements View.OnClickListener,QAListFragment.OnFragmentInteractionListener {

    private ImageView mBackImg;
    private TextView mTitle;
    private TextView mRightTxt;
    private EditText mEditSearch;
    private View mBtnSearch;
    private ImageView mImgQaClear;

    private String lastSearchTxt = "";

    protected AutoLineGroup mLayoutKeyword;
    protected ViewGroup mLayoutKword;
    protected ViewGroup mLayoutKword0;
    protected ViewGroup mLayoutKword1;
    protected TextView mKword0;
    protected TextView mKword1;
    protected TextView mKword2;
    protected TextView mKword3;
    protected TextView mKword4;
    protected TextView mKword5;
    protected TextView mKword6;
    protected TextView mKword7;

    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;

    private ProgressDialog dialog;

    private HttpQaListResponse mHttpQaListResponse;
    private HttpHotKeyResponse mHttpHotKeyResponse;

    private View.OnClickListener hotClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view instanceof TextView){
                mEditSearch.setText(((TextView)view).getText());
                loadQA(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        initDialog();
    }

    private void initDialog(){
        dialog = new ProgressDialog(QAListActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        dialog.setMessage(getString(R.string.progress_doing));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_qa_list);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.hengheng_qa_list_title);
        mRightTxt = (TextView)findViewById(R.id.txt_right);
        mRightTxt.setTextColor(getResources().getColor(R.color.main_green));
        mRightTxt.setVisibility(View.GONE);

        mImgQaClear = (ImageView) findViewById(R.id.img_qa_clear);
        mBtnSearch = findViewById(R.id.img_qa_search);
        mEditSearch = (EditText) findViewById(R.id.edit_qa_list_search);
        String keyword = getIntent().getStringExtra("keyword");
        if(keyword == null) keyword = "";
        mEditSearch.setText(keyword);
        if(keyword.length() > 0){
            mImgQaClear.setVisibility(View.VISIBLE);
        }else{
            mImgQaClear.setVisibility(View.GONE);
        }
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String txt = mEditSearch.getText().toString().trim();
                    if (!txt.equals(lastSearchTxt)) {
                        loadQA(true);
                    }

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditSearch.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mEditSearch.getText().length() > 0){
                    mImgQaClear.setVisibility(View.VISIBLE);
                }else{
                    mImgQaClear.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mLayoutKeyword = (AutoLineGroup) findViewById(R.id.layout_keyword);

        mLayoutKword = (ViewGroup) findViewById(R.id.layout_kword);
        mLayoutKword0 = (ViewGroup) findViewById(R.id.layout_kword_0);
        mLayoutKword1 = (ViewGroup) findViewById(R.id.layout_kword_1);
        mKword0 = (TextView) findViewById(R.id.kword_0);
        mKword1 = (TextView) findViewById(R.id.kword_1);
        mKword2 = (TextView) findViewById(R.id.kword_2);
        mKword3 = (TextView) findViewById(R.id.kword_3);
        mKword4 = (TextView) findViewById(R.id.kword_4);
        mKword5 = (TextView) findViewById(R.id.kword_5);
        mKword6 = (TextView) findViewById(R.id.kword_6);
        mKword7 = (TextView) findViewById(R.id.kword_7);

        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_tabstrip);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(20);

        mErrorLayout = (EmptyLayout) findViewById(R.id.error_layout);

        mTabsAdapter = new ViewPageFragmentAdapter(getSupportFragmentManager(),
                mTabStrip, mViewPager);
        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);

        bindOnClickLister(this, mBackImg, mRightTxt, mBtnSearch, mImgQaClear);

        loadHot();
        loadQA(true);
    }

    protected void setScreenPageLimit() {
    }

    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] titles = {"问题","话题","解答","专家","百科","文章"};
        int n = 0;
        for(String title : titles){
            adapter.addTab(title, "t"+n++, QAListFragment.class,
                    new Bundle());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_right:
                break;
            case R.id.btn_search: {
                String txt = mEditSearch.getText().toString().trim();
                if (!txt.equals(lastSearchTxt)) {
                    loadQA(true);
                }
                break;
            }
            case R.id.img_qa_clear: {
                mEditSearch.setText("");
                String txt = mEditSearch.getText().toString().trim();
                if (!txt.equals(lastSearchTxt)) {
                    loadQA(true);
                }
                break;
            }
            default:
                break;
        }
        //onPrepareOptionsMenu(menu);
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_LIST_QA: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpQaListResponse = gson.fromJson(response, HttpQaListResponse.class);
                    int mIndex = -1;
                    for(int i = 0;i < mTabsAdapter.getCount();i++) {
                        QAListFragment fragment = (QAListFragment)mTabsAdapter.instantiateItem(mViewPager,i);
                        fragment.setData(QAListActivity.this,mHttpQaListResponse,i);
                        if(mIndex < 0 && i == 5){
                            if(mHttpQaListResponse.getResponse().getContent().getArticleInfo().size() > 0){
                                mIndex = i;
                            }
                        }
                        if(mIndex < 0 && i == 4){
                            if(mHttpQaListResponse.getResponse().getContent().getEntryInfo().size() > 0){
                                mIndex = i;
                            }
                        }
                        if(mIndex < 0 && i == 3){
                            if(mHttpQaListResponse.getResponse().getContent().getExpertsInfo().size() > 0){
                                mIndex = i;
                            }
                        }
                        if(mIndex < 0 && i == 2){
                            if(mHttpQaListResponse.getResponse().getContent().getAnswerInfo().size() > 0){
                                mIndex = i;
                            }
                        }
                        if(mIndex < 0 && i == 1){
                            if(mHttpQaListResponse.getResponse().getContent().getTopicInfo().size() > 0){
                                mIndex = i;
                            }
                        }
                        if(mIndex < 0 && i == 0){
                            if(mHttpQaListResponse.getResponse().getContent().getQuestionInfo().size() > 0){
                                mIndex = i;
                            }
                        }
                        mViewPager.setCurrentItem(mIndex, false);
                    }
                } else {
                    for(int i = 0;i < mTabsAdapter.getCount();i++) {
                        QAListFragment fragment = (QAListFragment)mTabsAdapter.instantiateItem(mViewPager,i);
                        fragment.setData(QAListActivity.this,null,i);
                    }
                }
                if(dialog != null){
                    dialog.dismiss();
                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_HOT_KEY: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpHotKeyResponse  = gson.fromJson(response, HttpHotKeyResponse.class);
                    for(int i = 0;i < mHttpHotKeyResponse.getResponse().getContent().size();i++) {
                        final String hotkey = mHttpHotKeyResponse.getResponse().getContent().get(i).getKeyWord();
                        TextView view = (TextView)getLayoutInflater().inflate(R.layout.hot_item,null);
                        view.setText(hotkey);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mEditSearch.setText(hotkey);
                                loadQA(true);
                            }
                        });
                        mLayoutKeyword.addView(view);
                    }
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLayoutKeyword.requestLayout();
                        }
                    },10);

                    mLayoutKword.setVisibility(View.GONE);
                    mLayoutKword0.setVisibility(View.GONE);
                    mLayoutKword1.setVisibility(View.GONE);
                    mKword0.setVisibility(View.INVISIBLE);
                    mKword1.setVisibility(View.INVISIBLE);
                    mKword2.setVisibility(View.INVISIBLE);
                    mKword3.setVisibility(View.INVISIBLE);
                    mKword4.setVisibility(View.INVISIBLE);
                    mKword5.setVisibility(View.INVISIBLE);
                    mKword6.setVisibility(View.INVISIBLE);
                    mKword7.setVisibility(View.INVISIBLE);
                    int size = mHttpHotKeyResponse.getResponse().getContent().size();
                    if(size > 0){
                        mLayoutKword.setVisibility(View.VISIBLE);
                        mLayoutKword0.setVisibility(View.VISIBLE);
                    }
                    if(size > 4){
                        mLayoutKword1.setVisibility(View.VISIBLE);
                    }
                    if(size > 0){
                        mKword0.setVisibility(View.VISIBLE);
                        mKword0.setText(mHttpHotKeyResponse.getResponse().getContent().get(0).getKeyWord());
                        mKword0.setOnClickListener(hotClickListener);
                    }
                    if(size > 1){
                        mKword1.setVisibility(View.VISIBLE);
                        mKword1.setText(mHttpHotKeyResponse.getResponse().getContent().get(1).getKeyWord());
                        mKword1.setOnClickListener(hotClickListener);
                    }
                    if(size > 2){
                        mKword2.setVisibility(View.VISIBLE);
                        mKword2.setText(mHttpHotKeyResponse.getResponse().getContent().get(2).getKeyWord());
                        mKword2.setOnClickListener(hotClickListener);
                    }
                    if(size > 3){
                        mKword3.setVisibility(View.VISIBLE);
                        mKword3.setText(mHttpHotKeyResponse.getResponse().getContent().get(3).getKeyWord());
                        mKword3.setOnClickListener(hotClickListener);
                    }
                    if(size > 4){
                        mKword4.setVisibility(View.VISIBLE);
                        mKword4.setText(mHttpHotKeyResponse.getResponse().getContent().get(4).getKeyWord());
                        mKword4.setOnClickListener(hotClickListener);
                    }
                    if(size > 5){
                        mKword5.setVisibility(View.VISIBLE);
                        mKword5.setText(mHttpHotKeyResponse.getResponse().getContent().get(5).getKeyWord());
                        mKword5.setOnClickListener(hotClickListener);
                    }
                    if(size > 6){
                        mKword6.setVisibility(View.VISIBLE);
                        mKword6.setText(mHttpHotKeyResponse.getResponse().getContent().get(6).getKeyWord());
                        mKword6.setOnClickListener(hotClickListener);
                    }
                    if(size > 7){
                        mKword7.setVisibility(View.VISIBLE);
                        mKword7.setText(mHttpHotKeyResponse.getResponse().getContent().get(7).getKeyWord());
                        mKword7.setOnClickListener(hotClickListener);
                    }

                } else {
                    mLayoutKeyword.setVisibility(View.GONE);
                    mLayoutKword.setVisibility(View.GONE);
                }
                break;
            }
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }

    private void loadHot(){
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_HOT_KEY)
                .tag(Urls.ACTION_HOT_KEY)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_HOT_KEY;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    private void loadQA(boolean sp){
        String txt = mEditSearch.getText().toString().trim();
        lastSearchTxt = txt;
        if(sp){
            if(dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("searchKeyWord", txt);
            jsonObj.put("startRowNum", "");
            jsonObj.put("endRowNum", "");
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_INDEX_SEARCH)
                .tag(Urls.ACTION_INDEX_SEARCH)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_LIST_QA;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
    }
}
