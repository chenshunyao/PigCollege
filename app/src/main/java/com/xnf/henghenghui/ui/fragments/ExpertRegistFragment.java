
package com.xnf.henghenghui.ui.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.dao.SmsDao;
import com.xnf.henghenghui.logic.HhhErrorCode;
import com.xnf.henghenghui.logic.LoginManager;
import com.xnf.henghenghui.model.ErrorInfo;
import com.xnf.henghenghui.model.SmsInfo;
import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.ui.activities.ExpertUserInfoActivity;
import com.xnf.henghenghui.ui.activities.LoginActivity;
import com.xnf.henghenghui.ui.activities.NormalUserInfoActivity;
import com.xnf.henghenghui.ui.base.BaseFragment;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.utils.VerifyCodeManager;
import com.xnf.henghenghui.ui.view.CustomAlertDialog;
import com.xnf.henghenghui.ui.view.CustomProgressDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.CommonUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.MD5Calculator;
import com.xnf.henghenghui.util.SmsObserver;
import com.xnf.henghenghui.util.TDevice;
import com.xnf.henghenghui.util.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpertRegistFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "RegisterFragment";

    private LinearLayout[] mRegisterPage = new LinearLayout[4];

    private static final int EXPERT_REGIST_INPUT_PHONENUM = 0;
    private static final int EXPERT_REGIST_INPUT_VERIFYCODE = 1;
    private static final int EXPERT_REGIST_INPUT_INVITEDCODE = 2;
    private static final int EXPERT_REGIST_INPUT_PASSWORD = 3;

    private Button mPhoneNumPageNextStepBtn;
    private Button mRegisterBtn;
    private TextView mReGetCodeBtn;

    private Button mGotoLoginPageBtn;
    private Button mImplementPersonalInfoBtn;

    private Button mInvitedExpertEntrance;
    private Button mSubmitVerifyCodeBtn;
    private Button mSubmitInvitedCodeBtn;

    private EditText mPhoneNumEditText;
    private EditText mVerifyCodeEditText;
    private EditText mInvitedCodeEditText;
    private EditText mPasswdEditText;

    private TextView mVerifyCodeText;
    private TextView mInvitedCodeText;

    private boolean isInvited= false;

    private ImageView mBackBtn;

    private TextView mInvitedExpert;

    //设置默认的重发时间
    private static final int DEFAULT_VALID_TIME = 30;
    private int regetRequestTime = DEFAULT_VALID_TIME;

    private static final int GET_CODE = 0x00;
    private static final int INPUT_CODE = 0x01;
    private static final int INVITE_CODE = 0x02;



    protected static final int VCODE_IS_ERROR = 468;

    private long date = 0;

    private SmsObserver mSmsObserver;

    private String mPhoneNum = "";
    private String mPasswd = "";

    private ProgressDialog mProgressDialog;
    private String imUsername;

    private ImageView iv_hide;
    private ImageView iv_show;

    private CustomProgressDialog progressDialog;

    public static ExpertRegistFragment getInstance(Bundle bundle) {
        ExpertRegistFragment settingFragment = new ExpertRegistFragment();
        settingFragment.setArguments(bundle);
        return settingFragment;
    }

    @Override
    protected void setUIHandler() {
        super.setUIHandler();

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
    protected String setFragmentTag() {
        return TAG;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        if (mSmsObserver != null) {
            getActivity().getContentResolver().unregisterContentObserver(mSmsObserver);
        }
        super.onDetach();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container,
                            Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_expert_regist,container,
                false);
        mRegisterPage[0] = (LinearLayout) view.findViewById(R.id.expert_input_phonenum_layout);
        mRegisterPage[1] = (LinearLayout) view.findViewById(R.id.expert_input_verifycode_layout);
        mRegisterPage[2] = (LinearLayout) view.findViewById(R.id.expert_input_invitedcode_layout);
        mRegisterPage[3] = (LinearLayout) view.findViewById(R.id.expert_input_passwd_layout);

        mPhoneNumPageNextStepBtn = (Button) view.findViewById(R.id.btn_input_phonenum_nextstep);
        mPhoneNumPageNextStepBtn.setOnClickListener(this);

        mRegisterBtn = (Button) view.findViewById(R.id.btn_register);
        mRegisterBtn.setOnClickListener(this);

        mSubmitVerifyCodeBtn = (Button) view.findViewById(R.id.btn_summit_verifycode);
        mSubmitVerifyCodeBtn.setOnClickListener(this);

        mSubmitInvitedCodeBtn = (Button) view.findViewById(R.id.btn_submit_invitedcode);
        mSubmitInvitedCodeBtn.setOnClickListener(this);

        mReGetCodeBtn = (TextView) view.findViewById(R.id.btn_reget_code);
        mReGetCodeBtn.setOnClickListener(this);

        mInvitedExpertEntrance =(Button)view.findViewById(R.id.btn_imvited_entrance);
        mInvitedExpertEntrance.setOnClickListener(this);

        mPhoneNumEditText = (EditText) view.findViewById(R.id.et_phone);
        mVerifyCodeEditText = (EditText) view.findViewById(R.id.et_verifiCode);
        mInvitedCodeEditText = (EditText) view.findViewById(R.id.et_invitedcode);

        mPhoneNumEditText.addTextChangedListener(new TextWatcherListener(GET_CODE));
        mVerifyCodeEditText.addTextChangedListener(new TextWatcherListener(INPUT_CODE));
        mInvitedCodeEditText.addTextChangedListener(new TextWatcherListener(INPUT_CODE));

        mPasswdEditText = (EditText) view.findViewById(R.id.et_password);

        mSmsObserver = new SmsObserver(mHandler, getActivity());
        LoginManager.setHandler(mHandler);

        mBackBtn= (ImageView)view.findViewById(R.id.iv_back);
        mBackBtn.setOnClickListener(this);

        mInvitedExpert = (TextView)view.findViewById(R.id.invited_expert);
        mInvitedExpert.setOnClickListener(this);

        mVerifyCodeText =(TextView)view.findViewById(R.id.text_verify_code_hint);
        mInvitedCodeText = (TextView)view.findViewById(R.id.invited_expert_label);

        iv_hide = (ImageView) view.findViewById(R.id.iv_hide);
        iv_show = (ImageView) view.findViewById(R.id.iv_show);
        iv_hide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_hide.setVisibility(View.GONE);
                iv_show.setVisibility(View.VISIBLE);
                mPasswdEditText
                        .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = mPasswdEditText.getText();
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
                mPasswdEditText
                        .setTransformationMethod(PasswordTransformationMethod
                                .getInstance());
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = mPasswdEditText.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }

        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_input_phonenum_nextstep:
                if(mPhoneNumEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),getResources().getString(R.string.str_phonenum_is_null),Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取支持的国家列表
                // SMSSDK.getSupportedCountries();
                Log.d(TAG, "mPhoneNumEditText.getText().toString():" + mPhoneNumEditText.getText().toString());

                //SMSSDK.submitVerificationCode(String country, String phone, String code)
                mVerifyCodeEditText.setText("");
                mPhoneNum = mPhoneNumEditText.getText().toString();
                getCode(mPhoneNum);
                setRegisterViewVisibility(EXPERT_REGIST_INPUT_VERIFYCODE);
                mInvitedExpert.setVisibility(View.INVISIBLE);
                mVerifyCodeText.setText(getString(R.string.verify_code_hint)+mPhoneNum);
                break;
            case R.id.btn_register:
                mPasswd = CommonUtil.getEncryptPassWd(mPasswdEditText.getText().toString());
                if (mPasswd.isEmpty()) {
                    return;
                }
                mPasswd = mPasswd.substring(0,mPasswd.length()-1);
                registerIM(mPhoneNum, mPasswd);
                break;
            case R.id.btn_goto_login:
                // 重新显示登陆页面
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.btn_implement_persionalinfo:
                if (isInvited) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ExpertUserInfoActivity.class);
                    intent.putExtra(Utils.REGISTER_IMPLETE_INFO,true);
                    intent.putExtra(Utils.REGISTER_IMPLETE_USER_ID,mPhoneNum);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ExpertUserInfoActivity.class);
                    intent.putExtra(Utils.REGISTER_IMPLETE_INFO,true);
                    intent.putExtra(Utils.REGISTER_IMPLETE_USER_ID,mPhoneNum);
                    startActivity(intent);
                }
                break;
            case R.id.btn_reget_code:
            {
                getCode(mPhoneNum);
            }
                break;
            case R.id.btn_imvited_entrance:
            {
                isInvited =true;
                Toast.makeText(getActivity(),"邀请功能正在实现中，请稍等",Toast.LENGTH_SHORT).show();

//                if(mPhoneNumEditText.getText().toString().isEmpty()){
//                    Toast.makeText(getActivity(),getResources().getString(R.string.str_phonenum_is_null),Toast.LENGTH_SHORT).show();
//                    return;
//                }
                setRegisterViewVisibility(EXPERT_REGIST_INPUT_INVITEDCODE);
            }
            break;
            case R.id.btn_submit_invitedcode:
            {
                 if(mInvitedCodeEditText.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(),getResources().getString(R.string.str_invite_code_is_null),Toast.LENGTH_SHORT).show();
                 }
                setRegisterViewVisibility(EXPERT_REGIST_INPUT_PASSWORD);
                mPasswdEditText.requestFocus();
            }
            break;
            case R.id.btn_summit_verifycode:
            {
                if(mVerifyCodeEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),getResources().getString(R.string.str_code_is_null),Toast.LENGTH_SHORT).show();
                    return;
                }

                //SMSSDK.submitVerificationCode("86", mPhoneNumEditText.getText().toString(),mCodeEditText.getText().toString());
                //提交验证码
                LoginManager.checkVerifyCode(mPhoneNum,VerifyCodeManager.REGISTER,
                        mVerifyCodeEditText.getText().toString(),TDevice.getIMEI());
                //TODO 这里需要提交验证码，验证成功才能走下一步，否则需要继续
                showProgressDialog();
            }
            break;
            case  R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.invited_expert:
            {
                if(mPhoneNumEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),getResources().getString(R.string.str_phonenum_is_null),Toast.LENGTH_SHORT).show();
                    return;
                }
                isInvited =true;
                mInvitedExpert.setVisibility(View.INVISIBLE);

                //这个地方需要对该用户是否被邀请进行校验
                //获取支持的国家列表
                // SMSSDK.getSupportedCountries();
                Log.d(TAG, "mPhoneNumEditText.getText().toString():" + mPhoneNumEditText.getText().toString());

                //SMSSDK.submitVerificationCode(String country, String phone, String code)
                mVerifyCodeEditText.setText("");
                mPhoneNum = mPhoneNumEditText.getText().toString();
                setRegisterViewVisibility(EXPERT_REGIST_INPUT_INVITEDCODE);
                mInvitedCodeText.setText(mPhoneNum+getString(R.string.invite_code_hint));
                mInvitedCodeEditText.requestFocus();
            }
                break;
            default:
                break;
        }
    }

    private void setRegisterViewVisibility(int index) {
        for (int i = 0; i < 4; i++) {
            if (i == index) {
                mRegisterPage[i].setVisibility(View.VISIBLE);
            } else {
                mRegisterPage[i].setVisibility(View.GONE);
            }
        }
    }

    class TextWatcherListener implements TextWatcher {
        int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public TextWatcherListener(int type) {
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (type == GET_CODE) {
                if (s.length() == 11) {
                    // 设置获取验证码可以用
                    // mInputNumberTip.setVisibility(View.GONE);
                    mPhoneNumEditText.setBackgroundResource(R.color.white);
                }
//                else {
//                    ToastUtil.showToast(ManuallyLoginAcitivty.this,
//                            getString(R.string.input_is_wrong));
//                }
            } else if (type == INPUT_CODE) {
                if (s.length() == 6) {
                    //mInputPswTip.setVisibility(View.GONE);
                    mVerifyCodeEditText.setBackgroundResource(R.color.white);
                }
//                if (s.length() == 6) {
//                    L.i(TAG, "正在登录");
//                    mInputPswTip.setVisibility(View.GONE);
//                    String number = loginIsRunning();
//                    if (!TextUtils.isEmpty(number) && number.equals(phoneNumber)) {
//                        login(number, s.toString());// 登录
//                    } else {
//                        loginFailed();
//                        ToastUtil.showToast(ManuallyLoginAcitivty.this,
//                                getString(R.string.phone_number_is_not_same));
//                    }
//                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

//    /******************************************
//     * 手机号验证流程
//     *************************************/
//    class SmsObserver extends ContentObserver {
//        private Context context;
//
//        public SmsObserver(Handler handler, Context context) {
//            super(handler);
//            this.context = context;
//        }
//
//        @Override
//        public void onChange(boolean selfChange) {
//            L.d(TAG, "onChange");
//            super.onChange(selfChange);
//            SmsDao smsDao = new SmsDao(context, null);
//            SmsInfo info = smsDao.getSmsInfo(date);
//            if (info == null) {
//                return;
//            }
//            L.d(TAG, "message = " + info.getSmsbody() + "number = " + info.getPhoneNumber());
//            if ("1069003200004".equals(info.getPhoneNumber())) {
//                String code = getValidCode(info.getSmsbody());
//                L.d(TAG, "valid code = " + code);
//                //TODO ：这里需要对短信进行解析
//                Message msg = new Message();
//                msg.what = CodeUtil.GET_VCODE_SUCCESS;
//                msg.getData().putString("code", code);
//                mHandler.sendMessage(msg);
//            }
//
//        }
//    }

    private String getValidCode(String code) {
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(code);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }

    /**
     *
     * @param number
     */
    private void getCode(String number) {
        if (Utils.hasNoNetwork(getActivity())) {
            showSettingInternet();
            return;
        }
//        if (TextUtils.isEmpty(number)) {
//            ToastUtil.showToast(this, getString(R.string.please_input_number));
//            return;
//        }

        if (TextUtils.isEmpty(number) || number.length() < 11) {
            //mInputNumberTip.setText("请输入11位手机号码");
            //mInputNumberTip.setVisibility(View.VISIBLE);
            mPhoneNumEditText.setBackgroundResource(R.drawable.edittext_normal);
            return;
        }

        if (!Utils.isPhoneNum(number)) {
            new CustomAlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.info))
                    .setMessage(R.string.invalid_number)
                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            ToastUtil.showLongToast(getActivity(), "验证码已下发至您的手机，请注意查收");
            //AnalyticsUtil.getInstance(ManuallyLoginAcitivty.this).RegisteEvent(
            //         AnalyticsUtil.STATIC_INPUT_PHONE_NUMBER_23);
            //SMSSDK.getVerificationCode("86", number);
            //调用服务器的接口获取验证码
            LoginManager.getVerifyCode(number, VerifyCodeManager.REGISTER, TDevice.getIMEI());
            date = System.currentTimeMillis();
            // requestCode(number);
            getTime();
        }
    }

    private void getTime() {
        final Timer mTimer = new Timer();
        TimerTask mTask = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = CodeUtil.RE_GET_TIME_USFUL_CODE;
                message.obj = mTimer;
                mHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTask, 10, 1000);
    }

    private void showSettingInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String ok = getString(R.string.internet_setting);
        String cancle = getString(R.string.cancel);
        builder.setTitle(R.string.info)
                .setMessage(R.string.network_error_em)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                        startActivity(wifiSettingsIntent);
                    }
                })
                .setNegativeButton(cancle, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(this.isDetached()){
            return false;
        }
        switch (msg.what) {
            case CodeUtil.GET_VCODE_SUCCESS:
                String code = msg.getData().getString("code");
                if (!TextUtils.isEmpty(code)) {
                    mVerifyCodeEditText.setText(code);
                    mVerifyCodeEditText.setSelection(code.length());//set
                }
            case CodeUtil.RE_GET_TIME_USFUL_CODE:
                Timer timer = (Timer) msg.obj;
                if (timer == null) {
                    return false;
                }
                if (regetRequestTime >= 0) {
                    mReGetCodeBtn.setClickable(false);
                   // String reGetCode = getString(R.string.re_get_code);
                    mReGetCodeBtn.setText("重新发送" + "(" + regetRequestTime + ")");
                }
                regetRequestTime--;
                if (regetRequestTime == -1) {
                    regetRequestTime = DEFAULT_VALID_TIME;
                    mReGetCodeBtn.setClickable(true);
                    mReGetCodeBtn.setText(R.string.re_get_code);
                    timer.cancel();
                }
                break;
            case CodeUtil.CHECK_VCODE_SUCCESS:
                setRegisterViewVisibility(EXPERT_REGIST_INPUT_PASSWORD);
                break;
            case CodeUtil.CHECK_VCODE_FAILED:
                Toast.makeText(activity, "验证码错误，请确认", Toast.LENGTH_SHORT).show();
                break;
            case CodeUtil.CmdCode.MsgTypeCode.UI_MSG: {
                if (!getActivity().isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                // 保存用户名
                DemoHelper.getInstance().setCurrentUserName(imUsername);
                Toast.makeText(getActivity(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                //setRegisterViewVisibility(EX);
                //getActivity().finish();
            }
            break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_REGISTER: {
                if (!getActivity().isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                String ret = (String)msg.obj;
                L.d(TAG,"ret="+ret);
                if(ret.contains(HhhErrorCode.FAILED_RET)){
                    //Gson gson = new Gson();
                  // ErrorInfo error=gson.fromJson(ret, ErrorInfo.class);
                   // ret="{\"response\":{\"succeed\":0,\"errorCode\":\"20002\",\"errorInfo\":\"用户名已存在，不能重复注册\"}}";
                    ErrorInfo error= JSON.parseObject(ret, ErrorInfo.class);

                    //TODO 之后这个地方需要处理
                  // Toast.makeText(getActivity(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                  //  setRegisterViewVisibility(REGISTER_SUCCESS_LAUOUT);
                    if(Integer.parseInt(error.getResponse().getErrorCode()) == HhhErrorCode.ERROR_CODE_20002){
                        Toast.makeText(getActivity(),"用户名已存在，不能重复注册",Toast.LENGTH_SHORT).show();
                    }else if(Integer.parseInt(error.getResponse().getErrorCode()) == HhhErrorCode.ERROR_CODE_20005){
                        Toast.makeText(getActivity(),"邀请码错误或该用户未被邀请",Toast.LENGTH_SHORT).show();
                    }
                    //隐藏软键盘
                   InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                   imm.hideSoftInputFromWindow(mPasswdEditText.getWindowToken(),0);

                }else if(ret.contains(HhhErrorCode.SUCCESS_REQUEST)){
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                imUsername = MD5Calculator.calculateMD5(mPhoneNum).toLowerCase();
                                String imPasswd = MD5Calculator.calculateMD5(imUsername);

                                // 调用sdk注册方法
                                EMClient.getInstance().createAccount(imUsername, imPasswd);
//                                EMChatManager.getInstance().createAccountOnServer(imUsername, imPasswd);
                                //
                                // 保存用户名
                                DemoHelper.getInstance().setCurrentUserName(imUsername);

                                //// TODO: 2016/6/23
                                //setRegisterViewVisibility(REGISTER_SUCCESS_LAUOUT);
                                //直接跳转到完善资料页面
                                //TODO:暂时先跳转页面
                                if (!isInvited) {
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), ExpertUserInfoActivity.class);
                                    intent.putExtra(Utils.REGISTER_IMPLETE_INFO,true);
                                    intent.putExtra(Utils.REGISTER_IMPLETE_USER_ID,mPhoneNum);
                                    startActivity(intent);
                                } else {
                                    // 重新显示登陆页面
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    getActivity().finish();
                                }

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (final HyphenateException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (!getActivity().isFinishing())
                                            mProgressDialog.dismiss();
                                        int errorCode = e.getErrorCode();
                                        if (errorCode == EMError.NETWORK_ERROR) {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                        } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                        } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                        } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }).start();
                }

            }
            break;
            case  CodeUtil.CmdCode.MsgTypeCode.MSG_GET_VERIFY_CODE:
            {
                //获取验证码成功
                Log.d(TAG, "EVENT_GET_VERIFICATION_CODE");
                getActivity().getContentResolver().registerContentObserver(Uri.parse("content://sms"), true,
                        mSmsObserver);
            }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CHECK_CODE:
            {
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                String response = (String)msg.obj;
                //获取验证码成功
                if(Utils.getRequestStatus(response) == 1){
                    setRegisterViewVisibility(EXPERT_REGIST_INPUT_PASSWORD);
                    Log.d(TAG, "EVENT_CHECK_VERIFICATION_CODE");
                }else{
                    ToastUtil.showToast(getActivity(),"验证码错误，请确认");
                    //setRegisterViewVisibility(REGISTER_INPUT_PASSWD_PAGE);
                    Log.d(TAG, "EVENT_CHECK_VERIFICATION_CODE Failed");
                }
            }
            break;
            default:
                break;
        }
        return false;
    }

    /**********************************************
     * 注册流程
     *************************************/
    public void registerIM(final String username, final String pwd) {
        // (1)首先要注册环信账号，然后将环信的账号和自己的账号发送给业务服务器
        // 环信的账号就使用自己账号的MD5作为密码也可以，不提供修改环信账号的服务。
        //对于用户环信账号是虚拟的，不存在的。
        //（或者环信的账户为用户账号的MD5，密码为账户MD5的MD5，这样对于环信来说，用户的
        //手机号或其他信息也得到了保护）
        //(2) 注册成功的条件环信注册成功，然后业务服务器注册成功
        //(3) 注册过程中需要的手机号码校验直接与mob SDK交互，不需要与自身业务服务器交互
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.Is_the_registered));
        mProgressDialog.show();

        //TODO 先注册业务服务器,注册之后再注册环聊服务器
        //之后注册环聊服务器的工作就交给服务器来处理，客户端不处理这部分
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(username);
        userInfo.setPassWord(pwd);
        userInfo.setImei(HengHengHuiAppliation.IMEI);
        userInfo.setUserType("2");
        userInfo.setRegType("mobile");
        if(isInvited){
            LoginManager.inviteRegister(userInfo,mInvitedCodeEditText.getText().toString());
        }else{
            LoginManager.register(userInfo);
        }


    }

    private void showProgressDialog() {
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(this
                .getResources().getString(R.string.str_check_code));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                // finish();
               // cancelUpdate = true;
            }
        });
        progressDialog.show();
    }

    public boolean onBackPressed(){
        if(mRegisterPage[0].isShown()){
            mInvitedExpert.setVisibility(View.VISIBLE);
            return true;
        }else if(mRegisterPage[1].isShown()){
            setRegisterViewVisibility(EXPERT_REGIST_INPUT_PHONENUM);
            mInvitedExpert.setVisibility(View.VISIBLE);
            return false;
        }else if(mRegisterPage[2].isShown()){
            mInvitedExpert.setVisibility(View.VISIBLE);
            setRegisterViewVisibility(EXPERT_REGIST_INPUT_PHONENUM);
            return false;
        }else if(mRegisterPage[3].isShown()){
            mInvitedExpert.setVisibility(View.VISIBLE);
            setRegisterViewVisibility(EXPERT_REGIST_INPUT_PHONENUM);
            return false;
        }
        return true;
    }
}
