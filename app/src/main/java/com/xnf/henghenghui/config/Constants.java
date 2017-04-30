package com.xnf.henghenghui.config;

import android.os.Environment;

import com.xnf.henghenghui.util.FileUtil;
import com.xnf.henghenghui.util.NetworkUtil;

import java.io.File;


public class Constants {

   public static final String INTRO_SHOW_TAG="is_intropage_show";

   public static String NAME = "NAME";

   // APP_ID 替换为你的应用从官方网站申请到的合法appId
   public static final String APP_ID = "wxd930ea5d5a258f4f";

   public static final String WEICHAT_APPID ="wxfd8d3a4daa0311e4";

   public static final String QQ_APPID = "100942993";
   public static final String QQ_APPKEY = "8edd3cc7ca8dcc15082d6fe75969601b";

   public static final String INTENT_ACTION_LOGOUT = "henghenghui.action.LOGOUT";

   public static String fileLocation = null;
   static {
      fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
              + "Henghenghui"+ File.separator;
      File file = new File(fileLocation);
      if (!file.exists()) {
         file.mkdirs();
      }
   }
}
