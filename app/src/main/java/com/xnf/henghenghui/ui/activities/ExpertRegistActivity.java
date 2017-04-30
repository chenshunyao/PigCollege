package com.xnf.henghenghui.ui.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.fragments.ExpertRegistFragment;

public class ExpertRegistActivity extends BaseActivity implements View.OnClickListener {

    private ExpertRegistFragment mRegistFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_regist_expert);
        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();

        boolean isRegist_expert = getIntent().getBooleanExtra("IS_REGIST_EXPERT",false);
        if(mRegistFragment==null){
            mRegistFragment = new ExpertRegistFragment();
        }
        transaction.replace(R.id.regist_content,mRegistFragment);
        transaction.commit();


    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    public void onBackPressed() {
//        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
//            super.onBackPressed();
//        }else{
//            getSupportFragmentManager().popBackStack();
//        }
        if(mRegistFragment.onBackPressed()){
            super.onBackPressed();
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
            case R.id.invited_expert:
                Toast.makeText(this,"暂未实现，稍后完成",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
