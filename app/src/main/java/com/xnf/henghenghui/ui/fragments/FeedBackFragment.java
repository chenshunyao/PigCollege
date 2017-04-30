package com.xnf.henghenghui.ui.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.PersonalnfoManager;
import com.xnf.henghenghui.ui.base.BaseFragment;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.CustomProgressDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.StringUtils;
import com.xnf.henghenghui.util.TDevice;
import com.xnf.henghenghui.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FeedBackFragment extends BaseFragment2 {

    private CustomProgressDialog progressDialog;

    @InjectView(R.id.et_feedback)
    EditText mEtContent;
    @InjectView(R.id.et_contact)
    EditText mEtContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_feedback, null);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.submit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_send:
                String data = mEtContent.getText().toString();
                if (StringUtils.isEmpty(data)) {
                    ToastUtil.showToast(getActivity(),"你忘记写建议咯");
                } else {
                    data += "<br>";
                    data += mEtContact.getText() + "<br>";
                    data += TDevice.getVersionName() + "("
                            + TDevice.getVersionCode() + ")<br>";
                    showProgressDialog();
                    PersonalnfoManager.feedBack(LoginUserBean.getInstance().getLoginUserid(),data);
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {
        PersonalnfoManager.setHandler(mHandler);
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_FEEDBACK: {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                String response = (String)msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                      ToastUtil.showToast(getActivity(),"提交成功");
                      getActivity().finish();
                }else{
                      ToastUtil.showToast(getActivity(),"提交失败，请稍后重试");
                }
                break;
            }
            default:
                break;
        }
        return super.handleMessage(msg);
    }

    private void showProgressDialog() {
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(this
                .getResources().getString(R.string.setting_feedback));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
}