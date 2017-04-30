package com.xnf.henghenghui.ui.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.HotArticleModel;
import com.xnf.henghenghui.model.HotSubjectModel;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.ToastUtil;

/**
 * Created by Administrator on 2016/9/28.
 */

public class HotArticleAdapter extends ListBaseAdapter<HotArticleModel> implements View.OnClickListener {

    protected ImageLoader mImageLoader = ImageLoader.getInstance();
    @Override
    public void onClick(View v) {
        TextView favText = (TextView)v;
        favText.setText(R.string.cancel_favorite);
//        favText.setText(R.string.add_favorite);
    }

    class ArticleViewHolder{
        private ImageView imageView;
        private TextView titleTextView;
        private TextView descTextView;
        private TextView time;
        private TextView praiseNum;
        private TextView commentNum;
        private TextView mFavoriteBtn;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ArticleViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ArticleViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hot_article,parent,false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.article_img);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.actilce_title);
            viewHolder.descTextView = (TextView) convertView.findViewById(R.id.acticle_desc);
            viewHolder.time = (TextView)convertView.findViewById(R.id.article_time);
            viewHolder.praiseNum =(TextView)convertView.findViewById(R.id.article_priase_num);
            viewHolder.commentNum =(TextView)convertView.findViewById(R.id.article_comment_num);
            viewHolder.mFavoriteBtn =(TextView)convertView.findViewById(R.id.article_favorite);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ArticleViewHolder)convertView.getTag();
        }
        HotArticleModel article = (HotArticleModel) mDatas.get(position);
        //TODO test
//        String path = "http://120.76.73.10:8080/HenghengServer/attached/image/20161022/20161022212316_807.png";
//        article.setImage(path);
        if (article.getImage() != null && !article.getImage().isEmpty()) {
            //mImageLoader.displayImage(article.getImage(), viewHolder.imageView);
            Picasso.with(mContext).load(article.getImage()).error(R.drawable.default_icon).
                    placeholder(R.drawable.default_icon).into(viewHolder.imageView);
        }  else {
            viewHolder.imageView.setImageResource(R.drawable.default_icon);
        }
        viewHolder.titleTextView.setText(article.getTitle());
        viewHolder.descTextView.setText(article.getDesc());
        viewHolder.time.setText(article.getTime());
        viewHolder.praiseNum.setText("赞("+article.getZuanNum()+")");
        viewHolder.commentNum.setText("评论("+article.getCommentNum()+")");
        viewHolder.mFavoriteBtn.setOnClickListener(this);

        return convertView;
    }
}

