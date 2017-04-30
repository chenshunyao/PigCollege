package com.xnf.henghenghui.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
import com.xnf.henghenghui.model.HttpQuestionCategoryResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.LocalUserInfo;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QATypeSelectActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackImg;
    private TextView mTitle;
    private ImageView mRightImg;
    private ListView mQATypeList;
    private QATypeAdapter mAdapter;

    private int currSelect = -1;

    private String qatype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        qatype = getIntent().getStringExtra("qatype");
    }

    @Override
    protected void initData() {
        requestCategory();
    }

    private void requestCategory(){
        String userId = LoginUserBean.getInstance().getLoginUserid();

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_QUESTION_CATEGORY)
                .tag(Urls.ACTION_QUESTION_CATEGORY)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_QUESTION_CATEGORY;
                        msg.obj=s;
                        mHandler.sendMessage(msg);
                    }
                });
    }



    @Override
    protected void initView() {
        setContentView(R.layout.activity_qa_type_selection);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.txt_qa_type);
        mRightImg = (ImageView)findViewById(R.id.img_right);
//        mRightImg.setImageResource(R.drawable.master_detail_right);
        mRightImg.setVisibility(View.GONE);

//        List<String> list = new ArrayList<String>();
//        list.add("种猪");
//        list.add("猪病");
//        list.add("育种");
//        list.add("管理");
//        list.add("营养");
//        list.add("监测");
//        list.add("其他");
        mQATypeList = (ListView)findViewById(R.id.qa_type_list);
//        mAdapter = new QATypeAdapter(this,list);
//        mQATypeList.setAdapter(mAdapter);
//        mQATypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                currSelect = i;
//                mAdapter.notifyDataSetChanged();
//            }
//        });

        bindOnClickLister(this, mBackImg, mRightImg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_right:
                ;
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

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_QUESTION_CATEGORY: {
                String response = (String)msg.obj;
                L.e(TAG, "Reponse:"+response);
                if(Utils.getRequestStatus(response) == 1){
                    Gson gson = new Gson();
                    HttpQuestionCategoryResponse httpQuestionCategoryResponse = gson.fromJson(response,HttpQuestionCategoryResponse.class);
                    mAdapter = new QATypeAdapter(this,httpQuestionCategoryResponse.getResponse().getContent());
                    mQATypeList.setAdapter(mAdapter);
                    mQATypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            currSelect = i;
                            mAdapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.putExtra("rsqatype", ((HttpQuestionCategoryResponse.Content)mAdapter.getItem(i)).getQtCategoryId());
                            intent.putExtra("rsqaname", ((HttpQuestionCategoryResponse.Content)mAdapter.getItem(i)).getQtCategoryName());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                    int n = 0;
                    for(HttpQuestionCategoryResponse.Content content : httpQuestionCategoryResponse.getResponse().getContent()){
                        if(content.getQtCategoryId().equals(qatype)) {
                            currSelect = n;
                            break;
                        }
                        n++;
                    }
                }
                else{

                }
            }
        }
        return false;
    }

    private class QATypeAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<HttpQuestionCategoryResponse.Content> infoList;
        private QATypeViewHolder holder;
        public QATypeAdapter(Context context, List<HttpQuestionCategoryResponse.Content> infoList){
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
                convertView = inflater.inflate(R.layout.list_qa_type_item, null);
                holder = new QATypeViewHolder();
                holder.name = (TextView) convertView
                        .findViewById(R.id.qa_type_name);
                convertView.setTag(holder);
            } else{
                holder = (QATypeViewHolder) convertView.getTag();
            }
            holder.name.setText(infoList.get(position).getQtCategoryName());
            holder.name.setTextColor(Color.BLACK);
            holder.name.setBackgroundResource(R.color.translucent_background);
            if(currSelect == position){
                holder.name.setTextColor(context.getResources().getColor(R.color.main_green));
                holder.name.setBackgroundResource(R.drawable.qa_type_selected);
            }

            return convertView;
        }

        private class QATypeViewHolder {
            private TextView name;
        }
    }
}
