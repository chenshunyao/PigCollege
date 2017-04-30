package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.util.DensityUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpAnwserListResponse;
import com.xnf.henghenghui.model.HttpQuestionDetailResponse;
import com.xnf.henghenghui.model.HttpQuestionDetailV2Response;
import com.xnf.henghenghui.model.HttpUserQAListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.ChildRecyclerView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.ui.ViewPagerActivity;

public class QuestionDetailActivity extends BaseActivity implements View.OnClickListener,ListView.OnItemClickListener {

//    public static final int TYPE_MY_QUESTION = 0;
    public static boolean DATA_CHANGE = false;

    private String mQtid = null;

    private ImageView mBackImg;
    private TextView mTitle;
    private ImageView mRightImg;
    private TextView mRightTxt;

    private TextView mTxtCatagrory;
    private TextView mTxtDate;
    private TextView mTxtIntro;
    private ViewGroup mLayoutShuliang;
    private TextView mTxtShuliang;
    private ViewGroup mLayoutRiLing;
    private TextView mTxtRiLing;
    private ViewGroup mLayoutTiWen;
    private TextView mTxtTiWen;
    private ViewGroup mLayoutBingShi;
    private TextView mTxtBingShi;
    private ViewGroup mLayoutMianYi;
    private TextView mTxtMianYi;
    private ViewGroup mLayoutYongYao;
    private TextView mTxtYongYao;
    private TextView mTxtTxtAnswerNum;

    private View mQuestionHeader;

    private ChildRecyclerView mViewImgHorizontal;
    private GalleryAdapter mAdapter;

    private ProgressDialog dialog;

//    private int mType = TYPE_MY_QUESTION;

    private ListView mQuestionAnwserList;
    private AnwserAdapter mAnwserAdapter;

