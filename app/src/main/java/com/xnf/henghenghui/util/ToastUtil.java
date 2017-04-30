package com.xnf.henghenghui.util;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xnf.henghenghui.R;

public class ToastUtil {

    private static Toast mToast = null;
    private static String TAG=ToastUtil.class.getSimpleName();
    private static void createToast(Context context, String msg,int duration) {
        
       if(Looper.myLooper()==null || Looper.myLooper()!=Looper.getMainLooper())
        {
            L.e(TAG, "can't toast not in UI thread");
            return;
        }
        
        if (mToast == null) {
            mToast = new Toast(context.getApplicationContext());
            LayoutInflater mInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = mInflater.inflate(R.layout.henghenghui_toast, null);
            TextView toastText = (TextView) v.findViewById(R.id.toast_text);
            toastText.setText(msg);
            mToast.setView(v);
        }else{
            View toastView = mToast.getView();
            if(null != toastView){
                TextView tx = (TextView) toastView .findViewById(R.id.toast_text);
                tx.setText(msg);
            }
        }
        mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                0, 0);
        mToast.setDuration(duration);
        mToast.show();
    }

    public static void showToast(Context context, String msg) {
        createToast(context, msg,Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context,int id){
        createToast(context, context.getResources().getString(id),Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, String msg) {
        createToast(context, msg,Toast.LENGTH_LONG);
    }
    
    /**
     * show a toast in a thread;
     * @param context
     * @param msg
     */
    public static void showToastFromThread(final Context context,final String msg)
    {
        
        if(Looper.myLooper()==Looper.getMainLooper() && Looper.getMainLooper()!=null)
        {
            L.w(TAG, "you have been in main thread ,just call showToast()");
            showToast(context, msg);
            return;
        }
        
        Looper looper=Looper.getMainLooper();
        if(looper==null)
        {
            L.e(TAG, "can't create Handler without MainLooper..");
            return;
        }
        new Handler(looper).post(new Runnable() {
            @Override
            public void run() {
                showToast(context, msg);
            }
        });
    }

    public static void cancel() {
        if(mToast != null) {
            mToast.cancel();
        }
    }
}
