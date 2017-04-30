package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.LoginManager;
import com.xnf.henghenghui.logic.PersonalnfoManager;
import com.xnf.henghenghui.model.ExpertUserInfo;
import com.xnf.henghenghui.model.ExpertUserInfoResponse;
import com.xnf.henghenghui.model.LoginResponseModel;
import com.xnf.henghenghui.model.NoramlUserInfoResponse;
import com.xnf.henghenghui.model.NormalUserInfo;
import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.CustomProgressDialog;
import com.xnf.henghenghui.ui.view.ShareDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.CommonUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.MD5Calculator;
import com.xnf.henghenghui.util.NetworkUtil;
import com.xnf.henghenghui.util.StringUtils;

import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends BaseActivity {

    private static final String TAG ="LoginActivity";
    private EditText et_usertel;
    private EditText et_password;
    private Button btn_login;
    private TextView btn_register_normal;
    private TextView btn_register_expert;

    private ImageView iv_hide;
    private ImageView iv_show;

    private TextView mForgetPasswd;

    CustomProgressDialog dialog;

    private boolean progressShow;

    private String mUserName;
    private String mPasswd;

    private LinearLayout splashLayout;


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        splashLayout = (LinearLayout) findViewById(R.id.splash_layout);
        initLoginDialog();
        et_usertel = (EditText) findViewById(R.id.et_usertel);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register_normal = (TextView) findViewById(R.id.btn_register_normal);
        btn_register_expert = (TextView) findViewById(R.id.btn_register_expert);
        // 监听多个输入框
//        btn_qq_regist =(ImageView)findViewById(R.id.btn_qq_regist);
//        btn_wechat_regist = (ImageView)findViewById(R.id.btn_wechat_regist);

        et_usertel.addTextChangedListener(new TextChange());
        et_password.addTextChangedListener(new TextChange());

        iv_hide = (ImageView) findViewById(R.id.iv_hide);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        iv_hide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_hide.setVisibility(View.GONE);
                iv_show.setVisibility(View.VISIBLE);
                et_password
                        .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = et_password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }

            }

        });
        iv_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_show.setVisibility(View.GONE);
                iv_hide.setVisibility(View.VISIBLE);
                et_password
                        .setTransformationMethod(PasswordTransformationMethod
                                .getInstance());
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = et_password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }

        });

        mForgetPasswd = (TextView)findViewById(R.id.tv_forget_passwd);
        mForgetPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetPasswdActivity.class);
                startActivity(intent);
            }

        });

        /** init auth api**/
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                mPasswd= CommonUtil.getEncryptPassWd(et_password.getText().toString());
                mUserName = et_usertel.getText().toString().trim();
                mPasswd = mPasswd.substring(0,mPasswd.length()-1);
                login(mUserName,mPasswd);
            }

        });
        btn_register_normal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this,
//                        RegisterActivity.class));

                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("IS_REGIST_EXPERT",false);
                startActivity(intent);
            }

        });

        btn_register_expert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,ExpertRegistActivity.class);
                intent.putExtra("IS_REGIST_EXPERT",true);
                startActivity(intent);
            }

        });
//
//        btn_qq_regist.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                SHARE_MEDIA platform = SHARE_MEDIA.QQ;
//                mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
//            }
//
//        });

