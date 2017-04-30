package com.xnf.henghenghui.ui.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.io.FileLoader;
import com.xnf.henghenghui.io.FileType;
import com.xnf.henghenghui.ui.utils.HandlerInterface;
import com.xnf.henghenghui.ui.utils.HenghenghuiHandler;
import com.xnf.henghenghui.ui.view.HenghenghuiProgressDialog;
import com.xnf.henghenghui.util.L;

public abstract class BaseFragment extends Fragment implements HandlerInterface {

    private static final String TAG = "BaseFragment";
    
    protected Activity activity;

    protected Bundle bundle;
    
    private View rootView;
    
    protected ImageLoader mImageLoader = ImageLoader.getInstance();
    private FileLoader mFileLoader= FileLoader.getInstance();
    private ArrayList<Integer> mFileDownloadTaskList=new ArrayList<Integer>();

    protected boolean pauseOnScroll = false;
    protected boolean pauseOnFling = true;
    protected HenghenghuiHandler mHandler;
    protected DisplayImageOptions mOptions;
    //protected boolean isVisible;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        L.d(setTag(), "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.d(setTag(), "onActivityCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.d(setTag(), "onPause");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(setTag(), "onCreate");
        if (null == savedInstanceState) {
            bundle = null == getArguments() ? new Bundle() : new Bundle(getArguments());
        } else {
            bundle = savedInstanceState;
        }
        initData(bundle);
        mHandler = new HenghenghuiHandler(this);
        setUIHandler();
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
    }

    protected void setUIHandler() {
       //mHandlerCache = new HandlerCache();
        //mHandlerCache.registerHandler(mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            L.d(setTag(), "onCreateView");
            if(rootView == null){  
                rootView = initView(inflater, container, bundle);
            }
            
            if(null != rootView){
                ViewGroup parent = (ViewGroup) rootView.getParent();  
                if (parent != null) {  
                    parent.removeView(rootView);  
                }   
            }

            return rootView;
    }
    
       @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        L.d(setTag(), "setUserVisibleHint " + isVisibleToUser);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        L.d(setTag(), "onResume");
    }
    
    private String setTag(){
        return TextUtils.isEmpty(setFragmentTag()) ? TAG : setFragmentTag();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(setTag(), "onDestroy");
//        if (mHandlerCache != null) {
//            mHandlerCache.unRegisterHandler(mHandler);
//        }
//        mFileLoader.cancelDownload(mFileDownloadTaskList);
    }

    @Override
    public void onDestroyView() {
        L.d(setTag(), "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        L.d(setTag(), "onDetach");
        super.onDetach();
    }
    
    
    /**   Allow ImageLoader to stop load when ListView onScroll and onFling        */
    protected void applyScrollListener(AbsListView view) {
        if(view != null){
            view.setOnScrollListener(new PauseOnScrollListener(mImageLoader, pauseOnScroll, pauseOnFling));
        }
    }
    
    protected  File downloadGetLocalFile(FileType fileType,String url){
        return mFileLoader.getLocalFile(fileType, url);
    }    
    
    protected int downloadFile(FileType fileType, String url){
        int taskID= mFileLoader.downloadFile(FileLoader.FileLoadType.UI,  fileType, url);
        if(taskID>0) mFileDownloadTaskList.add(taskID);
        return taskID;
    }
    
    protected  int downloadFile(FileType fileType,String url, FileLoader.FileLoadListener fileLoadListener){
        int taskID=mFileLoader.downloadFile(FileLoader.FileLoadType.UI, fileType, url,fileLoadListener);
        if(taskID>0)  mFileDownloadTaskList.add(taskID);
        return taskID;
    }    
    
    protected void downloadCancel(int taskID)
    {
        mFileLoader.cancelDownload(taskID);
        Iterator<Integer>  iterator=  mFileDownloadTaskList.iterator();
        while (iterator.hasNext()) {
            Integer tID=iterator.next();
            if(tID==taskID)
            {
                iterator.remove();
                break;
            }            
        }
    }

    @Override
    public final void handleCommonMsg(Message msg) {
        
    }

    public void setAutoDismiss(final Context context,
            final Dialog dialog, int delay) {
//        mHandler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                if (context != null && dialog != null && dialog.isShowing()) {
//                    if (context instanceof Activity) {
//                        if (!((Activity) context).isFinishing()) {
//                            dialog.dismiss();
//                        }
//                    }
//                }
//            }
//        }, delay <= 0 ? 1000 : delay);
    }
    
    public void resetProgressMessage(Context context,HenghenghuiProgressDialog dialog,String message,Drawable d) {
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

    protected abstract String setFragmentTag();

    protected abstract void initData(Bundle bundle);

    protected abstract View initView(LayoutInflater inflater,
            ViewGroup container, Bundle bundle);
}
