package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.cityselection.CitySelectActivity;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpMasterListResponse;
import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MasterListActivity extends BaseActivity implements View.OnClickListener,ListView.OnItemClickListener {

    private ImageView mBackImg;
    private TextView mTitle;
    private ImageView mRightImg;
    private ListView mMasterList;
    private View mArea;
    private View mFenlei;
    private TextView mTextArea;
    private TextView mTextFenlei;
    private EditText mEditSearch;
    private View mBtnSearch;

    private ProgressDialog dialog;

    private String lastSearchTxt = "";
    private String mAreaCode = "";
    private String mAreaWord = "";
    private String mCategoryWord = "";
    private int mCategoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {
        initDialog();
    }

    private void initDialog(){
        dialog = new ProgressDialog(MasterListActivity.this);
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
        setContentView(R.layout.activity_master_list);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.hengheng_master_list_title);
        mRightImg = (ImageView)findViewById(R.id.img_right);
        mRightImg.setImageResource(R.drawable.title_dot_right);
        mRightImg.setVisibility(View.GONE);

//        List<UserInfo> list = new ArrayList<UserInfo>();
//        list.add(new UserInfo());
//        list.add(new UserInfo());
//        list.add(new UserInfo());
//        list.add(new UserInfo());
//        list.add(new UserInfo());
//        list.add(new UserInfo());
//        list.add(new UserInfo());
//        list.add(new UserInfo());
        mMasterList = (ListView)findViewById(R.id.master_list);
        mMasterList.setOnItemClickListener(this);
//        MasterAdapter adapter = new MasterAdapter(this,list);
//        mMasterList.setAdapter(adapter);
        mEditSearch = (EditText)findViewById(R.id.edit_search_master);
        mBtnSearch = findViewById(R.id.btn_search);
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String txt = mEditSearch.getText().toString().trim();
                    if(!txt.equals(lastSearchTxt)){
                        loadMaster(true);
                    }
                    return true;
                }
                return false;
            }
        });

        mArea = findViewById(R.id.layout_area);
        mFenlei = findViewById(R.id.layout_fenlei);

        mTextArea = (TextView)findViewById(R.id.text_area);
        mTextFenlei = (TextView)findViewById(R.id.text_fenlei);

        bindOnClickLister(this, mBackImg, mRightImg, mArea, mFenlei, mBtnSearch);

        loadMaster(false);
    }

    private void loadMaster(boolean sp){
        String txt = mEditSearch.getText().toString().trim();
        lastSearchTxt = txt;
        if(sp){
            if(dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("expertsId", "");
            jsonObj.put("expertsName", "");
            jsonObj.put("company", "");
            jsonObj.put("titles", "");
            jsonObj.put("address", "");
            jsonObj.put("isHot", "");
            jsonObj.put("keyWord", txt);
            jsonObj.put("areaInfo", mAreaCode);
            jsonObj.put("category", mCategoryWord);
            jsonObj.put("more", "");
            jsonObj.put("startRowNum", "");
            jsonObj.put("endRowNum", "");
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_EXPERTS_INFO_LIST)
                .tag(Urls.ACTION_EXPERTS_INFO_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_LIST_MASTER;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
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
            case R.id.layout_area: {
                Intent intent = new Intent();
                intent.setClass(this, CitySelectActivity.class);
                intent.putExtra("showAll",true);
                startActivityForResult(intent, Utils.CITY_REQUEST);
                break;
            }
            case R.id.layout_fenlei: {
                Intent intent = new Intent();
                intent.setClass(this, FenleiSelectActivity.class);
                intent.putExtra("FENLEI_ID",mCategoryId);
                startActivityForResult(intent, Utils.FENLEI_REQUEST);
                break;
            }
            case R.id.btn_search:{
                String txt = mEditSearch.getText().toString().trim();
                if(!txt.equals(lastSearchTxt)){
                    loadMaster(true);
                }
                break;
            }
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_LIST_MASTER: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    HttpMasterListResponse httpMasterListResponse = gson.fromJson(response, HttpMasterListResponse.class);
                    ArrayList<HttpMasterListResponse.Content> datas = new ArrayList<HttpMasterListResponse.Content>();
                    for(HttpMasterListResponse.Content c : httpMasterListResponse.getResponse().getContent()){
                        datas.add(c);
                    }
                    MasterAdapter adapter = new MasterAdapter(this,datas);
                    mMasterList.setAdapter(adapter);
                } else {
                    mMasterList.setAdapter(null);
                }
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.CITY_REQUEST:
                    String acode = data.getStringExtra("CITY_CODE");
                    String aword = data.getStringExtra("CITY_NAME");
                    if(!aword.equals(mAreaWord)){
                        if(aword.equals("全部城市")){
                            aword = "";
                            acode = "";
                            mTextArea.setText(R.string.all_city);
                        }else{
                            mTextArea.setText(aword);
                        }
                        mAreaCode = acode;
                        mAreaWord = aword;
                        loadMaster(true);
                    }
                    break;
                case Utils.FENLEI_REQUEST:
                    String cword = data.getStringExtra("FENLEI_NAME");
                    int cid = data.getIntExtra("FENLEI_ID", 0);
                    if(mCategoryId != cid){
                        if(cid == 0){
                            cword = "";
                            mTextFenlei.setText(R.string.all_fenlei);
                        }else{
                            mTextFenlei.setText(cword);
                        }
                        mCategoryId = cid;
                        mCategoryWord = cword;
                        loadMaster(true);
                    }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MasterAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<HttpMasterListResponse.Content> infoList;
        private MasterViewHolder holder;
        public MasterAdapter(Context context,List<HttpMasterListResponse.Content> infoList){
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
                convertView = inflater.inflate(R.layout.list_master_item, null);
                holder = new MasterViewHolder();
                holder.masterImg = (RoundImageView) convertView
                        .findViewById(R.id.inner_image);
                holder.signImg = (ImageView) convertView
                        .findViewById(R.id.inner_sign);
                holder.name = (TextView) convertView
                        .findViewById(R.id.inner_name);
                holder.duty = (TextView) convertView
                        .findViewById(R.id.inner_duty);
                holder.info = (TextView) convertView
                        .findViewById(R.id.inner_intro);
                holder.hot = (TextView) convertView
                        .findViewById(R.id.inner_hot);
                holder.company = (TextView) convertView
                        .findViewById(R.id.inner_company);
                convertView.setTag(holder);
            } else{
                holder = (MasterViewHolder) convertView.getTag();
            }

            final HttpMasterListResponse.Content product = infoList.get(position);
            if(product.getPhoto()==null || product.getPhoto().isEmpty()){
                holder.masterImg.setImageResource(R.drawable.shouyi2);
            }else{
                mImageLoader.displayImage(product.getPhoto(), holder.masterImg);
            }
            holder.name.setText(product.getUserName());
            holder.duty.setText(product.getTitles());
            holder.info.setText(product.getDesc());
            holder.hot.setText(getString(R.string.master_detail_hot,product.getHotValue()));
            holder.company.setText(product.getCompany());
            if("1".equals(product.getIsCert())) {
                holder.signImg.setVisibility(View.VISIBLE);
            }else{
                holder.signImg.setVisibility(View.GONE);
            }

            return convertView;
        }

        private class MasterViewHolder{
            RoundImageView  masterImg;
            private ImageView signImg;
            private TextView name;
            private TextView duty;
            private TextView info;
            private TextView hot;
            private TextView company;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, MasterDetailActivity.class);
        HttpMasterListResponse.Content product = (HttpMasterListResponse.Content)adapterView.getItemAtPosition(i);
        intent.putExtra("masterid",product.getExpertId());
        Utils.start_Activity(this, intent);
    }
}
