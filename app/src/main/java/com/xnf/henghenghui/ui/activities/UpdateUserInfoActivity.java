package com.xnf.henghenghui.ui.activities;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.ui.utils.LocalUserInfo;
import com.xnf.henghenghui.ui.utils.Utils;

public class UpdateUserInfoActivity extends Activity implements OnClickListener {

    private static String action;
    private TextView mUserInfoActivityTitle;
    private EditText mUserInfoEditText;
    private Button mSaveBtn;
    private ImageView mBack;
    private int mLimitNum=483;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_userinfo);

        //
        Intent intent = getIntent();
        action = intent.getAction();

        initView();
        mSaveBtn.setOnClickListener(this);
    }

    private void initView() {
        //final String nick= LocalUserInfo.getInstance(UpdateUserInfoActivity.this).getUserInfo("nick");
        mUserInfoEditText = (EditText) this.findViewById(R.id.et_nick);
        mUserInfoActivityTitle = (TextView) findViewById(R.id.tv_title);
        // userInfo.setText(nick);
        mSaveBtn = (Button) this.findViewById(R.id.tv_save);
        mBack = (ImageView) this.findViewById(R.id.iv_back);
        mBack.setOnClickListener(this);
        switch (Integer.parseInt(action)) {
            case Utils.UPDATE_USERNAME: {
                mUserInfoActivityTitle.setText(R.string.update_username_title);
                String ret = getIntent().getStringExtra("truth_name");
                mUserInfoEditText.setText(ret);
                mLimitNum = 32;
            }
            break;
            case Utils.UPDATE_USERNICK: {
                mUserInfoActivityTitle.setText(R.string.update_usernick_title);
                String ret = getIntent().getStringExtra("nick_name");
                mUserInfoEditText.setText(ret);
                mLimitNum = 32;
            }
            break;
            case Utils.UPDATE_USER_ADDRESS: {
                mUserInfoActivityTitle.setText(R.string.update_useraddress_title);
                String ret = getIntent().getStringExtra("address");
                mUserInfoEditText.setText(ret);
                mLimitNum = 32;
            }
            break;
            case Utils.UPDATE_WORK_UNIT: {
                String ret = getIntent().getStringExtra("user_work_unit");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText(R.string.update_userworkunit_title);
                mLimitNum = 64;
            }
            break;
            case Utils.UPDATE_JOB_TITLE: {
                mUserInfoActivityTitle.setText(R.string.update_job_title);
                String ret = getIntent().getStringExtra("duties");
                mUserInfoEditText.setText(ret);
                mLimitNum = 64;
            }
            break;
            case Utils.UPDATE_EMAIL:{
                String ret = getIntent().getStringExtra("user_email");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText("修改邮箱");
                mLimitNum = 64;
            }
            break;
            case Utils.UPDATE_RAISING_SCALE:
            {
                String ret = getIntent().getStringExtra("user_raising_scale");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText("修改养殖规模");
                mLimitNum = 64;
            }
            break;
            case Utils.UPDATE_BREED:
            {
                String ret = getIntent().getStringExtra("breed");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText("修改养殖物种");
                mLimitNum = 64;
            }
            break;
            case Utils.UPDATE_ENTERPRISE_TYPE:
            {
                String ret = getIntent().getStringExtra("enterprise_type");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText("修改企业类型");
                mLimitNum = 64;
            }
            break;
            case Utils.UPDATE_DUTIES:
            {   String ret = getIntent().getStringExtra("duties");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText("修改职务");
                mLimitNum = 64;
            }
            break;
            case Utils.UPDATE_FARM_NAME:
            {   String ret = getIntent().getStringExtra("farmName");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText("修改猪厂名称");

            }
            break;
            case Utils.UPDATE_FARM_ADDRESS:
            {   String ret = getIntent().getStringExtra("farmAddress");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText("修改猪厂地址");
                mLimitNum = 64;
            }
            break;
            case Utils.UPDATE_SKILLED_IN:
            {   String ret = getIntent().getStringExtra("skilledin");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText("修改擅长领域");
                mLimitNum = 256;
            }
            break;
            case Utils.UPDATE_BRIRF_INTRODUCTION:
            {   String ret = getIntent().getStringExtra("brief_introduction");
                mUserInfoEditText.setText(ret);
                mUserInfoActivityTitle.setText("修改专家简介");
            }
            break;
            default:
                break;
        }

        mUserInfoEditText.setHint("最多输入"+mLimitNum+"字");
        mUserInfoEditText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private boolean isEdit = true;
            private int selectionStart ;
            private int selectionEnd ;
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                Editable editable=mUserInfoEditText.getText();
                int len=editable.length();
                if(len>mLimitNum){
                    int selEndIndex= Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串  
                    String newStr=str.substring(0,mLimitNum);
                    mUserInfoEditText.setText(newStr);
                    editable=mUserInfoEditText.getText();
                    //新字符串的长度
                    int newLen =editable.length();
                    //旧光标位置超过字符串长度
                    if(selEndIndex>mLimitNum) {
                        selEndIndex = editable.length();
                    }//设置新光标所在的位置
                    Selection.setSelection(editable,selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                selectionStart = mUserInfoEditText.getSelectionStart();
//                selectionEnd = mUserInfoEditText.getSelectionEnd();
                if (temp.length() > mLimitNum) {
                    Toast.makeText(UpdateUserInfoActivity.this,
                            "您输入的的已经超过"+mLimitNum+"个字", Toast.LENGTH_SHORT)
                            .show();
//                    //s.delete(selectionStart-1, selectionEnd);
//                    s.delete(mLimitNum, mUserInfoEditText.length());
//                    int tempSelection = selectionStart;
//                    mUserInfoEditText.setText(s);
//                    mUserInfoEditText.setSelection(tempSelection);
                }
            }
        });

        mUserInfoEditText.setSelection(mUserInfoEditText.getText().toString().length());//set cursor to the end

    }

    private void updateIvnServer(final String newNick) {
//        Map<String, String> map = new HashMap<String, String>();
//         String  hxid=LocalUserInfo.getInstance(UpdateUserInfoActivity.this).getUserInfo("hxid");
//        map.put("newNick", newNick);
//        map.put("hxid", hxid);
//        final ProgressDialog dialog = new ProgressDialog(UpdateUserInfoActivity.this);
//        dialog.setMessage("正在更新...");
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //dialog.show();
//        LoadDataFromServer registerTask = new LoadDataFromServer(
//                UpdateUserInfoActivity.this, Constant.URL_UPDATE_Nick, map);
//
//        registerTask.getData(new DataCallBack() {
//
//            @SuppressLint("ShowToast")
//            @Override
//            public void onDataCallBack(JSONObject data) {
//                dialog.dismiss();
//                try {
//                    int code = data.getInteger("code");
//                    if (code == 1) {
//                        LocalUserInfo.getInstance(UpdateUserInfoActivity.this).setUserInfo("nick", newNick);
//                       finish();
//
//                    }
//                      else {
//
//                        Toast.makeText(UpdateUserInfoActivity.this,
//                                "更新失败...", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//
//                } catch (JSONException e) {
//
//                    Toast.makeText(UpdateUserInfoActivity.this, "数据解析错误...",
//                            Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//
//            }
//
//        });
    }

    public void saveUserInfo() {
        String newUserInfo = mUserInfoEditText.getText().toString().trim();
//                if (/*nick.equals(newUserInfo)||*/newUserInfo.equals("") || newUserInfo.equals("0")) {
//                    return;
//                }
        switch (Integer.parseInt(action)) {
            case Utils.UPDATE_USERNAME: {
                Intent intent = new Intent();
                intent.putExtra("user_name", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_USERNICK: {
                Intent intent = new Intent();
                intent.putExtra("user_nick", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_USER_ADDRESS: {
                Intent intent = new Intent();
                intent.putExtra("user_address", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_WORK_UNIT: {
                Intent intent = new Intent();
                intent.putExtra("user_work_unit", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_JOB_TITLE:{
                Intent intent = new Intent();
                intent.putExtra("user_jobtitle", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_EMAIL:{
                Intent intent = new Intent();
                intent.putExtra("user_email", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_RAISING_SCALE:
            {
                Intent intent = new Intent();
                intent.putExtra("user_raising_scale", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_BREED:
            {
                Intent intent = new Intent();
                intent.putExtra("user_breed", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_ENTERPRISE_TYPE:
            {
                Intent intent = new Intent();
                intent.putExtra("user_enterprise_type", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_DUTIES:{
                Intent intent = new Intent();
                intent.putExtra("duties", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_FARM_NAME:{
                Intent intent = new Intent();
                intent.putExtra("farmName", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_FARM_ADDRESS:{
                Intent intent = new Intent();
                intent.putExtra("farmAddress", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_SKILLED_IN:{
                Intent intent = new Intent();
                intent.putExtra("skilledin", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            case Utils.UPDATE_BRIRF_INTRODUCTION:{
                Intent intent = new Intent();
                intent.putExtra("brief_introduction", newUserInfo);
                setResult(RESULT_OK, intent);
            }
            break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save: {
                saveUserInfo();
            }
            break;
            case R.id.iv_back: {
                finish();
            }
            break;
            default:
                break;

        }
    }
}
