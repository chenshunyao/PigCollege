package com.xnf.henghenghui.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.xnf.henghenghui.R;

import java.util.List;

/**
 * Created by Administrator on 2016/2/13.
 */
public class HotViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener,BannerViewPager.BannerViewClickListener {
    private static final String TAG = HotViewPager.class.getName();
    public GestureDetector mGD;
    BannerViewClick mBannerViewClick;
    BannerViewPager mBannerViewPager;
    //public SubOnClickListener mListener;
    private boolean mIsScroll;
    private LinearLayout mLinlayout;
    //private LinearLayout mLinearLayout;
    private int mSize;
    HotImageView[] mHotImg = new HotImageView[0];

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage currentItem:" + mBannerViewPager.getCurrentItem());
            if(mBannerViewPager.getCurrentItem() == mSize - 1){
                mBannerViewPager.setCurrentItem(0);
            }else{
                mBannerViewPager.setCurrentItem(1 + mBannerViewPager.getCurrentItem(),true);
            }
            mHandler.sendMessageDelayed(mHandler.obtainMessage(0),3000L);
        }
    };

    public HotViewPager(Context context) {
        super(context);
        Layout(context);
    }

    public HotViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        Layout(context);
    }

    public void init(Context context){
        Layout(context);
    }

    private void Layout(Context context) {
        mGD = new GestureDetector(context,new MyGestureListener());
        RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View myView = LayoutInflater.from(context).inflate(R.layout.banner_layout,null);
        mBannerViewPager = (BannerViewPager) myView.findViewById(R.id.viewpager);
        //mLinearLayout = (LinearLayout) myView.findViewById(R.id.activity_layout);
        Point point = new Point();
        ((Activity)context).getWindowManager().getDefaultDisplay().getSize(point);
        int width = point.x;
        mBannerViewPager.getLayoutParams().width = width;
        mBannerViewPager.getLayoutParams().height = width/2;
        mBannerViewPager.addOnPageChangeListener(this);
        mBannerViewPager.setBannerViewClickListener(this);
        mBannerViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mIsScroll = true;
                mGD.onTouchEvent(event);
                getParent().requestDisallowInterceptTouchEvent(mIsScroll);
                return false;
            }
        });
        mLinlayout= (LinearLayout) myView.findViewById(R.id.radio_layout);
        /*RelativeLayout sub1 = (RelativeLayout) myView.findViewById(R.id.subject1);
        RelativeLayout sub2 = (RelativeLayout) myView.findViewById(R.id.subject2);
        sub1.setOnClickListener(new SubjectOnClick());
        sub2.setOnClickListener(new SubjectOnClick());*/
        addView(myView, localLayoutParams);
    }

    /*public void setSubOnClickListener(SubOnClickListener mListener) {
        this.mListener = mListener;
    }*/

    @Override
    public void setViewClickListener(View view) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position % mSize);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*public class SubjectOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.subject1:
                    if(mListener != null){
                        mListener.onSubject1Click(v);
                    }
                    break;
                case R.id.subject2:
                    if(mListener != null){
                        mListener.onSubject2Click(v);
                    }
                    break;
            }
        }
    }

    public static abstract interface SubOnClickListener {
        public abstract void onSubject1Click(View paramView);
        public abstract void onSubject2Click(View paramView);
    }*/


        @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHandler.sendEmptyMessageDelayed(0,3000L);
    }

    /*public void setVisibleLinear(boolean flag){
        if(flag){
            mLinearLayout.setVisibility(View.VISIBLE);
        }else{
            mLinearLayout.setVisibility(View.GONE);
        }
    }*/

    private void setImageBackground(int paramInt) {
        int i = mLinlayout.getChildCount();
        int j = 0;
        while(j < i){
            View view = mLinlayout.getChildAt(j);
            if(j == paramInt){
                view.setBackgroundResource(R.drawable.check_point);
                view.getBackground().setAlpha(230);
            }else{
                view.setBackgroundResource(R.drawable.uncheck_point);
                view.getBackground().setAlpha(127);
            }
            j++;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int i = ev.getAction();
        if(i == MotionEvent.ACTION_DOWN){
            mHandler.removeMessages(0);
        }else if(i == MotionEvent.ACTION_UP || i == MotionEvent.ACTION_CANCEL){
            mHandler.sendEmptyMessageDelayed(0,3000L);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 模拟假数据填充广告轮播
     * @param lists
     * @param paramBannerViewClick
     */
    public void setBaseAdapterData(List<HotImageView> lists, BannerViewClick paramBannerViewClick) {
        if(lists == null || lists.size() == 0){
            return;
        }
        mBannerViewClick = paramBannerViewClick;
        if(mBannerViewPager.getChildCount() > 0){
            mBannerViewPager.removeAllViews();
            mBannerViewPager.setAdapter(null);
        }
        mHandler.removeMessages(0);
        if(mLinlayout.getChildCount() > 0){
            mLinlayout.removeAllViews();
        }
        mSize = lists.size();
        mHotImg = new HotImageView[mSize];
        for(int i=0;i<mSize;i++){
            HotImageView imageview = lists.get(i);
            mHotImg[i] = imageview;
        }
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int j = 0;
        while(j < mSize){
            ImageView image = new ImageView(getContext());
            image.setLayoutParams(new RelativeLayout.LayoutParams(10,10));
            if(j == 0){
                image.setBackgroundResource(R.drawable.check_point);
            }else{
                image.setBackgroundResource(R.drawable.uncheck_point);
            }
            localLayoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.dimen_point);
            localLayoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.dimen_point);
            mLinlayout.addView(image,localLayoutParams);
            j++;
        }
        mBannerViewPager.setTag(null);
        mBannerViewPager.setAdapter(new HotPageAdapter());
        setCurrentItem(0);
        Log.d(TAG, "setCurrent 0");
        mHandler.sendEmptyMessageDelayed(0,3000L);
    }
    /**
     * from server get data
     * @param paramJSONArray
     * @param paramBannerViewClick
     * @throws JSONException
     */
    public void setBaseAdapterData(JSONArray paramJSONArray, BannerViewClick paramBannerViewClick)
            throws JSONException {
        if(paramJSONArray == null)return;
        mBannerViewClick = paramBannerViewClick;
        if(mBannerViewPager.getChildCount() > 0){
            mBannerViewPager.removeAllViews();
            mBannerViewPager.setAdapter(null);
        }
        mHandler.removeMessages(0);
        if(mLinlayout.getChildCount() > 0){
            mLinlayout.removeAllViews();
        }
        mSize = paramJSONArray.length();
        mHotImg = null;
        if(mSize > 1 && mSize < 4){
            for(int k = mSize;k < 2*mSize;k++){
                paramJSONArray.put(k,paramJSONArray.opt(k % mSize));
            }
        }
        mHotImg = new HotImageView[mSize];
        for(int i=0;i<paramJSONArray.length();i++){
            JSONObject localJSONObject = paramJSONArray.optJSONObject(i);
            HotImageView hotImg = new HotImageView(getContext());
            hotImg.setScaleType(ImageView.ScaleType.FIT_XY);
            hotImg.setTag(localJSONObject);
            hotImg.setId(i);
            //hotImg.setImageUrl(localJSONObject.getString("pic"),HotCacheManager.getInstance().getImageLoader());
            mHotImg[i] = hotImg;
            BitmapDrawable localBitmapDrawable = (BitmapDrawable)hotImg.getDrawable();
            if(localBitmapDrawable != null){
                localBitmapDrawable.setCallback(null);
                Bitmap bitmap = localBitmapDrawable.getBitmap();
                if(bitmap != null)bitmap.recycle();
            }
        }
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int j = 0;
        while(j < mSize){
            ImageView image = new ImageView(getContext());
            image.setLayoutParams(new RelativeLayout.LayoutParams(10,10));
            if(j == 0){
                image.setBackgroundResource(R.drawable.check_point);
            }else{
                image.setBackgroundResource(R.drawable.uncheck_point);
            }
            localLayoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.dimen_point);
            localLayoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.dimen_point);
            mLinlayout.addView(image,localLayoutParams);
            j++;
        }
        mBannerViewPager.setTag(paramJSONArray);
        mBannerViewPager.setAdapter(new HotPageAdapter());
        if(mSize > 1){
            setCurrentItem(100 * mSize);
        }else{
            setCurrentItem(0);
        }
        mHandler.sendEmptyMessageDelayed(0,3000L);
    }

    public void setCurrentItem(int paramInt){
        mBannerViewPager.setCurrentItem(paramInt);
    }

    public static abstract interface BannerViewClick{
        public abstract void subViewClickListener(View paramView);
    }

    class FixedSpeedScroller extends Scroller {
        private int mDuration = 1000;

        public FixedSpeedScroller(Context arg2){
            super(arg2);
        }

        public FixedSpeedScroller(Context paramInterpolator, Interpolator arg3){
            super(paramInterpolator,arg3);
        }

        public FixedSpeedScroller(Context paramInterpolator, Interpolator paramBoolean, boolean arg4){
            super(paramInterpolator,paramBoolean, arg4);
        }

        public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4){
            super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4, this.mDuration);
        }

        public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5){
            super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4, this.mDuration);
        }
    }

    public static abstract interface bannerDataCallback {
        //public abstract void bannerHotAnalyzeBackBlock(HotCommonResult paramMCCommonResult, String paramString1, String paramString2);
    }

    private final class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        private MyGestureListener(){

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(Math.abs(distanceY) < Math.abs(distanceX)){
                mIsScroll = true;
                return true;
            }
            mIsScroll = false;
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View view = ((HotPageAdapter)mBannerViewPager.getAdapter()).getmCurrentView();
            if(mBannerViewClick != null){
                mBannerViewClick.subViewClickListener(view);
            }
            return true;
        }
    }

    public class HotPageAdapter extends PagerAdapter{
        public View mCurrentView;

        public HotPageAdapter() {
        }

        public View getmCurrentView() {
            return mCurrentView;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            HotImageView img = mHotImg[position % mHotImg.length];
            if(img.getParent() != null){
                mBannerViewPager.removeView(img);
            }
            try{
                ((ViewPager)container).addView(img);
                return img;
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mHotImg[position % mHotImg.length].setImageBitmap(null);
        }

        /**
         * ?
         * @return
         */
        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurrentView = (View)object;
        }
    }
}
