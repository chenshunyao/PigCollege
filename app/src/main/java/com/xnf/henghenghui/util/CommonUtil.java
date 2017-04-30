package com.xnf.henghenghui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xnf.henghenghui.HengHengHuiAppliation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;

public class CommonUtil {

  /**
   * 全局上下文
   */
  public static Context appContext = HengHengHuiAppliation.getInstance().getApplicationContext();

  public static final String UPDATE_AVATAR_ACTION = "UPDATE_AVATAR_ACTION";

  public static String getDeviceInfo(Context context) {
    try{
      org.json.JSONObject json = new org.json.JSONObject();
      android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
              .getSystemService(Context.TELEPHONY_SERVICE);

      String device_id = tm.getDeviceId();

      android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

      String mac = wifi.getConnectionInfo().getMacAddress();
      json.put("mac", mac);

      if( TextUtils.isEmpty(device_id) ){
        device_id = mac;
      }

      if( TextUtils.isEmpty(device_id) ){
        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
      }

      json.put("device_id", device_id);

      return json.toString();
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }

  public static int getDeviceWidth(Context context){
    Point point = new Point();
    ((Activity)context).getWindowManager().getDefaultDisplay().getSize(point);
    int width = point.x;
    Log.d("commonUtil","getDeviceWidth width:"+width);
    return width;
  }

  //获取版本号
  public static int getVersionCode(Context context){
    try {
      return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
    }catch(PackageManager.NameNotFoundException e){
      return 0;
    }
  }

  /**
   * 获取加密后的密码
   */
  public static String getEncryptPassWd(String originalPasswd){
    return EncryptUtil.encryptBASE64(MD5Calculator.calculateMD5(originalPasswd));
  }

}