//        btn_wechat_regist.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//               // SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
//               // mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
//               // handleShare();
//                SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
//                mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
//            }
//
//        });

        //AutoLogin
        String lastUser = HengHengHuiAppliation.getInstance().getLastLoginUser();
        if(lastUser!= null && !lastUser.isEmpty()){
            mUserName = HengHengHuiAppliation.getInstance().getLastLoginUser();
            mPasswd = HengHengHuiAppliation.getInstance().getLastUserPasswd();
            et_password.setText(mPasswd);
            et_usertel.setText(mUserName);
            splashLayout.setVisibility(View.VISIBLE);
            login(mUserName,mPasswd);
        }else{
            splashLayout.setVisibility(View.GONE);
        }
    }

    private void login(String username,String passwd){
        if (!EaseCommonUtils.isNetWorkConnected(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            splashLayout.setVisibility(View.GONE);
            if(et_password != null){
                et_password.setText("");
            }
            return;
        }
        if(!(splashLayout.getVisibility() == View.VISIBLE)){
            dialog.show();
        }

        if(passwd != null && StringUtils.isEmpty(passwd)){
            Toast.makeText(LoginActivity.this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
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

    // 分享
    public void handleShare() {
        final ShareDialog dialog = new ShareDialog(LoginActivity.this,this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);
        dialog.setShareInfo("Title", "ShareContent", "http://www.baidu.com");
        dialog.show();
    }

    private void initLoginDialog(){
        progressShow = true;
        dialog = new CustomProgressDialog(LoginActivity.this);
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
    protected void onResume() {
        super.onResume();
        LoginManager.setHandler(mHandler);
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
        // 调用sdk登陆方法登陆聊天服务器MSG_LOGIN
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
                //timer.schedule(task, 1000, 50); // 1s后执行task,经过1s再次执行                  //TODO 适当延时处理，避免对话框消失太快
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!LoginActivity.this.isFinishing() && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        // 进入主页面
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        //TODO 登录成功后，直接保存登陆状态，回到桌面后再次进入时若已经登录成功，则直接进入
                        //主页
                        finish();
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
                        if (!LoginActivity.this.isFinishing() && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed)+message,
                                Toast.LENGTH_SHORT).show();
                        if(et_password != null){
                            et_password.setText("");
                        }
                        if(splashLayout!=null && splashLayout.isShown()){
                            splashLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {

        }
    };


    // EditText监听器
    class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {

            boolean Sign2 = et_usertel.getText().length() > 0;
            boolean Sign3 = et_password.getText().length() > 0;

            if (Sign2 & Sign3) {
                btn_login.setTextColor(0xFFFFFFFF);
                btn_login.setEnabled(true);
            }
            // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
            else {
                btn_login.setTextColor(0xFFD0EFC6);
                btn_login.setEnabled(false);
            }
        }

    }

    //Login in chat server and other
    private void login(final JSONObject json) {

        //final String nick = json.getString("nick");
        //final String hxid = json.getString("hxid");
        //final String password = json.getString("password");
        // String fxid = json.getString("fxid");
        // String tel = json.getString("tel");
        // String sex = json.getString("sex");
        // String sign = json.getString("sign");
        // String avatar = json.getString("avatar");
        // String region = json.getString("region");
        // 调用sdk登陆方法登陆聊天服务器
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
                final String response = (String)msg.obj;
                L.e(TAG, "Reponse:"+response);
                if(Utils.getRequestStatus(response) == 1){
                    L.d(TAG, "Reponse Login success");
                    loginIM();

                    Gson gson = new Gson();
                    LoginResponseModel responseModel = gson.fromJson(response,LoginResponseModel.class);
                    LoginUserBean.getInstance().setLoginStatus(LoginUserBean.LOGIN_OK);
                    LoginUserBean.getInstance().setLoginUserid(mUserName);
                    LoginUserBean.getInstance().setLoginUserType(Integer.parseInt(responseModel.getResponse().getContent().getUserType()));
                    HengHengHuiAppliation.getInstance().saveLoginInfo(mUserName,mPasswd);

                    bindPushClient();

                    if(NetworkUtil.isNetworkAvailable(this)){
                        if(LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.EXPERT_USER){
                            PersonalnfoManager.getExpertUserInfo(LoginUserBean.getInstance().getLoginUserid());
                        }else{
                            PersonalnfoManager.getNormalUserInfo(LoginUserBean.getInstance().getLoginUserid());
                        }
                    }

                }else{
                    //TODO 目前为了演示，密码错误时一样进入了主界面
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(splashLayout!=null && splashLayout.isShown()){
                                splashLayout.setVisibility(View.GONE);
                            }
                            if(dialog!=null && dialog.isShowing()){
                                dialog.dismiss();
                            }
                            if (!LoginActivity.this.isFinishing()) {
                                if (Utils.getErrorCode(response).equals("20003")) {
                                    Toast.makeText(LoginActivity.this, "该用户不存在，请核对后再登录", Toast.LENGTH_SHORT).show();
                                } else if (Utils.getErrorCode(response).equals("20004")) {
                                    Toast.makeText(LoginActivity.this, "该用户尚未通过审核，请等候", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "用户名或者密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                                }
                                if (et_password != null) {
                                    et_password.setText("");
                                }
                            }
                        };
                    },1000);
                }

            }
            break;
            case  CodeUtil.CmdCode.MsgTypeCode.MSG_LOGIN_FAILED:
            {
                if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
                Toast.makeText(LoginActivity.this, "登录失败，请重新登录", Toast.LENGTH_SHORT).show();
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_NORAMLUSER_INFO:{
                L.d(TAG,"get Personal Info:"+msg.obj);
                String personalInfo = (String)msg.obj;
                Gson gson = new Gson();
                NoramlUserInfoResponse normalUserInfo = gson.fromJson(personalInfo,NoramlUserInfoResponse.class);

                NoramlUserInfoResponse.Content  content = normalUserInfo.getResponse().getContent();
                if(content!=null){
                    NormalUserInfo info = new NormalUserInfo();
                    info.setUserId(content.getUserId());
                    info.setUserName(content.getUserName());
                    info.setNikeName(content.getNikeName());
                    info.setMobile(content.getMobile());
                    info.setAddress(content.getAddress());
                    info.setEmail(content.getEmail());
                    //info.setCity(content.getCity());
                    info.setCompany(content.getCompany());
                    // info.setSpecies(content.getSpecies());
                    // info.setBusinessType(content.getBusinessType());
                    info.setBreedScope(content.getBreedScope());
                    info.setPhoto(content.getPhoto());
                    info.setFarmAddress(content.getFarmAddress());
                    info.setFarmName(content.getFarmName());
                    info.setDuites(content.getTitle());
                    HengHengHuiAppliation.getInstance().saveNormalUserInfo(info);
                }

            }
            break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_EXPERTUSER_INFO:{
                String personalInfo = (String)msg.obj;
                Gson gson = new Gson();
                ExpertUserInfoResponse expertUserInfoResponse = gson.fromJson(personalInfo,ExpertUserInfoResponse.class);
                ExpertUserInfoResponse.Content  content = expertUserInfoResponse.getResponse().getContent();
                updateExpertInfo(content);
            }
            break;
            default:
                break;
        }
        return false;
    }

    private void updateExpertInfo(ExpertUserInfoResponse.Content content){
        if(content == null){
            return;
        }
        //TODO 将该用户的信息传递至服务器
        ExpertUserInfo expertUserInfo = new ExpertUserInfo();
        expertUserInfo.setUserId(LoginUserBean.getInstance().getLoginUserid());
        if(content.getUserName()!=null){
            expertUserInfo.setUserName(content.getUserName());
        }
        expertUserInfo.setMobile(LoginUserBean.getInstance().getLoginUserid());
        if(content.getEmail()!=null){
            expertUserInfo.setEmail(content.getEmail());
        }

        // normalUserInfo.setCity(tv_city.getText().toString());
        if (content.getAddress() != null) {
            expertUserInfo.setAddress(content.getAddress());
        }
        if (content.getIsRect() != null) {
            expertUserInfo.setIsRect(content.getIsRect());
        }
        if (content.getCertType() != null) {
            expertUserInfo.setCertType(content.getCertType());
        }
        if(content.getCompany()!=null){
            expertUserInfo.setCompany(content.getCompany());
        }
        if(content.getTitles()!=null){
            expertUserInfo.setTitles(content.getTitles());
        }
        if(content.getDesc()!=null){
            expertUserInfo.setDesc(content.getDesc());
        }
        if(content.getProfessional()!=null){
            expertUserInfo.setProfessional(content.getProfessional());
        }
        if(content.getPhoto()!=null){
            expertUserInfo.setPhoto(content.getPhoto());
        }
        HengHengHuiAppliation.getInstance().saveExpertUserInfo(expertUserInfo);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.d("auth", "on activity re 3");
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        //完全退出程序
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
//        if(System.currentTimeMillis() - exitTime > 2000) {
//            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        } else {
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
//        }
        super.onBackPressed();
    }
}
