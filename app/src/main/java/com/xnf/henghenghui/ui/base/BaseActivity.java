package com.xnf.henghenghui.ui.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;

import com.lzy.okhttputils.OkHttpUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.io.FileLoader;
import com.xnf.henghenghui.io.FileType;
import com.xnf.henghenghui.receiver.MyPushMessageReceiver;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.image.CircleBitmapDisplayer;
import com.xnf.henghenghui.ui.utils.HandlerCache;
import com.xnf.henghenghui.ui.utils.HandlerInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.xnf.henghenghui.ui.utils.HandlerInterface;
import com.xnf.henghenghui.ui.utils.HenghenghuiHandler;
import com.xnf.henghenghui.ui.view.HenghenghuiProgressDialog;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

public abstract class BaseActivity extends FragmentActivity implements
        HandlerInterface {

    protected static final String TAG = "BaseActivity";

    protected static final String PID_FLAG = "pid";

    protected static final int MY_PID = android.os.Process.myPid();

    protected int mCode;

    protected Intent mLastIntent;

    protected HenghenghuiHandler mHandler;

    protected ActionBar mActionBar;

    private HandlerCache mHandlerCache;

    protected ImageLoader mImageLoader = ImageLoader.getInstance();
    protected DisplayImageOptions mOptions;
    protected DisplayImageOptions mOptions1;
    private FileLoader mFileLoader = FileLoader.getInstance();
    private ArrayList<Integer> mFileDownloadTaskList = new ArrayList<Integer>();

    protected boolean pauseOnScroll = false;
    protected boolean pauseOnFling = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!handlerBundle(savedInstanceState)) {
            return;
        }
        initBaseData();
        initHandlerCache();
        initData();
        initView();
        initActionBar();
        mOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        mOptions1 = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer())
                .build();
    }

    private void initHandlerCache() {
        mHandlerCache = new HandlerCache();
        mHandlerCache.registerHandler(mHandler);
    }

    private void initBaseData() {
        mLastIntent = getIntent();
        mHandler = new HenghenghuiHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
    	if(null != mHandler){
    		mHandler.removeCallbacksAndMessages(null);
    	}
        if (mHandlerCache != null) {
            mHandlerCache.unRegisterHandler(mHandler);
        }
        // Cancel all Unfinished UI download task
        mFileLoader.cancelDownload(mFileDownloadTaskList);
        super.finish();
    }

    protected void initActionBar() {
        mActionBar = getActionBar();
        if(null != mActionBar){
        	mActionBar.setHomeButtonEnabled(true);
        	mActionBar.setDisplayShowTitleEnabled(true);
        	mActionBar.setLogo(R.drawable.action_bar_back_icon);
        }
    }

    /** Allow ImageLoader to stop load when ListView onScroll and onFling */
    protected void applyScrollListener(AbsListView view) {
        if (view != null) {
            view.setOnScrollListener(new PauseOnScrollListener(mImageLoader,
                    pauseOnScroll, pauseOnFling));
        }
    }

    protected File downloadGetLocalFile(FileType fileType, String url) {
        return mFileLoader.getLocalFile(fileType, url);
    }

    protected int downloadFile(FileType fileType, String url) {
        int taskID = mFileLoader.downloadFile(FileLoader.FileLoadType.UI, fileType, url);
        if (taskID > 0)
            mFileDownloadTaskList.add(taskID);
        return taskID;
    }

    protected int downloadFile(FileType fileType, String url,
            FileLoader.FileLoadListener fileLoadListener) {
        int taskID = mFileLoader.downloadFile(FileLoader.FileLoadType.UI, fileType, url,
                fileLoadListener);
        if (taskID > 0)
            mFileDownloadTaskList.add(taskID);
        return taskID;
    }

    protected void downloadCancel(int taskID) {
        mFileLoader.cancelDownload(taskID);
        Iterator<Integer> iterator = mFileDownloadTaskList.iterator();
        while (iterator.hasNext()) {
            Integer tID = iterator.next();
            if (tID == taskID) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBack();
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    protected void onBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected boolean handlerBundle(Bundle bundle) {
        if (null == bundle) {
            return true;
        }

        if (!bundle.containsKey(PID_FLAG)) {
            return true;
        }

        if (MY_PID == bundle.getInt(PID_FLAG)) {
            return true;
        }

        this.finish();
        return false;
    }

    @Override
	protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putInt(PID_FLAG, MY_PID);
    }

    public void setAutoDismiss(final Context context, final Dialog dialog,
            int delay) {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (context != null && dialog != null && dialog.isShowing()) {
                    if (context instanceof Activity) {
                        if (!((Activity) context).isFinishing()) {
                            dialog.dismiss();
                        }
                    }
                }
            }
        }, delay <= 0 ? 1000 : delay);
    }

    public void setAutoDismissAndFinish(final Context context,
            final Dialog dialog, int delay) {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (context != null && dialog != null && dialog.isShowing()) {
                    if (context instanceof Activity) {
                        if (!((Activity) context).isFinishing()) {
                            dialog.dismiss();
                            ((Activity) context).finish();
                        }
                    }
                }
            }
        }, delay <= 0 ? 500 : delay);
    }

    public void resetProgressMessage(Context context,
            HenghenghuiProgressDialog dialog, String message, Drawable d) {
        if (context != null && dialog != null && dialog.isShowing()) {
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing()) {
                    dialog.resetMessage(message);
                    dialog.setMessageDrawable(d);
                }
            }
        }
    }

    public void dismissDialogSafety(Context context, Dialog dialog) {
        if (context != null && dialog != null && dialog.isShowing()) {
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing()) {
                    dialog.dismiss();
                }
            }
        }
    }

    protected void bindOnClickLister(OnClickListener onClick, View... views) {
        if (onClick == null) {
            return;
        }
        if (views == null) {
            return;
        }
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(onClick);
            }
        }
    }

    protected void bindOnTouchLister(OnTouchListener onTouch, View... views) {
        if (onTouch == null) {
            return;
        }
        if (views == null) {
            return;
        }
        for (View view : views) {
            if (view != null) {
                view.setOnTouchListener(onTouch);
            }
        }
    }

    @Override
    public final void handleCommonMsg(Message msg) {

    }

    protected DisplayMetrics getDisplayMetrics() {
        DisplayMetrics dm = this.getApplicationContext().getResources()
                .getDisplayMetrics();
        return dm;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUI(newConfig, getDisplayMetrics());
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void setUI(Configuration newConfig, DisplayMetrics dm);

    public static String getRequestBody(String jsonString){
        L.d("csy", "jsonString:" + jsonString);
        return "{"+"\"request\""+":" + jsonString + "}";
    }

    public void bindPushClient(){
        String uid = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", uid);
            jsonObj.put("channelId", MyPushMessageReceiver.ClientId);
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        L.d(TAG, "onResponse:" + getRequestBody(jsonString));
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_CHANNEL_ID)
                .tag(Urls.ACTION_CHANNEL_ID)
                .postJson(getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                    }
                });
    }
}
