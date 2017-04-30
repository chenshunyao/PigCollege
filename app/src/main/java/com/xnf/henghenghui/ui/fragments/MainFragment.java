
package com.xnf.henghenghui.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.PostRequest;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.BaseManager;
import com.xnf.henghenghui.logic.Face2FaceManager;
import com.xnf.henghenghui.logic.MainPageManager;
import com.xnf.henghenghui.model.BannerResponseModel;
import com.xnf.henghenghui.model.ExpertQsItemModel;
import com.xnf.henghenghui.model.F2FListResponse;
import com.xnf.henghenghui.model.HotArticleModel;
import com.xnf.henghenghui.model.HttpArtilcleListResponse;
import com.xnf.henghenghui.model.HttpMainPageF2FResponseModel;
import com.xnf.henghenghui.model.HttpMasterListResponse;
import com.xnf.henghenghui.model.MainBannerResponseModel;
import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.activities.ArticleDetailActivity;
import com.xnf.henghenghui.ui.activities.ArticleDetailActivity2;
import com.xnf.henghenghui.ui.activities.BaikeActivity;
import com.xnf.henghenghui.ui.activities.ChatMainListActivity;
import com.xnf.henghenghui.ui.activities.ExpertQSDetailActivity;
import com.xnf.henghenghui.ui.activities.ExpertQSDetailActivity2;
import com.xnf.henghenghui.ui.activities.F2FListActivity;
import com.xnf.henghenghui.ui.activities.HotArticleListActivity;
import com.xnf.henghenghui.ui.activities.HotSubjectActivity;
import com.xnf.henghenghui.ui.activities.MainActivity;
import com.xnf.henghenghui.ui.activities.MasterDetailActivity;
import com.xnf.henghenghui.ui.activities.MasterListActivity;
import com.xnf.henghenghui.ui.activities.MeetingActivity;
import com.xnf.henghenghui.ui.activities.QAActivity;
import com.xnf.henghenghui.ui.adapter.HotArticleAdapter;
import com.xnf.henghenghui.ui.base.BaseFragment;
import com.xnf.henghenghui.ui.utils.LocalUserInfo;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.ChildRecyclerView;
import com.xnf.henghenghui.ui.view.ChildViewPager;
import com.xnf.henghenghui.ui.view.HotImageView;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.CommonUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.xnf.henghenghui.util.CodeUtil.CmdCode.MsgTypeCode.MSG_GET_TOPIC_ARTICLE_LIST;

public class MainFragment extends BaseFragment implements OnClickListener, ViewPager.OnPageChangeListener, ChildViewPager.IOnSingleTouchListener, AdapterView.OnItemClickListener {

    private static final String TAG = "MeFragment";

    private ViewGroup layoutHengHengQA;
    private ViewGroup layoutHengHengMeeting;
    private ViewGroup layoutHengHengCourse;
    private ViewGroup layoutHengHengHot;
    private ViewGroup layoutHenghengBaike;

    private LinearLayout mF2fLoadMore;
    private LinearLayout mHotArticleListLoadMore;

    private ChildRecyclerView viewMasterHorizontal;
    private ViewGroup layoutRemen;

    private ChildViewPager hotPager;
    private CirclePageIndicator hotIndicator;
    private ViewPagerAdapter indexBannerAdapter;

    private ArrayList<ImageView> images = new ArrayList<ImageView>();
    ArrayList<HttpMasterListResponse.Content> mHotMasterList = new ArrayList<HttpMasterListResponse.Content>();

    private ArrayList<View> f2f_mainpage_list = new ArrayList<View>();
    private ArrayList<View> topic_mainpage_list = new ArrayList<View>();
    private ChildViewPager f2fExpertPager;
    private F2fViewPagerAdapter f2fExpertAdapter;
    private List<ExpertQsItemModel> mExpertQsList;

    private ChildViewPager f2fTopicPager;
    private F2fTopicPagerAdapter f2fTopicAdapter;

    private ArrayList<HotArticleModel> mArticleList;
    private HotArticleAdapter mArticleListAdapter;

    //private RelativeLayout mLoading_layout;
    private ListView mListView;

    private LayoutInflater mInflater;

    private TextView mHenghenghuiQA;

