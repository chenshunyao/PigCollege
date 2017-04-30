package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpBaikeCategoryListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.adapter.ViewPageFragmentAdapter;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.fragments.BaikeListFragment;
import com.xnf.henghenghui.ui.fragments.QAListFragment;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.EmptyLayout;
import com.xnf.henghenghui.ui.view.PagerSlidingTabStrip;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

public class BaikeActivity extends BaseActivity implements View.OnClickListener,BaikeListFragment.OnFragmentInteractionListener {

    private ImageView mBackImg;
    private TextView mTitle;
    private TextView mRightTxt;

//    protected AutoLineGroup mLayoutKeyword;
    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;

    private ProgressDialog dialog;

    private HttpBaikeCategoryListResponse mHttpBaikeCategoryListResponse;
//    private HttpQaListResponse mHttpQaListResponse;
//    private HttpHotKeyResponse mHttpHotKeyResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        initDialog();
    }

    private void initDialog(){
        dialog = new ProgressDialog(BaikeActivity.this);
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
        setContentView(R.layout.activity_baike_list);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.hengheng_baike_list_title);
        mRightTxt = (TextView)findViewById(R.id.txt_right);
        mRightTxt.setTextColor(getResources().getColor(R.color.main_green));
        mRightTxt.setVisibility(View.GONE);

//        mLayoutKeyword = (AutoLineGroup) findViewById(R.id.layout_keyword);

        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_tabstrip);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(20);

        mErrorLayout = (EmptyLayout) findViewById(R.id.error_layout);

        mTabsAdapter = new ViewPageFragmentAdapter(getSupportFragmentManager(),
                mTabStrip, mViewPager);
        setScreenPageLimit();
//        onSetupTabAdapter(mTabsAdapter);

        bindOnClickLister(this, mBackImg, mRightTxt);

//        loadHot();
        loadBaike(true);
    }

    protected void setScreenPageLimit() {
    }

    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter,String[] titles) {
//        String[] titles = {"问题","话题","解答","专家"};
        int n = 0;
        for(String title : titles){
            adapter.addTab(title, "t"+n++, BaikeListFragment.class,
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
//            case R.id.btn_search:
//                String txt = mEditSearch.getText().toString().trim();
//                if(!txt.equals(lastSearchTxt)){
//                    loadBaike(true);
//                }
//                break;
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_BAIKE_CATEGORY: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpBaikeCategoryListResponse = gson.fromJson(response, HttpBaikeCategoryListResponse.class);
                    String[] names = new String[mHttpBaikeCategoryListResponse.getResponse().getContent().size()];
                    String[] ids = new String[mHttpBaikeCategoryListResponse.getResponse().getContent().size()];
                    int n = 0;
                    for(HttpBaikeCategoryListResponse.Content content : mHttpBaikeCategoryListResponse.getResponse().getContent()) {
                        names[n] = content.getCategoryName();
                        ids[n] = content.getCategoryId();
                        n++;
                    }
                    onSetupTabAdapter(mTabsAdapter,names);
                    for(int i = 0;i < mTabsAdapter.getCount();i++) {
                        BaikeListFragment fragment = (BaikeListFragment)mTabsAdapter.instantiateItem(mViewPager,i);
                        fragment.setData(BaikeActivity.this,ids[i]);
                    }
                } else {
//                    for(int i = 0;i < mTabsAdapter.getCount();i++) {
//                        QAListFragment fragment = (QAListFragment)mTabsAdapter.instantiateItem(mViewPager,i);
//                        fragment.setData(BaikeActivity.this,null,i);
//                    }
                }
                if(dialog != null){
                    dialog.dismiss();
                }
                break;
            }
//            case CodeUtil.CmdCode.MsgTypeCode.MSG_HOT_KEY: {
//                String response = (String) msg.obj;
//                if (Utils.getRequestStatus(response) == 1) {
//                    Gson gson = new Gson();
//                    mHttpHotKeyResponse  = gson.fromJson(response, HttpHotKeyResponse.class);
//                    for(int i = 0;i < mHttpHotKeyResponse.getResponse().getContent().size();i++) {
//                        final String hotkey = mHttpHotKeyResponse.getResponse().getContent().get(i).getKeyWord();
//                        TextView view = (TextView)getLayoutInflater().inflate(R.layout.hot_item,null);
//                        view.setText(hotkey);
//                        view.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                mEditSearch.setText(hotkey);
//                                loadQA(true);
//                            }
//                        });
//                        mLayoutKeyword.addView(view);
//                    }
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mLayoutKeyword.requestLayout();
//                        }
//                    },10);
//                } else {
//                    mLayoutKeyword.setVisibility(View.GONE);
//                }
//                break;
//            }
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }

//    private void loadHot(){
//        String userId = LoginUserBean.getInstance().getLoginUserid();
//        JSONObject jsonObj = new JSONObject();
//        try {
//            jsonObj.put("userId", userId);
//        } catch (Exception e) {
//        }
//        String jsonString = jsonObj.toString();
//        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_HOT_KEY)
//                .tag(Urls.ACTION_HOT_KEY)
//                .postJson(getRequestBody(jsonString))
//                .execute(new MyJsonCallBack<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        L.d(TAG, "onResponse:" + s);
//                        Message msg = new Message();
//                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_HOT_KEY;
//                        msg.obj = s;
//                        mHandler.sendMessage(msg);
//                    }
//                });
//    }

    private void loadBaike(boolean sp){
//        String txt = mEditSearch.getText().toString().trim();
//        lastSearchTxt = txt;
        if(sp){
            if(dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
//            jsonObj.put("searchKeyWord", txt);
//            jsonObj.put("startRowNum", "");
//            jsonObj.put("endRowNum", "");
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_BAIKE_CATEGORY)
                .tag(Urls.ACTION_BAIKE_CATEGORY)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_BAIKE_CATEGORY;
                        msg.obj = s;
//                        msg.obj = "{\"response\":{\"succeed\":1,\"arrayflag\":1,\"totalRow\":5,\"content\":[{\"categoryId\":\"E11111111111\",\"categoryName\":\"传染病\"},{\"categoryId\":\"E2222222222\",\"categoryName\":\"药理\"}]}}";
                        mHandler.sendMessage(msg);
                    }
                });
    }
}
