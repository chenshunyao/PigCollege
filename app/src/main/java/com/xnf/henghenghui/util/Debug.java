package com.xnf.henghenghui.util;

import java.util.HashMap;
import android.util.Log;

/**
 * @author Description: 封装Log，提供简便的打印日志的方法。
 */
public class Debug {
  public static final String TAG = "Allkids";
  //public static boolean isEnabled = true;
  public static boolean isEnabled = false;

  // 各个模块ID
  public static final int MODULE_MAIN = 0;
  public static final int MODULE_COURSEWARE_LIST = 1;
  public static final int MODULE_DOWNLOADED = 2;
  public static final int MODULE_DOWNLOADING = 3;
  public static final int MODULE_FAVORITELIST = 4;
  public static final int MODULE_MYCOURSEWARE = 5;
  public static final int MODULE_UPDATE = 6;
  public static final int MODULE_SCHEDULE = 7;
  public static final int MODULE_COURSEALBUM_DETAIL = 8;
  public static final int MODULE_USERCENTER = 9;
  public static final int MODULE_OTHERS = 10;


  // 声明模块名和模块ID的映射表
  public static final HashMap<Integer, String> moduleMapping = new HashMap<Integer, String>();

  // 初始化模块名和ID映射表
  static {
    moduleMapping.put(MODULE_MAIN, "Main");
    moduleMapping.put(MODULE_COURSEWARE_LIST, "Courseware List");
    moduleMapping.put(MODULE_DOWNLOADED, "Downloaded");
    moduleMapping.put(MODULE_DOWNLOADING, "Downloading");
    moduleMapping.put(MODULE_FAVORITELIST, "Favorite List");
    moduleMapping.put(MODULE_MYCOURSEWARE, "My Courseware");
    moduleMapping.put(MODULE_UPDATE, "Update");
    moduleMapping.put(MODULE_SCHEDULE, "Schedule");
    moduleMapping.put(MODULE_COURSEALBUM_DETAIL, "CourseAlbum Detail");
    moduleMapping.put(MODULE_USERCENTER, "UserCenter");
    moduleMapping.put(MODULE_OTHERS, "Others");
  }

  /**
   * 按模块名:类名， 打印严重信息
   * 
   * @param moduleId
   * @param currentCls
   * @param msg void TODO
   */
  public static void error(int moduleId, Object currentCls, String msg) {
    if (isEnabled) {
      Log.e(TAG, getTraceInfo() + " " + msg);
    }
  }

  public static void error(String msg) {
    if (isEnabled) {
      Log.e(TAG, getTraceInfo() + " " + msg);
    }
  }

  /**
   * @author
   * @Description:获取日志详细信息--(java文件 方法名 代码行) 方便调试定位问题
   */
  public static String getTraceInfo() {
    StringBuffer sb = new StringBuffer();
    StackTraceElement[] stacks = new Throwable().getStackTrace();
    String classname = stacks[2].getClassName();
    int lastindex = classname.lastIndexOf(".") + 1;
    if (lastindex >= 0) {
      classname = classname.substring(lastindex);
    }
    sb.append("[" + classname).append(".java->").append(stacks[2].getMethodName()).append("()->")
        .append(stacks[2].getLineNumber() + "]");
    // sb.append("[memory->"+Formatter.formatFileSize(FusionField.appContext,
    // Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "]");
    return sb.toString();
  }

  /**
   * 按模块名:类名，打印日志
   * 
   * @param moduleId
   * @param currentCls
   * @param msg void TODO
   */
  public static void print(int moduleId, Object currentCls, String msg) {
    if (isEnabled) {
      Log.i(TAG, getTraceInfo() + " " + msg);
    }
  }

  public static void print(String msg) {
    if (isEnabled) {
      Log.i(TAG, getTraceInfo() + " " + msg);
    }
  }
}
