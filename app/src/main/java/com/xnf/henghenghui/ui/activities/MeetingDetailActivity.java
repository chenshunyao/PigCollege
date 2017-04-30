package com.xnf.henghenghui.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.MeetingModel;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.fragments.LessonNoteFragment;
import com.xnf.henghenghui.ui.fragments.LiveRoomFragment;
import com.xnf.henghenghui.ui.fragments.MeetingCommentFragment;
import com.xnf.henghenghui.ui.fragments.MeetingDetailFragment;
import com.xnf.henghenghui.ui.view.MeetingSlidingTabLayout;
import com.xnf.henghenghui.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MeetingDetailActivity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ImageView mBackImg;
    private ImageView mShareImg;
    private TextView mTitleTxt;
    private MeetingModel mMeeting;
    private String mTitle;
    //会评id
    private String mCid;
    //会评状态
    private String mStatus;
    private MeetingSlidingTabLayout mMeetingSlideTabs;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList();
    private MeetingDetailFragment mMeetingDetail;
    private LiveRoomFragment mLiveRoom;
    private LessonNoteFragment mLessonNote;
    private MeetingCommentFragment mMeetingComment;
    private List<String> mTabNames = new ArrayList();
    int[] arrayOfInt = new int[1];

    @Override
    protected void initData() {
        getIntentData(getIntent());
        L.d("csy", "initData mCid:" + mCid);
        getIntent().putExtra("cId", mCid);
        getIntent().putExtra("status",mStatus);
        mMeetingDetail = new MeetingDetailFragment();
        mLiveRoom = new LiveRoomFragment();
        mLessonNote = new LessonNoteFragment();
        mMeetingComment = new MeetingCommentFragment();
        fragmentList.add(mMeetingDetail);
        fragmentList.add(mLiveRoom);
        fragmentList.add(mLessonNote);
        fragmentList.add(mMeetingComment);
        mTabNames.add(getResources().getString(R.string.meeting_detail));
        mTabNames.add(getResources().getString(R.string.meeting_live_room));
        mTabNames.add(getResources().getString(R.string.meeting_lesson_note));
        mTabNames.add(getResources().getString(R.string.meeting_comment));
        arrayOfInt[0] = getResources().getColor(R.color.tab_indicator);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_meeting_detail);
        mBackImg =(ImageView)findViewById(R.id.img_back);
        mShareImg = (ImageView)findViewById(R.id.img_right);
        mMeetingSlideTabs = (MeetingSlidingTabLayout) findViewById(R.id.meeting_detail_tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mMeetingSlideTabs.setSelectedIndicatorColors(arrayOfInt);
        //initial();
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),fragmentList));
        mMeetingSlideTabs.setViewPager(mViewPager);
        mMeetingSlideTabs.setDistributeEvenly(true);
        mViewPager.setCurrentItem(0);
        mShareImg.setImageResource(R.drawable.share_icon);
        mShareImg.setVisibility(View.INVISIBLE);
        mTitleTxt =(TextView)findViewById(R.id.txt_title);
        mTitleTxt.setText(mTitle);
        mBackImg.setVisibility(View.VISIBLE);
        bindOnClickLister(this, mBackImg, mShareImg);
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
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_right:
                Log.d("csy", "share click!");
                /*Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "哼哼课堂");
                intent.putExtra(Intent.EXTRA_TEXT, mCourse.getcDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent,"课程分享"));*/
                break;
            default:
                break;
        }
    }

    private void getIntentData(Intent intent){
        if(intent == null) return;
        mMeeting = (MeetingModel)intent.getSerializableExtra("meeting");
        Log.d("csy", "initdata mMeeting:" + mMeeting);
        if(mMeeting != null){
            mTitle= mMeeting.getTitle();
            mCid = mMeeting.getcId();
            mStatus = mMeeting.getStatus();
        }
    }

    /**
     * 获取会评Id
     * @return
     */
    public String getMeetingId(){
        return mCid;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("csy","onPageScrolled position:"+position);
    }

    @Override
    public void onPageSelected(int position) {
        /*switch(position){
            case 0:
                Log.d("csy", "page 0");
                mLiveRoom.setVisible(false);
                mLessonNote.setVisible(false);
                mMeetingComment.setVisible(false);
                mMeetingDetail.setVisible(true);
                break;
            case 1:
                Log.d("csy","page 1");
                mMeetingDetail.setVisible(false);
                mLessonNote.setVisible(false);
                mMeetingComment.setVisible(false);
                mLiveRoom.setVisible(true);
                break;
            case 2:
                Log.d("csy","page 2");
                mMeetingDetail.setVisible(false);
                mMeetingComment.setVisible(false);
                mLiveRoom.setVisible(false);
                mLessonNote.setVisible(true);
                break;
            case 3:
                Log.d("csy","page 3");
                mMeetingDetail.setVisible(false);
                mLiveRoom.setVisible(false);
                mLessonNote.setVisible(false);
                mMeetingComment.setVisible(true);
                break;
            default:
                break;
        }*/
        L.d("csy", "onPageSelected position:" + position);
        //initPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mList;
        FragmentManager mFragmentMgr;
        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
            this.mFragmentMgr = fm;
        }

       public MyFragmentAdapter(FragmentManager fm , List<Fragment> data){
            this(fm);
            mList = data;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
            /*int size = mFragmentArray.size();
            L.d("csy","getItem position:"+position +";size:"+size);
            return mFragmentArray.get(position % size);*/
        }

        @Override
        public int getCount() {
            return mList.size();
            //return mFragmentArray.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return super.isViewFromObject(view, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames.get(position);
        }
    }
}
