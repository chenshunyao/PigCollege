package com.xnf.henghenghui.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class XImageView extends ImageView {
    private final static String TAG = "XImageView";

    public XImageView(Context context) {
        super(context);
    }

    public XImageView(Context context, AttributeSet attrs)
    {  
    	super(context, attrs);  
    }  
    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
    
}
