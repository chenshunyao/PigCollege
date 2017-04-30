package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Message;
import android.os.Bundle;
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
import com.xnf.henghenghui.model.HttpMasterListResponse;
import com.xnf.henghenghui.model.HttpMasterQAListResponse;
import com.xnf.henghenghui.model.HttpUserQAListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.PreferencesWrapper;
import com.xnf.henghenghui.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyQuestionActivity extends BaseActivity implements View.OnClickListener,ListView.OnItemClickListener {

    public static boolean DATA_CHANGE = false;

    public static final int TYPE_MY_QUESTION = 0;
    public static final int TYPE_MY_ANWSER_QUESTION = 1;

    private ImageView mBackImg;
    private TextView mTitle;
    private ImageView mRightImg;

    private ProgressDialog dialog;

    private int mType = TYPE_MY_QUESTION;

    private ListView mQuestionList;

    private HttpUserQAListResponse mHttpUserQAListResponse;
    private HttpMasterQAListResponse mHttpMasterQAListResponse;

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
        dialog = new ProgressDialog(MyQuestionActivity.this);
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
        setContentView(R.layout.activity_my_question);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);

        mType = getIntent().getIntExtra("type",TYPE_MY_QUESTION);
        if(mType == TYPE_MY_QUESTION){
            mTitle.setText(R.string.hengheng_my_question_title);
        }
        if(mType == TYPE_MY_ANWSER_QUESTION){
            mTitle.setText(R.string.hengheng_my_anwser_question_title);
        }
        mRightImg = (ImageView)findViewById(R.id.img_right);
