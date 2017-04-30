package com.xnf.henghenghui.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.MainPageManager;
import com.xnf.henghenghui.logic.SubjectManager;
import com.xnf.henghenghui.model.HotArticleModel;
import com.xnf.henghenghui.model.HttpHotKeyResponse;
import com.xnf.henghenghui.model.HttpQaListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.adapter.ViewPageFragmentAdapter;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.fragments.FavoriteListFragment;
import com.xnf.henghenghui.ui.fragments.QAListFragment;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.EmptyLayout;
import com.xnf.henghenghui.ui.view.PagerSlidingTabStrip;
import com.xnf.henghenghui.ui.view.togglebutton.Util;
import com.xnf.henghenghui.ui.widget.AutoLineGroup;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import java.util.ArrayList;

public class MyFavoriteActivity extends BaseActivity implements View.OnClickListener,FavoriteListFragment.OnFragmentInteractionListener, PagerSlidingTabStrip.OnPagerChangeLis {

    private ImageView mBackImg;
    private TextView mTitle;
    private TextView mRightTxt;

    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;

    private ProgressDialog dialog;

    private HttpQaListResponse mHttpQaListResponse;

    private static final String TYPE_ARTICLE = "AR";
    private static final String TYPE_VIDEO = "CV";
    private static final String TYPE_MEETING = "CF";
    private static final String TYPE_QUESTION = "AQ";
    private static final String TYPE_TOPIC  = "TP";
    private static final String TYPE_SUBJECT = "ST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        initDialog();
        MainPageManager.setHandler(mHandler);
    }

    private void initDialog(){
        dialog = new ProgressDialog(MyFavoriteActivity.this);
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
        setContentView(R.layout.activity_my_favorite);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.favorite_label);
        mRightTxt = (TextView)findViewById(R.id.txt_right);
        mRightTxt.setTextColor(getResources().getColor(R.color.main_green));
        mRightTxt.setVisibility(View.GONE);


        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_tabstrip);
        mTabStrip.setOnPagerChange(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(20);
        mViewPager.addOnPageChangeListener(new  ViewPager.OnPageChangeListener()  {
             @Override
             public  void  onPageScrolled(int  position,  float  positionOffset,  int  positionOffsetPixels)  {

                    }

             @Override
             public  void  onPageSelected(int  position)  {


                 //mArticleList = getHotNews();
                 //获取热门文章
                // MainPageManager.getTopicHotArticleList(LoginUserBean.getInstance().getLoginUserid());
                      //Toast.makeText(MyFavoriteActivity.this,"Position:"+position,Toast.LENGTH_SHORT).show();
                 }

             @Override
             public  void  onPageScrollStateChanged(int  state)  {
                  if  (state  ==  ViewPager.SCROLL_STATE_DRAGGING)  {
                       //正在滑动      pager处于正在拖拽中
                      }  else  if  (state  ==  ViewPager.SCROLL_STATE_SETTLING)  {
                       //pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
                      }  else  if  (state  ==  ViewPager.SCROLL_STATE_IDLE)  {
                       //空闲状态    pager处于空闲状态
                      }
                 }
        });
        mErrorLayout = (EmptyLayout) findViewById(R.id.error_layout);

        mTabsAdapter = new ViewPageFragmentAdapter(getSupportFragmentManager(),
                mTabStrip, mViewPager);
        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);

        bindOnClickLister(this, mBackImg, mRightTxt);

        //TODO
        loadFavoriteList(0,0,TYPE_ARTICLE);
        loadFavoriteList(0,0,TYPE_VIDEO);
        loadFavoriteList(0,0,TYPE_MEETING);
        //loadFavoriteList(0,0,TYPE_QUESTION);
        loadFavoriteList(0,0,TYPE_SUBJECT);
        //SubjectManager.getSubjectList(LoginUserBean.getInstance().getLoginUserid(),"","","","","");

        //获取热门文章
        //MainPageManager.getTopicHotArticleList(LoginUserBean.getInstance().getLoginUserid());
    }

    protected void setScreenPageLimit() {
    }

    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        //String[] titles = {"文章","视频","会议","问题","专题"};
        String[] titles = {"文章","视频","会议","专题"};
        int n = 0;
        for(String title : titles){
            adapter.addTab(title, "t"+n++, FavoriteListFragment.class,
                    new Bundle());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_right:
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_TOPIC_ARTICLE_LIST:
            {
                String response = (String) msg.obj;
                FavoriteListFragment fragment = (FavoriteListFragment)mTabsAdapter.instantiateItem(mViewPager,0);
                fragment.setData(MyFavoriteActivity.this,response,Utils.TYPE_ARTICLE);
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_ARTICLE: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    FavoriteListFragment fragment = (FavoriteListFragment)mTabsAdapter.instantiateItem(mViewPager,0);
                    fragment.setData(MyFavoriteActivity.this,response,Utils.TYPE_ARTICLE);

                } else {


                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_VIDEO: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    FavoriteListFragment fragment = (FavoriteListFragment)mTabsAdapter.instantiateItem(mViewPager,1);
                    fragment.setData(MyFavoriteActivity.this,response, Utils.TYPE_VIDEO);

                } else {


                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_MEETING: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    FavoriteListFragment fragment = (FavoriteListFragment)mTabsAdapter.instantiateItem(mViewPager,2);
                    fragment.setData(MyFavoriteActivity.this,response,TYPE_MEETING);

                } else {


                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_QUESTION: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    //FavoriteListFragment fragment = (FavoriteListFragment)mTabsAdapter.instantiateItem(mViewPager,3);
                    //fragment.setData(MyFavoriteActivity.this,response,Utils.TYPE_QUESTION);

                } else {


                }
                break;
            }
