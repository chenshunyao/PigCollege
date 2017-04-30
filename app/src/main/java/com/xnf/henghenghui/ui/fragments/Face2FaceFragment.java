
package com.xnf.henghenghui.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.HotArticleModel;
import com.xnf.henghenghui.ui.adapter.ViewPageFragmentAdapter;
import com.xnf.henghenghui.ui.base.BaseViewPagerFragment;
import com.xnf.henghenghui.ui.interf.OnTabReselectListener;

import java.util.ArrayList;
import java.util.List;

public class Face2FaceFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    private static final String TAG = "Face2FaceFragment";

    public static Face2FaceFragment getInstance(Bundle bundle) {
        Face2FaceFragment settingFragment = new Face2FaceFragment();
        settingFragment.setArguments(bundle);
        return settingFragment;
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.face2face_viewpage_arrays);
        //专家面对面
        adapter.addTab(title[0], "F2F_CATEGORY", F2FCatergoryFragment.class,
                getBundle(0));
        //互动话题
        adapter.addTab(title[1], "TOPICLIST_FRAGMENT", TopicsListFragment.class,
                getBundle(1));
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseViewPagerFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
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
    public void onTabReselect() {

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
