package com.xnf.henghenghui.ui.activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
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
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.ui.ConversationListFragment;
import com.hyphenate.EMMessageListener;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.cityselection.CitySelectActivity;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpMasterListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.adapter.ViewPageFragmentAdapter;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.base.BaseViewPagerFragment;
import com.xnf.henghenghui.ui.fragments.EmptyFragment;
import com.xnf.henghenghui.ui.fragments.F2FCatergoryFragment;
import com.xnf.henghenghui.ui.fragments.PhoneRecordFragment;
import com.xnf.henghenghui.ui.fragments.TopicsListFragment;
import com.xnf.henghenghui.ui.interf.OnTabReselectListener;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.EmptyLayout;
import com.xnf.henghenghui.ui.view.PagerSlidingTabStrip;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatMainListActivity extends BaseActivity implements View.OnClickListener, OnTabReselectListener {

    private ImageView mBackImg;
    private TextView mTitle;
    private ImageView mRightImg;
//    private ListView mMasterList;
//    private View mArea;
//    private View mFenlei;
//    private TextView mTextArea;
//    private TextView mTextFenlei;
//    private EditText mEditSearch;
//    private View mBtnSearch;
    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;

    private ProgressDialog dialog;

//    private String lastSearchTxt = "";
//    private String mAreaWord = "";
//    private String mCategoryWord = "";
//    private int mCategoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {
        initDialog();
    }

    private void initDialog(){
        dialog = new ProgressDialog(ChatMainListActivity.this);
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
        setContentView(R.layout.base_viewpage_activity);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.hengheng_chatmain_title);
        mRightImg = (ImageView)findViewById(R.id.img_right);
        mRightImg.setImageResource(R.drawable.title_dot_right);
        mRightImg.setVisibility(View.GONE);

        bindOnClickLister(this, mBackImg, mRightImg);

        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_tabstrip);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mErrorLayout = (EmptyLayout) findViewById(R.id.error_layout);

        mTabsAdapter = new ViewPageFragmentAdapter(getSupportFragmentManager(),
                mTabStrip, mViewPager);
        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);
    }

    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.chatmain_viewpage_arrays);
        //图文咨询
        adapter.addTab(title[0], "CHAT_FRAGMENT", ConversationListFragment.class,
                getBundle(0));
        //电话咨询
        adapter.addTab(title[1], "PHONE_FRAGMENT", PhoneRecordFragment.class,
                getBundle(1));
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseViewPagerFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }

    protected void setScreenPageLimit() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
//                if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                List<android.support.v4.app.Fragment> list = getSupportFragmentManager().getFragments();
                if(list != null) {
                    for (android.support.v4.app.Fragment f : list) {
                        if(f instanceof ConversationListFragment){
                            ((ConversationListFragment)f).refresh();
                        } else if(f instanceof PhoneRecordFragment){
                            ((PhoneRecordFragment)f).refresh();
                        }
                    }
                }
