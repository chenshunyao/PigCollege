package com.xnf.henghenghui.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.PostRequest;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpCommitQaResponse;
import com.xnf.henghenghui.model.HttpUploadImageResponse;
import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.ChildRecyclerView;
import com.xnf.henghenghui.ui.view.togglebutton.Util;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.ImageUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class QAActivity extends BaseActivity implements View.OnClickListener {

    public static QAActivity instance;

    private ImageView mBackImg;
    private TextView mTitle;
    private TextView mRightTxt;
    private View mLayoutType;
    private ChildRecyclerView mViewImgHorizontal;
    private GalleryAdapter mAdapter;
    private TextView mQaType;
    private EditText mDetail;

    private String currImageName;
    private String currQaType;
    private String currQaName;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    private File mPath;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        mPath = new File(getExternalFilesDir(null),"henghenghui");
        if(!mPath.exists()){
            mPath.mkdirs();
        }
    }

    @Override
    protected void initData() {
        initDialog();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_qa);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.main_hengheng_qa_title);
        mRightTxt = (TextView)findViewById(R.id.txt_right);
        mRightTxt.setText(R.string.main_hengheng_qa_next);
        mRightTxt.setTextColor(getResources().getColor(R.color.main_green));
        mRightTxt.setVisibility(View.VISIBLE);

        mQaType = (TextView)findViewById(R.id.txt_qa_type);
        mDetail = (EditText)findViewById(R.id.edit_detail);

        mLayoutType = findViewById(R.id.layout_type);
        mViewImgHorizontal = (ChildRecyclerView)findViewById(R.id.view_img_horizontal);
        LinearLayoutManager managerMaster = new LinearLayoutManager(this);
        managerMaster.setOrientation(LinearLayoutManager.HORIZONTAL);
        mViewImgHorizontal.setLayoutManager(managerMaster);

        ArrayList<String> datas = new ArrayList<String>();
        datas.add("");

        mAdapter = new GalleryAdapter(this, datas);
        mViewImgHorizontal.setAdapter(mAdapter);

        bindOnClickLister(this, mBackImg, mRightTxt, mLayoutType);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_right:
                nextQA();
                break;
            case R.id.layout_type:
                Intent intent = new Intent();
                intent.setClass(this, QATypeSelectActivity.class);
                intent.putExtra("qatype",currQaType);
                startActivityForResult(intent, Utils.QA_TYPE_REQUEST);
                break;
            default:
                break;
        }
        //onPrepareOptionsMenu(menu);
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

    private void initDialog(){
        dialog = new ProgressDialog(QAActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        dialog.setMessage(getString(R.string.progress_doing));
    }

    private void nextQA(){
        if(currQaType == null){
            ToastUtil.showToast(this,getString(R.string.error_no_type));
            return;
        }
        String dtxt = mDetail.getText().toString().trim();
        if(dtxt.length() == 0){
            ToastUtil.showToast(this,getString(R.string.error_no_detail));
            return;
        }
        if(dtxt.length() < 20){
            ToastUtil.showToast(this,getString(R.string.error_less_detail));
            return;
        }
//        if(mAdapter.mDatas.size() <= 1){
//            ToastUtil.showToast(this,getString(R.string.error_no_img));
//            return;
//        }
        Intent intent = new Intent(this,QA2Activity.class);
        intent.putExtra("type",currQaType);
        intent.putExtra("detail",dtxt);
        intent.putExtra("imgs",mAdapter.mDatas);
        Utils.start_Activity(this,intent);
    }

//    private void commitQA(){
//        if(mAdapter.mDatas.size() > 1){
//            if(dialog != null) {
//                dialog.show();
//            }
//            PostRequest request = OkHttpUtils.post(Urls.SERVER_URL_ + Urls.ACTION_UPLOAD_IMAGE)
//                    .tag(Urls.ACTION_UPLOAD_IMAGE);
//            for(int i = 0;i < mAdapter.mDatas.size() - 1;i++){
//                String str = mAdapter.mDatas.get(i);
//                request.params("image" + i, new File(Uri.parse(str).getPath()), "image" + i + ".png");
//            }
//            request.execute(new MyJsonCallBack<String>() {
//                @Override
//                public void onResponse(String s) {
//                    L.d(TAG, "onResponse:" + s);
//                    Message msg = new Message();
//                    msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_UPLOAD_IMAGE;
//                    msg.obj=s;
//                    mHandler.sendMessage(msg);
//                }
//            });
//        }else{
//            ToastUtil.showToast(this,getString(R.string.error_no_img));
//        }
//    }

    @Override
    public boolean handleMessage(Message msg) {
//        switch (msg.what) {
//            case CodeUtil.CmdCode.MsgTypeCode.MSG_UPLOAD_IMAGE: {
//                String response = (String)msg.obj;
//                if (Utils.getRequestStatus(response) == 1) {
//                    Gson gson = new Gson();
//                    HttpUploadImageResponse httpUploadImageResponse = gson.fromJson(response, HttpUploadImageResponse.class);
//                    String imgid = httpUploadImageResponse.getResponse().getContent().getFileMappingId();
//                    String imgPath = httpUploadImageResponse.getResponse().getContent().getFilePath();
//
//                    String userId = LoginUserBean.getInstance().getLoginUserid();
//                    JSONObject jsonObj = new JSONObject();
//                    JSONObject inJsonObj = new JSONObject();
//                    try {
//                        jsonObj.put("userId", userId);
//                        jsonObj.put("qtCategoryId", currQaType);
//                        jsonObj.put("qtDesc", mDetail.getText());
//                        jsonObj.put("fileMappingId", imgid);
//                        jsonObj.put("qtPhotos", imgPath);
//                        inJsonObj.put("attackNum", mNum.getText());
//                        inJsonObj.put("days", mAge.getText());
//                        inJsonObj.put("temp", mQa.getText());
//                        inJsonObj.put("illnesses", mHistory.getText());
//                        inJsonObj.put("immune", mIll.getText());
//                        inJsonObj.put("medication", mMedication.getText());
//                        jsonObj.put("qtPigsInfo", inJsonObj);
//                    } catch (Exception e) {
//                    }
//                    String jsonString = jsonObj.toString();
//                    OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_ONLINE_ASKQUESTION)
//                            .tag(Urls.ACTION_ONLINE_ASKQUESTION)
//                            .postJson(getRequestBody(jsonString))
//                            .execute(new MyJsonCallBack<String>() {
//                                @Override
//                                public void onResponse(String s) {
//                                    L.d(TAG, "onResponse:" + s);
//                                    Message msg = new Message();
//                                    msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_COMMIT_QA;
//                                    msg.obj=s;
//                                    mHandler.sendMessage(msg);
//                                }
//                            });
//                }
//            }
//            case CodeUtil.CmdCode.MsgTypeCode.MSG_COMMIT_QA: {
//                String response = (String)msg.obj;
//                if (Utils.getRequestStatus(response) == 1) {
//                    Gson gson = new Gson();
//                    HttpCommitQaResponse httpCommitQaResponse = gson.fromJson(response, HttpCommitQaResponse.class);
//                    if(httpCommitQaResponse.getResponse().getSucceed() == 1){
//                        ToastUtil.showToast(QAActivity.this,getString(R.string.op_success));
//                        finish();
//                    }else{
//                        ToastUtil.showToast(QAActivity.this,getString(R.string.op_failed,
//                                httpCommitQaResponse.getResponse().getErrorCode(),
//                                httpCommitQaResponse.getResponse().getErrorInfo()));
//                    }
//                }
//                if(dialog != null) {
//                    dialog.dismiss();
//                }
//            }
//        }
        return false;
    }

    private class GalleryAdapter extends
            RecyclerView.Adapter<RecyclerView.ViewHolder>
    {

        private LayoutInflater mInflater;
        private ArrayList<String> mDatas;

        private static final int TYPE_FOOTER = 0;
        private static final int TYPE_ITEM = 1;

        public GalleryAdapter(Context context, ArrayList<String> datats)
        {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mDatas.size() - 1)
                return TYPE_FOOTER;

            return TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            if (i == TYPE_ITEM) {
                //inflate your layout and pass it to view holder
                View view = mInflater.inflate(R.layout.qa_commit_img_item,
                        viewGroup, false);
                ViewItemHolder viewHolder = new ViewItemHolder(view);

                ImageView innerImage = (ImageView)view.findViewById(R.id.inner_image);
                ImageView innerDelete = (ImageView)view.findViewById(R.id.inner_delete);
                viewHolder.mInnerImage = innerImage;
                viewHolder.mInnerDelete = innerDelete;
                return viewHolder;
            } else if (i == TYPE_FOOTER) {
                View view = mInflater.inflate(R.layout.qa_commit_img_footer,
                        viewGroup, false);
                ViewFooterHolder viewHolder = new ViewFooterHolder(view);
                ImageView innerImage = (ImageView)view.findViewById(R.id.inner_image);
                viewHolder.mInnerImage = innerImage;
                return viewHolder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i)
        {
            if(viewHolder instanceof ViewItemHolder) {
                ViewItemHolder itemHolder = (ViewItemHolder)viewHolder;
                final String url = mDatas.get(i);
                mImageLoader.displayImage(url,itemHolder.mInnerImage);
                itemHolder.mInnerDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatas.remove(i);
                        notifyDataSetChanged();
                    }
                });
            } else if(viewHolder instanceof ViewFooterHolder) {
                ViewFooterHolder footerHolder = (ViewFooterHolder)viewHolder;
                footerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getItemCount() < 9) {
                            showPhotoDialog();
                        }else{
                            ToastUtil.showToast(QAActivity.this,getString(R.string.error_most_img));
                        }
                    }
                });
            }
        }

        private class ViewFooterHolder extends RecyclerView.ViewHolder
        {
            public ViewFooterHolder(View v)
            {
                super(v);
            }
            ImageView mInnerImage;
        }

        private class ViewItemHolder extends RecyclerView.ViewHolder
        {
            public ViewItemHolder(View v)
            {
                super(v);
            }
            ImageView mInnerImage;
            ImageView mInnerDelete;
        }
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
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {
                currImageName = getNowTime() + ".png";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(mPath, currImageName)));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("相册");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currImageName = getNowTime() + ".png";
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

                dlg.cancel();
            }
        });
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.QA_TYPE_REQUEST: {
                    currQaType = data.getStringExtra("rsqatype");
                    currQaName = data.getStringExtra("rsqaname");
                    reFreshQaType();
                    break;
                }
                case PHOTO_REQUEST_TAKEPHOTO: {
                    //getPhotoZoom(Uri.fromFile(new File(mPath, currImageName)), 480);
                    Bitmap bm = BitmapFactory.decodeFile(new File(mPath, currImageName).getAbsolutePath());
                    bm = ImageUtil.zoomBitmap(bm, 720, 720);
                    ImageUtil.saveImage(new File(mPath, currImageName).getAbsolutePath(), bm, true);
                    mAdapter.mDatas.add(mAdapter.mDatas.size() - 1, Uri.fromFile(new File(mPath, currImageName)).toString());
                    mAdapter.notifyDataSetChanged();
                    mViewImgHorizontal.smoothScrollBy(Integer.MAX_VALUE, 0);
                    break;
                }
                case PHOTO_REQUEST_GALLERY: {
                    if (data != null) {
                        //startPhotoZoom(data.getData(), 480);
                        try {
                            Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                            bm = ImageUtil.zoomBitmap(bm, 720, 720);
                            ImageUtil.saveImage(new File(mPath, currImageName).getAbsolutePath(), bm);
                            mAdapter.mDatas.add(mAdapter.mDatas.size() - 1, Uri.fromFile(new File(mPath, currImageName)).toString());
                            mAdapter.notifyDataSetChanged();
                            mViewImgHorizontal.smoothScrollBy(Integer.MAX_VALUE, 0);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void reFreshQaType(){
        if(currQaType != null && currQaName != null){
            mQaType.setText(currQaName);
        }
    }
}
