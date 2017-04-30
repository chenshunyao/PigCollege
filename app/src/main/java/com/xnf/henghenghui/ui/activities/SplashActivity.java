package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.Constants;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.LoginManager;
import com.xnf.henghenghui.model.LoginResponseModel;
import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.MD5Calculator;
import com.xnf.henghenghui.util.PreferencesWrapper;

public class SplashActivity extends BaseActivity {

    private String mUserName;
    private String mPasswd;

    ProgressDialog dialog;

    private boolean progressShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //进入主页面
        String lastUser = HengHengHuiAppliation.getInstance().getLastLoginUser();
        if(lastUser!= null && !lastUser.isEmpty()){
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }else{
            setContentView(R.layout.activity_splash);
        }
       // initLoginDialog();
       // LoginManager.setHandler(mHandler);
    }

    private void initLoginDialog(){
        progressShow = true;
        dialog = new ProgressDialog(SplashActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        dialog.setMessage(getString(R.string.Is_landing));
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {
                if(LoginUserBean.getInstance().getLoginStatus()== LoginUserBean.LOGIN_OK){
                    //进入主页面
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else if(PreferencesWrapper.getInstance().getPreferenceBooleanValue(Constants.INTRO_SHOW_TAG)){

                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //之前已经登录过，直接使用历史用户登录
//                    String lastUser = HengHengHuiAppliation.getInstance().getLastLoginUser();
//                    if(lastUser!= null && !lastUser.isEmpty()){
//                        mUserName = HengHengHuiAppliation.getInstance().getLastLoginUser();
//                        mPasswd = HengHengHuiAppliation.getInstance().getLastUserPasswd();
//                        login(mUserName,mPasswd);
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                dialog.show();
//                            }
//                        });
//                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                    }
                    finish();
                }else {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        }).start();

    }

    private void login(String username,String passwd){
        if (!EaseCommonUtils.isNetWorkConnected(SplashActivity.this)) {
            Toast.makeText(SplashActivity.this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }

        //// TODO: 2016/1/26
        //登录业务服务器，这登录成功了才会去登录聊天服务器，以及绑定推送服务器
        //LoginManager.Login2(mUserName,mPasswd,"Android","75234563342213566");

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(username);
        userInfo.setPassWord(passwd);
        userInfo.setImei(HengHengHuiAppliation.IMEI);
        userInfo.setPlatform("Android");
        LoginManager.login(userInfo);

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
        switch (msg.what) {
            case  CodeUtil.CmdCode.MsgTypeCode.MSG_LOGIN:
            {
                String response = (String)msg.obj;
                L.e(TAG, "Reponse:"+response);
                if(Utils.getRequestStatus(response) == 1){
                    L.d(TAG, "Reponse Login success");
                    loginIM();

                    Gson gson = new Gson();
                    LoginResponseModel responseModel = gson.fromJson(response,LoginResponseModel.class);
                    LoginUserBean.getInstance().setLoginStatus(LoginUserBean.LOGIN_OK);
                    LoginUserBean.getInstance().setLoginUserid(mUserName);
                    LoginUserBean.getInstance().setLoginUserType(LoginUserBean.NORMAL_USER);
                    HengHengHuiAppliation.getInstance().saveLoginInfo(mUserName,mPasswd);

                    bindPushClient();
                }else{
                    //TODO 目前为了演示，密码错误时一样进入了主界面
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!SplashActivity.this.isFinishing() && dialog.isShowing()) {
                                dialog.dismiss();
                                Toast.makeText(SplashActivity.this,"用户名或者密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                            }
                        };
                    },2000);
//                    loginIM();
//                    LoginUserBean.getInstance().setLoginStatus(LoginUserBean.LOGIN_OK);
//                    LoginUserBean.getInstance().setLoginUserid(mUserName);
//                    LoginUserBean.getInstance().setLoginUserType(LoginUserBean.NORMAL_USER);
//                    L.d(TAG, "Reponse Login failed");
//                    HengHengHuiAppliation.getInstance().saveLoginInfo(mUserName,mPasswd);
                    //Gson gson = new Gson();
                    //ErrorInfo error =gson.fromJson(response,ErrorInfo.class);

                }

            }
            break;
            default:
                break;
        }
        return false;
    }

    public void loginIM() {

        //TODO 注册时使用的手机号的MD5作为IM账户
        final String currentUsername = MD5Calculator.calculateMD5(mUserName);
        final String currentPassword = MD5Calculator.calculateMD5(currentUsername);

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                if (!progressShow) {
                    return;
                }
                // 登陆成功，保存用户名
                DemoHelper.getInstance().setCurrentUserName(currentUsername);
                // 注册群组和联系人监听
                DemoHelper.getInstance().registerGroupAndContactListener();

                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                // ** manually load all local groups and
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        HengHengHuiAppliation.currentUserNick.trim());
                if (!updatenick) {
                    L.e(TAG, "update current user nick fail");
                }
                //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                //TODO 适当延时处理，避免对话框消失太快
                // 进入主页面
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!SplashActivity.this.isFinishing() && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        finish();
                        //TODO 登录成功后，直接保存登陆状态，回到桌面后再次进入时若已经登录成功，则直接进入
                        //主页
                    }
                },2000);
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        dialog .dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message
                                        +"\n已加入环聊相关功能，请用100~107作为用户名登录，密码111111",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
