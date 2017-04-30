package com.xnf.henghenghui.ui.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class ChildRecyclerView extends RecyclerView {
	
	private View mCurrentView; 
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	IOnSingleTouchListener onSingleTouchListener;

    private ArrayList<View> mHeaderViews = new ArrayList<>() ;

    private ArrayList<View> mFootViews = new ArrayList<>() ;
	
	public ChildRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public ChildRecyclerView(Context context) {
		super(context);
		init();
	}
	private void init(){
		MyScroll myScroll = new MyScroll();
		this.setOnScrollListener(myScroll);
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent arg0) {
//		// 当拦截触摸事件到达此位置的时候，返回true，
//		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
//		return true;
//	}
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		// 每次进行onTouch事件都记录当前的按下的坐标
//		curP.x = event.getX();
//		curP.y = event.getY();
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			// 记录按下时候的坐标
//			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
//			downP.x = event.getX();
//			downP.y = event.getY();
//			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//			getParent().requestDisallowInterceptTouchEvent(true);
//		}
//		if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//			getParent().requestDisallowInterceptTouchEvent(true);
//		}
//		if (event.getAction() == MotionEvent.ACTION_UP) {
//			// 在up时判断是否按下和松手的坐标为一个点
//			// 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
//			if (downP.x == curP.x && downP.y == curP.y) {
////				onSingleTouch(this.getId());
//				getParent().requestDisallowInterceptTouchEvent(true);
//			}
//		}
//		return super.onTouchEvent(event);
//	}
	/**
	 * 单击
	 */
//	public void onSingleTouch(int id) {
//		if (onSingleTouchListener != null) {
//			onSingleTouchListener.onSingleTouch(id);
//		}
//	}
	/**
	 * 创建点击事件接口
	 * 
	 * @author wanpg
	 * 
	 */
//	public interf OnSingleTouchListener {
//		public void onSingleTouch();
//	}
//	public void setOnSingleTouchListener(
//			IOnSingleTouchListener onSingleTouchListener) {
//		this.onSingleTouchListener = onSingleTouchListener;
//	} 
	  
    /** 
     * 滚动时回调的接口 
     */  
    private OnItemScrollChangeListener mItemScrollChangeListener;  
  
    public void setOnItemScrollChangeListener(  
            OnItemScrollChangeListener mItemScrollChangeListener)  
    {  
        this.mItemScrollChangeListener = mItemScrollChangeListener;  
    }  
  
    public interface OnItemScrollChangeListener  
    {  
        void onChange(View view, int position);  
    }  
  
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b)  
    {  
        super.onLayout(changed, l, t, r, b);  
  
        mCurrentView = getChildAt(0);  
  
        if (mItemScrollChangeListener != null)  
        {  
            mItemScrollChangeListener.onChange(mCurrentView,  
                    getChildPosition(mCurrentView));  
        }  
    }  
    
    private class MyScroll extends OnScrollListener{
    	@Override
    	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    		View newView = getChildAt(0);  
            if (mItemScrollChangeListener != null)  
            {  
                if (newView != null && newView != mCurrentView)  
                {  
                    mCurrentView = newView ;  
                    mItemScrollChangeListener.onChange(mCurrentView,  
                            getChildPosition(mCurrentView));  
      
                }  
            } 
    		super.onScrolled(recyclerView, dx, dy);
    	}
    	@Override
    	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    		super.onScrollStateChanged(recyclerView, newState);
    	}
    }

    public interface IOnSingleTouchListener {
        public void onSingleTouch(int id);
    }

}