    public static MainFragment getInstance(Bundle bundle) {
        MainFragment settingFragment = new MainFragment();
        settingFragment.setArguments(bundle);
        return settingFragment;
    }

    @Override
    protected void setUIHandler() {
        super.setUIHandler();
        MainPageManager.setHandler(mHandler);
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
        if (getActivity() == null) {
            return false;
        }
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_HOT_MASTER: {
                if (getActivity() == null) {
                    return false;
                }
                String response = (String) msg.obj;
                L.d(TAG, "-------Response:--------" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    HttpMasterListResponse httpMasterListResponse = gson.fromJson(response, HttpMasterListResponse.class);

                    for (HttpMasterListResponse.Content c : httpMasterListResponse.getResponse().getContent()) {
                        mHotMasterList.add(c);
                    }
                    //Footer
                    mHotMasterList.add(httpMasterListResponse.new Content());
                    GalleryAdapter indexBannerAdapterMaster = new GalleryAdapter(getActivity(), mHotMasterList);
                    viewMasterHorizontal.setAdapter(indexBannerAdapterMaster);
                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_INDEX_BANNER: {
                String response = (String) msg.obj;
                L.d(TAG, "Response:" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    MainBannerResponseModel bannerResponseModel = gson.fromJson(response, MainBannerResponseModel.class);
                    List<MainBannerResponseModel.BannerContent> contents = bannerResponseModel.getReponse().getContents();
                    L.d(TAG, "contents:" + contents.size());
                    images.clear();
                    for (int i = 0; i < contents.size(); i++) {
                        MainBannerResponseModel.BannerContent content = contents.get(i);
                        ImageView img = new ImageView(getActivity());
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                        if(content.getBannerImgURL()==null || content.getBannerImgURL().isEmpty()){
                            img.setImageResource(R.drawable.banner_default);
                        }else{
                            Picasso.with(getActivity()).load(content.getBannerImgURL()).error(R.drawable.banner_default).placeholder(R.drawable.banner_default).into(img);
                        }

                        images.add(img);
                    }
                    indexBannerAdapter.notifyDataSetChanged();
                } else {

                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_F2F_LIST: {
                String response = (String) msg.obj;
                L.d(TAG, "Response:" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    final F2FListResponse f2FListResponse = gson.fromJson(response, F2FListResponse.class);
                    int size = f2FListResponse.getResponse().getContent().size();
                    if (size > 3) {
                        size = 3;
                    }
                    f2f_mainpage_list.clear();
                    for (int i = 0; i < size; i++) {
                        LayoutInflater flater = LayoutInflater.from(getActivity());
                        View f2fView = flater.inflate(R.layout.f2f_mainpage_item_layout, null);
                        ImageView img = (ImageView) f2fView.findViewById(R.id.img_hot);
                        TextView titile = (TextView) f2fView.findViewById(R.id.txt_expert_hot);
                        TextView desc = (TextView) f2fView.findViewById(R.id.txt_hot_desc);
                        titile.setText(f2FListResponse.getResponse().getContent().get(i).getQtTitle());
                        String num = f2FListResponse.getResponse().getContent().get(i).getAnswerExpertCount();
                        desc.setText("参与专家" + Html.fromHtml("<font color=#FF0000>" + num + "</font>") + "人");
                        final int positon = i;
                        //img.setImageResource(R.drawable.subject_cover_default);

                        ExpertQsItemModel qs = new ExpertQsItemModel();
                        qs.setExpertQsId(f2FListResponse.getResponse().getContent().get(i).getQtId());
                        qs.setQsDescription(f2FListResponse.getResponse().getContent().get(i).getQtTitle());
                        qs.setExpertQsNum(f2FListResponse.getResponse().getContent().get(i).getAnswerExpertCount());
                        qs.setExpertQsLastestTime(f2FListResponse.getResponse().getContent().get(i).getLastReplyTime());
                        mExpertQsList.add(qs);

                        f2fView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), ExpertQSDetailActivity2.class);

                                intent.putExtra("qtid", (f2FListResponse.getResponse().getContent().get(positon).getQtId()));
//                                intent.putExtra("questionId", (f2FListResponse.getResponse().getContent().get(positon).getQtId()));
//                                intent.putExtra("questionDes", (f2FListResponse.getResponse().getContent().get(positon).getQtTitle()));
//                                intent.putExtra("qsExpertNum", (f2FListResponse.getResponse().getContent().get(positon).getAnswerExpertCount()));
//                                intent.putExtra("qsLastestTime", (f2FListResponse.getResponse().getContent().get(positon).getLastReplyTime()));
                                startActivity(intent);
                            }
                        });
                        f2f_mainpage_list.add(f2fView);
                    }
                    f2fExpertAdapter.notifyDataSetChanged();
                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_HOTPT_HOTTP: {
                String response = (String) msg.obj;
                L.d(TAG, "MSG_GET_HOTPT_HOTTP Response:" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    final HttpMainPageF2FResponseModel f2FListResponse = gson.fromJson(response, HttpMainPageF2FResponseModel.class);
                    f2f_mainpage_list.clear();
                    topic_mainpage_list.clear();
                    for (int i = 0; i < f2FListResponse.getResponse().getContent().getQtInfo().size(); i++) {
                        LayoutInflater flater = LayoutInflater.from(getActivity());
                        View f2fView = flater.inflate(R.layout.f2f_mainpage_item_layout, null);
                        ImageView img = (ImageView) f2fView.findViewById(R.id.img_hot);
                        TextView titile = (TextView) f2fView.findViewById(R.id.txt_expert_hot);
                        TextView desc = (TextView) f2fView.findViewById(R.id.txt_hot_desc);
                        titile.setText(f2FListResponse.getResponse().getContent().getQtInfo().get(i).getTitle());
                        String num = f2FListResponse.getResponse().getContent().getQtInfo().get(i).getAnswerExpertCount();
                        desc.setText("参与专家" +Html.fromHtml("<font color=\"#ff0000\">"+num+"</font>人"));

                        if (f2FListResponse.getResponse().getContent().getQtInfo().get(i).getPhoto() != null &&
                                !f2FListResponse.getResponse().getContent().getQtInfo().get(i).getPhoto().isEmpty()) {
                            //mImageLoader.displayImage(article.getImage(), viewHolder.imageView);
                            Picasso.with(getActivity()).load(f2FListResponse.getResponse().getContent().getQtInfo().get(i).getPhoto()).error(R.drawable.p2).
                                    placeholder(R.drawable.p2).into(img);
                        }  else {
                            img.setImageResource(R.drawable.p2);
                        }

                        final int positon = i;
                        ExpertQsItemModel qs = new ExpertQsItemModel();
                        qs.setExpertQsId(f2FListResponse.getResponse().getContent().getQtInfo().get(i).getId());
                        qs.setQsDescription(f2FListResponse.getResponse().getContent().getQtInfo().get(i).getDesc());
                        qs.setExpertQsLastestTime((f2FListResponse.getResponse().getContent().getQtInfo().get(positon).getLastReplyTime()));
                        qs.setExpertQsNum((f2FListResponse.getResponse().getContent().getQtInfo().get(positon).getAnswerExpertCount()));
                        mExpertQsList.add(qs);
                        f2f_mainpage_list.add(f2fView);
                    }
                    f2fExpertAdapter.notifyDataSetChanged();
                }
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_TOPICLIST: {
                L.d(TAG, "MSG_GET_TOPICLIST");
                topic_mainpage_list.clear();
                for (int i = 0; i < 3; i++) {
                    LayoutInflater flater = LayoutInflater.from(getActivity());
                    View f2fView = flater.inflate(R.layout.f2f_mainpage_item_layout, null);
                    ImageView img = (ImageView) f2fView.findViewById(R.id.img_hot);
                    TextView titile = (TextView) f2fView.findViewById(R.id.txt_expert_hot);
                    TextView desc = (TextView) f2fView.findViewById(R.id.txt_hot_desc);
                    topic_mainpage_list.add(f2fView);
                }
                f2fTopicAdapter.notifyDataSetChanged();
                break;
            }
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_TOPIC_ARTICLE_LIST: {
                String response = (String) msg.obj;
                L.d(TAG, "Response:" + response);
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    HttpArtilcleListResponse httpArtilcleListResponse = gson.fromJson(response, HttpArtilcleListResponse.class);
                    ArrayList<HttpArtilcleListResponse.Content> datas = new ArrayList<HttpArtilcleListResponse.Content>();
                    datas = httpArtilcleListResponse.getResponse().getContent();
                    L.d(TAG, "data size:" + datas.size());
                    int size = 0;
                    if (datas.size() >= 4) {
                        size = 4;
                    } else {
                        size = datas.size();
                    }
                    L.d(TAG, "mArticleList size:" + mArticleList.size());
                    for (int i = 0; i < size; i++) {
                        HotArticleModel model = new HotArticleModel();
                        model.setArticleId(datas.get(i).getArtId());
                        model.setTitle(datas.get(i).getArtTitle());
                        model.setDesc(datas.get(i).getrtDesc());
                        model.setImage(datas.get(i).getArtPhoto());
                        model.setCommentNum(datas.get(i).getCommentCount());
                        model.setTime(datas.get(i).getArtDateTime());
                        //model.setmUrl(datas.get(i).);
                        model.setZuanNum(datas.get(i).getPraiseCount());
                        L.d(TAG, "datas.get(i).getCommentCount():" + datas.get(i).getCommentCount());
                        mArticleList.add(model);
                    }


                    for (int i = 0; i < size; i++) {
                        final int postion = i;
                        View convertView = mInflater.inflate(R.layout.list_item_hot_article, null);
                        //convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hot_article,parent,false);
                        ImageView imageView = (ImageView) convertView.findViewById(R.id.article_img);
                        TextView titleTextView = (TextView) convertView.findViewById(R.id.actilce_title);
                        TextView descTextView = (TextView) convertView.findViewById(R.id.acticle_desc);
                        TextView time = (TextView) convertView.findViewById(R.id.article_time);
                        TextView praiseNum = (TextView) convertView.findViewById(R.id.article_priase_num);
                        TextView commentNum = (TextView) convertView.findViewById(R.id.article_comment_num);
                        TextView mFavoriteBtn = (TextView) convertView.findViewById(R.id.article_favorite);

                        if (mArticleList.get(i).getImage() != null && !(mArticleList.get(i).getImage().isEmpty())) {
                            Picasso.with(getActivity()).load(mArticleList.get(i).getImage()).error(R.drawable.default_icon).
                                    placeholder(R.drawable.default_icon).into(imageView);
                        } else {
                            imageView.setImageResource(R.drawable.default_icon);
                        }
                        titleTextView.setText(mArticleList.get(i).getTitle());
                        descTextView.setText(mArticleList.get(i).getDesc());
                        time.setText(mArticleList.get(i).getTime());
                        praiseNum.setText("赞(" + mArticleList.get(i).getZuanNum() + ")");
                        commentNum.setText("评论(" + mArticleList.get(i).getCommentNum() + ")");
                        convertView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ArticleDetailActivity2.class);
                                intent.putExtra("ARTICLE_ID", mArticleList.get(postion).getArticleId());
                                Utils.start_Activity(getActivity(), intent);
                            }
                        });
                        layoutRemen.addView(convertView);
                    }