    private HttpQuestionDetailV2Response mHttpQuestionDetailV2Response;
    private HttpAnwserListResponse mHttpAnwserListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DATA_CHANGE = false;
//        setContentView(R.layout.activity_my_question);
    }

    @Override
    protected void initData() {
        initDialog();
    }

    private void initDialog(){
        dialog = new ProgressDialog(QuestionDetailActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        dialog.setMessage(getString(R.string.progress_doing));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_question_detail);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
//        if(mType == TYPE_MY_QUESTION){
        mTitle.setText(R.string.hengheng_question_detail_title);
//        }
        mRightImg = (ImageView)findViewById(R.id.img_right);
//        mRightImg.setImageResource(R.drawable.title_dot_right);
        mRightImg.setVisibility(View.GONE);
        mRightTxt = (TextView)findViewById(R.id.txt_right);
        mRightTxt.setTextColor(getResources().getColor(R.color.main_green));
        mRightTxt.setText("我来回答");
        if(LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.NORMAL_USER) {
            mRightTxt.setVisibility(View.GONE);
        }else if(LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.EXPERT_USER){
            mRightTxt.setVisibility(View.VISIBLE);
        }

        mQuestionHeader = getLayoutInflater().inflate(R.layout.list_question_header,null);

        mTxtCatagrory = (TextView)mQuestionHeader.findViewById(R.id.txt_catagrory);
        mTxtDate = (TextView)mQuestionHeader.findViewById(R.id.txt_date);
        mTxtIntro = (TextView)mQuestionHeader.findViewById(R.id.txt_intro);
        mLayoutShuliang = (ViewGroup)mQuestionHeader.findViewById(R.id.layout_shuliang);
        mTxtShuliang = (TextView)mQuestionHeader.findViewById(R.id.txt_shuliang);
        mLayoutRiLing = (ViewGroup)mQuestionHeader.findViewById(R.id.layout_riling);
        mTxtRiLing = (TextView)mQuestionHeader.findViewById(R.id.txt_riling);
        mLayoutTiWen = (ViewGroup)mQuestionHeader.findViewById(R.id.layout_tiwen);
        mTxtTiWen = (TextView)mQuestionHeader.findViewById(R.id.txt_tiwen);
        mLayoutBingShi = (ViewGroup)mQuestionHeader.findViewById(R.id.layout_bingshi);
        mTxtBingShi = (TextView)mQuestionHeader.findViewById(R.id.txt_bingshi);
        mLayoutMianYi = (ViewGroup)mQuestionHeader.findViewById(R.id.layout_mianyi);
        mTxtMianYi = (TextView)mQuestionHeader.findViewById(R.id.txt_mianyi);
        mLayoutYongYao = (ViewGroup)mQuestionHeader.findViewById(R.id.layout_yongyao);
        mTxtYongYao = (TextView)mQuestionHeader.findViewById(R.id.txt_yongyao);
        mTxtTxtAnswerNum = (TextView)mQuestionHeader.findViewById(R.id.txt_anwser_num);

        mViewImgHorizontal = (ChildRecyclerView)mQuestionHeader.findViewById(R.id.view_img_horizontal);
        LinearLayoutManager managerMaster = new LinearLayoutManager(this);
        managerMaster.setOrientation(LinearLayoutManager.HORIZONTAL);
        mViewImgHorizontal.setLayoutManager(managerMaster);

//        mAdapter = new GalleryAdapter(this, datas);
//        mViewImgHorizontal.setAdapter(mAdapter);

        mQuestionAnwserList = (ListView)findViewById(R.id.question_anwser_list);
        mQuestionAnwserList.addHeaderView(mQuestionHeader,null,false);
//        mQuestionAnwserList.setOnItemClickListener(this);

        bindOnClickLister(this, mBackImg, mRightTxt);

        mQtid = getIntent().getStringExtra("qtid");

//        testData();
        loadQuestionDetail(false);
        loadAnwserList(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(DATA_CHANGE){
            loadQuestionDetail(false);
            loadAnwserList(true);
        }
    }

    //    private void testData() {
//        mTxtCatagrory.setText("营养");
//        mTxtDate.setText("2016-7-30");
//        mTxtIntro.setText("我的猪可以飞，新品种，我要发财了！我的猪可以飞，新品种，我要发财了！我的猪可以飞，新品种，我要发财了！我的猪可以飞，新品种，我要发财了！");
//        mTxtShuliang.setText(getString(R.string.txt_shuliang_intro,"100"));
//        mTxtRiLing.setText(getString(R.string.txt_riling_intro,"10个月"));
//        mTxtTiWen.setText(getString(R.string.txt_tiwen_intro,"39度"));
//        mTxtBingShi.setText(getString(R.string.txt_bingshi_intro,"无"));
//        mTxtMianYi.setText(getString(R.string.txt_bingshi_intro,"常规免疫"));
//        mTxtYongYao.setText(getString(R.string.txt_bingshi_intro,"无"));
//        mTxtTxtAnswerNum.setText(getString(R.string.txt_anwser_num_1, "3"));
//
//        ArrayList<String> datas = new ArrayList<String>();
//        datas.add("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg");
//        datas.add("http://img5.imgtn.bdimg.com/it/u=3104746645,2369427236&fm=21&gp=0.jpg");
//        datas.add("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg");
//        datas.add("http://img5.imgtn.bdimg.com/it/u=3104746645,2369427236&fm=21&gp=0.jpg");
//        datas.add("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg");
//        datas.add("http://img5.imgtn.bdimg.com/it/u=3104746645,2369427236&fm=21&gp=0.jpg");
//        datas.add("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg");
//        mAdapter = new GalleryAdapter(this, datas);
//        mViewImgHorizontal.setAdapter(mAdapter);
//
//        mAnwserAdapter = new AnwserAdapter(this, datas);
//        mQuestionAnwserList.setAdapter(mAnwserAdapter);
//    }

    private void loadQuestionDetail(boolean sp) {
        if (sp) {
            if (dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("qtId", mQtid);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_QUESTIONV2_INFO)
                .tag(Urls.ACTION_QUESTIONV2_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_QUESTIONV2_INFO;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    private void loadAnwserList(boolean sp){
        if (sp) {
            if (dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("qtId", mQtid);
            jsonObj.put("qtBeginTime", "");
            jsonObj.put("qtEndTime", "");
            jsonObj.put("startRowNum", "");
            jsonObj.put("endRowNum", "");
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_ANSWER_QUESTION_INFO)
                .tag(Urls.ACTION_ANSWER_QUESTION_INFO)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_ANSWER_QUESTION_INFO;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
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
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_QUESTIONV2_INFO: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpQuestionDetailV2Response = gson.fromJson(response, HttpQuestionDetailV2Response.class);
                    HttpQuestionDetailV2Response.Content content = mHttpQuestionDetailV2Response.getResponse().getContent();

                    mTxtCatagrory.setText(content.getQtCategoryName());
                    mTxtIntro.setText(content.getQtTitle());
                    if(TextUtils.isEmpty(content.getAttackNum())){
                        mLayoutShuliang.setVisibility(View.GONE);
                    }else {
                        mTxtShuliang.setText(content.getAttackNum());
                    }
                    if(TextUtils.isEmpty(content.getDays())){
                        mLayoutRiLing.setVisibility(View.GONE);
                    }else {
                        mTxtRiLing.setText(content.getDays());
                    }
                    if(TextUtils.isEmpty(content.getTemp())){
                        mLayoutTiWen.setVisibility(View.GONE);
                    }else {
                        mTxtTiWen.setText(content.getTemp());
                    }
                    if(TextUtils.isEmpty(content.getIllnesses())){
                        mLayoutBingShi.setVisibility(View.GONE);
                    }else {
                        mTxtBingShi.setText(content.getIllnesses());
                    }
                    if(TextUtils.isEmpty(content.getImmune())){
                        mLayoutMianYi.setVisibility(View.GONE);
                    }else {
                        mTxtMianYi.setText(content.getImmune());
                    }
                    if(TextUtils.isEmpty(content.getMedication())){
                        mLayoutYongYao.setVisibility(View.GONE);
                    }else {
                        mTxtYongYao.setText(content.getMedication());
                    }
                    String phonostrs = content.getQtPhotos();
                    String[] photos = phonostrs.split("\\|");
                    if(photos.length != 0 && !TextUtils.isEmpty(photos[0])){
                        mAdapter = new GalleryAdapter(this, photos);
                        mViewImgHorizontal.setAdapter(mAdapter);
                        mViewImgHorizontal.setVisibility(View.VISIBLE);
                    }else{
                        mViewImgHorizontal.setVisibility(View.GONE);
                    }
                } else {
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_ANSWER_QUESTION_INFO: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpAnwserListResponse = gson.fromJson(response, HttpAnwserListResponse.class);
                    List<HttpAnwserListResponse.Content> datas = mHttpAnwserListResponse.getResponse().getContent();
                    mTxtTxtAnswerNum.setText(getString(R.string.txt_anwser_num_1, datas.size()));
                    mTxtTxtAnswerNum.setVisibility(View.VISIBLE);

                    if(datas.size() == 0){
                        mQuestionAnwserList.setAdapter(new AnwserAdapter(this, new ArrayList<HttpAnwserListResponse.Content>()));
                        mTxtTxtAnswerNum.setVisibility(View.GONE);

                        mTxtDate.setText("");
                        mTxtDate.setVisibility(View.GONE);
                    }else {
                        mAnwserAdapter = new AnwserAdapter(this, datas);
                        mQuestionAnwserList.setAdapter(mAnwserAdapter);

                        mTxtDate.setText(datas.get(datas.size() - 1).getAqDateTime());
//                        mTxtDate.setVisibility(View.VISIBLE);
                        mTxtDate.setVisibility(View.GONE);
                    }
                } else {
                    mQuestionAnwserList.setAdapter(new AnwserAdapter(this, new ArrayList<HttpAnwserListResponse.Content>()));
                    mTxtTxtAnswerNum.setVisibility(View.GONE);
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_right:
                Intent intent = new Intent(this, AnwserQuestionActivity.class);
                intent.putExtra("qtid",mQtid);
                Utils.start_Activity(this, intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Intent intent = new Intent(this, QuestionDetailActivity.class);
//        HttpUserQAListResponse.Content content = (HttpUserQAListResponse.Content)adapterView.getItemAtPosition(i);
//        intent.putExtra("qtid",content.getQtId());
//        Utils.start_Activity(this, intent);
//        ToastUtil.showToast(MyQuestionActivity.this,"onItemClick");
    }

    private class AnwserAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<HttpAnwserListResponse.Content> infoList;
        private AnwserViewHolder holder;
        public AnwserAdapter(Context context,List<HttpAnwserListResponse.Content> infoList){
            this.inflater = LayoutInflater.from(context);
            this.infoList = infoList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(R.layout.list_question_anwser_item, null);
                holder = new AnwserViewHolder();
                holder.innerImage = (ImageView) convertView
                        .findViewById(R.id.inner_image);
                holder.innerDate = (TextView) convertView
                        .findViewById(R.id.inner_date);
                holder.innerIntro = (TextView) convertView
                        .findViewById(R.id.inner_intro);
                holder.innerName = (TextView) convertView
                        .findViewById(R.id.inner_name);
                holder.innerDuty = (TextView) convertView
                        .findViewById(R.id.inner_duty);
                holder.innerZan = (TextView) convertView
                        .findViewById(R.id.inner_zan);
                holder.layoutAnwserImg0 = (ViewGroup)convertView
                        .findViewById(R.id.layout_anwser_img_0);
                holder.layoutAnwserImg1 = (ViewGroup)convertView
                        .findViewById(R.id.layout_anwser_img_1);
                holder.innerAnwserImage0 = (ImageView) convertView
                        .findViewById(R.id.inner_anwser_image_0);
                holder.innerAnwserImage1 = (ImageView) convertView
                        .findViewById(R.id.inner_anwser_image_1);
                holder.innerAnwserImage2 = (ImageView) convertView
                        .findViewById(R.id.inner_anwser_image_2);
                holder.innerAnwserImage3 = (ImageView) convertView
                        .findViewById(R.id.inner_anwser_image_3);
                holder.innerAnwserImage4 = (ImageView) convertView
                        .findViewById(R.id.inner_anwser_image_4);
                holder.innerAnwserImage5 = (ImageView) convertView
                        .findViewById(R.id.inner_anwser_image_5);
                holder.innerAnwserImage6 = (ImageView) convertView
                        .findViewById(R.id.inner_anwser_image_6);
                holder.innerAnwserImage7 = (ImageView) convertView
                        .findViewById(R.id.inner_anwser_image_7);
                convertView.setTag(holder);
            } else{
                holder = (AnwserViewHolder) convertView.getTag();
            }

            final HttpAnwserListResponse.Content anwser = infoList.get(position);

            if (anwser.getAqUserPhoto() == null || anwser.getAqUserPhoto().isEmpty()) {
                holder.innerImage.setImageResource(R.drawable.shouyi2);
            } else {
                mImageLoader.displayImage(anwser.getAqUserPhoto(), holder.innerImage);
            }

//            mImageLoader.displayImage(anwser.getAqUserPhoto(), holder.innerImage);
            holder.innerName.setText(anwser.getAqUserName());
            holder.innerDuty.setText(anwser.getTitle());
            holder.innerIntro.setText(anwser.getAqContent());
            holder.innerDate.setText(anwser.getAqDateTime());
            holder.innerZan.setText(getString(R.string.zan_count, anwser.getPraiseCount()));

            holder.innerName.setCompoundDrawablePadding(DensityUtil.dip2px(context, 6));
            holder.innerName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.detail_vip,0);

            holder.layoutAnwserImg0.setVisibility(View.GONE);
            holder.layoutAnwserImg1.setVisibility(View.GONE);
            holder.innerAnwserImage0.setVisibility(View.INVISIBLE);
            holder.innerAnwserImage1.setVisibility(View.INVISIBLE);
            holder.innerAnwserImage2.setVisibility(View.INVISIBLE);
            holder.innerAnwserImage3.setVisibility(View.INVISIBLE);
            holder.innerAnwserImage4.setVisibility(View.INVISIBLE);
            holder.innerAnwserImage5.setVisibility(View.INVISIBLE);
            holder.innerAnwserImage6.setVisibility(View.INVISIBLE);
            holder.innerAnwserImage7.setVisibility(View.INVISIBLE);
            String phonostrs = anwser.getAqPhoto();
            final String[] photos = phonostrs.split("\\|");
            if(photos.length != 0 && !TextUtils.isEmpty(photos[0])){
                if(photos.length > 0){
                    holder.layoutAnwserImg0.setVisibility(View.VISIBLE);
                    holder.innerAnwserImage0.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage(photos[0], holder.innerAnwserImage0);
                    holder.innerAnwserImage0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(QuestionDetailActivity.this, ViewPagerActivity.class);
                            intent.putExtra("imglist", photos);
                            intent.putExtra("curindex", 0);
                            Utils.start_Activity(QuestionDetailActivity.this, intent);
                        }
                    });
                }
                if(photos.length > 1){
                    holder.innerAnwserImage1.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage(photos[1], holder.innerAnwserImage1);
                    holder.innerAnwserImage1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(QuestionDetailActivity.this, ViewPagerActivity.class);
                            intent.putExtra("imglist", photos);
                            intent.putExtra("curindex", 1);
                            Utils.start_Activity(QuestionDetailActivity.this, intent);
                        }
                    });
                }
                if(photos.length > 2){
                    holder.innerAnwserImage2.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage(photos[2], holder.innerAnwserImage2);
                    holder.innerAnwserImage2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(QuestionDetailActivity.this, ViewPagerActivity.class);
                            intent.putExtra("imglist", photos);
                            intent.putExtra("curindex", 2);
                            Utils.start_Activity(QuestionDetailActivity.this, intent);
                        }
                    });
                }
                if(photos.length > 3){
                    holder.innerAnwserImage3.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage(photos[3], holder.innerAnwserImage3);
                    holder.innerAnwserImage3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(QuestionDetailActivity.this, ViewPagerActivity.class);
                            intent.putExtra("imglist", photos);
                            intent.putExtra("curindex", 3);
                            Utils.start_Activity(QuestionDetailActivity.this, intent);
                        }
                    });
                }
                if(photos.length > 4){
                    holder.layoutAnwserImg1.setVisibility(View.VISIBLE);
                    holder.innerAnwserImage4.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage(photos[4], holder.innerAnwserImage4);
                    holder.innerAnwserImage4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(QuestionDetailActivity.this, ViewPagerActivity.class);
                            intent.putExtra("imglist", photos);
                            intent.putExtra("curindex", 4);
                            Utils.start_Activity(QuestionDetailActivity.this, intent);
                        }
                    });
                }
                if(photos.length > 5){
                    holder.innerAnwserImage5.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage(photos[5], holder.innerAnwserImage5);
                    holder.innerAnwserImage5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(QuestionDetailActivity.this, ViewPagerActivity.class);
                            intent.putExtra("imglist", photos);
                            intent.putExtra("curindex", 5);
                            Utils.start_Activity(QuestionDetailActivity.this, intent);
                        }
                    });
                }
                if(photos.length > 6){
                    holder.innerAnwserImage6.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage(photos[6], holder.innerAnwserImage6);
                    holder.innerAnwserImage6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(QuestionDetailActivity.this, ViewPagerActivity.class);
                            intent.putExtra("imglist", photos);
                            intent.putExtra("curindex", 6);
                            Utils.start_Activity(QuestionDetailActivity.this, intent);
                        }
                    });
                }
                if(photos.length > 7){
                    holder.innerAnwserImage7.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage(photos[7], holder.innerAnwserImage7);
                    holder.innerAnwserImage7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(QuestionDetailActivity.this, ViewPagerActivity.class);
                            intent.putExtra("imglist", photos);
                            intent.putExtra("curindex", 7);
                            Utils.start_Activity(QuestionDetailActivity.this, intent);
                        }
                    });
                }
            }

