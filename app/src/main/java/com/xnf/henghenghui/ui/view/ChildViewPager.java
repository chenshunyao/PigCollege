package com.xnf.henghenghui.ui.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ChildViewPager extends ViewPager implements Runnable {
	
	private static final int POST_DELAYED_TIME = 1000 * 5;  
    // 手指是否放在上面  
    private boolean touching;
	private boolean autorollMark;
    private boolean autoroll;
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	IOnSingleTouchListener onSingleTouchListener;
	
	SwipeRefreshLayout swipeRefreshLayout;

	public ChildViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ChildViewPager(Context context) {
		super(context);
	}
	public void setAutoRoll(boolean autoroll){
		this.autoroll = autoroll;
		this.autorollMark = autoroll;
		postDelayed(this, POST_DELAYED_TIME);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// 当拦截触摸事件到达此位置的时候，返回true，
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
		return true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if(swipeRefreshLayout != null){
				swipeRefreshLayout.setEnabled(false);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if(swipeRefreshLayout != null){
				swipeRefreshLayout.setEnabled(true);
			}
			break;
		}
		
		// 每次进行onTouch事件都记录当前的按下的坐标
		curP.x = event.getX();
		curP.y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 记录按下时候的坐标
			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
			downP.x = event.getX();
			downP.y = event.getY();
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//			getParent().requestDisallowInterceptTouchEvent(true);
			
			touching = true;
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			touching = false;
			// 在up时判断是否按下和松手的坐标为一个点
			// 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
			if (downP.x == curP.x && downP.y == curP.y) {
				onSingleTouch(this.getId());
				return true;
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP  
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
			touching = false;
		}
		return super.onTouchEvent(event);
	}
	/**
	 * 单击
	 */
	public void onSingleTouch(int id) {
		if (onSingleTouchListener != null) {
			onSingleTouchListener.onSingleTouch(id);
		}
	}
	/**
	 * 创建点击事件接口
	 * 
	 * @author wanpg
	 * 
	 */
//	public interface OnSingleTouchListener {
//		public void onSingleTouch();
//	}
	public void setOnSingleTouchListener(
			IOnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}
	@Override
	public void run() {
		if(autoroll && autorollMark){
			if (getAdapter() != null && getAdapter().getCount() > 1 && !touching) {  
	            setCurrentItem((getCurrentItem() + 1) % getAdapter().getCount(), true);  
	        }
	        postDelayed(this, POST_DELAYED_TIME);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		autoroll = true;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		autoroll = false;
	}

	public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout){
		this.swipeRefreshLayout = swipeRefreshLayout;
	}

	public interface IOnSingleTouchListener {
		public void onSingleTouch(int id);
	}
}