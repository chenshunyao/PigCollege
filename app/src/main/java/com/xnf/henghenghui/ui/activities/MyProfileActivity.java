package com.xnf.henghenghui.ui.activities;

import android.content.res.Configuration;
import android.os.Message;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.ui.base.BaseActivity;

public class MyProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

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
