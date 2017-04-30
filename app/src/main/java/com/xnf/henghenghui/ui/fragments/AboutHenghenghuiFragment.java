package com.xnf.henghenghui.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.MainPageManager;
import com.xnf.henghenghui.model.AppUpdateResponseModel;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.utils.UIHelper;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.CustomProgressDialog;
import com.xnf.henghenghui.update.AppManager;
import com.xnf.henghenghui.update.DownloadManager;
import com.xnf.henghenghui.update.DownloadProgressListener;
import com.xnf.henghenghui.update.FileDownloader;
import com.xnf.henghenghui.update.UpdateManager;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.CommonUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.LanguageUtil;
import com.xnf.henghenghui.util.NetworkUtil;
import com.xnf.henghenghui.util.TDevice;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/5/1.
 */
public class AboutHenghenghuiFragment extends BaseFragment2 {

    private static final String TAG = "AboutHenghenghuiFragment";
    private List<Map<String, String>> appsInfo = null;

    @SuppressLint("UseSparseArrays")
    private CustomProgressDialog progressDialog;

    private UpdateManager updateManager = null;

    private UpdateCheckAsyncTask updateCheckAsyncTask;

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

    @InjectView(R.id.tv_version)
    TextView mTvVersionStatus;

    @InjectView(R.id.tv_version_name)
    TextView mTvVersionName;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.rl_check_update).setOnClickListener(this);
        view.findViewById(R.id.tv_henghuisite).setOnClickListener(this);
        view.findViewById(R.id.tv_knowmore).setOnClickListener(this);

        int localcode = LanguageUtil.getLocaleCode();
        updateManager = new UpdateManager(getActivity(), localcode);
    }

    @Override
    public void initData() {
        mTvVersionName.setText("V " + TDevice.getVersionName());
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.rl_check_update:
                onClickUpdate();
                break;
            case R.id.tv_henghuisite:
                UIHelper.openBrowser(getActivity(), "https://www.xnfyzr.com");
                break;
            case R.id.tv_knowmore:
                UIHelper.openBrowser(getActivity(),
                        "https://www.xnfyzr.com");
                break;
            default:
                break;
        }
    }

    private void onClickUpdate() {
        showProgressDialog();
        //updateCheckAsyncTask = new UpdateCheckAsyncTask();
        // updateCheckAsyncTask.execute();
        int version = CommonUtil.getVersionCode(getActivity());
        checkAppUpdate(version);
    }

    private String getRequestBody(String jsonString) {
        return "{" + "\"request\"" + ":" + jsonString + "}";
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

    class UpdateCheckAsyncTask extends
            AsyncTask<String, String, List<Map<String, String>>> {

        @Override
        protected List<Map<String, String>> doInBackground(String... params) {
            L.d(TAG, "doInBackground");
            try {
                appsInfo = updateManager.getAppsUpdateInfo();
                if (appsInfo != null && updateManager.getTips() != null) {
//                    application.setTips_remote(updateManager.getTips());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return appsInfo;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> result) {
            super.onPostExecute(result);
            if (cancelUpdate) {
                return;
            }
            L.d(TAG, "onPostExecute");
            //获取待升级的信息
            appsInfo = result;
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (result == null) {
                Toast.makeText(getActivity(), R.string.soft_update_no, Toast.LENGTH_SHORT).show();
            } else {
                //检查到新版本
                //Toast.makeText(SettingActivity.this, "检查新版本", Toast.LENGTH_SHORT).show();
                if (!NetworkUtil.enableConneting(getActivity())) {
                    Toast.makeText(getActivity(), R.string.connection_not_valid, Toast.LENGTH_SHORT).show();
                    return;
                }
                PackageInfo pinfo;
                try {
                    pinfo = getActivity().getPackageManager().getPackageInfo(
                            getActivity().getPackageName(), 0);
                    int runningVersion = pinfo.versionCode;
                    for (int i = 0; i < appsInfo.size(); i++) {
                        if (appsInfo.get(i).get("package").equals(getActivity().getPackageName())
                                && (Integer.valueOf(appsInfo.get(i).get("version")) > runningVersion)) {
                            appInfo = appsInfo.get(i);
                            showNoticeDialog();
                            break;
                        }
                        if (i == appsInfo.size() - 1) {
                            Toast.makeText(getActivity(), R.string.soft_update_no, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                //TODO 获取到下载地址信息，暂时还不知道detron voip phone
                //TODO 这里可以弹出一个对话框显示进度。
//                int position = 0;
//                appInfo = appsInfo.get(position);
//                showNoticeDialog();
                L.d("keven", "Result:" + result.toString());
            }
        }
    }

    private void showProgressDialog() {
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(this
                .getResources().getString(R.string.setting_app_checking_update));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                // finish();
                cancelUpdate = true;
            }
        });
        progressDialog.show();
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        mDownloadProgressDialog = new ProgressDialog(getActivity());
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
        if (NetworkUtil.enableConneting(getActivity())) {
            task.execute(apkFile, String.valueOf(0),
                    apkSize, remoteVersion);
        } else {
            Toast.makeText(getActivity(), R.string.connection_not_valid, Toast.LENGTH_SHORT).show();
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
                    loader = new FileDownloader(getActivity(), UpdateManager.DOWN_LOAD_SERVER + params[0], new File(AppManager.APK_FILE_TEMP_LOCATION));
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
                if (NetworkUtil.enableConneting(getActivity())) {
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

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
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
                                CommonUtil.getVersionCode(getActivity())) {
                            Toast.makeText(getActivity(), "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                        } else {
                            // 构造对话框
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(R.string.soft_update_title);
                            builder.setMessage(R.string.soft_update_info);
                            // 更新
                            builder.setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // 显示下载对话框
                                    //showDownloadDialog();
                                    L.d("yuhuan","Download url："+installurl);
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
                    }catch(Exception e){
                        Toast.makeText(getActivity(), "检查新版本失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

//                    if (Utils.getRequestStatus(response) == 1) {
//                        Gson gson = new Gson();
//                        AppUpdateResponseModel appUpdate = gson.fromJson(response, AppUpdateResponseModel.class);
//                        final String url = appUpdate.getResponse().getContent().getVersionUrl();
//                        if (appUpdate.getResponse().getContent().getHasNew().equals("1")) {
//                            try{
//                                if (Integer.parseInt(appUpdate.getResponse().getContent().getVersionId()) <=
//                                        CommonUtil.getVersionCode(getActivity())) {
//                                    Toast.makeText(getActivity(), "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // 构造对话框
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                    builder.setTitle(R.string.soft_update_title);
//                                    builder.setMessage(R.string.soft_update_info);
//                                    // 更新
//                                    builder.setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                            // 显示下载对话框
//                                            //showDownloadDialog();
//                                            L.d("yuhuan","Download url："+url);
//                                            downLoadApk(url);
//                                        }
//                                    });
//                                    // 稍后更新
//                                    builder.setNegativeButton(R.string.soft_update_later, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                                    Dialog noticeDialog = builder.create();
//                                    noticeDialog.show();
//                                }
//                            }catch (Exception e){
//                                Toast.makeText(getActivity(), "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(getActivity(), "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), "检查新版本失败，请稍后重试", Toast.LENGTH_SHORT).show();
//                    }
                    break;
                case DOWNLOAD_ERROR:
                    Toast.makeText(getActivity(), "下载新版本失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * 从服务器中下载APK
	 */
    protected void downLoadApk(final String url) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(getActivity());
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

}
