
package com.xnf.henghenghui.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.viewpagerindicator.CirclePageIndicator;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.CourseManager;
import com.xnf.henghenghui.model.BannerResponseModel;
import com.xnf.henghenghui.model.CourseModel;
import com.xnf.henghenghui.model.VideoListResponseModel;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.activities.CourseDetailActivity;
import com.xnf.henghenghui.ui.activities.CourseListActivity;
import com.xnf.henghenghui.ui.base.BaseFragment;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.ChildRecyclerView;
import com.xnf.henghenghui.ui.view.ChildViewPager;
import com.xnf.henghenghui.ui.view.HotImageView;
import com.xnf.henghenghui.ui.view.HotViewPager;
import com.xnf.henghenghui.ui.view.MyGridView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.CommonUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends BaseFragment implements OnClickListener,ViewPager.OnPageChangeListener, ChildViewPager.IOnSingleTouchListener,MyGridView.MoreOnClickListener,MyGridView.CourseOnItemClickListener{

    private static final String TAG = "csy_CourseFragment";
    private HotViewPager mHvp;
    private MyGridView mFreeGridView;
    private ChildRecyclerView mHotGridView;
    private MyGridView mPrivGridView;
    //private Button mHotMoreBtn;
    private ArrayList<CourseModel> mFree;
    private ArrayList<CourseModel> mHots;
    private ArrayList<CourseModel> mDiscount;
    private List<HotImageView> mDatas;
    private int[] drawables = new int[]{R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p1};
    //热门推荐课程
    private static final String HOT_COURSE = "2";
    private static final String HOT_START = "1";
    private static final String HOT_END = "8";
    //免费学习课程
    private static final String FREE_COURSE = "1";
    private static final String FREE_START = "1";
    private static final String FREE_END = "6";
    //限时特惠学习课程
    private static final String DISCOUNT_COURSE = "3";
    private static final String DISCOUNT_START = "1";
    private static final String DISCOUNT_END = "4";
    private GalleryAdapter mAdapterMaster;

    private static final String ADD_FAV_OPT = "1";
    private static final String CANCEL_FAV_OPT = "0";

    private ChildViewPager hotPager;
    private CirclePageIndicator hotIndicator;
    private ViewPagerAdapter indexBannerAdapter;
    private String imageurls[] = new String[]{
            "http://www.zhu213.com:8080/HenghengServer/images/te1.png"
    };
    private ArrayList<ImageView> images = new ArrayList<ImageView>();

    public static CourseFragment getInstance(Bundle bundle) {
        CourseFragment settingFragment = new CourseFragment();
        settingFragment.setArguments(bundle);
        return settingFragment;
    }

    @Override
    protected void setUIHandler() {
        super.setUIHandler();
        CourseManager.setHandler(mHandler);

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
        switch(msg.what){
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_BANNER:
                String response = (String)msg.obj;
                L.e(TAG, "Reponse:" + response);
                if(Utils.getRequestStatus(response) == 1){
                    L.d(TAG, "Reponse banner success");
                    Gson gson = new Gson();
                    BannerResponseModel responseModel = gson.fromJson(response,BannerResponseModel.class);
                    //L.d(TAG, "banner size:"+responseModel.getReponse().getContents().size());
                    List<BannerResponseModel.BannerContent> contents = responseModel.getReponse().getContents();
                    mDatas = new ArrayList<HotImageView>();
                    for(int i=0;i<contents.size();i++){
                        BannerResponseModel.BannerContent content = contents.get(i);
                        ImageView img = new ImageView(getActivity());
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                        //将数据填充到datas中
                        mImageLoader.displayImage(content.getBannerImgURL(), img);
                        images.add(img);
                    }
                    indexBannerAdapter.notifyDataSetChanged();
                }else{
                    L.d(TAG, "Reponse banner fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_HOTVIDEO_LIST:
                String resp  =(String)msg.obj;
                L.d(TAG, "hot Response:" + resp);
                if(Utils.getRequestStatus(resp) == 1){
                    L.d(TAG,"resp videolist success");
                    Gson gson = new Gson();
                    VideoListResponseModel videoListModel = gson.fromJson(resp,VideoListResponseModel.class);
                    List<VideoListResponseModel.VideoContent> contents = videoListModel.getReponse().getContent();
                    L.d("csy","videolist contents size:"+contents.size());
                    mHots = new ArrayList<CourseModel>();
                    for(int i=0;i<contents.size();i++){
                        VideoListResponseModel.VideoContent content = contents.get(i);
                        CourseModel course = new CourseModel();
                        course.setcVid(content.getvId());
                        course.setcType(2);
                        course.setcTitle(content.getVideoTitle());
                        course.setcPlayTime(Integer.valueOf(content.getPlayCount()));
                        course.setcPrice(Float.valueOf(content.getPrice()));
                        course.setcImageUrl(content.getThumbnailURL());
                        mHots.add(course);
                    }
                    if(mHots.size() > 0) mHots.add(new CourseModel());
                    if(mHots != null && mHots.size() > 0){
                        L.d(TAG,"mHots size:"+mHots.size());
                        if(getActivity() == null){
                            break;
                        }
                        mAdapterMaster = new GalleryAdapter(getActivity().getBaseContext(), mHots);
                        mHotGridView.setAdapter(mAdapterMaster);
                    }
                }else{
                    L.d(TAG,"resp videolist fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_FREEVIDEO_LIST:
                String resp1  =(String)msg.obj;
                L.d(TAG,"free Response:"+resp1);
                if(Utils.getRequestStatus(resp1) == 1){
                    L.d(TAG,"resp videolist success");
                    Gson gson = new Gson();
                    VideoListResponseModel videoListModel = gson.fromJson(resp1,VideoListResponseModel.class);
                    List<VideoListResponseModel.VideoContent> contents = videoListModel.getReponse().getContent();
                    L.d("csy","videolist contents size:"+contents.size());
                    mFree = new ArrayList<CourseModel>();
                    for(int i=0;i<contents.size();i++){
                        VideoListResponseModel.VideoContent content = contents.get(i);
                        CourseModel course = new CourseModel();
                        course.setcVid(content.getvId());
                        course.setcType(1);
                        course.setcTitle(content.getVideoTitle());
                        course.setcPlayTime(Integer.valueOf(content.getPlayCount()));
                        String price = content.getPrice();
                        L.d("csy", "@price:" + price);
                        if(price == null || price.equals("null") || price.equals("")){
                            course.setcPrice(0.0f);
                        }else{
                            L.d("csy","2 price:"+price);
                            course.setcPrice(Float.valueOf(price));
                        }
                        course.setcImageUrl(content.getThumbnailURL());
                        mFree.add(course);
                    }
                    if(mFree != null && mFree.size() > 0){
                        mFreeGridView.setBaseAdapterData(mFree, getContext(),mImageLoader,mOptions);
                    }
                }else{
                    L.d(TAG,"resp videolist fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_DISCOUNTVIDEO_LIST:
                String resp2  =(String)msg.obj;
                L.d(TAG,"discount Response:"+resp2);
                if(Utils.getRequestStatus(resp2) == 1){
                    L.d(TAG,"resp videolist success");
                    Gson gson = new Gson();
                    VideoListResponseModel videoListModel = gson.fromJson(resp2,VideoListResponseModel.class);
                    List<VideoListResponseModel.VideoContent> contents = videoListModel.getReponse().getContent();
                    L.d("csy","videolist contents size:"+contents.size());
                    mDiscount = new ArrayList<CourseModel>();
                    for(int i=0;i<contents.size();i++){
                        VideoListResponseModel.VideoContent content = contents.get(i);
                        CourseModel course = new CourseModel();
                        course.setcVid(content.getvId());
                        course.setcType(3);
                        course.setcTitle(content.getVideoTitle());
                        course.setcPlayTime(Integer.valueOf(content.getPlayCount()));
                        course.setcPrice(Float.valueOf(content.getPrice()));
                        course.setcImageUrl(content.getThumbnailURL());
                        mDiscount.add(course);
                    }
                    if(mDiscount != null && mDiscount.size() > 0){
                        mPrivGridView.setBaseAdapterData(mDiscount,getContext(),mImageLoader,mOptions);
                    }
                }else{
                    L.d(TAG,"resp videolist fail");
                }
                break;
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
        //mDatas = getAdData();
        //异步获取课程信息
        //mFree = getCourseData();
        //mHots = getCourseHots();
        //mDiscount = getCourseDiscount();
        L.d("csy", "mHandler:" + mHandler);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container,
            Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_course, container,
                false);
        /*mHvp = (HotViewPager) view.findViewById(R.id.course_viewpager);
        mHvp.init(getContext());*/
        images.clear();
        /*for(String url : imageurls){
            ImageView img = new ImageView(getActivity());
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setImageResource(R.drawable.banaer_test);
            images.add(img);
        }*/
        hotPager = (ChildViewPager) view.findViewById(R.id.course_ad_pager);
        hotIndicator = (CirclePageIndicator) view.findViewById(R.id.course_ad_indicator);
        indexBannerAdapter = new ViewPagerAdapter();
        hotPager.setAdapter(indexBannerAdapter);
        hotIndicator.setViewPager(hotPager);
        hotIndicator.setOnPageChangeListener(this);
        hotPager.setOnSingleTouchListener(this);
        hotPager.setAutoRoll(true);
        //mHvp.setBaseAdapterData(mDatas, this);
        //mHvp.setVisibleLinear(false);
        mFreeGridView = (MyGridView) view.findViewById(R.id.free_course);
        //mFreeGridView.setBaseAdapterData(mFree, getContext());
        mFreeGridView.setMoreOnClickListener(this);
        mFreeGridView.setmOnItemCourseListener(this);
        //setListViewHeightBasedOnChildren(mFreeGridView.getGridView());
        mHotGridView = (ChildRecyclerView) view.findViewById(R.id.hot_course_horizontal);
        LinearLayoutManager managerMaster = new LinearLayoutManager(getActivity());
        managerMaster.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHotGridView.setLayoutManager(managerMaster);
        initHotCourse();
        //GalleryAdapter adapterMaster = new GalleryAdapter(getActivity(), mHots);
        //mHotGridView.setAdapter(adapterMaster);
        int spacingInPixels = getResources().getInteger(R.integer.space);
        Log.d("csy", "initView spacingInPixels:" + spacingInPixels);
        mHotGridView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        mPrivGridView = (MyGridView) view.findViewById(R.id.discount_course);
        //mPrivGridView.setBaseAdapterData(mDiscount,getContext());
        mPrivGridView.setMoreOnClickListener(this);
        mPrivGridView.setmOnItemCourseListener(this);
        //setListViewHeightBasedOnChildren(mPrivGridView.getGridView());
        return view;
    }

    private void initHotCourse(){
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("videoType",HOT_COURSE);
            jsonObj.put("startRowNum",HOT_START);
            jsonObj.put("endRowNum",HOT_END);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CLASS_VIDEO_LIST)
                .tag(Urls.ACTION_CLASS_VIDEO_LIST)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_HOTVIDEO_LIST;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    public static String getRequestBody(String jsonString){
        L.d("csy", "jsonString:" + jsonString);
        return "{"+"\"request\""+":" + jsonString + "}";
    }

    @Override
    public void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CourseManager.requestBanner(LoginUserBean.getInstance().getLoginUserid());
                //CourseManager.requestCourseList(LoginUserBean.getInstance().getLoginUserid(),HOT_COU,RSE HOT_START, HOT_END);
                CourseManager.requestCourseList(LoginUserBean.getInstance().getLoginUserid(), FREE_COURSE, FREE_START, FREE_END);
                CourseManager.requestCourseList(LoginUserBean.getInstance().getLoginUserid(),DISCOUNT_COURSE,DISCOUNT_START,DISCOUNT_END);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //热门推荐
            case R.id.hot_course_more:
                Intent intent = new Intent(getActivity(),CourseListActivity.class);
                intent.putExtra("type",2);
                Utils.start_Activity(getActivity(),intent);
                break;
            default:
                break;
        }
    }

    public void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(3, 3, 3, 3);
        // 设置参数
        listView.setLayoutParams(params);
    }

    /**
     * 获取轮播广告的数据
     * @return
     */
    private List<HotImageView> getAdData(){
        List<HotImageView> list = new ArrayList<HotImageView>();
        for(int i=0;i<4;i++){
            HotImageView image = new HotImageView(getContext());
            //设置默认图片
            image.setDefaultImageResId(drawables[i]);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setId(i);
            list.add(image);
        }
        return list;
    }

    /**
     * 获取免费课程的数据
     * @return
     */
    private List<CourseModel> getCourseData(){
        List<CourseModel> lists = new ArrayList<CourseModel>();
        /**
         * 模拟六条假的视频信息
         */
        for(int i= 0;i<6;i++){
            CourseModel course = new CourseModel();
            course.setcTitle(getResources().getString(R.string.course_title));
            course.setcPlayTime(500);
            course.setcType(1);
            course.setcPrice(0.0f);
            course.setcVid("XMjkyODg4NDI0");
            lists.add(course);
        }
        return lists;
    }

    /**
     * 获取热门推荐课程的数据
     * @return
     */
    private ArrayList<CourseModel> getCourseHots(){
        ArrayList<CourseModel> lists = new ArrayList<CourseModel>();
        for(int i=0;i<4;i++){
            CourseModel course = new CourseModel();
            course.setcTitle(getResources().getString(R.string.course_title));
            course.setcPlayTime(600);
            course.setcType(2);
            course.setcPrice(2.0f);
            course.setcVid("XMjkyODg4NDI0");
            lists.add(course);
        }
        return lists;
    }

    /**
     * 获取限时特惠课程的数据
     * @return
     */
    private ArrayList<CourseModel> getCourseDiscount(){
        ArrayList<CourseModel> lists = new ArrayList<CourseModel>();
        for(int i=0;i<4;i++){
            CourseModel course = new CourseModel();
            course.setcTitle(getResources().getString(R.string.course_title));
            course.setcPlayTime(800);
            course.setcType(3);
            course.setcPrice(1.0f);
            course.setcVid("XMjkyODg4NDI0");
            lists.add(course);
        }
        return lists;
    }

    /*@Override
    public void subViewClickListener(View paramView) {

    }*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSingleTouch(int id) {
        if(id == R.id.hot_pager){
            int current = hotPager.getCurrentItem();
        }
    }

    @Override
    public void MoreOnClick(int type) {
        Log.d("csy","moreOnClick type:"+type);
        if(type == 1){
            Intent intent = new Intent(getActivity(),CourseListActivity.class);
            intent.putExtra("type",1);
            Utils.start_Activity(getActivity(),intent);
        }else if(type == 3){
            Intent intent = new Intent(getActivity(),CourseListActivity.class);
            intent.putExtra("type",3);
            Utils.start_Activity(getActivity(),intent);
        }
    }

    @Override
    public void OnItemClickCourse(int type,int position) {
        ArrayList<CourseModel> cour;
        CourseModel course = null;
        Bundle bundle = new Bundle();
        if (type == 1 && mFree != null && mFree.size() > 0) {
            course = mFree.get(position);
            cour = mFree;
            //cour.remove(course);
            bundle.putParcelableArrayList("freecourse",cour);
        }else if(type == 2 && mHots != null && mHots.size() > 0){
            course = mHots.get(position);
            cour = mHots;
            //cour.remove(course);
            bundle.putParcelableArrayList("hotcourse",cour);
        }else if(type == 3 && mDiscount != null && mDiscount.size() > 0){
            course = mDiscount.get(position);
            cour = mDiscount;
            //cour.remove(course);
            bundle.putParcelableArrayList("discourse",cour);
        }
        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);

        Log.d("csy", "onitemclickcourse course:"+course);
        bundle.putSerializable("course", course);

        intent.putExtras(bundle);
        Utils.start_Activity(getActivity(),intent);
    }

    /**
     * 热门推荐数据适配器
     */
    private class GalleryAdapter extends
            RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater mLayoutInflater;
        private List<CourseModel> courses;
        private static final int TYPE_FOOTER = 0;
        private static final int TYPE_ITEM = 1;

        public GalleryAdapter(Context context,List<CourseModel> datas){
            L.d(TAG,"GalleryAdapter context:"+context);
            mLayoutInflater = LayoutInflater.from(context);
            courses = datas;
        }

        @Override
        public int getItemCount() {
            L.d(TAG,"@size:"+courses.size());
            return courses.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == courses.size() - 1) {
                L.d(TAG,"getItemType position:"+position);
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            if (i == TYPE_ITEM) {
                View view  = mLayoutInflater.inflate(R.layout.fragment_course_item,parent,false);
                ViewItemHolder viewholder = new ViewItemHolder(view);
                ViewGroup group = (ViewGroup)view.findViewById(R.id.course_item_layout);
                ImageView icon = (ImageView) view.findViewById(R.id.course_img);
                TextView title = (TextView) view.findViewById(R.id.course_title);
                TextView playtime = (TextView) view.findViewById(R.id.play_time);
                TextView price = (TextView) view.findViewById(R.id.course_price);
                viewholder.mViewGroup = group;
                viewholder.mCourseImage = icon;
                viewholder.mCourseTitle = title;
                viewholder.mPlayTime = playtime;
                viewholder.mCoursePrice = price;
                return viewholder;
            }else if(i == TYPE_FOOTER){
                View view = mLayoutInflater.inflate(R.layout.course_hot_footer,
                        parent, false);
                ViewFootHolder viewHolder = new ViewFootHolder(view);
                Button moreHotBtn = (Button)view.findViewById(R.id.hot_course_more);
                viewHolder.mMoreBtn = moreHotBtn;
                return viewHolder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
            final CourseModel course = courses.get(position);
            L.d(TAG,"@POSITION:"+position);
            if(viewHolder instanceof ViewItemHolder){
                ViewItemHolder itemHolder = (ViewItemHolder)viewHolder;
               // mImageLoader.displayImage(course.getcImageUrl(), itemHolder.mCourseImage);
                L.d(TAG, "onBindViewHolder title[" + position + "]:" + course.getcTitle());
                itemHolder.mCourseTitle.setText(course.getcTitle());
                itemHolder.mPlayTime.setText(getString(R.string.course_play_count,course.getcPlayTime()));
                itemHolder.mCoursePrice.setText(getString(R.string.course_price, course.getcPrice()));
                mImageLoader.displayImage(course.getcImageUrl(),itemHolder.mCourseImage,mOptions);
                itemHolder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                        Bundle bundle = new Bundle();
                        ArrayList<CourseModel> cour = mHots;
                        Log.d("csy", "onitemclickcourse course:" + course);
                        bundle.putSerializable("course", course);
                        bundle.putParcelableArrayList("hotcourse",cour);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }else if(viewHolder instanceof ViewFootHolder){
                L.d(TAG,"onBindViewHolder title["+position+"]:ViewFootHolder");
                ViewFootHolder footViewHolder = (ViewFootHolder)viewHolder;
                footViewHolder.mMoreBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),CourseListActivity.class);
                        intent.putExtra("type",Integer.valueOf(HOT_COURSE));
                        Utils.start_Activity(getActivity(),intent);
                    }
                });
            }

        }
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

    private class ViewItemHolder extends RecyclerView.ViewHolder
    {
        public ViewItemHolder(View v)
        {
            super(v);
        }
        ViewGroup mViewGroup;
        ImageView mCourseImage;
        TextView mCourseTitle;
        TextView mPlayTime;
        TextView mCoursePrice;
    }

    private class ViewFootHolder extends RecyclerView.ViewHolder{
        public ViewFootHolder(View v)
        {
            super(v);
        }
        Button mMoreBtn;
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.right = space;
        }
    }


}