                    mArticleListAdapter.setData(mArticleList);
                } else {

                }
                break;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    protected String setFragmentTag() {
        return TAG;
    }

    @Override
    protected void initData(Bundle bundle) {
        mExpertQsList = new ArrayList<ExpertQsItemModel>();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container,
                            Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_main, container,
                false);
        mInflater = inflater;
        images.clear();

        hotPager = (ChildViewPager) view.findViewById(R.id.hot_pager);
        hotIndicator = (CirclePageIndicator) view.findViewById(R.id.hot_indicator);
        indexBannerAdapter = new ViewPagerAdapter();
        hotPager.setAdapter(indexBannerAdapter);
        hotIndicator.setViewPager(hotPager);
        hotIndicator.setOnPageChangeListener(this);
        hotPager.setOnSingleTouchListener(this);
        hotPager.setAutoRoll(true);

        f2fExpertPager = (ChildViewPager) view.findViewById(R.id.f2f_expert_hot_viewpager);
        f2fExpertAdapter = new F2fViewPagerAdapter();
        f2fExpertPager.setAdapter(f2fExpertAdapter);
        f2fExpertPager.setOnSingleTouchListener(this);
        f2fExpertPager.setAutoRoll(true);

        f2fTopicPager = (ChildViewPager) view.findViewById(R.id.f2f_topic_hot_viewpager);
        f2fTopicAdapter = new F2fTopicPagerAdapter();
        f2fTopicPager.setAdapter(f2fTopicAdapter);
        f2fTopicPager.setOnSingleTouchListener(this);
        f2fTopicPager.setAutoRoll(true);

        layoutHengHengQA = (ViewGroup) view.findViewById(R.id.layout_hengheng_qa);
        layoutHengHengCourse = (ViewGroup) view.findViewById(R.id.layout_hengheng_course);
        layoutHengHengMeeting = (ViewGroup) view.findViewById(R.id.layout_hengheng_meeting);
        layoutHengHengHot = (ViewGroup) view.findViewById(R.id.layout_hengheng_hot);
        layoutHenghengBaike = (ViewGroup) view.findViewById(R.id.layout_hengheng_baike);
        viewMasterHorizontal = (ChildRecyclerView) view.findViewById(R.id.view_master_horizontal);

        mF2fLoadMore = (LinearLayout) view.findViewById(R.id.mainpage_f2f_loadmore);
        mF2fLoadMore.setOnClickListener(this);

        mHotArticleListLoadMore = (LinearLayout) view.findViewById(R.id.mainpage_hotarticle_loadmore);
        mHotArticleListLoadMore.setOnClickListener(this);

        LinearLayoutManager managerMaster = new LinearLayoutManager(getActivity());
        managerMaster.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewMasterHorizontal.setLayoutManager(managerMaster);

        mHenghenghuiQA = (TextView)view.findViewById(R.id.txt_henghenghui_qa);
        if(LoginUserBean.getInstance().getLoginUserType()==LoginUserBean.EXPERT_USER){
            mHenghenghuiQA.setText(R.string.henghenghui_answer);
        }else{
            mHenghenghuiQA.setText(R.string.henghenghui_qa);
        }

        //获取首页广告
        MainPageManager.getIndexBanner(LoginUserBean.getInstance().getLoginUserid(), "index");
        //获取热门专家
        initHotMaster();
        //获取专家面对面推荐
        //MainPageManager.getF2fHotList(LoginUserBean.getInstance().getLoginUserid());
        //获取话题推荐
        //MainPageManager.getTopicHotList(LoginUserBean.getInstance().getLoginUserid());
        //获取热门文章
        MainPageManager.getTopicHotArticleList(LoginUserBean.getInstance().getLoginUserid());

        //TODO：暂时这样处理
        //Face2FaceManager.getF2FQsList(LoginUserBean.getInstance().getLoginUserid(), "QT00003", mHandler);
        MainPageManager.getTopicHotQtTpList(LoginUserBean.getInstance().getLoginUserid(), 3,3);

        mListView = (ListView) view.findViewById(R.id.listview);
        mArticleListAdapter = new HotArticleAdapter();
        mArticleList = new ArrayList<HotArticleModel>();
        // mArticleListAdapter.setData(mArticleList);
        mListView.setAdapter(mArticleListAdapter);
        //setListViewHeightBasedOnChildren(mListView);
        mListView.setOnItemClickListener(this);

        layoutRemen = (ViewGroup) view.findViewById(R.id.layout_remen);
        layoutHengHengQA.setOnClickListener(this);
        layoutHengHengMeeting.setOnClickListener(this);
        layoutHengHengCourse.setOnClickListener(this);
        layoutHenghengBaike.setOnClickListener(this);
        layoutHengHengHot.setOnClickListener(this);
        return view;
    }

    private void initHotMaster() {
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("expertsId", "");
            jsonObj.put("expertsName", "");
            jsonObj.put("company", "");
            jsonObj.put("titles", "");
            jsonObj.put("address", "");
            jsonObj.put("keyWord", "");
            jsonObj.put("areaInfo", "");
            jsonObj.put("category", "");
            jsonObj.put("isHot", "1");
            jsonObj.put("more", "8");
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
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_HOT_MASTER;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    public static String getRequestBody(String jsonString) {
        L.d("csy", "jsonString:" + jsonString);
        return "{" + "\"request\"" + ":" + jsonString + "}";
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter=new IntentFilter();
        filter.addAction(CommonUtil.UPDATE_AVATAR_ACTION);
        getActivity().registerReceiver(updateAvataReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(updateAvataReceiver);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_hengheng_qa: {
                if (LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.NORMAL_USER) {
                    Intent intent = new Intent(getActivity(), QAActivity.class);
                    Utils.start_Activity(getActivity(), intent);
                } else if (LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.EXPERT_USER) {
                    Intent intent = new Intent(getActivity(), ChatMainListActivity.class);
                    Utils.start_Activity(getActivity(), intent);
                }
            }

            break;
            case R.id.layout_hengheng_meeting: {
                Intent intent = new Intent(getActivity(), MeetingActivity.class);
                Utils.start_Activity(getActivity(), intent);
            }
            break;
            case R.id.layout_hengheng_baike: {
                Intent intent = new Intent(getActivity(), BaikeActivity.class);
                Utils.start_Activity(getActivity(), intent);
            }
            break;
            case R.id.layout_hengheng_hot: {
                Intent intent = new Intent(getActivity(), HotSubjectActivity.class);
                Utils.start_Activity(getActivity(), intent);
            }
            break;
            case R.id.layout_hengheng_course: {
                ((MainActivity) getActivity()).turnToPage("course");
            }
            break;
            case R.id.mainpage_f2f_loadmore: {
                ((MainActivity) getActivity()).turnToPage("hot");
            }
            break;
            case R.id.mainpage_hotarticle_loadmore: {
                Intent intent = new Intent(getActivity(), HotArticleListActivity.class);
                Utils.start_Activity(getActivity(), intent);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ArticleDetailActivity2.class);
        intent.putExtra("ARTICLE_ID", mArticleList.get(position).getArticleId());
        Utils.start_Activity(getActivity(), intent);
    }

    private class GalleryAdapter extends
            RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater mInflater;
        private ArrayList<HttpMasterListResponse.Content> mDatas;

        private static final int TYPE_FOOTER = 0;
        private static final int TYPE_ITEM = 1;

        public GalleryAdapter(Context context, ArrayList<HttpMasterListResponse.Content> datats) {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mDatas.size() - 1)
                return TYPE_FOOTER;

            return TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == TYPE_ITEM) {
                //inflate your layout and pass it to view holder
                View view = mInflater.inflate(R.layout.main_master_item2,
                        viewGroup, false);
                ViewItemHolder viewHolder = new ViewItemHolder(view);

                ViewGroup group = (ViewGroup) view.findViewById(R.id.master_layout);
                RoundImageView innerImage = (RoundImageView) view.findViewById(R.id.inner_image);
                innerImage.setType(RoundImageView.TYPE_CIRCLE);
                innerImage.dp2px(64);
                ImageView innerSign = (ImageView) view.findViewById(R.id.inner_sign);
                TextView innerName = (TextView) view.findViewById(R.id.inner_name);
                TextView innerDuty = (TextView) view.findViewById(R.id.inner_duty);
                viewHolder.mViewGroup = group;
                viewHolder.mInnerImage = innerImage;
                viewHolder.mInnerSign = innerSign;
                viewHolder.mInnerName = innerName;
                viewHolder.mInnerDuty = innerDuty;
                return viewHolder;
            } else if (i == TYPE_FOOTER) {
                View view = mInflater.inflate(R.layout.main_master_footer,
                        viewGroup, false);
                ViewFooterHolder viewHolder = new ViewFooterHolder(view);
                ImageView innerImage = (ImageView) view.findViewById(R.id.inner_image);
                Button innerButton = (Button) view.findViewById(R.id.inner_button);
                viewHolder.mInnerImage = innerImage;
                viewHolder.mInnerButton = innerButton;
                return viewHolder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
            if (viewHolder instanceof ViewItemHolder) {
                ViewItemHolder itemHolder = (ViewItemHolder) viewHolder;
                final HttpMasterListResponse.Content product = mDatas.get(i);
                if (product.getPhoto() == null || product.getPhoto().isEmpty()) {
                    itemHolder.mInnerImage.setImageResource(R.drawable.shouyi2);
                } else {
                    Picasso.with(getActivity()).load(product.getPhoto()).error(R.drawable.shouyi2).
                            placeholder(R.drawable.shouyi2).into(itemHolder.mInnerImage);
                }

                itemHolder.mInnerName.setText(product.getUserName());
                itemHolder.mInnerDuty.setText(product.getTitles());
                if ("1".equals(product.getIsCert())) {
                    itemHolder.mInnerSign.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.mInnerSign.setVisibility(View.GONE);
                }

                //如果设置了回调，则设置点击事件
                itemHolder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //toDetail();
                        //Toast.makeText(MainFragment.this.getActivity(),"i="+i,Toast.LENGTH_SHORT).show();
//                        String username = "10" + i;
//                        startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
                        Intent intent = new Intent(getActivity(), MasterDetailActivity.class);
                        intent.putExtra("masterid", product.getExpertId());
                        Utils.start_Activity(getActivity(), intent);
                    }
                });
            } else if (viewHolder instanceof ViewFooterHolder) {
                ViewFooterHolder footerHolder = (ViewFooterHolder) viewHolder;
                footerHolder.mInnerButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MasterListActivity.class);
                        Utils.start_Activity(getActivity(), intent);
                    }
                });
            }
        }

        private class ViewFooterHolder extends RecyclerView.ViewHolder {
            public ViewFooterHolder(View v) {
                super(v);
            }

            ImageView mInnerImage;
            Button mInnerButton;
        }

        private class ViewItemHolder extends RecyclerView.ViewHolder {
            public ViewItemHolder(View v) {
                super(v);
            }

            ViewGroup mViewGroup;
            ImageView mInnerImage;
            ImageView mInnerSign;
            TextView mInnerName;
            TextView mInnerDuty;
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {
    }

    @Override
    public void onPageScrolled(int position, float degree, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    private class ViewPagerAdapter extends PagerAdapter {

        // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
        @Override
        public int getCount() {
            return images.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(images.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(images.get(position));
            return images.get(position);
        }
    }

    private class F2fViewPagerAdapter extends PagerAdapter {

        // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
        @Override
        public int getCount() {
            return f2f_mainpage_list.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(f2f_mainpage_list.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(f2f_mainpage_list.get(position));
            return f2f_mainpage_list.get(position);
        }
    }

    private class F2fTopicPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topic_mainpage_list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(topic_mainpage_list.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(topic_mainpage_list.get(position));
            return topic_mainpage_list.get(position);
        }
    }


    @Override
    public void onSingleTouch(int id) {
        if (id == R.id.hot_pager) {
            int current = hotPager.getCurrentItem();
//            try{
//                BannerBean banner = bannerlist.get(current);
//                if("1".equals(banner.getBanner_url_type())){//内部
//                    toDetail(banner.getProd_id());
//                } else if("2".equals(banner.getBanner_url_type())){//外部
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    Uri content_url = Uri.parse(banner.getBanner_url());
//                    intent.setData(content_url);
//                    startActivity(intent);
//                }
//            }catch(Exception e){
//            }
        } else if (id == R.id.f2f_expert_hot_viewpager) {
            int current = f2fExpertPager.getCurrentItem();
            if(mExpertQsList == null || mExpertQsList.size() == 0){
                return;
            }
            Intent intent = new Intent();
            intent.setClass(getActivity(), ExpertQSDetailActivity2.class);
            intent.putExtra("qtid", (mExpertQsList.get(current).getExpertQsId()));
//            intent.putExtra("questionId",(mExpertQsList.get(current).getExpertQsId()));
//            intent.putExtra("questionDes",(mExpertQsList.get(current).getQsDescription()));
//            intent.putExtra("qsExpertNum",(mExpertQsList.get(current).getExpertQsNum()));
//            intent.putExtra("qsLastestTime",(mExpertQsList.get(current).getExpertQsLastestTime()));
            startActivity(intent);
        }
    }

    private BroadcastReceiver updateAvataReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent){
               if(mHotMasterList!=null&& mHotMasterList.size()>0){
                   for(int i=0;i<mHotMasterList.size();i++){
                       if(mHotMasterList.get(i).getExpertId()!=null && mHotMasterList.get(i).getExpertId().equals(LoginUserBean.getInstance().getLoginUserid())){
                          String photoPath =  intent.getStringExtra("PHOTO_PATH");
                           if(photoPath!=null){
                               mHotMasterList.get(i).setPhoto(Urls.SERVER_IP+photoPath);
                               viewMasterHorizontal.getAdapter().notifyDataSetChanged();
                               break;
                           }

                       }
                   }

               }
            }
    };
}
