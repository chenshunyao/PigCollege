package com.xnf.henghenghui.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.CourseModel;
import com.xnf.henghenghui.ui.activities.CourseDetailActivity;
import com.xnf.henghenghui.ui.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/3/3.
 */
public class MyGridView extends RelativeLayout implements AdapterView.OnItemClickListener{

    private TextView mTitle;
    private GridViewForScrollView mGrid;
    private LinearLayout mMoreLayout;
    private int mType;
    private List<CourseModel> mCourses;
    private MoreOnClickListener mMoreOnClickListener;
    private CourseOnItemClickListener mOnItemCourseListener;
    private MyGridAdpater mGridAdpater;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mImageOption;

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public interface  MoreOnClickListener{
        public void MoreOnClick(int type);
    }

    public interface CourseOnItemClickListener{
        public void OnItemClickCourse(int type,int position);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_course_area,this);
        mTitle = (TextView) view.findViewById(R.id.title_area);
        mGrid = (GridViewForScrollView) view.findViewById(R.id.free_course_gridview);
        mGrid.setOnItemClickListener(this);
        mMoreLayout = (LinearLayout) view.findViewById(R.id.more_layout);
        mMoreLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreOnClickListener.MoreOnClick(mType);
            }
        });
    }

    public void setTitleValue(){
        if(mTitle != null){
            if(mType == 1){
                mTitle.setText(getResources().getString(R.string.course_free));
            }else if(mType == 2){
                mTitle.setText(getResources().getString(R.string.course_hot));
            }else if(mType == 3){
                mTitle.setText(getResources().getString(R.string.course_priv));
            }
        }
    }

    public void setMoreOnClickListener(MoreOnClickListener mMoreOnClickListener) {
        this.mMoreOnClickListener = mMoreOnClickListener;
    }

    public void setmOnItemCourseListener(CourseOnItemClickListener mOnItemCourseListener) {
        this.mOnItemCourseListener = mOnItemCourseListener;
    }
    /**
     * GridView item点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("csy","position:"+position);
        /*if(mCourses == null || mCourses.size() == 0)return;
        CourseModel course = mCourses.get(position);
        Intent intent = new Intent(getContext(), CourseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Course",course);
        intent.putExtras(bundle);
        Utils.start_Activity();*/
        mOnItemCourseListener.OnItemClickCourse(mType,position);
    }
    /**
     * 填充数据
     * @param lists
     */
    public void setBaseAdapterData(List<CourseModel> lists,Context context,ImageLoader imageloader,DisplayImageOptions options) {
        if (lists == null || lists.size() == 0) {
            return;
        }
        mCourses = lists;
        mImageLoader = imageloader;
        mImageOption = options;
        mType = ((CourseModel)lists.get(0)).getcType();
        setTitleValue();
        Log.d("csy", "list size:" + lists.size());
        /*if(mType == 0){
            mGridAdpater = new MyGridAdpater(lists, context);
        }else if(mType == 2){
            mGridAdpater = new MyGridAdpater(lists, context);
        }*/
        mGrid.setAdapter(new MyGridAdpater(lists, context));
        //setListViewHeightBasedOnChildren(mGrid);
        //mGridAdpater.notifyDataSetChanged();
    }

    public GridView getGridView(){
        return mGrid;
    }

    class MyGridAdpater extends BaseAdapter{
        private List<CourseModel> lists;
        private LayoutInflater mLayoutInflater;
        private Context mContext;

        public MyGridAdpater(List<CourseModel> datas,Context context){
            lists = datas;
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }
        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder = null;
            if(convertView == null){
                mViewHolder = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.fragment_course_item1,null);
                mViewHolder.image = (ImageView) convertView.findViewById(R.id.course_img);
                mViewHolder.title = (TextView) convertView.findViewById(R.id.course_title);
                mViewHolder.palytime = (TextView) convertView.findViewById(R.id.play_time);
                mViewHolder.price = (TextView) convertView.findViewById(R.id.course_price);
                convertView.setTag(mViewHolder);
            }else{
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            CourseModel model = lists.get(position);
            //mViewHolder.image.setImageResource(R.drawable.p6);
            mViewHolder.title.setText(model.getcTitle());
            mViewHolder.palytime.setText(mContext.getString(R.string.course_play_count,model.getcPlayTime()));
            if(mType == 1){
                mViewHolder.price.setText(mContext.getString(R.string.course_free1));
            }else{
                mViewHolder.price.setText(mContext.getString(R.string.course_price,model.getcPrice()));
            }
            mImageLoader.displayImage(model.getcImageUrl(),mViewHolder.image,mImageOption);
            return convertView;
        }

        class ViewHolder{
            private ImageView image;
            private TextView title;
            private TextView palytime;
            private TextView price;
        }
    }
}
