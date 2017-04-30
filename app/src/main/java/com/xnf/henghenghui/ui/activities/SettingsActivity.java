package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Message;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.hyphenate.EMCallBack;
import com.hyphenate.chatuidemo.DemoHelper;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.model.SimpleBackPage;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.UIHelper;
import com.xnf.henghenghui.ui.utils.Utils;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackImg;
    private Button mExitBtn;
    private TextView mFeedBackBtn;
    private TextView mAboutBtn;
    private TextView mNotificationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_settings);
        mBackImg =(ImageView)findViewById(R.id.img_back);
        mExitBtn =(Button)findViewById(R.id.btnexit);

        mFeedBackBtn =(TextView)findViewById(R.id.txt_feedback);
        mAboutBtn = (TextView)findViewById(R.id.txt_about);
        mNotificationBtn = (TextView)findViewById(R.id.txt_notification);
        bindOnClickLister(this,mBackImg,mExitBtn,mFeedBackBtn,mAboutBtn,mNotificationBtn,findViewById(R.id.txt_usersafe),findViewById(R.id.txt_usersafe),findViewById(R.id.txt_yinsi),
                findViewById(R.id.txt_about));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnexit:
                logout();
                break;
            case R.id.txt_feedback:
                UIHelper.showSimpleBack(this, SimpleBackPage.FEED_BACK);
                break;
            case R.id.txt_about:
                UIHelper.showAboutHenghenghui(this);
                break;
            case R.id.txt_notification:
                UIHelper.showSettingNotification(this);
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            default:
                break;
        }
        //onPrepareOptionsMenu(menu);
    }

    private void logout() {
        LoginUserBean.getInstance().setLoginStatus(LoginUserBean.LOGIN_OUT);
        LoginUserBean.getInstance().setLoginUserid(null);
        sendBroadcast(new Intent(Utils.LOGIN_OUT_ACTION));
        HengHengHuiAppliation.getInstance().Logout();
        logoutIM();

//        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
//        final ProgressDialog pd = new ProgressDialog(SettingActivity.this);
//        pd.setMessage("正在退出登陆..");
//        pd.setCanceledOnTouchOutside(false);
//        pd.show();
//        HengHengHuiAppliation.getInstance().logout(new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//                SettingActivity.this.runOnUiThread(new Runnable() {
//                    public void run() {
//                        pd.dismiss();
//                        // 重新显示登陆页面
//                        finish();
//                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
//
//                    }
//                });
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//
//            }
//
//            @Override
//            public void onError(int code, String message) {
//
//            }
//        });
    }

    private void logoutIM() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
       DemoHelper.getInstance().logout(true,new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        finish();
                        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(SettingsActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();


                    }
                });
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
        return false;
    }

}
