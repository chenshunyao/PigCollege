package com.xnf.henghenghui.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.MeetingManager;
import com.xnf.henghenghui.model.LiveConferenceListModel;
import com.xnf.henghenghui.model.LiveRoomModel;
import com.xnf.henghenghui.model.MeetingListResponseModel;
import com.xnf.henghenghui.model.MeetingModel;
import com.xnf.henghenghui.ui.base.BaseFragment3;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.MoreDialog;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public class LiveRoomFragment extends BaseFragment3 implements View.OnClickListener {
    private static final String TAG = "csy_"+LiveRoomFragment.class.getName();
    private ListView mListView;
    //private List<MeetingModel> mLists;
    private List<LiveRoomModel> mLists;
    private LiveRoomAdapter mLiveRoomAdapter;
    private String mId;
    private String mStatus;
    private LiveConferenceThread mLiveThread;
    ProgressDialog mDialog;
    private boolean mProgressShow;
    private static final String GIVE_PRAISE_TYPE = "1";
    private static final String CANCEL_PRAISE_TYPE = "2";
    private boolean mIsPraise = false;
    private android.view.animation.Animation mAnimation;
    private LinearLayout mExtendMore;
    private TextView mLiveRoomStatus;
    private TextView mLiveRoomOnlineNum;
    private List<String> mTags = new ArrayList<>();

    public static LiveRoomFragment getIntance(Bundle bundle){
        LiveRoomFragment liveroom = new LiveRoomFragment();
        liveroom.setArguments(bundle);
        return liveroom;
    }

    @Override
    protected String setFragmentTag() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "2 LiveRoomFragment onCreate setHandler mHandler:" + mHandler);
        initloadingDialog();
        //MeetingManager.setHandler(mHandler);
        if(mTags.size() > 0) {
            mTags.clear();
        }
        mTags.add("liveConf");
        PushManager.setTags(getActivity().getApplicationContext(), mTags);
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d(TAG, "LiveRoomFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hengheng.liveroom.push");
        getActivity().registerReceiver(pushBroadcaseReciever,filter);
    }

    private void initloadingDialog(){
        mProgressShow = true;
        mDialog = new ProgressDialog(getContext());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mProgressShow = false;
            }
        });
        mDialog.setMessage(getString(R.string.dl_waiting));
    }

    @Override
    protected void lazyLoad() {
        MeetingManager.setHandler(mHandler);
        L.d(TAG,"LiveRoomFragment lazyload");
        if(mLiveThread == null){
            mDialog.show();
            L.d(TAG,"LiveRoomFragment lazyload getdata");
            mLiveThread = new LiveConferenceThread();
            mLiveThread.start();
        }
        if(mLiveRoomOnlineNum != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("online_num", Activity.MODE_PRIVATE);
            mLiveRoomOnlineNum.setText(getString(R.string.conference_online_num, sharedPreferences.getString("MEETING_ONLINE_NUM", "0")));
        }
    }

    @Override
    protected void initData(Bundle bundle) {
        //mLists = getLiveRoomData();
        mId = getActivity().getIntent().getStringExtra("cId");
        mStatus = getActivity().getIntent().getStringExtra("status");
        L.d(TAG, "initData Liveroom  mId:" + mId+";status:"+mStatus);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.liveroom_fragment_layout,container,false);
        mListView = (ListView)view.findViewById(R.id.meeting_live_room);
        mLiveRoomStatus =(TextView)view.findViewById(R.id.meeting_detail_status);
        if(!TextUtils.isEmpty(mStatus)){
            //0:未开始；1：直播中；2：已结束
            if(CodeUtil.NO_START.equals(mStatus)){
                //L.d("csy_"+TAG,"0 mMeetingStatus setText:"+status);
                mLiveRoomStatus.setText(getString(R.string.meeting_status0));
            }else if(CodeUtil.LIVING_CONFERENCE.equals(mStatus)){
                //L.d("csy_"+TAG,"1 mMeetingStatus setText:"+status);
                mLiveRoomStatus.setText(getString(R.string.meeting_status1));
            }else if(CodeUtil.CONFERENCE_END.equals(mStatus)) {
                //L.d("csy_"+TAG,"2 mMeetingStatus setText:"+status);
                mLiveRoomStatus.setText(getString(R.string.meeting_status2));
            }
        }
        mLiveRoomOnlineNum =(TextView)view.findViewById(R.id.meeting_detail_join);
        mExtendMore =(LinearLayout)view.findViewById(R.id.meeting_liveroom_extend);
        //mListView.setAdapter(new LiveRoomAdapter(getContext(),mLists));
        //mLists = new ArrayList<MeetingModel>();
        mLists = new ArrayList<LiveRoomModel>();
        mLiveRoomAdapter = new LiveRoomAdapter(getContext());
        mListView.setAdapter(mLiveRoomAdapter);
        mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.nn);
        return view;
    }

    class LiveConferenceThread extends Thread{
        public volatile boolean exit = false;
        @Override
        public void run() {
            L.d(TAG,"LiveConferenceThread run exit:"+exit);
            if(!exit) {
                MeetingManager.requestLiveMeetingList(LoginUserBean.getInstance().getLoginUserid(),mId, "", "",mHandler);
                //MeetingManager.requestMeetingList(LoginUserBean.getInstance().getLoginUserid() ,"","",Utils.CONFERENCE_TYPE_LIVE,mHandler);
            }
        }
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_LIVECONFERENCE_LIST:
                String resp  =(String)msg.obj;
                L.d(TAG, "@liveconference list Response:" + resp);
                if(mDialog != null && mDialog.isShowing()){
                    mDialog.dismiss();
                }
                if(mLists.size() > 0){
                    mLists.clear();
                }
                if(Utils.getRequestStatus(resp) == 1){
                    L.d(TAG, "liveconference status 1");
                    Gson gson = new Gson();
                    LiveConferenceListModel responseModel = gson.fromJson(resp,LiveConferenceListModel.class);
                    List<LiveConferenceListModel.LiveConferenceContent> contents = responseModel.getReponse().getContent();
                    /*MeetingListResponseModel model = gson.fromJson(resp,MeetingListResponseModel.class);
                    List<MeetingListResponseModel.MeetingContent> contents = model.getReponse().getContent();*/
                    L.d("csy","handler message contents size:"+contents.size());
                    for(int i=0;i<contents.size();i++){
                        //MeetingListResponseModel.MeetingContent content = contents.get(i);
                        LiveConferenceListModel.LiveConferenceContent content = contents.get(i);
                        LiveRoomModel liveroom = new LiveRoomModel();
                        //MeetingModel liveroom = new MeetingModel();
                        liveroom.setcId(content.getcId());
                        //视频链接
                        //liveroom.setUrl(content.getcURL());
                        //评论Id
                        liveroom.setCommId(content.getCommId());
                        //视频截图
                        //liveroom.setImgUrl(content.getThumbnailURL());
                        //评论内容
                        liveroom.setContent(content.getContent());
                        //状态
                        //liveroom.setStatus(content.getStatus());
                        //评论用户id
                        liveroom.setUserId(content.getUserId());
                        //标题
                        //liveroom.setTitle(content.getTitle());
                        //评论用户昵称
                        //liveroom.setNickName(content.getNikeName());
                        //评论用户头像
                        liveroom.setUserPhoto(content.getUserPhoto());
                        //评论图片
                        liveroom.setPhotos(content.getPhotos());
                        //地点
                        //liveroom.setPlace(content.getAddress());
                        //发表时间
                        liveroom.setPublishTime(content.getPublishTime());
                        //内容
                        //liveroom.setDesc(content.getContent());
                        //昵称
                        String name = (content.getNikeName() == null) ? "" : content.getNikeName();
                        liveroom.setNickName(name);
                        String count = TextUtils.isEmpty(content.getPraiseCount()) ? "0" : content.getPraiseCount();
                        //点赞数
                        liveroom.setPraiseCount(count);
                        mLists.add(liveroom);
                    }
                    L.d("csy","liveconference list mList size:"+mLists.size());
                    if(mLists != null && mLists.size() > 0){
                        mLiveRoomAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(mListView);
                        mExtendMore.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(getContext(),R.string.live_no_data_info,Toast.LENGTH_SHORT).show();
                    }
                }else{
                    L.d(TAG,"resp live conference list fail");
                    Toast.makeText(getContext(),R.string.live_no_data_info,Toast.LENGTH_SHORT).show();
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_GIVE_PRAISE:
                String res = (String)msg.obj;
                L.d(TAG, "give praise Response:" + res);
                if(Utils.getRequestStatus(res) == 1) {
                    L.d(TAG, "resp give praise success");
                    if(mIsPraise){
                        ToastUtil.showToast(getContext(),"取消点赞成功!");
                        mIsPraise = false;
                    }else{
                        ToastUtil.showToast(getContext(),"点赞成功!");
                        mIsPraise = true;
                    }
                    //mZhan.setImageResource(R.drawable.course_detail_zhan);
                    //mGivePraise.setEnabled(false);
                    //mGivePraise.setClickable(false);
                }else{
                    L.d(TAG,"resp give praise fail");
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

    @Override
    public void onClick(View v) {

    }

    class LiveRoomAdapter extends BaseAdapter implements View.OnClickListener{
        //List<LiveRoomModel> mList;
        LayoutInflater mInflater;
        Context mContext;
        LiveHolderView holderView;

        public LiveRoomAdapter(Context context){
            mContext = context;
            mInflater = LayoutInflater.from(context);
            //mList = datas;
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
            if(convertView == null){
                holderView = new LiveHolderView();
                convertView = mInflater.inflate(R.layout.liveroom_item_layout,parent,false);
                holderView.praiseArea =(LinearLayout)convertView.findViewById(R.id.compere_function_area);
                holderView.pTime = (TextView) convertView.findViewById(R.id.publish_time);
                holderView.nickName = (TextView)convertView.findViewById(R.id.nick_name);
                holderView.content = (TextView)convertView.findViewById(R.id.live_conference_content);
                holderView.img1 =(ImageView)convertView.findViewById(R.id.live_conference_photo);
                holderView.pariseCount =(TextView)convertView.findViewById(R.id.compere_give_like);
                holderView.praiseArea.setOnClickListener(this);
                convertView.setTag(holderView);
            }else{
                holderView = (LiveHolderView)convertView.getTag();
            }
            //MeetingModel model = mList.get(position);
            LiveRoomModel model = mLists.get(position);
            holderView.pTime.setText(getShortTime(model.getPublishTime()));
            //L.d("csy","position:"+position+";nickname:"+model.getNickName());
            holderView.nickName.setText(getString(R.string.live_nick_name,model.getNickName()));
            holderView.content.setText(model.getContent());
            //if(!TextUtils.isEmpty(model.getPhotos())){
                //holderView.img1.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(model.getPhotos())) {
                holderView.img1.setVisibility(View.VISIBLE);
                mImageLoader.displayImage(model.getPhotos(), holderView.img1, mOptions);
            }else{
                holderView.img1.setVisibility(View.GONE);
            }
            //}
            holderView.pariseCount.setText(getString(R.string.live_praise_count,model.getPraiseCount()));
            return convertView;
        }

        private String getShortTime(String ptime){
            //L.d("csy","ptime:"+ptime);
            if(TextUtils.isEmpty(ptime))return "";
            String[] time = ptime.split(" ");
            //L.d("csy","time length:"+time.length);
            if(time.length > 1 && time[1].length() > 5){
                //L.d("csy","time1:"+time[1].substring(0,5));
                return time[1].substring(0,5);
            }
            return "";
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                /*case R.id.live_room_more:
                    MoreDialog dialog = new MoreDialog(getActivity(),mContext);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    break;*/
                case R.id.compere_function_area:
                    final TextView v1 = ((TextView)v.findViewById(R.id.tv_one));
                    if(!mIsPraise) {
                        MeetingManager.requestGivePraise(LoginUserBean.getInstance().getLoginUserid(), mId, GIVE_PRAISE_TYPE,mHandler);
                        v1.setVisibility(View.VISIBLE);
                        v1.startAnimation(mAnimation);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                v1.setVisibility(View.GONE);
                            }
                        }, 700);
                        ((ImageView)v.findViewById(R.id.give_like)).setImageResource(R.drawable.course_detail_zhan);
                    }else{
                        MeetingManager.requestGivePraise(LoginUserBean.getInstance().getLoginUserid(), mId, CANCEL_PRAISE_TYPE,mHandler);
                        ((ImageView)v.findViewById(R.id.give_like)).setImageResource(R.drawable.course_detail_nozhan);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    static class LiveHolderView{
        private LinearLayout praiseArea;
        private TextView pTime;
        private TextView nickName;
        private TextView content;
        private TextView pariseCount;
        private ImageView img1;
    }

    /*private List<LiveRoomModel> getLiveRoomData(){
        List<LiveRoomModel> datas = new ArrayList<LiveRoomModel>();
        for(int i=0;i<2;i++){
            LiveRoomModel liveroom = new LiveRoomModel();
            liveroom.setCompereName("主持人");
            liveroom.setLiveDate("11月19日 13:36");
            liveroom.setLiveContent(getString(R.string.compere_content));
            datas.add(liveroom);
        }
        return datas;
    }*/

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(pushBroadcaseReciever);
        super.onPause();
    }

    private BroadcastReceiver pushBroadcaseReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String uid = LoginUserBean.getInstance().getLoginUserid();
            //重新请求接口,刷新页面
            L.d(TAG,"pushBroadcaseReciever uid:"+uid+";mId:"+mId);
            MeetingManager.requestLiveMeetingList(uid,mId, "", "",mHandler);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(TAG,"onDestroy del tag");
        PushManager.delTags(getActivity().getApplicationContext(), mTags);
    }
}
