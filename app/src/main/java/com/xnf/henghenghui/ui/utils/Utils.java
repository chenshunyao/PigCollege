
package com.xnf.henghenghui.ui.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.io.FileType;
import com.xnf.henghenghui.util.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Utils {

    private static final String TAG = "Utils";

    public static final String LOGIN_OUT_ACTION="login_out_action";

    /*****************************************FLAG********************************************/
    /**
     * 注册后为了完善
     */
    public  static final String REGISTER_IMPLETE_INFO = "REGISTER_IMPLETE_INFO";
    /**
     * 待完善的用户ID信息
     */
    public  static final String REGISTER_IMPLETE_USER_ID = "REGISTER_IMPLETE_USER_ID";
    /**
     * 修改个人信息
     */
    public static final String MODIFY_PERSIONAL_INFO = "MODIFY_PERSIONAL_INFO";
    /**
     * 注册的用户类型，不同的类型，完善资料的信息也不一样
     */
    public static final String REGISTER_USER_TYPE = "REGISTER_USER_TYPE";
    /**
     * 注册的类型为专家
     */
    public static final int REGISTER_USER_EXPERT =1;
    /**
     * 注册的类型为普通用户
     */
    public static final int REGISTER_USER_NORMAL =2;

    /**
     * 用于startActivityForResult
     */
    public static final int PHOTO_REQUEST_TAKEPHOTO = 2001;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = PHOTO_REQUEST_TAKEPHOTO+1;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = PHOTO_REQUEST_TAKEPHOTO+2;//
    public static final int UPDATE_USERNAME = PHOTO_REQUEST_TAKEPHOTO+3;//
    public static final int UPDATE_USERNICK = PHOTO_REQUEST_TAKEPHOTO+4;//
    public static final int CITY_REQUEST = PHOTO_REQUEST_TAKEPHOTO+5;// 选择城市
    public static final int CITY_REQUEST_SUCCESS = PHOTO_REQUEST_TAKEPHOTO+6;// 选择成功
    public static final int FENLEI_REQUEST = PHOTO_REQUEST_TAKEPHOTO+7;// 选择分类
    public static final int FENLEI_REQUEST_SUCCESS = PHOTO_REQUEST_TAKEPHOTO+8;// 选择成功
    public static final int QA_TYPE_REQUEST = PHOTO_REQUEST_TAKEPHOTO+9;// 选择提问分类
    public static final int QA_TYPE_REQUEST_SUCCESS = PHOTO_REQUEST_TAKEPHOTO+10;// 选择提问成功
    public static final int UPDATE_USER_ADDRESS= PHOTO_REQUEST_TAKEPHOTO+11;// 选择成功
    public static final int UPDATE_WORK_UNIT = PHOTO_REQUEST_TAKEPHOTO+12;// 选择成功
    public static final int UPDATE_JOB_TITLE = PHOTO_REQUEST_TAKEPHOTO+13;// 选择成功
    public static final int UPDATE_EMAIL = PHOTO_REQUEST_TAKEPHOTO+14;// 更新EMAIL
    public static final int UPDATE_RAISING_SCALE = PHOTO_REQUEST_TAKEPHOTO+15;//
    public static final int UPDATE_BREED = PHOTO_REQUEST_TAKEPHOTO+16;//
    public static final int UPDATE_ENTERPRISE_TYPE = PHOTO_REQUEST_TAKEPHOTO+17;//
    public static final int UPDATE_DUTIES= PHOTO_REQUEST_TAKEPHOTO+18;//
    public static final int UPDATE_FARM_NAME = PHOTO_REQUEST_TAKEPHOTO+19;//
    public static final int UPDATE_FARM_ADDRESS = PHOTO_REQUEST_TAKEPHOTO+20;//
    public static final int UPDATE_SKILLED_IN= PHOTO_REQUEST_TAKEPHOTO+21;//
    public static final int UPDATE_BRIRF_INTRODUCTION= PHOTO_REQUEST_TAKEPHOTO+22;//
    public static final int REQUEST_CROP = 6709;

    //收藏的类型
    public static final String TYPE_ARTICLE = "AR";
    public static final String TYPE_VIDEO = "CV";
    public static final String TYPE_MEETING = "CF";
    public static final String TYPE_QUESTION = "AQ";
    public static final String TYPE_TOPIC  = "TP";
    public static final String TYPE_SUBJECT = "ST";

    //会评的类型
    public static final String CONFERENCE_TYPE_PREPARE = "0";
    public static final String CONFERENCE_TYPE_LIVE = "1";
    public static final String CONFERENCE_TYPE_END = "2";

    /**
     * 关闭 Activity
     *
     * @param activity
     */
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }

    /**
     * 打开Activity
     *
     * @param activity
     */
    public static void start_Activity(Activity activity,Intent intent){
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);

    }

    /*
     * judge the status of SIM card
     * @return true if sim card is ok,otherwise is false
     */
    public static boolean isSimStatusOk(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        boolean isOk = true;
        switch (tm.getSimState()) {
            case TelephonyManager.SIM_STATE_UNKNOWN:
            case TelephonyManager.SIM_STATE_ABSENT:
                isOk = false;
                break;
            default:
                break;
        }
        return isOk;
    }
    
    
    public static boolean hasNoNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        boolean connected = false;
        if (wifiNetInfo.isConnected()) {
            connected = true;
        } else {
            if (mobNetInfo != null) {
                if (mobNetInfo.isConnected())
                    connected = true;
            }
        }

        return !connected;
    }

    public static boolean isPhoneNum(String numStr) {
        if (numStr.matches("^([0-9]{11})?$")) {
            return true;
        }
        return false;
    }

    public static int getRequestStatus(String jsonString){
        int ret=0;
        try {
            JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("response");
            ret = jsonObject.getInt("succeed");
            L.d("csy","getRequestStatus ret:"+ret);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;

    }

    public static String getErrorCode(String jsonString){
        String ret = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("response");
            ret = jsonObject.getString("errorCode");
            L.d("csy","getRequestStatus ret:"+ret);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;

    }
}