//            mImageLoader.displayImage("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg", holder.innerAnwserImage1);
//            mImageLoader.displayImage("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg", holder.innerAnwserImage2);
//            mImageLoader.displayImage("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg", holder.innerAnwserImage3);
//            mImageLoader.displayImage("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg", holder.innerAnwserImage4);
//            mImageLoader.displayImage("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg", holder.innerAnwserImage5);
//            mImageLoader.displayImage("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg", holder.innerAnwserImage6);
//            mImageLoader.displayImage("http://img2.imgtn.bdimg.com/it/u=2903830974,1746361104&fm=21&gp=0.jpg", holder.innerAnwserImage7);

//            holder.innerCatagrory.setText(content.getQtCatagrory());
//            holder.innerDate.setText(content.getQtDateTime());
//            holder.innerIntro.setText(content.getQtTitle());
//            holder.innerAnwserNum.setText(getString(R.string.txt_anwser_num, content.getAnswerCount() == null ? "0" : content.getAnswerCount()));
//            holder.innerBtnDetail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ToastUtil.showToast(MyQuestionActivity.this,"button");
//                }
//            });

            return convertView;
        }

        private class AnwserViewHolder{
            private ImageView innerImage;
            private TextView innerName;
            private TextView innerDuty;
            private TextView innerIntro;
            private TextView innerDate;
            private TextView innerZan;
            private ViewGroup layoutAnwserImg0;
            private ViewGroup layoutAnwserImg1;
            private ImageView innerAnwserImage0;
            private ImageView innerAnwserImage1;
            private ImageView innerAnwserImage2;
            private ImageView innerAnwserImage3;
            private ImageView innerAnwserImage4;
            private ImageView innerAnwserImage5;
            private ImageView innerAnwserImage6;
            private ImageView innerAnwserImage7;
        }
    }

    private class GalleryAdapter extends
            RecyclerView.Adapter<RecyclerView.ViewHolder>
    {

        private LayoutInflater mInflater;
        private String[] mDatas;

        public GalleryAdapter(Context context, String[] datats)
        {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        @Override
        public int getItemCount()
        {
            return mDatas.length;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            //inflate your layout and pass it to view holder
            View view = mInflater.inflate(R.layout.qa_detail_img_item,
                    viewGroup, false);
            ViewItemHolder viewHolder = new ViewItemHolder(view);

            ImageView innerImage = (ImageView)view.findViewById(R.id.inner_image);
            viewHolder.mInnerImage = innerImage;
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i)
        {
            if(viewHolder instanceof ViewItemHolder) {
                ViewItemHolder itemHolder = (ViewItemHolder)viewHolder;
                final String url = mDatas[i];
                mImageLoader.displayImage(url,itemHolder.mInnerImage);

                itemHolder.mInnerImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(QuestionDetailActivity.this,ViewPagerActivity.class);
                        intent.putExtra("imglist",mDatas);
                        intent.putExtra("curindex",i);
                        Utils.start_Activity(QuestionDetailActivity.this,intent);
                    }
                });
            }
        }

        private class ViewItemHolder extends RecyclerView.ViewHolder
        {
            public ViewItemHolder(View v)
            {
                super(v);
            }
            ImageView mInnerImage;
        }
    }
}