//            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_TOPIC: {
//                String response = (String) msg.obj;
//                if (Utils.getRequestStatus(response) == 1) {
//                    Gson gson = new Gson();
//
//                } else {
//                    FavoriteListFragment fragment = (FavoriteListFragment)mTabsAdapter.instantiateItem(mViewPager,4);
//                    fragment.setData(MyFavoriteActivity.this,null,Utils.TYPE_TOPIC);
//
//                }
//                break;
//            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_SUBJECT: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    FavoriteListFragment fragment = (FavoriteListFragment)mTabsAdapter.instantiateItem(mViewPager,3);
                    fragment.setData(MyFavoriteActivity.this,response,Utils.TYPE_SUBJECT);
                } else {

                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_SUBJECT_LIST: {
//                String response = (String) msg.obj;
//                if (Utils.getRequestStatus(response) == 1) {
//                    FavoriteListFragment fragment = (FavoriteListFragment)mTabsAdapter.instantiateItem(mViewPager,3);
//                    fragment.setData(MyFavoriteActivity.this,response,Utils.TYPE_SUBJECT);
//
//
//                } else {
//
//                }
                break;
            }
            default:
                break;
        }
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }

    private void loadFavoriteList(int startRow,int endRow ,final String Type){
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("startRowNum","");
            jsonObj.put("endRowNum","");
            // TODO 建议按照类别返回不同的收藏数据
            jsonObj.put("keyType",Type);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_GET_NEW_FAVORITELIST)
                .tag(Urls.ACTION_GET_NEW_FAVORITELIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        if(Type.equals(TYPE_ARTICLE)){
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_ARTICLE;
                        }else if(Type.equals(TYPE_VIDEO)){
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_VIDEO;
                        }else if(Type.equals(TYPE_MEETING)){
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_MEETING;
                        }else if(Type.equals(TYPE_QUESTION)){
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_QUESTION;
                        }else if(Type.equals(TYPE_TOPIC)){
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_TOPIC;
                        }else if(Type.equals(TYPE_SUBJECT)){
                            msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_GET_FAVORITELIST_SUBJECT;
                        }

                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    @Override
    public void onChanged(int page) {
        //Toast.makeText(this,"收藏与关注："+page,Toast.LENGTH_SHORT).show();
    }
}
