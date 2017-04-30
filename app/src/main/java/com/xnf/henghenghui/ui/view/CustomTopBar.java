package com.xnf.henghenghui.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xnf.henghenghui.R;

/**
 * Created by Administrator on 2016/2/22.
 */
public class CustomTopBar extends RelativeLayout {

    private Button leftButton,rightButton1,rightButton2;
    private TextView mTitleText;
    private String mTitle;
    private float titleTextSize;
    private String leftText;
    private Drawable leftBackground;
    private String rightText;
    private String rightText1;
    private Drawable rightBackground;
    private LayoutParams leftParams;
    private LayoutParams rightParams;
    private LayoutParams rightParams1;
    private LayoutParams textParams;
    private TopBarOnClickListener mListener;

    public interface  TopBarOnClickListener{
        public void goBack();
        public void onTransmit();
        public void onCollect();
    }

    public void setTopBarOnClickListener(TopBarOnClickListener listener){
        mListener = listener;
    }
    public CustomTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTopBar);
        leftText = ta.getString(R.styleable.CustomTopBar_leftText);
        leftBackground = ta.getDrawable(R.styleable.CustomTopBar_leftBackground);
        rightText = ta.getString(R.styleable.CustomTopBar_rightText);
        rightText1 = ta.getString(R.styleable.CustomTopBar_rightText1);
        rightBackground = ta.getDrawable(R.styleable.CustomTopBar_rightBackground);
        mTitle = ta.getString(R.styleable.CustomTopBar_mTitle);
        titleTextSize = ta.getDimension(R.styleable.CustomTopBar_titleTextSize, 0);
        ta.recycle();
        init(context);
    }

    private void init(Context context){
        View localView = LayoutInflater.from(context).inflate(R.layout.topbar_layout,this);
        leftButton = (Button) localView.findViewById(R.id.left_btn);
        rightButton1 = (Button) localView.findViewById(R.id.right_btn);
        rightButton2 = (Button) localView.findViewById(R.id.right1_btn);
        mTitleText = (TextView) localView.findViewById(R.id.bar_title);
        leftButton.setText(leftText);
        rightButton1.setText(rightText);
        rightButton2.setText(rightText1);
        mTitleText.setText(mTitle);
        mTitleText.setTextSize(titleTextSize);
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBack();
            }
        });
        rightButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTransmit();
            }
        });
        rightButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCollect();
            }
        });
    }

}
