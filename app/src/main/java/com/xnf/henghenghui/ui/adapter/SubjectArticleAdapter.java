package com.xnf.henghenghui.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.HotSubjectModel;

/**
 * Created by Administrator on 2016/9/28.
 */

public class SubjectArticleAdapter extends ListBaseAdapter<HotSubjectModel> {
    private Context mContext;

    class SubjectViewHolder{
        private ImageView imageView;
        private TextView titleTextView;
        private TextView descTextView;
        private TextView time;
        private TextView praiseNum;
        private TextView mFavoriteBtn;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        SubjectViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new SubjectViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hot_subject,parent,false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.article_img);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.actilce_title);
            viewHolder.descTextView = (TextView) convertView.findViewById(R.id.acticle_desc);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (SubjectViewHolder)convertView.getTag();
        }
        HotSubjectModel article = (HotSubjectModel) mDatas.get(position);
        viewHolder.imageView.setImageResource(R.drawable.news);
        viewHolder.titleTextView.setText(article.getTitle());
        viewHolder.descTextView.setText(article.getDesc());
        return convertView;
    }
}

