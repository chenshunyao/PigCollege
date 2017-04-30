package com.xnf.henghenghui.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.ui.dialog.CommonDialog;
import com.xnf.henghenghui.util.TDevice;

/**
 * Created by Administrator on 2016/5/8.
 */
public class MoreDialog extends CommonDialog implements View.OnClickListener {

    private Context context;
    private String title;
    private String content;
    private String link;
    private Activity mActivity;

    private MoreDialog(Activity activity,Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
        this.context = context;
        mActivity = activity;
    }

    @SuppressLint("InflateParams")
    private MoreDialog(Activity activity,Context context, int defStyle) {
        super(context, defStyle);
        this.context = context;
        mActivity = activity;
        View shareView = getLayoutInflater().inflate(
                R.layout.more_dialog_content, null);
        shareView.findViewById(R.id.more_collect_text).setOnClickListener(this);
        shareView.findViewById(R.id.more_share_text)
                .setOnClickListener(this);
        shareView.findViewById(R.id.more_cancel_text).setOnClickListener(
                this);
        setContent(shareView, 0);
    }

    public MoreDialog(Activity activity,Context context) {
        this(activity, context, R.style.dialog_bottom);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_collect_text:
                startCollect();
                break;
            case R.id.more_share_text:
                shareToOthers();
                break;
            case R.id.more_cancel_text:
                startCancel();
                break;
            default:
                break;
        }
        this.dismiss();
    }

    private void startCollect(){

    }

    private void shareToOthers(){
        final ShareDialog dialog = new ShareDialog(mActivity, mActivity);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);
        dialog.setShareInfo("Title", "ShareContent", "http://www.baidu.com");
        dialog.show();
    }

    private void startCancel(){

    }
}
