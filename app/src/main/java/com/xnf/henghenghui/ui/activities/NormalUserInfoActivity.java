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
import com.xnf.henghenghui.model.HttpUploadImageResponse;
import com.xnf.henghenghui.model.NoramlUserInfoResponse;
import com.xnf.henghenghui.model.NormalUserInfo;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.LocalUserInfo;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.CustomAlertDialog;
import com.xnf.henghenghui.ui.view.CustomProgressDialog;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.ImageUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.MD5Calculator;
import com.xnf.henghenghui.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NormalUserInfoActivity extends BaseActivity implements TakePhoto.TakeResultListener {

    private static final String TAG = NormalUserInfoActivity.class.getSimpleName();
    private TakePhoto takePhoto;
    private TextView mBtnModifyInfo;
    //个人信息
    private LinearLayout re_avatar;
    private RelativeLayout re_truth_name_layout;
    private RelativeLayout re_namenick_layout;
    private RelativeLayout re_city;
    private RelativeLayout re_address;
    private RoundImageView iv_avatar;
    private RelativeLayout re_work_unit;

    private TextView tv_truth_name;
    private TextView tv_nick_name;
    private TextView tv_city;
    private TextView tv_address;
    private TextView tv_work_unit;

    //畜牧信息
    private RelativeLayout re_breed;
    private RelativeLayout re_enterprise_type;
    private RelativeLayout re_raising_scale;
    private RelativeLayout re_duties;
    private TextView tv_breed;
    private TextView tv_enterprise_type;
    private TextView tv_raising_scale;
    private TextView tv_duties;

    //其他
    private RelativeLayout re_email;
    private TextView tv_email;

    private RelativeLayout re_farmName;
    private TextView tv_farmName;
    private RelativeLayout re_farmAddress;
    private TextView tv_farmAddress;

    private Button mGotoLoginBtn;

    private String imageName;

    //private LoadUserAvatar avatarLoader;
    String nick;

    private TextView mTitleTextView;

    private CustomProgressDialog progressDialog;

    private NormalUserInfo oldNormalUserInfo;

    private NormalUserInfo newNormalUserInfo;

    private boolean mIsImpleteInfo;

    private String mUserId;

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
        setContentView(R.layout.activity_normaluser_info);

        String avatar = LocalUserInfo.getInstance(NormalUserInfoActivity.this)
                .getUserInfo("avatar");
        mTitleTextView = (TextView)findViewById(R.id.info_title);

        mBtnModifyInfo = (TextView) findViewById(R.id.modify_personal_info_btn);
        mBtnModifyInfo.setOnClickListener(new MyListener());
        //个人信息
        re_avatar = (LinearLayout) this.findViewById(R.id.re_avatar);
        re_truth_name_layout = (RelativeLayout) this.findViewById(R.id.re_truth_name_layout);
        re_namenick_layout = (RelativeLayout) this.findViewById(R.id.re_namenick_layout);
        re_city = (RelativeLayout) this.findViewById(R.id.re_city);
        re_address = (RelativeLayout) this.findViewById(R.id.re_address);
        re_avatar.setOnClickListener(new MyListener());
        re_truth_name_layout.setOnClickListener(new MyListener());
        re_namenick_layout.setOnClickListener(new MyListener());
        re_city.setOnClickListener(new MyListener());
        re_address.setOnClickListener(new MyListener());
        re_work_unit = (RelativeLayout) this.findViewById(R.id.re_work_unit);
        re_work_unit.setOnClickListener(new MyListener());

        iv_avatar = (RoundImageView) this.findViewById(R.id.iv_avatar);
        iv_avatar.setType(RoundImageView.TYPE_ROUND);
        iv_avatar.setBorderRadius(3);
        tv_truth_name = (TextView) this.findViewById(R.id.tv_truth_name);
        tv_city = (TextView) this.findViewById(R.id.tv_city);
        tv_nick_name = (TextView) this.findViewById(R.id.tv_nick_name);
        tv_address = (TextView) this.findViewById(R.id.tv_address);
        tv_nick_name.setText(nick);
        tv_work_unit = (TextView) this.findViewById(R.id.tv_work_unit);


        //畜牧信息
        re_duties = (RelativeLayout)this.findViewById(R.id.re_duties);
        re_duties.setOnClickListener(new MyListener());

        re_breed = (RelativeLayout) this.findViewById(R.id.re_breed);
        re_enterprise_type = (RelativeLayout) this.findViewById(R.id.re_enterprise_type);

        re_raising_scale = (RelativeLayout) this.findViewById(R.id.re_raising_scale);
        re_breed.setOnClickListener(new MyListener());
        re_enterprise_type.setOnClickListener(new MyListener());
        re_raising_scale.setOnClickListener(new MyListener());

        tv_enterprise_type = (TextView) this.findViewById(R.id.tv_enterprise_type);
        tv_raising_scale = (TextView) this.findViewById(R.id.tv_raising_scale);
        tv_breed = (TextView) this.findViewById(R.id.tv_breed);
        tv_raising_scale = (TextView) this.findViewById(R.id.tv_raising_scale);
        tv_duties = (TextView)this.findViewById(R.id.tv_duties);

        //其他
        re_email = (RelativeLayout) this.findViewById(R.id.re_email);
        re_email.setOnClickListener(new MyListener());
        tv_email = (TextView) this.findViewById(R.id.tv_email);

        re_farmName = (RelativeLayout)this.findViewById(R.id.re_farm_name);
        re_farmName.setOnClickListener(new MyListener());
        tv_farmName = (TextView)this.findViewById(R.id.tv_farm_name);

        re_farmAddress = (RelativeLayout)this.findViewById(R.id.re_farm_address);
        re_farmAddress.setOnClickListener(new MyListener());
        tv_farmAddress = (TextView)this.findViewById(R.id.tv_farm_address);


        mGotoLoginBtn = (Button) findViewById(R.id.btn_goto_login);
        mGotoLoginBtn.setOnClickListener(new MyListener());

        Intent intent = getIntent();
        mIsImpleteInfo = intent.getBooleanExtra(Utils.REGISTER_IMPLETE_INFO, false);

        if (mIsImpleteInfo) {
            mBtnModifyInfo.setVisibility(View.GONE);
            mGotoLoginBtn.setVisibility(View.VISIBLE);
            mTitleTextView.setText(R.string.settings_implement_info);
            mUserId = intent.getStringExtra(Utils.REGISTER_IMPLETE_USER_ID);
        } else {
            mBtnModifyInfo.setVisibility(View.VISIBLE);
            mGotoLoginBtn.setVisibility(View.GONE);
            mTitleTextView.setText(R.string.settings_modify_info);
            mUserId = LoginUserBean.getInstance().getLoginUserid();
            String personalInfo = getIntent().getStringExtra("PERSONAL_INFO");
            initPersonalInfo(personalInfo);
        }

        showUserAvatar(iv_avatar, avatar);
       // String personalInfo = getIntent().getStringExtra("PERSONAL_INFO");
        //initPersonalInfo(personalInfo);

        LoginManager.setHandler(mHandler);
        //initPersonalInfo();
        oldNormalUserInfo = HengHengHuiAppliation.getInstance().getLoginNormalUser();
        newNormalUserInfo = oldNormalUserInfo;
    }

    private void initPersonalInfo(String personalInfo){
        if(personalInfo.isEmpty()){
            PersonalnfoManager.getNormalUserInfo(LoginUserBean.getInstance().getLoginUserid());
            return;
        }
        L.d(TAG,"PersonalInfo:"+personalInfo);
        if(LoginUserBean.isInfoModified){
            PersonalnfoManager.getNormalUserInfo(LoginUserBean.getInstance().getLoginUserid());
            return;
        }
        Gson gson = new Gson();
        NoramlUserInfoResponse normalUserInfo = gson.fromJson(personalInfo,NoramlUserInfoResponse.class);
        NoramlUserInfoResponse.Content  content = normalUserInfo.getResponse().getContent();

        if(tv_truth_name!=null){
            tv_truth_name.setText(content.getUserName());
        }
//        if(tv_city!=null){
//            tv_city.setText(content.getCity());
//        }
        if(tv_nick_name != null){
            tv_nick_name.setText(content.getNikeName());
        }
        if(tv_address!=null){
            tv_address.setText(content.getAddress());
        }
//        if(tv_enterprise_type !=null){
//            tv_enterprise_type.setText(content.getBusinessType());
//        }
        if(tv_raising_scale != null){
            tv_raising_scale.setText(content.getBreedScope());
        }
//        if(tv_breed != null){
//            tv_breed.setText(content.getSpecies());
//        }
        if(tv_work_unit !=null){
            tv_work_unit.setText(content.getCompany());
        }
        if(tv_email != null){
            tv_email.setText(content.getEmail());
        }
        if(tv_duties != null){
            tv_duties.setText(content.getTitle());
        }
        if(tv_farmAddress != null){
            tv_farmAddress.setText(content.getFarmAddress());
        }
        if(tv_farmName != null){
            tv_farmName.setText(content.getFarmName());
        }
    }

    private void initPersonalInfo(){
        oldNormalUserInfo = HengHengHuiAppliation.getInstance().getLoginNormalUser();
        newNormalUserInfo = oldNormalUserInfo;
        if(tv_truth_name!=null){
            tv_truth_name.setText(oldNormalUserInfo.getUserName());
        }
        if(tv_city!=null){
//            tv_city.setText(oldNormalUserInfo.getCity());
        }
        if(tv_nick_name != null){
            tv_nick_name.setText(oldNormalUserInfo.getNikeName());
        }
        if(tv_address!=null){
            tv_address.setText(oldNormalUserInfo.getAddress());
        }
        if(tv_enterprise_type !=null){
//            tv_enterprise_type.setText(oldNormalUserInfo.getBusinessType());
        }
        if(tv_raising_scale != null){
            tv_raising_scale.setText(oldNormalUserInfo.getBreedScope());
        }
//        if(tv_breed != null){
//            tv_breed.setText(oldNormalUserInfo.getSpecies());
//        }
        if(tv_work_unit !=null){
            tv_work_unit.setText(oldNormalUserInfo.getCompany());
        }
        if(tv_email != null){
            tv_email.setText(oldNormalUserInfo.getEmail());
        }
        if(tv_duties != null){
            tv_duties.setText(oldNormalUserInfo.getDuites());
        }
        if(tv_farmName != null){
            tv_farmName.setText(oldNormalUserInfo.getFarmName());
        }
        if(tv_farmAddress != null){
            tv_farmAddress.setText(oldNormalUserInfo.getFarmAddress());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void takeSuccess(String imagePath) {
        L.d(TAG,"imagePath:"+imagePath);
        BitmapFactory.Options option=new BitmapFactory.Options();
        option.inSampleSize=2;
        Bitmap bitmap=BitmapFactory.decodeFile(imagePath,option);
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
                case R.id.modify_personal_info_btn: {
                    if(checkIfUserInfoChanged()){
                        showModifyInfoDialog();
                    }
                }
                break;
                case R.id.re_avatar:
                    showPhotoDialog();
                    break;
                case R.id.re_truth_name_layout: {
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.setAction(String.valueOf(Utils.UPDATE_USERNAME));
                    intent.putExtra("truth_name",tv_truth_name.getText());
                    startActivityForResult(intent, Utils.UPDATE_USERNAME);
                }
                break;
                case R.id.re_namenick_layout: {
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.setAction(String.valueOf(Utils.UPDATE_USERNICK));
                    intent.putExtra("nick_name",tv_nick_name.getText());
                    startActivityForResult(intent, Utils.UPDATE_USERNICK);
                }
                break;
                case R.id.re_address: {
//                    Intent intent = new Intent(NormalUserInfoActivity.this,
//                            UpdateUserInfoActivity.class);
//                    intent.setAction(String.valueOf(Utils.UPDATE_USER_ADDRESS));
//                    intent.putExtra("address",tv_address.getText());
//                    startActivityForResult(intent, Utils.UPDATE_USER_ADDRESS);
                    Intent intent = new Intent();
                    intent.setClass(NormalUserInfoActivity.this, CitySelectActivity.class);
                    startActivityForResult(intent, Utils.CITY_REQUEST);
                }
                break;
                case R.id.re_city: {
                    Intent intent = new Intent();
                    intent.setClass(NormalUserInfoActivity.this, CitySelectActivity.class);
                    startActivityForResult(intent, Utils.CITY_REQUEST);
                }
                break;
                case R.id.re_work_unit: {
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.setAction(String.valueOf(Utils.UPDATE_WORK_UNIT));
                    intent.putExtra("user_work_unit",tv_work_unit.getText());
                    startActivityForResult(intent, Utils.UPDATE_WORK_UNIT);
                }
                break;
                case R.id.re_raising_scale:
                {
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.putExtra("user_raising_scale",tv_raising_scale.getText());
                    intent.setAction(String.valueOf(Utils.UPDATE_RAISING_SCALE));
                    startActivityForResult(intent, Utils.UPDATE_RAISING_SCALE);
                }
                    break;
                case R.id.btn_goto_login: {
                    if(checkIfUserInfoChanged()){
                        showModifyInfoDialog();
                    }
                }
                break;
                case R.id.re_email: {
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.putExtra("user_email",tv_email.getText());
                    intent.setAction(String.valueOf(Utils.UPDATE_EMAIL));
                    startActivityForResult(intent, Utils.UPDATE_EMAIL);
                }
                break;
                case R.id.re_breed:
                {
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.putExtra("breed",tv_breed.getText());
                    intent.setAction(String.valueOf(Utils.UPDATE_BREED));
                    startActivityForResult(intent, Utils.UPDATE_BREED);
                }
                break;
                case R.id.re_enterprise_type:
                {
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.setAction(String.valueOf(Utils.UPDATE_ENTERPRISE_TYPE));
                    intent.putExtra("enterprise_type",tv_enterprise_type.getText());
                    startActivityForResult(intent, Utils.UPDATE_ENTERPRISE_TYPE);
                }
                break;
                case R.id.re_duties:{
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.putExtra("duties",tv_breed.getText());
                    intent.setAction(String.valueOf(Utils.UPDATE_DUTIES));
                    startActivityForResult(intent, Utils.UPDATE_DUTIES);
                }
                break;
                case R.id.re_farm_name:
                {
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.putExtra("farmName",tv_farmName.getText());
                    intent.setAction(String.valueOf(Utils.UPDATE_FARM_NAME));
                    startActivityForResult(intent, Utils.UPDATE_FARM_NAME);
                }
                break;
                case R.id.re_farm_address:
                {
                    Intent intent = new Intent(NormalUserInfoActivity.this,
                            UpdateUserInfoActivity.class);
                    intent.putExtra("farmAddress",tv_farmAddress.getText());
                    intent.setAction(String.valueOf(Utils.UPDATE_FARM_ADDRESS));
                    startActivityForResult(intent, Utils.UPDATE_FARM_ADDRESS);
                }
                break;
                default:
                    break;

            }
        }

    }

    private boolean checkIfUserInfoChanged(){
        L.d(TAG,"1:"+newNormalUserInfo.toString());
        L.d(TAG,"2:"+HengHengHuiAppliation.getInstance().getLoginNormalUser().toString());
        if(!newNormalUserInfo.toString().equals(HengHengHuiAppliation.getInstance().getLoginNormalUser().toString())){
            L.d(TAG,"user name changed");
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
        tv_paizhao.setText("拍照");
        if (ContextCompat.checkSelfPermission(NormalUserInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(NormalUserInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
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
                    CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
                    CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                    getTakePhoto().onEnableCompress(compressConfig,true).onPickFromCaptureWithCrop(imageUri,cropOptions);
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
                    CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
                    CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                    getTakePhoto().onEnableCompress(compressConfig,true).onPickFromGalleryWithCrop(imageUri,cropOptions);
                }
            }
        });

    }

    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     * <p/>
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.d(TAG,"requestCode:"+requestCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.CITY_REQUEST: {
                    String cityName = data.getStringExtra("CITY_NAME");
                    String cityCode = data.getStringExtra("CITY_CODE");
                    L.d(TAG,"CityName:"+cityName+",CityCode:"+cityCode);
                    //ToastUtil.showToast(this, cityName);
                    if (tv_address != null) {
                        tv_address.setText(cityName);
                    }
                    newNormalUserInfo.setAddress(cityName);
                   }
                    break;
                case Utils.UPDATE_USER_ADDRESS: {
                    String cityName = data.getStringExtra("CITY_NAME");
                    //ToastUtil.showToast(this, cityName);
                    if (tv_city != null) {
                        tv_city.setText(cityName);
                    }
                    newNormalUserInfo.setAddress(cityName);
                }
                break;
                case Utils.UPDATE_USERNAME: {
                    String userName = data.getStringExtra("user_name");
                    if (tv_truth_name != null) {
                        tv_truth_name.setText(userName);
                    }
                    newNormalUserInfo.setUserName(userName);
                }
                break;
                case Utils.UPDATE_USERNICK: {
                    String nickName = data.getStringExtra("user_nick");
                    if (tv_nick_name != null) {
                        tv_nick_name.setText(nickName);
                    }
                    newNormalUserInfo.setNikeName(nickName);
                }
                break;
                case Utils.UPDATE_WORK_UNIT: {
                    String workUnit = data.getStringExtra("user_work_unit");
                    if (tv_work_unit != null) {
                        tv_work_unit.setText(workUnit);
                    }
                    newNormalUserInfo.setCompany(workUnit);
                }
                break;
                case Utils.UPDATE_EMAIL:{
                    String ret = data.getStringExtra("user_email");
                    if (tv_email != null) {
                        tv_email.setText(ret);
                    }
                    newNormalUserInfo.setEmail(ret);
                }
                break;
                case Utils.UPDATE_RAISING_SCALE:
                {
                    String ret = data.getStringExtra("user_raising_scale");
                    if (tv_raising_scale != null) {
                        tv_raising_scale.setText(ret);
                    }
                    newNormalUserInfo.setBreedScope(ret);
                }
                break;
                case Utils.UPDATE_BREED:
                {
                    String ret = data.getStringExtra("user_breed");
                    if (tv_breed != null) {
                        tv_breed.setText(ret);
                    }
                }
                break;
                case Utils.UPDATE_ENTERPRISE_TYPE:
                {
                    String ret = data.getStringExtra("user_enterprise_type");
                    if (tv_enterprise_type != null) {
                        tv_enterprise_type.setText(ret);
                    }
                   // newNormalUserInfo.setBusinessType(ret);
                }
                break;
                case Utils.UPDATE_DUTIES:{
                    String ret = data.getStringExtra("duties");
                    if (tv_duties != null) {
                        tv_duties.setText(ret);
                    }
                    newNormalUserInfo.setDuites(ret);
                }
                break;
                case Utils.UPDATE_FARM_NAME:{
                    String ret = data.getStringExtra("farmName");
                    if (tv_farmName != null) {
                        tv_farmName.setText(ret);
                    }
                    newNormalUserInfo.setFarmName(ret);
                }
                break;
                case Utils.UPDATE_FARM_ADDRESS:{
                    String ret = data.getStringExtra("farmAddress");
                    if (tv_farmAddress != null) {
                        tv_farmAddress.setText(ret);
                    }
                    newNormalUserInfo.setFarmAddress(ret);
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
        request.params("image",new File(Uri.parse(image).getPath()), imageName);
        newNormalUserInfo.setAvatarLocalPath(image);
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
        NormalUserInfo userInfo = HengHengHuiAppliation.getInstance().getLoginNormalUser();
        if(userInfo != null && userInfo.getAvatarLocalPath()!= null){
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(userInfo.getAvatarLocalPath(), option);
            L.d(TAG,"userInfo.getAvatarLocalPath():"+userInfo.getAvatarLocalPath());
            iv_avatar.setImageBitmap(bitmap);
        }else if(userInfo != null && userInfo.getPhoto()!= null){
            L.d(TAG,"userInfo.getPhoto():"+userInfo.getPhoto());
            mImageLoader.displayImage(Urls.SERVER_URL+userInfo.getPhoto(), iv_avatar);
        }else{
            iv_avatar.setImageResource(R.drawable.hengheng_course);
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_MODIFY_NORMALUSER_INFO:
            {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                String ret = (String) msg.obj;
                L.d(TAG, "ret=" + ret);
                if (ret.contains(HhhErrorCode.FAILED_RET)) {
                    Toast.makeText(this, "更新个人信息失败，请重试", Toast.LENGTH_SHORT).show();
                } else if (ret.contains(HhhErrorCode.SUCCESS_REQUEST)) {
                    Toast.makeText(this, "更新个人信息成功", Toast.LENGTH_SHORT).show();
                    //HengHengHuiAppliation.getInstance().updateNormalUserInfo(newNormalUserInfo);
                    HengHengHuiAppliation.getInstance().saveNormalUserInfo(newNormalUserInfo);
                    if(mIsImpleteInfo){
                        startActivity(new Intent(NormalUserInfoActivity.this, LoginActivity.class));
                        finish();
                    }else{
                        PersonalnfoManager.getNormalUserInfo(LoginUserBean.getInstance().getLoginUserid());
                    }
                }
            }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_NORAMLUSER_INFO:
            {
                String ret = (String) msg.obj;
                LoginUserBean.isInfoModified = false;
                if (ret.contains(HhhErrorCode.FAILED_RET)) {
                    //Toast.makeText(this, "获取个人信息失败，请重试", Toast.LENGTH_SHORT).show();
                } else if (ret.contains(HhhErrorCode.SUCCESS_REQUEST)) {
                    //Toast.makeText(this, "获取个人信息成功", Toast.LENGTH_SHORT).show();
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
                    newNormalUserInfo.setFileMappingId(imgid);
                    newNormalUserInfo.setPhoto(imgPath);
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
        progressDialog = new CustomProgressDialog(NormalUserInfoActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        String hintMsg = mIsImpleteInfo?getResources().getString(R.string.setting_implement_personalinfo):
                getResources().getString(R.string.setting_modify_personalinfo);
        progressDialog.setMessage(hintMsg);
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
        String hintMsg = mIsImpleteInfo?getResources().getString(R.string.setting_implement_personalinfo_hint):
                getResources().getString(R.string.setting_modify_personalinfo_hint);
        new CustomAlertDialog.Builder(this)
                .setTitle(getString(R.string.info))
                .setMessage(hintMsg)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "modify_personal_info_btn");
                        showProgressDialog();
                        //TODO 将该用户的信息传递至服务器
                        NormalUserInfo normalUserInfo = new NormalUserInfo();
                        normalUserInfo.setUserId(mUserId);
                        normalUserInfo.setUserName(tv_truth_name.getText().toString());
                        normalUserInfo.setNikeName(tv_nick_name.getText().toString());
                        normalUserInfo.setMobile(LoginUserBean.getInstance().getLoginUserid());
                        normalUserInfo.setEmail(tv_email.getText().toString());
                       // normalUserInfo.setCity(tv_city.getText().toString());
                        if (StringUtils.isEmpty(tv_address.getText().toString())) {
                            normalUserInfo.setAddress("");
                        } else {
                            normalUserInfo.setAddress(tv_address.getText().toString());
                        }
                        normalUserInfo.setDuites(tv_duties.getText().toString());
                        normalUserInfo.setCompany(tv_work_unit.getText().toString());
                       // normalUserInfo.setSpecies(tv_breed.getText().toString());
                      //  normalUserInfo.setBusinessType(tv_enterprise_type.getText().toString());
                        normalUserInfo.setBreedScope(tv_raising_scale.getText().toString());
                        normalUserInfo.setFarmName(tv_farmName.getText().toString());
                        normalUserInfo.setFarmAddress(tv_farmAddress.getText().toString());
                        normalUserInfo.setFileMappingId(newNormalUserInfo.getFileMappingId());
                        normalUserInfo.setPhoto(newNormalUserInfo.getPhoto());
                        LoginManager.modifyBreederInfo(normalUserInfo);
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
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto=new TakePhotoImpl(this,this);
        }
        return takePhoto;
    }
}
