package com.xnf.henghenghui.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.CourseManager;
import com.xnf.henghenghui.model.CourseModel;
import com.xnf.henghenghui.model.VideoListResponseModel;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.CustomListView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/11.
 */
public class CourseListActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private ImageView mBackImg;
    private TextView mTitle;
    private CustomListView mListView;
    private ArrayList<CourseModel> mLists;
    private int mType;
    private CourseListAdapter mAdapter;
    private static final int LOAD_DATA_FINISH = -1;
    private static final String VIDEO_LIST_START = "1";
    private static final String VIDEO_LIST_END = "6";

    @Override
    protected void initData() {
        mLists = new ArrayList<CourseModel>();
        mType = getIntent().getIntExtra("type",-1);
        Log.d("csy", "initData type:" + mType);
        CourseManager.setHandler(mHandler);
        //if(mType != 1){
        //    mLists = getCourseList(mType);
        //}
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_course_list);
        mBackImg =(ImageView)findViewById(R.id.img_back);
        mTitle =(TextView)findViewById(R.id.txt_title);
        if(mType == 1){
            mTitle.setText(R.string.course_free);
        }else if(mType == 2){
            mTitle.setText(R.string.course_hot);
        }else if(mType == 3){
            mTitle.setText(R.string.course_priv);
        }
        mListView = (CustomListView) findViewById(R.id.course_list);
        //不是免费课程则获取本地假数据
        /*if(mType != 1){
            mAdapter = new CourseListAdapter(getBaseContext(), mLists);
            mListView.setAdapter(mAdapter);
        }*/
        mListView.setonLoadListener(new CustomListView.OnLoadListener() {

            @Override
            public void onLoad() {
                //TODO 加载更多
                Log.e(TAG, "onLoad");
                loadData();
            }
        });
        mListView.setOnItemClickListener(this);
        //setListViewHeightBasedOnChildren(mListView);
        mBackImg.setVisibility(View.VISIBLE);

        bindOnClickLister(this, mBackImg);
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
    protected void onStart() {
        super.onStart();
        //免费课程
        //if(mType == 1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CourseManager.requestCourseList(LoginUserBean.getInstance().getLoginUserid() ,String.valueOf(mType), VIDEO_LIST_START, VIDEO_LIST_END);
                }
            }).start();
        //}
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what){
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_HOTVIDEO_LIST:
                String resp  =(String)msg.obj;
                L.d(TAG, "hot Response:" + resp+";mType:"+mType);
                if(Utils.getRequestStatus(resp) == 1){
                    L.d(TAG, "resp videolist success");
                    initialVideoList(resp,mType);
                }else{
                    L.d(TAG,"resp videolist fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_FREEVIDEO_LIST:
                String resp1  =(String)msg.obj;
                L.d(TAG,"free Response:"+resp1+";mType:"+mType);
                if(Utils.getRequestStatus(resp1) == 1){
                    L.d(TAG,"resp videolist success");
                    initialVideoList(resp1,mType);
                }else{
                    if(mLists != null && mLists.size() > 0 && mAdapter != null){
                        Toast.makeText(CourseListActivity.this,R.string.data_load_finish,Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(LOAD_DATA_FINISH);
                    }
                    L.d(TAG,"resp videolist fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_DISCOUNTVIDEO_LIST:
                String resp2  =(String)msg.obj;
                L.d(TAG,"discount Response:"+resp2+";mType:"+mType);
                if(Utils.getRequestStatus(resp2) == 1){
                    L.d(TAG,"resp videolist success");
                    initialVideoList(resp2,mType);
                }else{
                    L.d(TAG,"resp videolist fail");
                }
                break;
            case LOAD_DATA_FINISH:
                if(mAdapter!=null){
                    mAdapter.notifyDataSetChanged();
                }
                mListView.onLoadComplete();
                break;
            default:
                break;
        }
        return false;
    }

    public void loadData(){
        new Thread(){
            @Override
            public void run() {
                int count = mLists.size();
                //if(mType == 1){
                    CourseManager.requestCourseList(LoginUserBean.getInstance().getLoginUserid() ,String.valueOf(mType), String.valueOf(count + Integer.valueOf(VIDEO_LIST_START) + 1), String.valueOf(count+Integer.valueOf(VIDEO_LIST_END)));
                /*}else{
                    for(int i=count;i<count+10;i++){
                        CourseModel model = new CourseModel();
                        model.setcTitle("怎样饲养小猪1");
                        model.setcPrice(5.0f);
                        model.setcDuration(50);
                        model.setcPlayTime(2500);
                        model.setcZhan(100);
                        model.setcType(mType);
                        model.setcCommentCount(500);
                        model.setcVid("XMjkyODg4NDI0");
                        mLists.add(model);
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(LOAD_DATA_FINISH);
                }*/
            }
        }.start();
    }

    private void initialVideoList(String resp1,int type) {
        Gson gson = new Gson();
        VideoListResponseModel videoListModel = gson.fromJson(resp1,VideoListResponseModel.class);
        List<VideoListResponseModel.VideoContent> contents = videoListModel.getReponse().getContent();
        L.d("csy", "videolist contents size:" + contents.size());

        for(int i=0;i<contents.size();i++){
            VideoListResponseModel.VideoContent content = contents.get(i);
            CourseModel course = new CourseModel();
            course.setcVid(content.getvId());
            course.setcType(type);
            course.setcTitle(content.getVideoTitle());
            course.setcPlayTime(Integer.valueOf(content.getPlayCount()));
            if(content.getClassTimeLong() == null || "null".equalsIgnoreCase(content.getClassTimeLong())){
                course.setcDuration(0);
            }else{
                course.setcDuration(Long.valueOf(content.getClassTimeLong()));
            }
            String price = content.getPrice();
            if(price == null || price.equals("null") || price.equals("")){
                course.setcPrice(0.0f);
            }else{
                course.setcPrice(Float.valueOf(price));
            }
            course.setcImageUrl(content.getThumbnailURL());
            mLists.add(course);
        }
        if(mLists != null && mLists.size() > 0){
            if(mAdapter == null){
                mAdapter = new CourseListAdapter(getBaseContext(), mLists);
                mListView.setAdapter(mAdapter);
            }else{
                mHandler.sendEmptyMessage(LOAD_DATA_FINISH);
            }
        }
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

    private List<CourseModel> getCourseList(int type){
        List<CourseModel> courses = new ArrayList<CourseModel>();
        //异步地去服务器请求type对应的数据
        for(int i=0;i<5;i++){
            CourseModel model = new CourseModel();
            model.setcTitle("怎样饲养小猪");
            model.setcPrice(5.0f);
            model.setcDuration(50);
            model.setcPlayTime(2500);
            model.setcZhan(100);
            model.setcType(type);
            model.setcCommentCount(500);
            model.setcVid("XMjkyODg4NDI0");
            courses.add(model);
        }
        return courses;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("csy", "CourseListActivity position:" + position);
        CourseModel course = mLists.get(position);
        ArrayList<CourseModel> cour = mLists;
        Log.d("csy", "onitemclick course:" + course);
        Intent intent = new Intent(this, CourseDetailActivity.class);
        Bundle bundle = new Bundle();
        if(mType == 1){
            bundle.putParcelableArrayList("freecourse", cour);
        }else if(mType == 2){
            bundle.putParcelableArrayList("hotcourse", cour);
        }else if(mType == 3){
            bundle.putParcelableArrayList("discourse", cour);
        }
        bundle.putSerializable("course", course);
        intent.putExtras(bundle);
        Utils.start_Activity(this,intent);
    }

    private class CourseListAdapter extends BaseAdapter{
        private CourseModel mCourse;
        private LayoutInflater mLayoutInflater;
        private List<CourseModel> datas;
        private Context mContext;
        public CourseListAdapter(Context context,List<CourseModel> courses){
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            datas = courses;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CourseViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new CourseViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.list_course_item,parent,false);
                viewHolder.icon =(ImageView)convertView.findViewById(R.id.course_icon);
                viewHolder.title =(TextView)convertView.findViewById(R.id.course_title);
                viewHolder.price =(TextView)convertView.findViewById(R.id.course_price);
                viewHolder.duration =(TextView)convertView.findViewById(R.id.course_duration);
                viewHolder.time =(TextView)convertView.findViewById(R.id.course_time);
                viewHolder.zhan =(TextView)convertView.findViewById(R.id.course_zhan);
                viewHolder.comment =(TextView)convertView.findViewById(R.id.course_comment);
                viewHolder.linear =(LinearLayout)convertView.findViewById(R.id.view_detail);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (CourseViewHolder) convertView.getTag();
            }
            mCourse = datas.get(position);
            //final CourseModel course = mCourse;
            //viewHolder.linear.setOnClickListener(this);
            mImageLoader.displayImage(mCourse.getcImageUrl(), viewHolder.icon, mOptions);
            //viewHolder.icon.setImageResource(R.drawable.p6);
            viewHolder.title.setText(mCourse.getcTitle());
            if(mCourse.getcType() == 1){
                viewHolder.price.setText(getString(R.string.course_free1));
            }else{
                viewHolder.price.setText(getString(R.string.course_price, mCourse.getcPrice()));
            }
            viewHolder.duration.setText(getString(R.string.course_time_duration, mCourse.getcDuration()));
            viewHolder.time.setText(getString(R.string.course_play_count, mCourse.getcPlayTime()));
            viewHolder.zhan.setText(getString(R.string.praise_count, mCourse.getcZhan()));
            viewHolder.comment.setText(getString(R.string.comment_count, mCourse.getcCommentCount()));
            return convertView;
        }

       /* @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.view_detail:
                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course", mCourse);
                    intent.putExtras(bundle);
                    Utils.start_Activity(CourseListActivity.this,intent);
                    break;
                default:
                    break;
            }
        }*/
    }

    class CourseViewHolder{
        private ImageView icon;
        private TextView title;
        private TextView price;
        private TextView duration;
        private TextView time;
        private TextView zhan;
        private TextView comment;
        private LinearLayout linear;
        //private TextView info;
        //private ImageView detail;
    }
}
