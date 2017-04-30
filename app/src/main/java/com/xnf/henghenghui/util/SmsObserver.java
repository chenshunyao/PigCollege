package com.xnf.henghenghui.util;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;

import com.xnf.henghenghui.dao.SmsDao;
import com.xnf.henghenghui.model.SmsInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/22.
 */

public class SmsObserver extends ContentObserver {
    private static final String TAG = "SmsObserver";
    private Context context;
    private Handler mHandler;

    public SmsObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
        this.mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
         L.d(TAG, "onChange");
        super.onChange(selfChange);
        SmsDao smsDao = new SmsDao(context, null);
        long date = System.currentTimeMillis()-60*1000;
        SmsInfo info = smsDao.getSmsInfo(date);
        if (info == null) {
            return;
        }
        L.d(TAG, "message = " + info.getSmsbody() + "number = " + info.getPhoneNumber());
        if ("10690203444132225970".equals(info.getPhoneNumber())) {
            String code = getValidCode(info.getSmsbody());
            L.d(TAG, "valid code = " + code);
            //TODO ：这里需要对短信进行解析
            Message msg = new Message();
            msg.what = CodeUtil.GET_VCODE_SUCCESS;
            msg.getData().putString("code", code);
            mHandler.sendMessage(msg);
        }
    }

    private String getValidCode(String code) {
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(code);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }
}
