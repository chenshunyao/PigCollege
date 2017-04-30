package com.xnf.henghenghui.ui.view;

import android.R.integer;
import android.R.raw;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import com.xnf.henghenghui.R;

public class MyRadioButton extends RadioButton {

    private int mDrawableWidth;// xml文件中设置的大小
    private int mDrawableHeight;// xml文件中设置的大小

    public MyRadioButton(Context context) {
        this(context, null, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            Log.i("MyRadioButton", "attr:" + attr);
            switch (attr) {
                case R.styleable.MyRadioButton_imgWidth:
                    mDrawableWidth = a.getDimensionPixelSize(R.styleable.MyRadioButton_imgWidth, 50);
                    break;
                case R.styleable.MyRadioButton_imgHeight:
                    mDrawableHeight = a.getDimensionPixelSize(R.styleable.MyRadioButton_imgHeight, 50);
                    break;
                case R.styleable.MyRadioButton_drawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableBottom:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableRight:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                default :
                    break;
            }
        }
        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);

    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
                                                        Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

}