//        mRightImg.setImageResource(R.drawable.title_dot_right);
        mRightImg.setVisibility(View.GONE);

        mQuestionList = (ListView)findViewById(R.id.question_list);
        mQuestionList.setOnItemClickListener(this);

        bindOnClickLister(this, mBackImg);

        loadQuestion(true);

        String uid = LoginUserBean.getInstance().getLoginUserid();
        PreferencesWrapper.getInstance().setPreferenceIntValue("question"+uid, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(DATA_CHANGE) {
            loadQuestion(true);
        }
    }

    private void loadQuestion(boolean sp) {
        if (sp) {
            if (dialog != null) {
                dialog.show();
            }
        }
        if(mType == TYPE_MY_QUESTION){
            String userId = LoginUserBean.getInstance().getLoginUserid();
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("userId", userId);
                jsonObj.put("beginTime", "");
                jsonObj.put("endTime", "");
                jsonObj.put("qtCategoryId", "");
                jsonObj.put("qtUserId", userId);
                jsonObj.put("qtTitle", "");
                jsonObj.put("startRowNum", "");
                jsonObj.put("endRowNum", "");
            } catch (Exception e) {
            }
            String jsonString = jsonObj.toString();
            OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_USER_ASK_QUESTION)
                    .tag(Urls.ACTION_USER_ASK_QUESTION)
                    .postJson(getRequestBody(jsonString))
                    .execute(new MyJsonCallBack<String>() {
                        @Override
                        public void onResponse(String s) {
                            L.d(TAG, "onResponse:" + s);
                            Message msg = new Message();
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_USER_ASK_QUESTION;
                            msg.obj = s;
                            mHandler.sendMessage(msg);
                        }
                    });
        }else if(mType == TYPE_MY_ANWSER_QUESTION){
            String userId = LoginUserBean.getInstance().getLoginUserid();
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("userId", userId);
                jsonObj.put("qtCategoryId", "");
            } catch (Exception e) {
            }
            String jsonString = jsonObj.toString();
            OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_MY_ANSWER_QUESTION_LISY)
                    .tag(Urls.ACTION_MY_ANSWER_QUESTION_LISY)
                    .postJson(getRequestBody(jsonString))
                    .execute(new MyJsonCallBack<String>() {
                        @Override
                        public void onResponse(String s) {
                            L.d(TAG, "onResponse:" + s);
                            Message msg = new Message();
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_MASTER_ANSWER_QUESTION;
                            msg.obj = s;
                            mHandler.sendMessage(msg);
                        }
                    });
        }
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_USER_ASK_QUESTION: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpUserQAListResponse = gson.fromJson(response, HttpUserQAListResponse.class);
                    ArrayList<HttpUserQAListResponse.Content> datas = new ArrayList<HttpUserQAListResponse.Content>();
                    for(HttpUserQAListResponse.Content c : mHttpUserQAListResponse.getResponse().getContent()){
                        datas.add(c);
                    }
                    UserQuestionAdapter adapter = new UserQuestionAdapter(this, datas, null);
                    mQuestionList.setAdapter(adapter);
                } else {
                    mQuestionList.setAdapter(null);
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_MASTER_ANSWER_QUESTION: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpMasterQAListResponse = gson.fromJson(response, HttpMasterQAListResponse.class);
                    ArrayList<HttpMasterQAListResponse.Content> datas = new ArrayList<HttpMasterQAListResponse.Content>();
                    for (HttpMasterQAListResponse.Content c : mHttpMasterQAListResponse.getResponse().getContent()) {
                        datas.add(c);
                    }
                    UserQuestionAdapter adapter = new UserQuestionAdapter(this, null, datas);
                    mQuestionList.setAdapter(adapter);
                } else {
                    mQuestionList.setAdapter(null);
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
        Intent intent = new Intent(this, QuestionDetailActivity.class);
        if(mType == TYPE_MY_QUESTION) {
            HttpUserQAListResponse.Content content = (HttpUserQAListResponse.Content) adapterView.getItemAtPosition(i);
            intent.putExtra("qtid", content.getQtId());
        } else if(mType == TYPE_MY_ANWSER_QUESTION) {
            HttpMasterQAListResponse.Content content = (HttpMasterQAListResponse.Content) adapterView.getItemAtPosition(i);
            intent.putExtra("qtid", content.getQtId());
        }
        Utils.start_Activity(this, intent);
//        ToastUtil.showToast(MyQuestionActivity.this,"onItemClick");
    }

    private class UserQuestionAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<HttpUserQAListResponse.Content> infoUQAList;
        private List<HttpMasterQAListResponse.Content> infoMQAList;
        private MasterViewHolder holder;
        public UserQuestionAdapter(Context context,List<HttpUserQAListResponse.Content> infoUQAList,
                                   List<HttpMasterQAListResponse.Content> infoMQAList){
            this.inflater = LayoutInflater.from(context);
            this.infoUQAList = infoUQAList;
            this.infoMQAList = infoMQAList;
            this.context = context;
        }

        @Override
        public int getCount() {
            if(mType == TYPE_MY_QUESTION) {
                return infoUQAList.size();
            }else if(mType == TYPE_MY_ANWSER_QUESTION){
                return infoMQAList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(mType == TYPE_MY_QUESTION) {
                return infoUQAList.get(position);
            }else if(mType == TYPE_MY_ANWSER_QUESTION){
                return infoMQAList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(R.layout.list_question_item, null);
                holder = new MasterViewHolder();
                holder.innerCatagrory = (TextView) convertView
                        .findViewById(R.id.inner_catagrory);
                holder.innerDate = (TextView) convertView
                        .findViewById(R.id.inner_date);
                holder.innerIntro = (TextView) convertView
                        .findViewById(R.id.inner_intro);
                holder.innerAnwserNum = (TextView) convertView
                        .findViewById(R.id.inner_anwser_num);
                holder.innerBtnDetail = (TextView) convertView
                        .findViewById(R.id.inner_btn_detail);
                holder.innerBtnAnwser = (TextView) convertView
                        .findViewById(R.id.inner_btn_anwser);
                convertView.setTag(holder);
            } else{
                holder = (MasterViewHolder) convertView.getTag();
            }

            if(mType == TYPE_MY_QUESTION) {
                final HttpUserQAListResponse.Content content = infoUQAList.get(position);
                holder.innerCatagrory.setText(content.getQtCatagrory());
                holder.innerDate.setText(content.getQtDateTime());
                holder.innerIntro.setText(content.getQtTitle());
                holder.innerAnwserNum.setText(getString(R.string.txt_anwser_num, content.getAnswerCount() == null ? "0" : content.getAnswerCount()));
                holder.innerBtnAnwser.setVisibility(View.GONE);
                holder.innerBtnDetail.setVisibility(View.VISIBLE);
            }else if(mType == TYPE_MY_ANWSER_QUESTION) {
                final HttpMasterQAListResponse.Content content = infoMQAList.get(position);
                holder.innerCatagrory.setText(content.getQtCatagroryName());
                holder.innerDate.setText(content.getLastReplyTime());
                holder.innerIntro.setText(content.getQtTitle());
                holder.innerAnwserNum.setText(getString(R.string.txt_anwser_num, content.getAnswerExpertCount() == null ? "0" : content.getAnswerExpertCount()));
                holder.innerBtnAnwser.setVisibility(View.VISIBLE);
                holder.innerBtnDetail.setVisibility(View.GONE);
                holder.innerBtnAnwser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyQuestionActivity.this, AnwserQuestionActivity.class);
                        intent.putExtra("qtid",content.getQtId());
                        Utils.start_Activity(MyQuestionActivity.this, intent);
                    }
                });
            }
            return convertView;
        }

        private class MasterViewHolder{
            private TextView innerCatagrory;
            private TextView innerDate;
            private TextView innerIntro;
            private TextView innerAnwserNum;
            private TextView innerBtnDetail;
            private TextView innerBtnAnwser;
        }
    }
}
