package com.xnf.henghenghui.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
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

import com.google.gson.Gson;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.MeetingManager;
import com.xnf.henghenghui.model.MeetingListResponseModel;
import com.xnf.henghenghui.model.MeetingModel;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MeetingActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ImageView mBackImg;
    private TextView mTitle;
    private List<MeetingModel> mMeetings = new ArrayList<MeetingModel>();
    private ListView mListView;
    private MeetingListAdapter mMeetingAdapter;

    @Override
    protected void initData() {
        //mMeetings = getMeetingData();
        MeetingManager.setHandler(mHandler);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_meeting_list);
        mBackImg =(ImageView)findViewById(R.id.img_back);
        mTitle =(TextView)findViewById(R.id.txt_title);
        mListView =(ListView)findViewById(R.id.meeting_list);
        mMeetingAdapter = new MeetingListAdapter(this);
        mListView.setAdapter(mMeetingAdapter);
        //setListViewHeightBasedOnChildren(mListView);
        mListView.setOnItemClickListener(this);
        mTitle.setText(R.string.hengheng_meeting_comment);
        mBackImg.setVisibility(View.VISIBLE);
        mMeetings = new ArrayList<MeetingModel>();
        bindOnClickLister(this, mBackImg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MeetingManager.requestMeetingList(LoginUserBean.getInstance().getLoginUserid() ,"","","",mHandler);
            }
        }).start();
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MeetingModel meeting = mMeetings.get(position);
        if(meeting == null)return;
        Intent intent = new Intent(this, MeetingDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("meeting", meeting);
        intent.putExtras(bundle);
        Utils.start_Activity(MeetingActivity.this, intent);
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_MEETING_LIST:
                String resp  =(String)msg.obj;
                L.d(TAG, "hot Response:" + resp);
                if(Utils.getRequestStatus(resp) == 1){
                    L.d(TAG, "resp meetinglist success");
                    mMeetings.clear();
                    Gson gson = new Gson();
                    MeetingListResponseModel model = gson.fromJson(resp,MeetingListResponseModel.class);
                    List<MeetingListResponseModel.MeetingContent> lists = model.getReponse().getContent();
                    L.d("csy","handleMessage lists size:"+lists.size());
                    for(int i=0;i<lists.size();i++){
                        MeetingListResponseModel.MeetingContent content = lists.get(i);
                        MeetingModel meeting = new MeetingModel();
                        meeting.setcId(content.getcId());
                        meeting.setTitle(content.getTitle());
                        meeting.setTime(content.getBeginTime());
                        meeting.setEndTime(content.getEndTime());
                        meeting.setPlace(content.getAddress());
                        meeting.setImgUrl(content.getThumbnailURL());
                        meeting.setStatus(content.getStatus());
                        meeting.setJoinCount(100);
                        meeting.setAttention(500);
                        mMeetings.add(meeting);
                    }
                    L.d("csy","handleMessage mMeetings size:"+mMeetings.size());
                    if(mMeetings != null && mMeetings.size() > 0){
                        /*if(mMeetingAdapter == null){
                            mMeetingAdapter = new MeetingListAdapter(getBaseContext(),mMeetings);
                            mListView.setAdapter(mMeetingAdapter);
                            //setListViewHeightBasedOnChildren(mListView);
                        }else{

                        }*/
                        mMeetingAdapter.notifyDataSetChanged();
                    }
                }else{
                    L.d(TAG,"resp meetinglist fail");
                }
                break;
        }
        return false;
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

    private List<MeetingModel> getMeetingData(){
        List<MeetingModel> meetings = new ArrayList<MeetingModel>();
        for(int i=0;i<4;i++){
            MeetingModel meeting = new MeetingModel();
            meeting.setTitle("第二届健康养猪芙蓉论坛");
            meeting.setTime("2016-02-21");
            meeting.setPlace("芙蓉区隆化大酒店");
            meeting.setStatus("1");
            meeting.setJoinCount(100);
            meeting.setAttention(500);
            meetings.add(meeting);
        }
        return meetings;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    private class MeetingListAdapter extends BaseAdapter{
        //private MeetingModel mMeeting;
        private LayoutInflater mLayoutInflater;
        //private List<MeetingModel> datas;
        //private Context mContext;
        public MeetingListAdapter(Context context){
            //mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            //datas = meeting;
        }

        @Override
        public int getCount() {
            return mMeetings.size();
        }

        @Override
        public Object getItem(int position) {
            return mMeetings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MeetingViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new MeetingViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.list_meeting_item,parent,false);
                viewHolder.icon =(ImageView)convertView.findViewById(R.id.meeting_icon);
                viewHolder.title =(TextView)convertView.findViewById(R.id.meeting_title);
                viewHolder.time =(TextView)convertView.findViewById(R.id.meeting_time);
                viewHolder.endtime =(TextView)convertView.findViewById(R.id.meeting_endtime);
                viewHolder.place =(TextView)convertView.findViewById(R.id.meeting_place);
                viewHolder.status =(TextView)convertView.findViewById(R.id.meeting_status);
//                viewHolder.joincount =(TextView)convertView.findViewById(R.id.meeting_join);
                //viewHolder.attention =(TextView)convertView.findViewById(R.id.meeting_attention);
                //viewHolder.linear =(LinearLayout)convertView.findViewById(R.id.meeting_detail);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (MeetingViewHolder) convertView.getTag();
            }
            final MeetingModel mMeeting = mMeetings.get(position);
            //viewHolder.linear.setOnClickListener(this);
            //viewHolder.icon.setImageResource(R.drawable.p6);
            mImageLoader.displayImage(mMeeting.getImgUrl(), viewHolder.icon);
            viewHolder.title.setText(mMeeting.getTitle());
            viewHolder.time.setText(getString(R.string.meeting_time, StringUtils.getShortTime(mMeeting.getTime())));
            viewHolder.endtime.setText(getString(R.string.meeting_end_time,StringUtils.getShortTime(mMeeting.getEndTime())));
            viewHolder.place.setText(getString(R.string.meeting_place,mMeeting.getPlace()));
            int status = Integer.valueOf(mMeeting.getStatus());
            if(status == 2){
                viewHolder.status.setText(getString(R.string.meeting_status2));
                viewHolder.status.setBackgroundResource(R.drawable.conference_status_0);
            }else if(status == 1){
                viewHolder.status.setText(getString(R.string.meeting_status1));
                viewHolder.status.setBackgroundResource(R.drawable.conference_status_1);
            }else if(status == 0){
                viewHolder.status.setText(getString(R.string.meeting_status0));
                viewHolder.status.setBackgroundResource(R.drawable.conference_status_0);
            }
//            viewHolder.joincount.setText(getString(R.string.meeting_join, mMeeting.getJoinCount()));
            //viewHolder.attention.setText(getString(R.string.meeting_attention, mMeeting.getAttention()));
            return convertView;
        }
    }

    class MeetingViewHolder{
        private ImageView icon;
        private TextView status;
        private TextView title;
        private TextView place;
        private TextView joincount;
        private TextView time;
        private TextView endtime;
        //private TextView attention;
        private LinearLayout linear;
    }
}
