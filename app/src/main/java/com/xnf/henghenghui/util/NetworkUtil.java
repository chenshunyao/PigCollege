package com.xnf.henghenghui.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class NetworkUtil {

  private static String LOG_TAG = "NetworkUtil";

  public static Uri uri = Uri.parse("content://telephony/carriers");

  public static final int NETWORK_TYPE_NONE = 0x0; // 断网情况
  public static final int NETWORK_TYPE_WIFI = 0x1; // WiFi模式
  public static final int NETWOKR_TYPE_MOBILE = 0x2; // gprs模式

  public static final int SETTING_TYPE_WIFI = 0x1; // WIFI ONLY
  public static final int SETTING_TYPE_ALL = 0x2; // ALL

  public static String LOGIN_STATE_DATA = "login_state_data";
  public static String NET_SETTING_OPTION = "net_setting_option";

  public static boolean checkNetState(Context context) {
    boolean netstate = false;
    ConnectivityManager connectivity =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity != null) {
      NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (int i = 0; i < info.length; i++) {
          if (info[i].getState() == NetworkInfo.State.CONNECTED) {
            netstate = true;
            break;
          }
        }
      }
    }
    return netstate;
  }

  /**
   * 判断MOBILE网络是否可用
   * 
   * @param context
   * @return
   * @throws Exception
   */
  public static boolean isMobileDataEnable(Context context) throws Exception {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean isMobileDataEnable = false;

    isMobileDataEnable =
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            .isConnectedOrConnecting();

    return isMobileDataEnable;
  }

  /**
   * 判断是否有网络连接
   */
  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivity =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (connectivity == null) {
      Debug.print(Debug.MODULE_OTHERS,NetworkUtil.class,"couldn't get connectivity manager");
    } else {
      NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (int i = 0; i < info.length; i++) {
          if (info[i].isConnected()) {
            Debug.print(Debug.MODULE_OTHERS,NetworkUtil.class,info[i].toString() + "network is available");
            return true;
          }
        }
      }
    }
    Debug.print(Debug.MODULE_OTHERS,NetworkUtil.class, "network is not available");
    return false;
  }

  /**
   * 判断网络是否为漫游
   */
  public static boolean isNetworkRoaming(Context context) {
    ConnectivityManager connectivity =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity == null) {
      Debug.print(Debug.MODULE_OTHERS,NetworkUtil.class,"couldn't get connectivity manager");
    } else {
      NetworkInfo info = connectivity.getActiveNetworkInfo();
      if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
        TelephonyManager tm =
            (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null && tm.isNetworkRoaming()) {
          Debug.print(Debug.MODULE_OTHERS,NetworkUtil.class, "network is roaming");
          return true;
        } else {
          Debug.print(Debug.MODULE_OTHERS,NetworkUtil.class,"network is not roaming");
        }
      } else {
        Debug.print(Debug.MODULE_OTHERS,NetworkUtil.class,"not using mobile network");
      }
    }
    return false;
  }

  /**
   * 判断wifi 是否可用
   * 
   * @param context
   * @return
   * @throws Exception
   */
  public static boolean isWifiDataEnable(Context context) throws Exception {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean isWifiDataEnable = false;
    isWifiDataEnable =
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
    return isWifiDataEnable;
  }

//  /**
//   * 获取无线网卡的地址
//   *
//   * @param context
//   * @return
//   */
//  public static String getWifiMacAddress() {
//    // 在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
//    String macAddress = null;
//    WifiManager wifiMgr =
//        (WifiManager) MultimediaApp.getInstance().getSystemService(Context.WIFI_SERVICE);
//    WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
//    if (null != info) {
//      macAddress = info.getMacAddress();
//    }
//    return macAddress;
//  }

  /**
   * 获取设备的有线地址
   * 
   * @return
   */
  public static String getLanMacAddress() {
    String macSerial = null;
    String str = "";
    try {
      Process pp = Runtime.getRuntime().exec("cat /sys/class/net/eth0/address ");
      InputStreamReader ir = new InputStreamReader(pp.getInputStream());
      LineNumberReader input = new LineNumberReader(ir);


      for (; null != str;) {
        str = input.readLine();
        if (str != null) {
          macSerial = str.trim();// 去空格
          break;
        }
      }
    } catch (IOException ex) {
      // 赋予默认值
      ex.printStackTrace();
    }
    return macSerial;
  }

  /**
   * 获取设备的序列号
   */
  public static String getSerialNumber() {
    String serial = Build.SERIAL;
    if (serial != null && !serial.equals("")) {
      return serial;
    } else {
      return "Unknown";
    }
  }

  /**
   * 获取IMEI号
   */
  public  static String getIMEI(Context context){
    TelephonyManager telephonyManager =
            (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    return telephonyManager.getDeviceId();
  }

  private static int getCurrentNetType(Context context) {
    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
    NetworkInfo gprs = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // gprs

    if (wifi != null && wifi.getState() == NetworkInfo.State.CONNECTED) {
      return NETWORK_TYPE_WIFI;
    } else if (gprs != null && gprs.getState() == NetworkInfo.State.CONNECTED) {
      return NETWOKR_TYPE_MOBILE;
    }
    return NETWORK_TYPE_NONE;
  }

  public static  boolean enableConneting(Context context) {
    if ((getCurrentNetType(context) == NETWORK_TYPE_NONE)) {
      return false;
    }
    return true;
  }


}
