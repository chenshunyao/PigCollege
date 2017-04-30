
package com.xnf.henghenghui.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.HotArticleModel;
import com.xnf.henghenghui.ui.activities.SubjectActivity;
import com.xnf.henghenghui.ui.base.BaseFragment;
import com.xnf.henghenghui.ui.view.HotImageView;
import com.xnf.henghenghui.ui.view.HotViewPager;

import java.util.ArrayList;
import java.util.List;

public class HotFragment extends BaseFragment implements OnClickListener,AdapterView.OnItemClickListener,HotViewPager.BannerViewClick,HotViewPager.bannerDataCallback{

    private static final String TAG = "HotFragment";
    private RelativeLayout mLoading_layout;
    //HotPullToRefreshView replace ListView
    private ListView mListView;
    private HotViewPager mHvp;
    private ImageView mLoading_iv;
    private Context mContext;
    private ListView mLV = null;
    private List<HotImageView> mDatas;
    private List<HotArticleModel> mNews;
    private LinearLayout mPicNews;
    private TextView mPicTitle;
    private ImageView mPicImage1;
    private ImageView mPicImage2;
    private ImageView mPicImage3;
    private List<ImageView> views;
    private LinearLayout mLinearLayout;
    //假数据
    private int[] drawables = new int[]{R.drawable.ex1,R.drawable.ex2,R.drawable.ex3,R.drawable.ex4};
    private int[] picdrawables = new int[]{R.drawable.p1,R.drawable.p2,R.drawable.p3};
    public static HotFragment getInstance(Bundle bundle) {
        HotFragment settingFragment = new HotFragment();
        settingFragment.setArguments(bundle);
        return settingFragment;
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
        return false;
    }

    @Override
    protected String setFragmentTag() {
        return TAG;
    }

    @Override
    protected void initData(Bundle bundle) {
        //广告，文章及图片新闻数据信息
        //mDatas = getAdData();
        mNews = getHotNews();
        //此处可以根据需要自由设定，这里只是简单的测试
    }

    private void getPicNews() {
        mPicTitle.setText("元宵节到了，大家都去东湖赏花灯");
        mPicImage1.setImageResource(picdrawables[0]);
        mPicImage2.setImageResource(picdrawables[1]);
        mPicImage3.setImageResource(picdrawables[2]);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container,
            Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_hot, container,
                false);
        mHvp = (HotViewPager) view.findViewById(R.id.hotviewpager);
        mHvp.init(getContext());
        mHvp.setBaseAdapterData(mDatas, this);
        //mHvp.setSubOnClickListener(this);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.activity_layout);
        RelativeLayout sub1 = (RelativeLayout) view.findViewById(R.id.subject1);
        RelativeLayout sub2 = (RelativeLayout) view.findViewById(R.id.subject2);
        sub1.setOnClickListener(this);
        sub2.setOnClickListener(this);
        mLoading_layout = (RelativeLayout) view.findViewById(R.id.loading_layout);
        mLoading_iv = (ImageView) view.findViewById(R.id.loading_img);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setAdapter(new HotArticleAdapter(getContext(), mNews));
        setListViewHeightBasedOnChildren(mListView);
        mListView.setOnItemClickListener(this);
        mPicNews = (LinearLayout) view.findViewById(R.id.container);
        mPicNews.setOnClickListener(this);
        mPicTitle = (TextView) view.findViewById(R.id.pic_news_title);
        mPicImage1 = (ImageView) view.findViewById(R.id.pic_news_first);
        mPicImage2 = (ImageView) view.findViewById(R.id.pic_news_second);
        mPicImage3 = (ImageView) view.findViewById(R.id.pic_news_third);
//        getPicNews();
        return view;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.container:
                Log.d(TAG, "PicNews Click !");
                break;
            case R.id.subject1:
                Log.d(TAG,"onSubject1Click SubjectActivity");
                startActivity(new Intent(getActivity(), SubjectActivity.class));
                break;
            case R.id.subject2:
                startActivity(new Intent(getActivity(), SubjectActivity.class));
                break;
        }
    }

    @Override
    public void subViewClickListener(View paramView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    /**
     * 专题封面1的点击事件
     *
     */
    /*public void onSubject1Click(View paramView) {
        Log.d(TAG,"onSubject1Click SubjectActivity");
        startActivity(new Intent(getActivity(), SubjectActivity.class));
    }*/
    /**
     * 专题封面2的点击事件
     * @param
     */
    /*public void onSubject2Click(View paramView) {
        startActivity(new Intent(getActivity(), SubjectActivity.class));
    }*/

    /*private List<HotImageView> getAdData(){
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
    }*/

    private List<HotArticleModel> getHotNews(){
        List<HotArticleModel> list = new ArrayList<HotArticleModel>();
        for(int i=0;i<4;i++){
            HotArticleModel article = new HotArticleModel();
            article.setTitle("习近平节后首场调研");
            article.setDesc("中国人常说，“形势比人强”，办好事必须顺势而为。新闻报道一直与时间在赛跑，传播形势与格局的变迁，对岛叔这样的新闻人来说，更加敏感。");
            list.add(article);
        }
        return list;
    }

    private class HotArticleAdapter extends BaseAdapter{
        private Context mContext;
        private List<HotArticleModel> mLists;

        public HotArticleAdapter(Context context,List<HotArticleModel> datas){
            mContext = context;
            mLists = datas;
        }

        @Override
        public int getCount() {
            return mLists.size();
        }

        @Override
        public Object getItem(int position) {
            return mLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new MyViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_hot_article,parent,false);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.article_img);
                viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.actilce_title);
                viewHolder.descTextView = (TextView) convertView.findViewById(R.id.acticle_desc);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (MyViewHolder)convertView.getTag();
            }
            HotArticleModel article = mLists.get(position);
            viewHolder.imageView.setImageResource(R.drawable.news);
            viewHolder.titleTextView.setText(article.getTitle());
            viewHolder.descTextView.setText(article.getDesc());
            return convertView;
        }
    }

    class MyViewHolder{
        private ImageView imageView;
        private TextView titleTextView;
        private TextView descTextView;
    }
}
