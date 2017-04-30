package com.xnf.henghenghui.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.hyphenate.chatuidemo.DemoModel;
import com.google.gson.Gson;
import com.hyphenate.chatuidemo.domain.RobotUser;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.io.FileLoader;
import com.xnf.henghenghui.model.HttpUserListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.MD5Calculator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Response;


/**
 * 日志服务，日志默认会存储在SDcar里,如果没有SDcard会存储在内存中的安装目录下面。 
 * 1.本服务默认在SDcard中每天生成一个日志文件,
 * 2.如果有SDCard的话会将之前内存中的文件拷贝到SDCard中 
 * 3.如果没有SDCard，在安装目录下只保存当前在写日志
 * 4.SDcard的装载卸载动作会在步骤2,3中切换 
 * 5.SDcard中的日志文件只保存7天 
 * 6.监视App运行的LogCat日志
 * （sdcard mounted）:sdcard/cmcc/qixin/log/yyyy-MM-dd HHmmss.log（默认） 
 * 7.监视App运行的LogCat日志
 * （sdcard unmounted）:/data/data/包名/files/log/yyyy-MM-dd HHmmss.log
 * 8.本service的运行日志：/data/data/包名/files/log/Log.log
 *
 * 
 */
public class ChatService extends Service {
	private static final String TAG = "ChatService";

    private boolean isLoading = false;

	private DemoModel emModel = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isLoading) {
            new ChatUserSyncThread().start();
        }
		return Service.START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		Log.i(TAG, "ChatService onCreate");
		emModel = new DemoModel(this);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class ChatUserSyncThread extends Thread{

		@Override
		public void run() {
            isLoading = true;
            try{
                List<RobotUser> robotList = new ArrayList<RobotUser>();

                String userId = LoginUserBean.getInstance().getLoginUserid();
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("userId", userId);
                    jsonObj.put("qryUserType", "1");
                    jsonObj.put("startRowNum", "");
                    jsonObj.put("endRowNum", "");
                } catch (Exception e) {
                }
                String jsonString = jsonObj.toString();
                Response response = OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_USER_CONTACT_LIST)
                        .tag(Urls.ACTION_USER_CONTACT_LIST)
                        .postJson("{" + "\"request\"" + ":" + jsonString + "}")
                        .execute();
                if(response.isSuccessful()) {
                    String body = response.body().string();
                    if (Utils.getRequestStatus(body) == 1) {
                        Gson gson = new Gson();
                        HttpUserListResponse httpUserListResponse = gson.fromJson(body, HttpUserListResponse.class);
                        for(HttpUserListResponse.Content content : httpUserListResponse.getResponse().getContent()){
                            RobotUser user = new RobotUser(MD5Calculator.calculateMD5(content.getUserId()));
                            user.setNick(content.getUserName());
                            user.setAvatar(content.getPhoto());
			                robotList.add(user);
                        }
                    }
                }

                jsonObj = new JSONObject();
                try {
                    jsonObj.put("userId", userId);
                    jsonObj.put("qryUserType", "2");
                    jsonObj.put("startRowNum", "");
                    jsonObj.put("endRowNum", "");
                } catch (Exception e) {
                }
                jsonString = jsonObj.toString();
                response = OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_USER_CONTACT_LIST)
                        .tag(Urls.ACTION_USER_CONTACT_LIST)
                        .postJson("{"+"\"request\""+":" + jsonString + "}")
                        .execute();
                if(response.isSuccessful()) {
                    String body = response.body().string();
                    if (Utils.getRequestStatus(body) == 1) {
                        Gson gson = new Gson();
                        HttpUserListResponse httpUserListResponse = gson.fromJson(body, HttpUserListResponse.class);
                        for(HttpUserListResponse.Content content : httpUserListResponse.getResponse().getContent()){
                            RobotUser user = new RobotUser(MD5Calculator.calculateMD5(content.getUserId()));
                            user.setNick(content.getUserName());
                            user.setAvatar(content.getPhoto());
                            robotList.add(user);
                        }
                    }
                }

                emModel.saveRobotList(robotList);
            }catch(Exception e){
            }finally{
                isLoading = false;
            }
		}
	}
}
