package com.xnf.henghenghui.update;

import android.app.ProgressDialog;
import android.os.Environment;

import com.xnf.henghenghui.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

/**
 * 下载管理器
 *
 * @author ynfeng
 */
public class DownloadManager {
    private DownloadListener listener = null;
    private Stack<DownloadTask> downLoadQueue = new Stack<DownloadTask>();

    /**
     * 从栈中取出一个下载任务开始执行
     *
     * @throws IOException
     */
    public void startDownload() throws IOException {

        while (!downLoadQueue.isEmpty()) {
            DownloadTask task = downLoadQueue.pop();
            downLoad(task);
        }
    }

    /**
     * 向栈中加入一个下载任务
     *
     * @param task
     */
    public void addDownloadTask(DownloadTask task) {
        downLoadQueue.push(task);
    }

    public void downLoad(DownloadTask task) throws IOException {
        try {
            URL url = new URL(task.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            InputStream in = conn.getInputStream();
            task.setStart(true);
            FileOutputStream fos = new FileOutputStream(task.getLocation(),
                    true);
            byte[] buffer = new byte[1024];
            int c = 0;
            long downLoadedBytes = 0;
            while ((c = in.read(buffer)) != -1) {
                fos.write(buffer, 0, c);
                downLoadedBytes += c;
                if (this.listener != null) {
                    task.setDownloadedSize(downLoadedBytes);
                    listener.downLoadProgress(task);
                }
            }
            in.close();
            fos.close();
            if (this.listener != null) {
                listener.downLoadComplete(task);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setListener(DownloadListener listener) {
        this.listener = listener;
    }

    public void mkdirs(String dirs) {
        File file = new File(dirs);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void renameFile(String src, String dest) {
        File srcFile = new File(src);
        File destFile = new File(dest);
        if (srcFile.exists()) {
            deleteFile(dest);
            srcFile.renameTo(destFile);
        }
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            FileUtil.isFolderExists(Environment.getExternalStorageDirectory()+"/henghenghui/appUpdate/");
            File file = new File(Environment.getExternalStorageDirectory()+"/henghenghui/appUpdate/", "Henghenghui.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }


}
