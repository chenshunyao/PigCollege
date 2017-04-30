package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.LoginManager;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.HenghenghuiHandler;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.utils.VerifyCodeManager;
import com.xnf.henghenghui.ui.view.CleanEditText;
import com.xnf.henghenghui.ui.view.CustomProgressDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.CommonUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.MD5Calculator;
import com.xnf.henghenghui.util.RegexUtils;
import com.xnf.henghenghui.util.SmsObserver;
import com.xnf.henghenghui.util.TDevice;
import com.xnf.henghenghui.util.ToastUtil;

public class ForgetPasswdActivity extends BaseActivity implements View.OnClickListener {

    // 界面控件
    private EditText phoneEdit;
    private EditText passwordEdit;
    private EditText verifyCodeEdit;
    private Button getVerifiCodeButton;
    private Button modifyPasswdBtn;

    private ImageView mBackBtn;

    private VerifyCodeManager codeManager;

    private CustomProgressDialog progressDialog;

    private SmsObserver mSmsObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSmsObserver != null) {
            getContentResolver().unregisterContentObserver(mSmsObserver);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_forget_passwd2);
        getVerifiCodeButton = getView(R.id.btn_send_verifi_code);
        getVerifiCodeButton.setOnClickListener(this);
        phoneEdit = getView(R.id.et_phone);
        phoneEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        phoneEdit.requestFocus();
        verifyCodeEdit = getView(R.id.et_verifiCode);
        verifyCodeEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        passwordEdit = getView(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // 点击虚拟键盘的done
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    commit();
                }
                return false;
            }
        });
        mBackBtn= (ImageView)findViewById(R.id.iv_back);
        mBackBtn.setOnClickListener(this);
        modifyPasswdBtn =(Button)findViewById(R.id.btn_modify_passwd);
        modifyPasswdBtn.setOnClickListener(this);
        codeManager = new VerifyCodeManager(this, phoneEdit, getVerifiCodeButton);

       //mHandler = new HenghenghuiHandler(this);
        LoginManager.setHandler(mHandler);
        mSmsObserver = new SmsObserver(mHandler, this);
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
            case  CodeUtil.CmdCode.MsgTypeCode.MSG_MODIFY_PASSWD:
            {
                String response = (String)msg.obj;
                L.e(TAG, "Reponse:"+response);
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if(Utils.getRequestStatus(response) == 1){
                    Toast.makeText(this,"修改密码成功",Toast.LENGTH_SHORT).show();
                    this.finish();
                    startActivity(new Intent(this, LoginActivity.class));
                }else{
                    Toast.makeText(this,"修改密码失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case CodeUtil.GET_VCODE_SUCCESS:
                String code = msg.getData().getString("code");
                if (!TextUtils.isEmpty(code)) {
                    verifyCodeEdit.setText(code);
                    verifyCodeEdit.setSelection(code.length());//set
                }
                break;
            default:
                break;
        }
        return false;
    }



    /**
     * 通用findViewById,减少重复的类型转换
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            L.e(TAG, "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }


    private void commit() {
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String code = verifyCodeEdit.getText().toString().trim();

        if (checkInput(phone, password, code)) {
            // TODO:请求服务端修改密码
            showProgressDialog();
            LoginManager.modifyPassWd(phone,
                    TDevice.getIMEI(),phone,code,VerifyCodeManager.MODIFY_PASSWD,CommonUtil.getEncryptPassWd(password));
        }
    }

    private boolean checkInput(String phone, String password, String code) {
        if (TextUtils.isEmpty(phone)) { // 电话号码为空
            ToastUtil.showShort(this, R.string.tip_phone_can_not_be_empty);
        } else {
            if (!RegexUtils.checkMobile(phone)) { // 电话号码格式有误
                ToastUtil.showShort(this, R.string.tip_phone_regex_not_right);
            } else if (TextUtils.isEmpty(code)) { // 验证码不正确
                ToastUtil.showShort(this, R.string.tip_please_input_code);
            } else if (password.length() < 6 || password.length() > 32
                    || TextUtils.isEmpty(password)) { // 密码格式
                ToastUtil.showShort(this,
                        R.string.tip_please_input_6_32_password);
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_verifi_code:
                // TODO 请求接口发送验证码
                getContentResolver().registerContentObserver(Uri.parse("content://sms"), true,
                        mSmsObserver);
                codeManager.getVerifyCode(VerifyCodeManager.MODIFY_PASSWD);

                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_modify_passwd:
                commit();
                break;
            default:
                break;
        }
    }

    private void showProgressDialog() {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(this
                .getResources().getString(R.string.setting_modify_passwd));
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
}
