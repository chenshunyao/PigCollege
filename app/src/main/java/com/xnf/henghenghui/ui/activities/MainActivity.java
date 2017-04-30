package com.xnf.henghenghui.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lzy.okhttputils.OkHttpUtils;
import com.umeng.analytics.MobclickAgent;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.service.ChatService;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.base.BaseFragment;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.dialog.DialogControl;
import com.xnf.henghenghui.ui.fragments.F2FCatergoryFragment;
import com.xnf.henghenghui.ui.fragments.Face2FaceFragment;
import com.xnf.henghenghui.ui.fragments.MainFragment;
import com.xnf.henghenghui.ui.fragments.CourseFragment;
import com.xnf.henghenghui.ui.fragments.MeFragment;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.CustomProgressDialog;
import com.xnf.henghenghui.ui.view.MyRadioButton;
import com.xnf.henghenghui.update.AppManager;
import com.xnf.henghenghui.update.DownloadManager;
import com.xnf.henghenghui.update.DownloadProgressListener;
import com.xnf.henghenghui.update.FileDownloader;
import com.xnf.henghenghui.update.UpdateManager;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.CommonUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.NetworkUtil;
import com.xnf.henghenghui.util.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements OnClickListener,F2FCatergoryFragment.OnFragmentInteractionListener,DialogControl {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MyRadioButton tab1, tab2, tab3, tab4;
    private TextView tab_1_n, tab_3_n;
    private ViewGroup mainTopSearchBar;
    private ViewGroup mainTopSettingBar;
    private EditText editQaSearch;
    private ImageView mImgQaClear;
    private TextView editQaSearchHint;
    private ImageView mSettingBtn;
    private ImageView mSearchBtn;
    private ImageView mSaoBtn;

    private List<Map<String, String>> appsInfo = null;

    @SuppressLint("UseSparseArrays")
    private CustomProgressDialog progressDialog;

    private UpdateManager updateManager = null;

    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载失败 */
    private static final int DOWNLOAD_ERROR = 3;

    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    /* 更新进度条 */
    //private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    private Map<String, String> appInfo;

    private ProgressDialog mDownloadProgressDialog;

    //// TODO: 2016/1/20
    /**
     * qixin TAG
     */
    private String mMainFragmentTag = "main";
    /**
     * contact TAG
     */
    private String mHotFragmentTag = "hot";
    /**
     * me TAG
     */
    private String mCourseFragmentTag = "course";
    /**
     * setting TAG
     */
    private String mMeFragmentTag = "me";

    private String currentTag = mMainFragmentTag;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // onSaveInstanceState是用来保存UI状态的，你可以使用它保存你所想保存的东西，在Activity杀死之前，它一般在onStop或者onPause之前触发
        outState.putString("tag", currentTag);
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // onRestoreInstanceState则是在onResume之前触发回复状态，至于复写这个方法后onCreate方法是否会被调用。
        // 1.Activity被杀死了，onCreate会被调用，且onRestoreInstanceState 在 onResume之前恢复上次保存的信息。
        // 2.Activity没被杀死，onCreate不会被调用，但onRestoreInstanceState 仍然会被调用，在 onResume之前恢复上次保存的信息。
        currentTag = savedInstanceState.getString("tag", "");
    }

    @Override
    protected void initData() {
        registReceiver();
        startService(new Intent(this, ChatService.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        setTheme(R.style.AppBaseTheme);
        tab1 = (MyRadioButton) findViewById(R.id.tab_1);
        tab2 = (MyRadioButton) findViewById(R.id.tab_2);
        tab3 = (MyRadioButton) findViewById(R.id.tab_3);
        tab4 = (MyRadioButton) findViewById(R.id.tab_4);
        tab_1_n = (TextView) findViewById(R.id.tab_1_n);
        tab_3_n = (TextView) findViewById(R.id.tab_3_n);

        initTabButton(tab1,R.drawable.icon_tab_main);
        initTabButton(tab2,R.drawable.icon_tab_course);
        initTabButton(tab3,R.drawable.icon_tab_f2f);
        initTabButton(tab4,R.drawable.icon_tab_me);

        mImgQaClear = (ImageView) findViewById(R.id.img_qa_clear);
        editQaSearch = (EditText)findViewById(R.id.edit_qa_search);
        editQaSearchHint = (TextView)findViewById(R.id.edit_qa_search_hint);
        editQaSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editQaSearch.getText().length() > 0){
                    mImgQaClear.setVisibility(View.VISIBLE);
                }else{
                    mImgQaClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    editQaSearchHint.setVisibility(View.GONE);
                }else{
                    editQaSearchHint.setVisibility(View.VISIBLE);
                }
            }
        });
        editQaSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    Intent it = new Intent(MainActivity.this, QAListActivity.class);
                    it.putExtra("keyword", editQaSearch.getText().toString().trim());
                    Utils.start_Activity(MainActivity.this, it);
                    editQaSearch.setText("");