//                mTabsAdapter.notifyDataSetChanged();
//                if (currentTabIndex == 0) {
//                    // 当前页面如果为聊天历史页面，刷新此页面
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                }
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
//            case R.id.layout_area: {
//                Intent intent = new Intent();
//                intent.setClass(this, CitySelectActivity.class);
//                intent.putExtra("showAll",true);
//                startActivityForResult(intent, Utils.CITY_REQUEST);
//                break;
//            }
//            case R.id.layout_fenlei: {
//                Intent intent = new Intent();
//                intent.setClass(this, FenleiSelectActivity.class);
//                intent.putExtra("FENLEI_ID",mCategoryId);
//                startActivityForResult(intent, Utils.FENLEI_REQUEST);
//                break;
//            }
            default:
                break;
        }
        //onPrepareOptionsMenu(menu);
    }

    @Override
    public void onTabReselect() {

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
//            case CodeUtil.CmdCode.MsgTypeCode.MSG_LIST_MASTER: {
//                String response = (String) msg.obj;
//                if (Utils.getRequestStatus(response) == 1) {
//                    Gson gson = new Gson();
//                    HttpMasterListResponse httpMasterListResponse = gson.fromJson(response, HttpMasterListResponse.class);
//                    ArrayList<HttpMasterListResponse.Content> datas = new ArrayList<HttpMasterListResponse.Content>();
//                    for(HttpMasterListResponse.Content c : httpMasterListResponse.getResponse().getContent()){
//                        datas.add(c);
//                    }
//                    MasterAdapter adapter = new MasterAdapter(this,datas);
//                    mMasterList.setAdapter(adapter);
//                } else {
//                    mMasterList.setAdapter(null);
//                }
//                if(dialog != null){
//                    dialog.dismiss();
//                }
//            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case Utils.CITY_REQUEST:
//                    String aword = data.getStringExtra("CITY_NAME");
//                    if(!aword.equals(mAreaWord)){
//                        if(aword.equals("全部城市")){
//                            aword = "";
//                            mTextArea.setText(R.string.all_city);
//                        }else{
//                            mTextArea.setText(aword);
//                        }
//                        mAreaWord = aword;
//                        loadMaster(true);
//                    }
//                    break;
//                case Utils.FENLEI_REQUEST:
//                    String cword = data.getStringExtra("FENLEI_NAME");
//                    int cid = data.getIntExtra("FENLEI_ID", 0);
//                    if(mCategoryId != cid){
//                        if(cid == 0){
//                            mTextFenlei.setText(R.string.all_fenlei);
//                        }else{
//                            mTextFenlei.setText(cword);
//                        }
//                        mCategoryId = cid;
//                        mCategoryWord = cword;
//                        loadMaster(true);
//                    }
//                break;
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private class MasterAdapter extends BaseAdapter {
//        private Context context;
//        private LayoutInflater inflater;
//        private List<HttpMasterListResponse.Content> infoList;
//        private MasterViewHolder holder;
//        public MasterAdapter(Context context,List<HttpMasterListResponse.Content> infoList){
//            this.inflater = LayoutInflater.from(context);
//            this.infoList = infoList;
//            this.context = context;
//        }
//
//        @Override
//        public int getCount() {
//            return infoList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return infoList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if(convertView == null){
//                convertView = inflater.inflate(R.layout.list_master_item, null);
//                holder = new MasterViewHolder();
//                holder.masterImg = (ImageView) convertView
//                        .findViewById(R.id.inner_image);
//                holder.signImg = (ImageView) convertView
//                        .findViewById(R.id.inner_sign);
//                holder.name = (TextView) convertView
//                        .findViewById(R.id.inner_name);
//                holder.duty = (TextView) convertView
//                        .findViewById(R.id.inner_duty);
//                holder.info = (TextView) convertView
//                        .findViewById(R.id.inner_intro);
//                convertView.setTag(holder);
//            } else{
//                holder = (MasterViewHolder) convertView.getTag();
//            }
//
//            final HttpMasterListResponse.Content product = infoList.get(position);
//            //mImageLoader.displayImage(product.getPhoto(), holder.masterImg);
//            //TODO
//            holder.masterImg.setImageResource(R.drawable.shouyi2);
//            holder.name.setText(product.getUserName());
//            holder.duty.setText(product.getTitles());
//            holder.info.setText(product.getDesc());
//            if("1".equals(product.getIsCert())) {
//                holder.signImg.setVisibility(View.VISIBLE);
//            }else{
//                holder.signImg.setVisibility(View.GONE);
//            }
//
//            return convertView;
//        }
//
//        private class MasterViewHolder{
//            private ImageView masterImg;
//            private ImageView signImg;
//            private TextView name;
//            private TextView duty;
//            private TextView info;
//        }
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Intent intent = new Intent(this, MasterDetailActivity.class);
//        HttpMasterListResponse.Content product = (HttpMasterListResponse.Content)adapterView.getItemAtPosition(i);
//        intent.putExtra("masterid",product.getExpertId());
//        Utils.start_Activity(this, intent);
//    }
}
