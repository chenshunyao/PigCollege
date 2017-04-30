package com.xnf.henghenghui.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.fragments.RegisterFragment;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private RegisterFragment mRegistFragment;

    private ImageView mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register);
        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();

        boolean isRegist_expert = getIntent().getBooleanExtra("IS_REGIST_EXPERT",false);
        if(mRegistFragment==null){
            mRegistFragment = new RegisterFragment();
            mRegistFragment.setRegistType(isRegist_expert);
        }
        transaction.replace(R.id.regist_content,mRegistFragment);
        transaction.commit();

        mBackBtn= (ImageView)findViewById(R.id.iv_back);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            super.onBackPressed();
        }else{
            getSupportFragmentManager().popBackStack();
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.iv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