//                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(editQaSearch.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        mSearchBtn = (ImageView)findViewById(R.id.btn_search);
        mSaoBtn = (ImageView)findViewById(R.id.btn_sao);

        mainTopSearchBar =(ViewGroup)findViewById(R.id.main_top_searchbar_layout);
        mainTopSettingBar =(ViewGroup)findViewById(R.id.main_top_settingbar_layout);

        mSettingBtn = (ImageView)findViewById(R.id.setting_btn);
        mSettingBtn.setOnClickListener(this);
        //mainTopSearchBar.setVisibility(View.VISIBLE);
//        tab_title =(TextView)findViewById(R.id.main_top_ll_title);
//
//        tab_title.setText(R.string.tab_main);
        bindOnClickLister(this, tab1, tab2, tab3, tab4, mSearchBtn, mSaoBtn, mImgQaClear);

        if (!TextUtils.isEmpty(currentTag)) {
            if (mMainFragmentTag.equals(currentTag)) {
                onClick(tab1);
            } else if (mCourseFragmentTag.equals(currentTag)) {
                onClick(tab2);
            } else if (mHotFragmentTag.equals(currentTag)) {
                onClick(tab3);
            } else if (mMeFragmentTag.equals(currentTag)) {
                onClick(tab4);
            }
        } else {
            tab1.setChecked(true);
            turnToFragment(MainFragment.getInstance(new Bundle()),
                    mMainFragmentTag);
            currentTag = mMainFragmentTag;
        }

        String deviceInfo = CommonUtil.getDeviceInfo(this);
        L.d(TAG, "device info:" + deviceInfo);

        checkUpdate();
    }

    private void checkUpdate(){
        int version = CommonUtil.getVersionCode(this);
        checkAppUpdate(version);
    }

    public void checkAppUpdate(int currentVersion) {
        L.d(TAG, "currentVersion:" + currentVersion);

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("currentVersion", String.valueOf(currentVersion));
        } catch (Exception e) {
        }

        String jsonString = jsonObj.toString();

        L.d(TAG, "jsonString:" + "{" + "\"request\"" + ":" + jsonString + "}");

        OkHttpUtils.get("http://api.fir.im/apps/latest/582f38daca87a87bdf0013c7?api_token=64a6dd8c6840c734850410dcd6dbb29c")
                .tag(Urls.ACTION_CHECK_UPDATE)
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_CHECK_UPDATE;
                        msg.obj = (String) s;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    private void initTabButton(RadioButton btn,int drawableId){
//        Drawable drawableWeiHui = getResources().getDrawable(drawableId);
//        drawableWeiHui.setBounds(0, 0, getResources().getInteger(R.integer.main_radio_btn_width),
//                getResources().getInteger(R.integer.main_radio_btn_height));//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
//        btn.setCompoundDrawables(null, drawableWeiHui, null, null);//只放上面

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_1:
                setTitle(getString(R.string.app_name));
                mainTopSearchBar.setVisibility(View.VISIBLE);
                mainTopSettingBar.setVisibility(View.GONE);
                tab1.setChecked(true);
                turnToFragment(MainFragment.getInstance(new Bundle()),
                        mMainFragmentTag);
                currentTag = mMainFragmentTag;
                break;
            case R.id.tab_2:
                setTitle(getString(R.string.tab_course));
                mainTopSearchBar.setVisibility(View.VISIBLE);
                mainTopSettingBar.setVisibility(View.GONE);
                tab2.setChecked(true);
                turnToFragment(CourseFragment.getInstance(new Bundle()), mCourseFragmentTag);
                currentTag = mCourseFragmentTag;
                break;
            case R.id.tab_3:
                setTitle(getString(R.string.tab_f2f));
                mainTopSearchBar.setVisibility(View.VISIBLE);
                mainTopSettingBar.setVisibility(View.GONE);
                tab3.setChecked(true);
                turnToFragment2(F2FCatergoryFragment.getInstance(new Bundle()), mHotFragmentTag);
                currentTag = mHotFragmentTag;
                break;
            case R.id.tab_4:
                setTitle(getString(R.string.tab_me));
                mainTopSearchBar.setVisibility(View.GONE);
                mainTopSettingBar.setVisibility(View.VISIBLE);
                tab4.setChecked(true);
                tab_3_n.setVisibility(View.INVISIBLE);
                turnToFragment(MeFragment.getInstance(new Bundle()),
                        mMeFragmentTag);
                currentTag = mMeFragmentTag;
                //PersonalnfoManager.getPersonInfo(LoginUserBean.getInstance().getLoginUserid());
                break;
            case R.id.setting_btn: {
                Intent intent = new Intent(this, SettingsActivity.class);
                Utils.start_Activity(this, intent);
                break;
            }
            case R.id.btn_search: {
                Intent it = new Intent(MainActivity.this, QAListActivity.class);
                it.putExtra("keyword", editQaSearch.getText().toString().trim());
                Utils.start_Activity(MainActivity.this, it);
                editQaSearch.setText("");
                break;
            }
            case R.id.btn_sao: {
                IntentIntegrator integrator = new IntentIntegrator(this);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                integrator.setPrompt("扫描二维码");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
                integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES, 0);
                break;
            }
            case R.id.img_qa_clear:
                editQaSearch.setText("");
                break;
            default:
                break;
        }

        if (currentTag == mMeFragmentTag) {
            mainTopSearchBar.setVisibility(View.GONE);
            mainTopSettingBar.setVisibility(View.VISIBLE);
        } else {
            mainTopSearchBar.setVisibility(View.VISIBLE);
            mainTopSettingBar.setVisibility(View.GONE);
        }
        //onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IntentIntegrator.REQUEST_CODE){
                ToastUtil.showToast(this,data.getStringExtra(Intents.Scan.RESULT));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Fragment jump
     * o'n
     *
     * @param frag Target Base Fragment
     * @param tag
     */
    public void turnToFragment(BaseFragment frag, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        boolean isFragmentExist = true;
        if (fragment == null) {
            isFragmentExist = false;
            fragment = frag;
        }
        if (fragment.isAdded()) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (isFragmentExist) {
            ft.replace(R.id.main_middle_ll, fragment);
        } else {
            ft.replace(R.id.main_middle_ll, fragment, tag);
        }
        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
        currentTag = tag;
    }

    /**
     * Fragment jump
     * o'n
     *
     * @param frag Target Base Fragment
     * @param tag
     */
    public void turnToFragment2(BaseFragment2 frag, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        boolean isFragmentExist = true;
        if (fragment == null) {
            isFragmentExist = false;
            fragment = frag;
        }
        if (fragment.isAdded()) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (isFragmentExist) {
            ft.replace(R.id.main_middle_ll, fragment);
        } else {
            ft.replace(R.id.main_middle_ll, fragment, tag);
        }
        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
        currentTag = tag;
    }

    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        if (currentTag == mMeFragmentTag) {
            mainTopSearchBar.setVisibility(View.GONE);
            mainTopSettingBar.setVisibility(View.VISIBLE);
        } else {
            mainTopSearchBar.setVisibility(View.VISIBLE);
            mainTopSettingBar.setVisibility(View.GONE);
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistReceiver();
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message,true);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
//                if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
//                // 刷新bottom bar消息未读数
//                updateUnreadLabel();
//                if (currentTabIndex == 0) {
//                    // 当前页面如果为聊天历史页面，刷新此页面
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);

    }

    @Override
    public boolean handleNotifyMessage(Message msg) {
        return false;
    }

    @Override
    public boolean handleUIMessage(Message msg) {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case DOWNLOAD:
                // 设置进度条位置
                mDownloadProgressDialog.setProgress(msg.arg1);
                break;
            case DOWNLOAD_FINISH:
                // 安装文件
                //installApk();
                if (mDownloadDialog != null && mDownloadDialog.isShowing()) {
                    mDownloadDialog.dismiss();
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CHECK_UPDATE:
                String response = (String) msg.obj;
                L.e(TAG, "Reponse:" + response);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                try {
                    JSONObject versionJsonObj = new JSONObject(response);
                    // FIR上当前的versionCode
                    int firVersionCode = Integer.parseInt(versionJsonObj.getString("version"));
                    // FIR上当前的versionName
                    String firVersionName = versionJsonObj.getString("versionShort");
                    String changelog = versionJsonObj.getString("changelog");
                    final String installurl = versionJsonObj.getString("install_url");

                    if (firVersionCode <=
                            CommonUtil.getVersionCode(this)) {
//                        Toast.makeText(this, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                    } else {
//                        firVersionName = "111.fu";
                        if(firVersionName.endsWith("fu")){
                            // 构造对话框
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(R.string.soft_update_title);
                            builder.setMessage(R.string.soft_update_info_force);
                            // 更新
                            builder.setPositiveButton(R.string.soft_update_updatebtn_force, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // 显示下载对话框
                                    //showDownloadDialog();
                                    L.d("yuhuan", "Download url：" + installurl);
                                    downLoadApk(installurl);
                                }
                            });
                            // 稍后更新
                            builder.setNegativeButton(R.string.soft_update_later_force, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            Dialog noticeDialog = builder.create();
                            noticeDialog.show();
                        }else {
                            // 构造对话框
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(R.string.soft_update_title);
                            builder.setMessage(R.string.soft_update_info);
                            // 更新
                            builder.setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // 显示下载对话框
                                    //showDownloadDialog();
                                    L.d("yuhuan", "Download url：" + installurl);
                                    downLoadApk(installurl);
                                }
                            });
                            // 稍后更新
                            builder.setNegativeButton(R.string.soft_update_later, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            Dialog noticeDialog = builder.create();
                            noticeDialog.show();
                        }
                    }
                }catch(Exception e){
                    Toast.makeText(this, "检查新版本失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
                break;
            case DOWNLOAD_ERROR:
                Toast.makeText(this, "下载新版本失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog() {

        mDownloadProgressDialog = new ProgressDialog(this);
        //mDownloadProgressDialog.setIcon(R.drawable.voip_icon);
        mDownloadProgressDialog.setTitle(R.string.soft_update_title);
        //mDownloadProgressDialog.setMessage("请稍后……");
        mDownloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条对话框//样式（水平，旋转）

        mDownloadProgressDialog.setButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadProgressDialog.setCanceledOnTouchOutside(false);

        //显示
        mDownloadProgressDialog.show();
        // 现在文件
        cancelUpdate = false;
        downloadApk();

    }

    private void downloadApk() {
        //应用包的相应信息
        final String packageName = appInfo
                .get(UpdateManager.UPDATE_CONFIG_FILE_PACKAGE_NODE);
        final String apkFile = appInfo
                .get(UpdateManager.UPDATE_CONFIG_FILE_APK_NODE);
        final String remoteVersion = appInfo
                .get(UpdateManager.UPDATE_CONFIG_FILE_VERSION_NODE);
        final String apkSize = appInfo
                .get(UpdateManager.UPDATE_CONFIG_FILE_APK_SIZE_NODE);

        //下载应用
        //final AppManager appManager = new AppManager(SettingActivity.this);

        MDownLoadTask task = new MDownLoadTask();
        if (NetworkUtil.enableConneting(this)) {
            task.execute(apkFile, String.valueOf(0),
                    apkSize, remoteVersion);
        } else {
            Toast.makeText(this, R.string.connection_not_valid, Toast.LENGTH_SHORT).show();
        }
    }

    class MDownLoadTask extends AsyncTask<String, Long, String> {
        private boolean isWorking = true;
        private FileDownloader loader = null;
        private int id = -1;
        private int maxSize = -1;
        private String apkFile;
        private String remoteVersion;
        private boolean isDelTmpFile = false;

        public void stopThread() {
            isWorking = false;
            if (loader != null) {
                loader.stopThread();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(final String... params) {
            while (loader == null) {
                if (!isWorking) {
                    return null;
                }
                try {
                    loader = new FileDownloader(MainActivity.this, UpdateManager.DOWN_LOAD_SERVER + params[0], new File(AppManager.APK_FILE_TEMP_LOCATION));
                } catch (Exception e) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                if (!isWorking) {
                    return null;
                }
            }

            id = Integer.parseInt(params[1]);
            apkFile = params[0];
            remoteVersion = params[3];
            maxSize = loader.getFileSize();
            mDownloadProgressDialog.setMax(maxSize);
            float all = maxSize / (1024 * 1024);
            //progressDialog.setProgressNumberFormat(String.format("%1d M/%2d M", 0, all));

            try {
                loader.download(new DownloadProgressListener() {
                    @Override
                    public void onDownloadSize(long size) {
                        publishProgress(size);
                        if (cancelUpdate) {
                            loader.stopThread();
                        }
                    }

                    @Override
                    public void onDownloadComplete(long size) {
                        DownloadManager.renameFile(
                                AppManager.APK_FILE_TEMP_LOCATION + apkFile,
                                AppManager.APK_FILE_LOCATION + remoteVersion + "_"
                                        + apkFile);
                        isDelTmpFile = false;
                        publishProgress(size);

                        File folder = new File(AppManager.APK_FILE_LOCATION);
                        if (folder != null) {
                            File[] subFiles = folder.listFiles();
                            if (subFiles != null && (subFiles.length > 0)) {
                                for (File file : subFiles) {
                                    if (file.getName().endsWith(apkFile)
                                            && !(file.getName().equals(remoteVersion
                                            + "_" + apkFile))) {
                                        file.delete();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onDownloadingDelTmpFile() {
                        isDelTmpFile = true;
                    }
                });
            } catch (Exception e) {
//              handler.obtainMessage(-1).sendToTarget();
                publishProgress(-1L);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            File file = new File(AppManager.APK_FILE_TEMP_LOCATION + apkFile);
            if (!file.exists()) {
                if (isDelTmpFile) {
                    return;
                }
            }
            if (values[0] == maxSize) {
                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                if (mDownloadProgressDialog != null && mDownloadProgressDialog.isShowing()) {
                    mDownloadProgressDialog.setProgress(maxSize);
                    mDownloadProgressDialog.dismiss();
                }
                if (!cancelUpdate) {
                    //下载完毕，安装
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(new File(
                                    AppManager.APK_FILE_LOCATION
                                            + remoteVersion + "_" + apkFile)),
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                }
            } else {
                if (NetworkUtil.enableConneting(MainActivity.this)) {
                    long progress = values[0];
                    float present = progress / (1024 * 1024);
                    float all = maxSize / (1024 * 1024);
                    //progressDialog.setProgressNumberFormat(String.format("%1d M/%2d M", present, all));
                    Message msg = new Message();
                    msg.what = DOWNLOAD;
                    msg.arg1 = (int) progress;
                    mHandler.sendMessage(msg);
                } else {


                }
            }
        }

    }

    /*
     * 从服务器中下载APK
	 */
    protected void downLoadApk(final String url) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        pd.setCancelable(false);
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = DownloadManager.getFileFromServer(url, pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                    Message msg = new Message();
                    msg.what = DOWNLOAD_ERROR;
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            // if ((System.currentTimeMillis() - exitTime) > 2000) {
            //Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();
            //  exitTime = System.currentTimeMillis();
            //} else {
            if(LoginUserBean.getInstance().getLoginStatus() == LoginUserBean.LOGIN_OUT){
                currentTag = mMainFragmentTag;
                moveTaskToBack(false);
            }else{
                if(currentTag!=mMainFragmentTag){
                    turnToPage("main");
                }else{
                    currentTag = mMainFragmentTag;
                    moveTaskToBack(false);
                }
            }
            //logoutIM();
            //finish();

            // }
//            return true;
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void logoutIM() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        finish();
                        //startActivity(new Intent(MainActivity.this, LoginActivity.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
            }
        });
    }

    private void registReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Utils.LOGIN_OUT_ACTION);
        registerReceiver(myReceiver, filter);
    }

    private void unregistReceiver() {
        unregisterReceiver(myReceiver);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Utils.LOGIN_OUT_ACTION)) {
                L.d(TAG, "Login out");
                finish();
            }
        }

    };


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setToolBarVisiblity(){
       mainTopSearchBar.setVisibility(View.GONE);
        mainTopSettingBar.setVisibility(View.VISIBLE);
    }

    public void turnToPage(String page){
        if (mMainFragmentTag.equals(page)) {
            onClick(tab1);
        } else if (mCourseFragmentTag.equals(page)) {
            onClick(tab2);
        } else if (mHotFragmentTag.equals(page)) {
            onClick(tab3);
        } else if (mMeFragmentTag.equals(page)) {
            onClick(tab4);
        }
    }

    @Override
    public void hideWaitDialog() {

    }

    @Override
    public ProgressDialog showWaitDialog() {
        return null;
    }

    @Override
    public ProgressDialog showWaitDialog(int resid) {
        return null;
    }

    @Override
    public ProgressDialog showWaitDialog(String text) {
        return null;
    }
}
