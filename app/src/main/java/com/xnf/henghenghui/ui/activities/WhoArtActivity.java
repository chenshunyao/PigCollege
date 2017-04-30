package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
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
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpMasterAnwserQAListResponse;
import com.xnf.henghenghui.model.HttpMasterArtListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WhoArtActivity extends BaseActivity implements View.OnClickListener,ListView.OnItemClickListener {

    private String mUserid;
    private String mUserName;

    private ImageView mBackImg;
    private TextView mTitle;
    private ImageView mRightImg;

    private ProgressDialog dialog;

    private ListView mArtList;

    private HttpMasterArtListResponse mHttpMasterArtListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_my_question);
    }

    @Override
    protected void initData() {
        initDialog();
    }

    private void initDialog(){
        dialog = new ProgressDialog(WhoArtActivity.this);
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
        setContentView(R.layout.activity_who_art);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);

        mUserid = getIntent().getStringExtra("userid");
        mUserName = getIntent().getStringExtra("username");

        mTitle.setText(getString(R.string.hengheng_who_art_title,mUserName));

        mRightImg = (ImageView)findViewById(R.id.img_right);
//        mRightImg.setImageResource(R.drawable.title_dot_right);
        mRightImg.setVisibility(View.GONE);

        mArtList = (ListView)findViewById(R.id.art_list);
        mArtList.setOnItemClickListener(this);

        bindOnClickLister(this, mBackImg);

        loadQuestion(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if(DATA_CHANGE) {
//            loadQuestion(true);
//        }
    }

    private void loadQuestion(boolean sp) {
        if (sp) {
            if (dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("topicId", "");
            jsonObj.put("expertId", mUserid);
            jsonObj.put("keyWord", "");
            jsonObj.put("commend", "");
            jsonObj.put("startRowNum", "");
            jsonObj.put("endRowNum", "");
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_SUBJECT_ARTICLE_LIST)
                .tag(Urls.ACTION_SUBJECT_ARTICLE_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_ARTICLE_LIST;
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_ARTICLE_LIST:{
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpMasterArtListResponse = gson.fromJson(response, HttpMasterArtListResponse.class);
                    ArrayList<HttpMasterArtListResponse.Content> datas = new ArrayList<HttpMasterArtListResponse.Content>();
                    for (HttpMasterArtListResponse.Content c : mHttpMasterArtListResponse.getResponse().getContent()) {
                        datas.add(c);
                    }
                    WhoArtAdapter adapter = new WhoArtAdapter(this, datas);
                    mArtList.setAdapter(adapter);
                } else {
                    mArtList.setAdapter(null);
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
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Intent intent = new Intent(this, QuestionDetailActivity.class);
//        HttpMasterAnwserQAListResponse.Content content = (HttpMasterAnwserQAListResponse.Content) adapterView.getItemAtPosition(i);
//        intent.putExtra("qtid", content.getQtId());
//        Utils.start_Activity(this, intent);
//        ToastUtil.showToast(MyQuestionActivity.this,"onItemClick");
        HttpMasterArtListResponse.Content content = (HttpMasterArtListResponse.Content) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(this, ArticleDetailActivity2.class);
        intent.putExtra("ARTICLE_ID",content.getArtId());
        Utils.start_Activity(this, intent);
    }

    private class WhoArtAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<HttpMasterArtListResponse.Content> infoList;
        private MasterViewHolder holder;
        public WhoArtAdapter(Context context,List<HttpMasterArtListResponse.Content> infoList){
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
                convertView = inflater.inflate(R.layout.list_art_item, null);
                holder = new MasterViewHolder();
                holder.innerArt = (TextView) convertView
                        .findViewById(R.id.master_art_item);
                convertView.setTag(holder);
            } else{
                holder = (MasterViewHolder) convertView.getTag();
            }

//            if(mType == TYPE_MY_QUESTION) {
                final HttpMasterArtListResponse.Content content = infoList.get(position);
                holder.innerArt.setText(content.getArtTitle());
//            }else if(mType == TYPE_MY_ANWSER_QUESTION) {
//                final HttpMasterAnwserQAListResponse.Content content = infoList.get(position);
//                holder.innerCatagrory.setText(content.getQtCatagroryName());
//                holder.innerDate.setText(content.getLastReplyTime());
//                holder.innerIntro.setText(content.getQtTitle());
//                holder.innerAnwserNum.setText(getString(R.string.txt_anwser_num, content.getAnswerExpertCount() == null ? "0" : content.getAnswerExpertCount()));
//                holder.innerBtnAnwser.setVisibility(View.VISIBLE);
//                holder.innerBtnDetail.setVisibility(View.GONE);
//                holder.innerBtnAnwser.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(WhoAnwserQuestionActivity.this, AnwserQuestionActivity.class);
//                        intent.putExtra("qtid",content.getQtId());
//                        Utils.start_Activity(WhoAnwserQuestionActivity.this, intent);
//                    }
//                });
//            }
            return convertView;
        }

        private class MasterViewHolder{
            private TextView innerArt;
        }
    }
}
