package com.xnf.henghenghui.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.uitl.TConstant;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.PostRequest;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.cityselection.CitySelectActivity;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.HhhErrorCode;
import com.xnf.henghenghui.logic.LoginManager;
import com.xnf.henghenghui.logic.PersonalnfoManager;
import com.xnf.henghenghui.model.ExpertUserInfo;
import com.xnf.henghenghui.model.ExpertUserInfoResponse;
import com.xnf.henghenghui.model.HttpUploadImageResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.CustomAlertDialog;
import com.xnf.henghenghui.ui.view.CustomProgressDialog;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.CommonUtil;
import com.xnf.henghenghui.util.ImageUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.MD5Calculator;
import com.xnf.henghenghui.util.StringUtils;
import com.xnf.henghenghui.util.ToastUtil;

import java.io.File;

public class ExpertUserInfoActivity extends BaseActivity  implements TakePhoto.TakeResultListener{
 
    private static final String TAG = ExpertUserInfoActivity.class.getSimpleName();
    private TakePhoto takePhoto;
    private TextView mBtnModifyInfo;
    //个人信息
    private LinearLayout re_avatar;
    private RelativeLayout re_truth_name_layout;
    private RelativeLayout re_job_title_layout;
    private RelativeLayout re_workunit;
    private RelativeLayout re_address;
    private RoundImageView iv_avatar;
    private TextView tv_truth_name;
    private TextView tv_job_title;
    private TextView tv_workunit;
    private TextView tv_address;

    //畜牧信息
    private RelativeLayout re_authentication;
    private RelativeLayout re_skilled_in;
    private RelativeLayout re_brief_introduction;
    private TextView tv_authentication;
    private TextView tv_skilled_in;
    private TextView tv_brief_introduction;

    //其他
    private RelativeLayout re_email;
    private TextView tv_email;

    private Button mGotoLoginBtn;

    private String imageName;

    private String mUserId;


    private CustomProgressDialog progressDialog;

    private File mPath;

    private ExpertUserInfo oldExpertUserInfo;

    private ExpertUserInfo newExpertUserInfo;

