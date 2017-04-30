package com.xnf.henghenghui.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.Constants;
import com.xnf.henghenghui.ui.dialog.CommonDialog;
import com.xnf.henghenghui.util.TDevice;

public class ShareDialog extends CommonDialog implements
        View.OnClickListener {

    private Context context;
    private String title;
    private String content;
    private String link;
    private Activity mActivity;

    private ShareDialog(Activity activity,Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
        this.context = context;
        mActivity = activity;
    }

    @SuppressLint("InflateParams")
    private ShareDialog(Activity activity,Context context, int defStyle) {
        super(context, defStyle);
        this.context = context;
        mActivity = activity;
        View shareView = getLayoutInflater().inflate(
                R.layout.dialog_cotent_share, null);
        shareView.findViewById(R.id.ly_share_qq).setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_copy_link)
                .setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_more_option).setOnClickListener(
                this);
        shareView.findViewById(R.id.ly_share_sina_weibo).setOnClickListener(
                this);
        shareView.findViewById(R.id.ly_share_weichat).setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_weichat_circle)
                .setOnClickListener(this);
        setContent(shareView, 0);
    }

    public ShareDialog(Activity activity,Context context) {
        this(activity,context, R.style.dialog_bottom);
    }

    @SuppressWarnings("deprecation")
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

    public void setShareInfo(String title, String content, String link) {
        this.title = title;
        this.content = content;
        this.link = link;
    }

    @Override
    public void onClick(View v) {
        if (!checkCanShare()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ly_share_weichat_circle:
                shareToWeiChatCircle();
                break;
            case R.id.ly_share_weichat:
                shareToWeiChat();
                break;
            case R.id.ly_share_sina_weibo:
                shareToSinaWeibo();
                break;
            case R.id.ly_share_qq:
                shareToQQ();
                break;
            case R.id.ly_share_copy_link:
                TDevice.copyTextToBoard(this.link);
                break;
            case R.id.ly_share_more_option:
                TDevice.showSystemShareOption((Activity)this.context,
                        this.title, this.link);
                break;
            default:
                break;
        }
        this.dismiss();
    }

    @SuppressWarnings("deprecation")
    private void shareToWeiChatCircle() {
        UMImage image = new UMImage(context, R.drawable.henghenghui_share);
        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                .withMedia(image)
                .withTitle(title)
                .withText(content)
                .withTargetUrl(link)
                .share();
    }

    @SuppressWarnings("deprecation")
    private void shareToWeiChat() {
        UMImage image = new UMImage(context, R.drawable.henghenghui_share);
        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                .withMedia(image)
                .withTitle(title)
                .withText(content)
                .withTargetUrl(link)
                .share();
    }

    private void shareToSinaWeibo() {
        UMImage image = new UMImage(context, R.drawable.splash_image);
        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                .withMedia(image)
                .withTitle(title)
                .withText(content)
                .withTargetUrl(link)
                .share();
    }

    private void shareToQQ() {

        UMImage image = new UMImage(context, R.drawable.splash_image);
        String url = link;
        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                .withMedia(image)
                .withTitle(title)
                .withText(content)
                .withTargetUrl(link)
                .share();
    }

    private UMImage getShareImg() {
        UMImage img = new UMImage(this.context, R.drawable.ic_share);
        return img;
    }

    private boolean checkCanShare() {
        boolean canShare = true;
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(link)) {
            canShare = false;
        }
        return canShare;
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {

            //Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
           // Toast.makeText(context,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
           // Toast.makeText(context,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
