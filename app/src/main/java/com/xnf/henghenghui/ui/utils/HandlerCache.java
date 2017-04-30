package com.xnf.henghenghui.ui.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.xnf.henghenghui.util.Cache;
import com.xnf.henghenghui.util.L;


public class HandlerCache implements Handler.Callback {

    private static final String TAG = "HandlerCache";

    public static List<Handler> sUIHandlers =  new ArrayList<Handler>();
    
    public HandlerCache() {
        Cache.getCache().setCallBack(this);
    }

    public boolean registerHandler(Handler handler) {
        boolean result;
        synchronized (sUIHandlers) {
            result = sUIHandlers.add(handler);
        }
        Cache.getCache().setCallBack(this);
        return result;
    }

    public boolean unRegisterHandler(Handler handler) {
        if (sUIHandlers == null || sUIHandlers.size() == 0) {
            return true;
        }
        boolean result;
        synchronized (sUIHandlers) {
            result = sUIHandlers.remove(handler);
        }
        Cache.getCache().setCallBack(this);
        return result;
    }

    @Override
    public boolean handleMessage(Message message) {
        if(null == message){
            L.e(TAG, "bad msg : message is null.");
            return false;
        }
        L.d(TAG, "handleMessage code = " + Integer.toHexString(message.what));
        if(null == sUIHandlers){
            L.e(TAG, "sUIHandlers is null.");
            return false;
        }
        
        synchronized (sUIHandlers) {
            Iterator<Handler> iterator = sUIHandlers.iterator();
            while (iterator.hasNext()) {
                Handler handler = iterator.next();
                if (handler == null) {
                    continue;
                }
                handler.sendMessage(Message.obtain(message));
            }
        }
        return false;
    }

}
