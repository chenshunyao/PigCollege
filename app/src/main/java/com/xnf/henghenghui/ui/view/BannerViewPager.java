package com.xnf.henghenghui.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.xnf.henghenghui.R;

/**
 * TODO: document your custom view class.
 */
public class BannerViewPager extends ViewPager {

    private BannerViewClickListener listener;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBannerViewClickListener(BannerViewClickListener paramBannerViewClickListener){
        this.listener = paramBannerViewClickListener;
    }

    public static abstract interface BannerViewClickListener {
        public abstract void setViewClickListener(View view);
    }
}
