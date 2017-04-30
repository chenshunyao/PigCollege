package com.xnf.henghenghui.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.HotSubjectModel;
import com.xnf.henghenghui.util.L;

/**
 * Created by Administrator on 2016/9/28.
 */

public class HotSubjectAdapter extends ListBaseAdapter<HotSubjectModel> {
    private Context mContext;
    protected ImageLoader mImageLoader = ImageLoader.getInstance();

    class SubjectViewHolder {
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
        if (convertView == null) {
            viewHolder = new SubjectViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hot_subject, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.article_img);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.actilce_title);
            viewHolder.descTextView = (TextView) convertView.findViewById(R.id.acticle_desc);
            viewHolder.time = (TextView) convertView.findViewById(R.id.subject_time);
            viewHolder.praiseNum = (TextView) convertView.findViewById(R.id.subjiect_priase_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SubjectViewHolder) convertView.getTag();
        }
        HotSubjectModel subject = (HotSubjectModel) mDatas.get(position);
        //TODO test
//        String path = "http://120.76.73.10:8080/HenghengServer/attached/image/20161022/20161022212316_807.png";
//        subject.setImage(path);
        if (subject != null && subject.getImage() != null) {
            Picasso.with(mContext).load(subject.getImage()).error(R.drawable.banner_default).
                    placeholder(R.drawable.banner_default).into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageResource(R.drawable.cover_default);
        }
        viewHolder.titleTextView.setText(subject.getTitle());
        viewHolder.descTextView.setText(subject.getDesc());
        viewHolder.time.setText(subject.getTime());
        viewHolder.praiseNum.setText("赞(" + subject.getZuanNum() + ")");
        return convertView;
    }
}