    private boolean mIsImpleteInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        String sd_default = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_expert_info);

        mBtnModifyInfo = (TextView) findViewById(R.id.modify_personal_info_btn);
        mBtnModifyInfo.setOnClickListener(new MyListener());
        //个人信息
        re_avatar = (LinearLayout) this.findViewById(R.id.re_avatar);
        re_truth_name_layout = (RelativeLayout) this.findViewById(R.id.re_truth_name_layout);
        re_job_title_layout = (RelativeLayout) this.findViewById(R.id.re_job_title_layout);
        re_workunit = (RelativeLayout) this.findViewById(R.id.re_work_unit);
        re_address = (RelativeLayout) this.findViewById(R.id.re_address);
        re_avatar.setOnClickListener(new MyListener());
        re_truth_name_layout.setOnClickListener(new MyListener());
        re_job_title_layout.setOnClickListener(new MyListener());
        re_workunit.setOnClickListener(new MyListener());
        re_address.setOnClickListener(new MyListener());
        iv_avatar = (RoundImageView) this.findViewById(R.id.iv_avatar);
        iv_avatar.setType(RoundImageView.TYPE_ROUND);
        iv_avatar.setBorderRadius(3);
        tv_truth_name = (TextView) this.findViewById(R.id.tv_truth_name);
        tv_workunit = (TextView) this.findViewById(R.id.tv_work_unit);
        tv_job_title = (TextView) this.findViewById(R.id.tv_job_title);
        tv_address = (TextView) this.findViewById(R.id.tv_address);

        //资格认证
        re_authentication = (RelativeLayout) this.findViewById(R.id.re_authentication);
        tv_authentication = (TextView) this.findViewById(R.id.tv_breed);
        re_authentication.setOnClickListener(new MyListener());

        //介绍
        re_skilled_in = (RelativeLayout) this.findViewById(R.id.re_skilled_in);
        re_skilled_in.setOnClickListener(new MyListener());
        tv_skilled_in = (TextView) this.findViewById(R.id.tv_skilled_in);

        re_brief_introduction = (RelativeLayout) this.findViewById(R.id.re_brief_introduction);
        re_brief_introduction.setOnClickListener(new MyListener());
        tv_brief_introduction = (TextView) this.findViewById(R.id.tv_brief_introduction);


        //其他
        re_email = (RelativeLayout) this.findViewById(R.id.re_email);
        re_email.setOnClickListener(new MyListener());
        tv_email = (TextView) this.findViewById(R.id.tv_email);
        showUserAvatar(iv_avatar, "");

        mGotoLoginBtn = (Button) findViewById(R.id.btn_goto_login);
        mGotoLoginBtn.setOnClickListener(new MyListener());

        Intent intent = getIntent();
        mIsImpleteInfo = intent.getBooleanExtra(Utils.REGISTER_IMPLETE_INFO, false);
        if (mIsImpleteInfo) {
            mBtnModifyInfo.setVisibility(View.GONE);
            mGotoLoginBtn.setVisibility(View.VISIBLE);
            mUserId = intent.getStringExtra(Utils.REGISTER_IMPLETE_USER_ID);
        } else {
            mBtnModifyInfo.setVisibility(View.VISIBLE);
            mGotoLoginBtn.setVisibility(View.GONE);
            mUserId = LoginUserBean.getInstance().getLoginUserid();
            String personalInfo = getIntent().getStringExtra("PERSONAL_INFO");
            initPersonalInfo(personalInfo);
        }

        //PersonalnfoManager.getExpertUserInfo(LoginUserBean.getInstance().getLoginUserid());
        LoginManager.setHandler(mHandler);
        //initPersonalInfo();
        oldExpertUserInfo = HengHengHuiAppliation.getInstance().getLoginExpertUser();
        newExpertUserInfo = oldExpertUserInfo;
    }

    private void initPersonalInfo(String personalInfo){

        if(personalInfo== null || personalInfo.isEmpty()){
            PersonalnfoManager.getExpertUserInfo(LoginUserBean.getInstance().getLoginUserid());
            return;
        }
//        if(LoginUserBean.isInfoModified){
//            PersonalnfoManager.getExpertUserInfo(LoginUserBean.getInstance().getLoginUserid());
//            return;
//        }
        Gson gson = new Gson();
        ExpertUserInfoResponse userInfo = gson.fromJson(personalInfo,ExpertUserInfoResponse.class);
        ExpertUserInfoResponse.Content  content = userInfo.getResponse().getContent();
        updateExpertInfo(content);
        if(tv_truth_name!=null){
            tv_truth_name.setText(content.getUserName());
        }
        if(tv_workunit!=null){
            tv_workunit.setText(content.getCompany());
        }
        if(tv_address!=null){
            tv_address.setText(content.getAddress());
        }
        if(tv_job_title!=null){
            tv_job_title.setText(content.getTitles());
        }

        if(tv_skilled_in != null){
            tv_skilled_in.setText(content.getProfessional());
        }
        if(tv_brief_introduction != null){
            tv_brief_introduction.setText(content.getDesc());
        }
        if(tv_email != null){
            tv_email.setText(content.getEmail());
        }
        if(tv_authentication != null){
            tv_authentication.setText(content.getCertType());
        }
    }

    private void updateExpertInfo(ExpertUserInfoResponse.Content content){
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

    private void initPersonalInfo(){
        oldExpertUserInfo = HengHengHuiAppliation.getInstance().getLoginExpertUser();
        newExpertUserInfo = oldExpertUserInfo;
        if(tv_truth_name!=null){
            tv_truth_name.setText(oldExpertUserInfo.getUserName());
        }
        if(tv_workunit!=null){
            tv_workunit.setText(oldExpertUserInfo.getCompany());
        }
        if(tv_address!=null){
            tv_address.setText(oldExpertUserInfo.getAddress());
        }
        if(tv_authentication != null){
            tv_authentication.setText(oldExpertUserInfo.getIsRect());
        }
        if(tv_email != null){
            tv_email.setText(oldExpertUserInfo.getEmail());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        LoginManager.setHandler(mHandler);
    }

 @Override
    public void takeSuccess(String imagePath) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, option);
        iv_avatar.setImageBitmap(bitmap);
        updateAvatarInServer(imagePath);
    }

    @Override
    public void takeFail(String msg) {

    }

    @Override
    public void takeCancel() {

    }

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.modify_personal_info_btn:{
                    if(checkIfUserInfoChanged()){
                        showModifyInfoDialog();
                    }
                }
                    break;
                case R.id.re_avatar:
                    showPhotoDialog();
                    break;
                case R.id.re_truth_name_layout: {
                    Intent intent = new Intent(ExpertUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.setAction(String.valueOf(Utils.UPDATE_USERNAME));
                    if(tv_truth_name!=null && tv_truth_name.getText()!= null){
                        intent.putExtra("truth_name",tv_truth_name.getText());
                    }else{
                        intent.putExtra("truth_name","");
                    }

                    startActivityForResult(intent, Utils.UPDATE_USERNAME);
                }
                break;
                case R.id.re_job_title_layout:{
                    Intent intent = new Intent(ExpertUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.setAction(String.valueOf(Utils.UPDATE_JOB_TITLE));
                    if(tv_job_title!=null && tv_job_title.getText()!= null){
                        intent.putExtra("duties",tv_job_title.getText());
                    }else{
                        intent.putExtra("duties","");
                    }
                    startActivityForResult(intent, Utils.UPDATE_JOB_TITLE);
                }
                    break;
                case R.id.re_skilled_in:
                {
                    Intent intent = new Intent(ExpertUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.setAction(String.valueOf(Utils.UPDATE_SKILLED_IN));
                    if(tv_skilled_in!=null && tv_skilled_in.getText()!= null){
                        intent.putExtra("skilledin",tv_skilled_in.getText());
                    }else{
                        intent.putExtra("skilledin","");
                    }
                    startActivityForResult(intent, Utils.UPDATE_SKILLED_IN);
                }
                    break;
                case R.id.re_address: {
                    Intent intent = new Intent();
                    intent.setClass(ExpertUserInfoActivity.this, CitySelectActivity.class);
                    startActivityForResult(intent, Utils.CITY_REQUEST);
                }
                break;
                case R.id.re_work_unit: {
                    Intent intent = new Intent(ExpertUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.setAction(String.valueOf(Utils.UPDATE_WORK_UNIT));
                    intent.putExtra("user_work_unit",tv_workunit.getText());
                    startActivityForResult(intent, Utils.UPDATE_WORK_UNIT);
                }
                break;
                case R.id.re_brief_introduction:{
                    Intent intent = new Intent(ExpertUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.setAction(String.valueOf(Utils.UPDATE_BRIRF_INTRODUCTION));
                    if(tv_brief_introduction!=null && tv_brief_introduction.getText()!= null){
                        intent.putExtra("brief_introduction",tv_brief_introduction.getText());
                    }else{
                        intent.putExtra("brief_introduction","");
                    }
                    startActivityForResult(intent, Utils.UPDATE_BRIRF_INTRODUCTION);
                }
                    break;
                case R.id.re_authentication:{

                }
                    break;
                case R.id.btn_goto_login: {
                    if(checkIfUserInfoChanged()){
                        showModifyInfoDialog();
                    }else{
                        Toast.makeText(ExpertUserInfoActivity.this,getResources().getString(R.string.henghenghui_implement_data),Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case R.id.re_email: {
                    Intent intent = new Intent(ExpertUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    if(tv_email!=null && tv_email.getText()!= null){
                        intent.putExtra("user_email",tv_email.getText());
                    }else{
                        intent.putExtra("user_email","");
                    }
                    intent.setAction(String.valueOf(Utils.UPDATE_EMAIL));
                    startActivityForResult(intent, Utils.UPDATE_EMAIL);
                }
                break;
                default:
                    break;

            }
        }

    }
    private boolean checkIfUserInfoChanged(){
        if(LoginUserBean.isInfoModified ){
            return false;
        }
        if(!newExpertUserInfo.toString().equals(HengHengHuiAppliation.getInstance().getLoginExpertUser().toString())){
            return true;
        }
        return false;
    }

    private void showPhotoDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alertdialog);
        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        if (ContextCompat.checkSelfPermission(ExpertUserInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(ExpertUserInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        tv_paizhao.setText("拍照");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {
                {
                    dlg.cancel();
                    imageName = System.currentTimeMillis() + ".jpg";
                    //imageName = MD5Calculator.calculateMD5(System.currentTimeMillis() + ".jpg");
                    File file = new File(Environment.getExternalStorageDirectory(), "/Henghenghui/Avatar/" + imageName);
                    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                    Uri imageUri = Uri.fromFile(file);
                    CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(400).create();
                    CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                    getTakePhoto().onEnableCompress(compressConfig, true).onPickFromCaptureWithCrop(imageUri, cropOptions);
                }
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("相册");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                {
                    dlg.cancel();
                    imageName = System.currentTimeMillis() + ".jpg";
                    //imageName = MD5Calculator.calculateMD5(System.currentTimeMillis() + ".jpg");
                    File file = new File(Environment.getExternalStorageDirectory(), "/Henghenghui/Avatar/" + imageName);
                    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                    Uri imageUri = Uri.fromFile(file);
                    CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(400).create();
                    CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                    getTakePhoto().onEnableCompress(compressConfig, true).onPickFromGalleryWithCrop(imageUri, cropOptions);
                }
            }
        });

    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.CITY_REQUEST: {
                    String cityName = data.getStringExtra("CITY_NAME");
                    String cityCode = data.getStringExtra("CITY_CODE");
                    //ToastUtil.showToast(this, cityName);
                    if (tv_address != null) {
                        tv_address.setText(cityName);
                    }
                    newExpertUserInfo.setAddress(cityName);
                   }
                    break;
                case Utils.UPDATE_USER_ADDRESS: {
                    String cityName = data.getStringExtra("user_address");
                    //ToastUtil.showToast(this, cityName);
                    if (tv_address != null) {
                        tv_address.setText(cityName);
                    }
                    newExpertUserInfo.setAddress(cityName);
                }
                break;
                case Utils.UPDATE_WORK_UNIT:{
                    String workUnit = data.getStringExtra("user_work_unit");
                    if (tv_workunit != null) {
                        tv_workunit.setText(workUnit);
                    }
                    newExpertUserInfo.setCompany(workUnit);
                }
                break;
                case Utils.UPDATE_USERNAME: {
                    String userName = data.getStringExtra("user_name");
                    if (tv_truth_name != null) {
                        tv_truth_name.setText(userName);
                    }
                    newExpertUserInfo.setUserName(userName);
                }
                break;
                case Utils.UPDATE_JOB_TITLE: {
                    String jobtitle = data.getStringExtra("user_jobtitle");
                    if (tv_job_title != null) {
                        tv_job_title.setText(jobtitle);
                    }
                    newExpertUserInfo.setTitles(jobtitle);
                }
				break;
                case Utils.UPDATE_EMAIL:{
                    String ret = data.getStringExtra("user_email");
                    if (tv_email != null) {
                        tv_email.setText(ret);
                    }
                    newExpertUserInfo.setEmail(ret);
                }
                break;
                case Utils.UPDATE_SKILLED_IN:{
                    String ret = data.getStringExtra("skilledin");
                    if (tv_skilled_in != null) {
                        tv_skilled_in.setText(ret);
                    }
                    newExpertUserInfo.setProfessional(ret);
                }
                break;
                case Utils.UPDATE_BRIRF_INTRODUCTION:{
                    String ret = data.getStringExtra("brief_introduction");
                    if (tv_brief_introduction != null) {
                        tv_brief_introduction.setText(ret);
                    }
                    newExpertUserInfo.setDesc(ret);
                }
                break;
                case TConstant.RC_PICK_PICTURE_FROM_GALLERY_CROP:
                case TConstant.RC_PICK_PICTURE_FROM_GALLERY_ORIGINAL:
                case TConstant.RC_PICK_PICTURE_FROM_DOCUMENTS_ORIGINAL:
                case TConstant.RC_PICK_PICTURE_FROM_DOCUMENTS_CROP:
                case TConstant.RC_PICK_PICTURE_FROM_CAPTURE_CROP:
                case TConstant.RC_PICK_PICTURE_FROM_CAPTURE:
                case TConstant.RC_CROP:
                case Utils.REQUEST_CROP:
                {
                    getTakePhoto().onActivityResult(requestCode, resultCode, data);
                }
                break;
                default:
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @SuppressLint("SdCardPath")
    private void updateAvatarInServer(final String image) {
        PostRequest request = OkHttpUtils.post(Urls.SERVER_URL_ + Urls.ACTION_UPLOAD_IMAGE)
                .tag(Urls.ACTION_UPLOAD_IMAGE);
        request.params("image", new File(Uri.parse(image).getPath()), imageName);
        newExpertUserInfo.setAvatarLocalPath(image);
        HengHengHuiAppliation.getInstance().updateUserInfo("user.avatarLocalPath",image);
        request.execute(new MyJsonCallBack<String>() {
            @Override
            public void onResponse(String s) {
                L.d(TAG, "onResponse:" + s);
                Message msg = new Message();
                msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_UPLOAD_IMAGE;
                msg.obj=s;
                mHandler.sendMessage(msg);

            }
        });
    }

    private void showUserAvatar(ImageView iamgeView, String avatar) {
        ExpertUserInfo userInfo = HengHengHuiAppliation.getInstance().getLoginExpertUser();
        if(userInfo != null && userInfo.getAvatarLocalPath()!= null){
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(userInfo.getAvatarLocalPath(), option);
            iv_avatar.setImageBitmap(bitmap);
        }else if(userInfo != null && userInfo.getPhoto()!= null){
            mImageLoader.displayImage(userInfo.getPhoto(), iv_avatar);
        }else{
            iv_avatar.setImageResource(R.drawable.shouyi2);
        }
    }

    public void back(View view) {
        finish();
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_MODIFY_EXPERTUSER_INFO:{
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                String ret = (String) msg.obj;
                L.d(TAG, "ret=" + ret);
                if (ret.contains(HhhErrorCode.FAILED_RET)) {
                    Toast.makeText(this, "更新个人信息失败，请重试", Toast.LENGTH_SHORT).show();
                } else if (ret.contains(HhhErrorCode.SUCCESS_REQUEST)) {
                    Toast.makeText(this, "更新个人信息成功", Toast.LENGTH_SHORT).show();
                    //HengHengHuiAppliation.getInstance().updateExpertUserInfo(newExpertUserInfo);
                    HengHengHuiAppliation.getInstance().saveExpertUserInfo(newExpertUserInfo);
                    if (mIsImpleteInfo) {
                        startActivity(new Intent(ExpertUserInfoActivity.this, LoginActivity.class));
                        finish();
                    }else{
                        PersonalnfoManager.getExpertUserInfo(LoginUserBean.getInstance().getLoginUserid());
                    }
                }
            }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_EXPERTUSER_INFO:
            {
                String ret = (String) msg.obj;
                LoginUserBean.isInfoModified = false;
                if (ret.contains(HhhErrorCode.FAILED_RET)) {
                    //Toast.makeText(this, "获取个人信息失败，请重试", Toast.LENGTH_SHORT).show();
                } else if (ret.contains(HhhErrorCode.SUCCESS_REQUEST)) {
                    initPersonalInfo(ret);
                }

            }
            break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_UPLOAD_IMAGE: {

                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Toast.makeText(this, "上传图片成功", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    HttpUploadImageResponse httpUploadImageResponse = gson.fromJson(response, HttpUploadImageResponse.class);
                    String imgid = httpUploadImageResponse.getResponse().getContent().getFileMappingId();
                    String imgPath = httpUploadImageResponse.getResponse().getContent().getFilePath();
                    newExpertUserInfo.setFileMappingId(imgid);
                    newExpertUserInfo.setPhoto(imgPath);
                    Intent intent = new Intent(CommonUtil.UPDATE_AVATAR_ACTION);
                    intent.putExtra("PHOTO_PATH",imgPath);
                    sendBroadcast(intent);

                } else {

                }
            }
            break;
            default:
                break;
        }
        return false;
    }

  

    private void showProgressDialog() {
        progressDialog = new CustomProgressDialog(ExpertUserInfoActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(this
                .getResources().getString(R.string.progress_doing));
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
    @Override
    public void onBackPressed() {
        if( checkIfUserInfoChanged()){
            showModifyInfoDialog();
            return;
        }
        super.onBackPressed();
    }

    private void showModifyInfoDialog(){
        new CustomAlertDialog.Builder(this)
                .setTitle(getString(R.string.info))
                .setMessage("个人信息已经修改，是否提交？")
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("yuahun", "modify_personal_info_btn");
                        showProgressDialog();
                        //TODO 将该用户的信息传递至服务器
                        ExpertUserInfo expertUserInfo = new ExpertUserInfo();
                        expertUserInfo.setUserId(mUserId);
                        if(tv_truth_name.getText()!=null){
                            expertUserInfo.setUserName(tv_truth_name.getText().toString());
                        }
                        //若是注册就用传递过来的值，若是登录用户
                        expertUserInfo.setMobile(mUserId);
                        if(tv_email.getText()!=null){
                            expertUserInfo.setEmail(tv_email.getText().toString());
                        }

                       // normalUserInfo.setCity(tv_city.getText().toString());
                        if (StringUtils.isEmpty(tv_address.getText().toString())) {
                            expertUserInfo.setAddress("");
                        } else {
                            expertUserInfo.setAddress(tv_address.getText().toString());
                        }
                         //TODO
                        expertUserInfo.setIsRect("1");
                        expertUserInfo.setCertType("1");
                        if (StringUtils.isEmpty(tv_workunit.getText().toString())) {
                            expertUserInfo.setCompany("");
                        } else {
                            expertUserInfo.setCompany(tv_workunit.getText().toString());
                        }
                        if (StringUtils.isEmpty(tv_brief_introduction.getText().toString())) {
                            expertUserInfo.setDesc("");
                        } else {
                            expertUserInfo.setDesc(tv_brief_introduction.getText().toString());
                        }
                        if (StringUtils.isEmpty(tv_skilled_in.getText().toString())) {
                            expertUserInfo.setProfessional("");
                        } else {
                            expertUserInfo.setProfessional(tv_skilled_in.getText().toString());
                        }
                        if (StringUtils.isEmpty(tv_job_title.getText().toString())) {
                            expertUserInfo.setTitles("");
                        } else {
                            expertUserInfo.setTitles(tv_job_title.getText().toString());
                        }
                       // normalUserInfo.setSpecies(tv_breed.getText().toString());
                      //  normalUserInfo.setBusinessType(tv_enterprise_type.getText().toString());
                        if(newExpertUserInfo.getFileMappingId()!=null){
                            expertUserInfo.setFileMappingId(newExpertUserInfo.getFileMappingId());
                        }else{
                            expertUserInfo.setFileMappingId("");
                        }
                        if(newExpertUserInfo.getPhoto()!=null){
                            expertUserInfo.setPhoto(newExpertUserInfo.getPhoto());
                        }else{
                            expertUserInfo.setPhoto("");
                        }
                        LoginManager.modifyExpertInfo(expertUserInfo);
                        LoginUserBean.isInfoModified = true;
                    }
                }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = new TakePhotoImpl(this, this);
        }
        return takePhoto;
    }
}
