package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chatuidemo.DemoHelper;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.model.SimpleBackPage;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.UIHelper;
import com.xnf.henghenghui.ui.utils.Utils;

public class IntegrationActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_integration);
        mBackImg =(ImageView)findViewById(R.id.img_back);
        mBackImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
             case  R.id.img_back:
             {
                 onBackPressed();
             }
            break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

}
