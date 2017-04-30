package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpAnwserListResponse;
import com.xnf.henghenghui.model.HttpBaikeInfoResponse;
import com.xnf.henghenghui.model.HttpQuestionDetailV2Response;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.ChildRecyclerView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BaikeDetailActivity extends BaseActivity implements View.OnClickListener {

    private String mBkid = null;
    private String mBkname = null;

    private ImageView mBackImg;
    private TextView mTitle;
    private TextView mName;
    private WebView mWebView;
//    private ImageView mRightImg;
//    private TextView mRightTxt;

    private ProgressDialog dialog;

    private HttpBaikeInfoResponse mHttpBaikeInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        initDialog();
    }

    private void initDialog(){
        dialog = new ProgressDialog(BaikeDetailActivity.this);
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
        setContentView(R.layout.activity_baike_detail);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.hengheng_baike_detail_title);

        mWebView = (WebView)findViewById(R.id.web_view);
        WebSettings wsettings = mWebView.getSettings();
        wsettings.setJavaScriptEnabled(false);
        wsettings.setJavaScriptCanOpenWindowsAutomatically(false);
        wsettings.setAppCacheEnabled(true);
        wsettings.setBuiltInZoomControls(false);
        wsettings.setDisplayZoomControls(false);
        wsettings.setSupportZoom(false);
        wsettings.setLoadsImagesAutomatically(true);
        wsettings.setDefaultTextEncodingName("utf-8");

        mName = (TextView)findViewById(R.id.txt_name);

        bindOnClickLister(this, mBackImg);

        mBkid = getIntent().getStringExtra("bkid");
        mBkname = getIntent().getStringExtra("bkname");
        mName.setText(mBkname);

        loadBaikeDetail(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadBaikeDetail(boolean sp) {
        if (sp) {
            if (dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("entryId", mBkid);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_BAIKE_INFO)
                .tag(Urls.ACTION_BAIKE_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_BAIKE_INFO;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_BAIKE_INFO: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpBaikeInfoResponse = gson.fromJson(response, HttpBaikeInfoResponse.class);
                    try {

//                        mWebView.loadData(URLEncoder.encode(mHttpBaikeInfoResponse.getResponse().getContent().getEntryContent(), "gb2312"), "text/html", "gb2312");
                        mWebView.loadData(mHttpBaikeInfoResponse.getResponse().getContent().getEntryContent(), "text/html; charset=utf-8", null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
//                        mWebView.loadData(URLEncoder.encode(getString(R.string.hengheng_baike_detail_failed), "gb2312"), "text/html", "gb2312");
                        mWebView.loadData(getString(R.string.hengheng_baike_detail_failed), "text/html; charset=utf-8", null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

}
