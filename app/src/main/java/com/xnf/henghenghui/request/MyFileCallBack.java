package com.xnf.henghenghui.request;

import android.support.annotation.Nullable;

import com.lzy.okhttputils.callback.FileCallBack;
import com.lzy.okhttputils.request.BaseRequest;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/1/15
 * 描    述：
 * 修订历史：
 * ================================================
 */
public abstract class MyFileCallBack extends FileCallBack {

    public MyFileCallBack(String destFileDir, String destFileName) {
        super(destFileDir, destFileName);
    }

    @Override
    public File parseNetworkResponse(Response response) throws Exception {
        System.out.println("parseNetworkResponse");
        return super.parseNetworkResponse(response);
    }

    @Override
    public void onBefore(BaseRequest request) {
        System.out.println("onBefore");

    }

    @Override
    public void onAfter(@Nullable File file, Call call, Response response, @Nullable Exception e) {
        System.out.println("onAfter");
    }

    @Override
    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        System.out.println("upProgress -- " + totalSize + "  " + currentSize + "  " + progress + "  " + networkSpeed);
    }

    @Override
    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        System.out.println("downloadProgress -- " + totalSize + "  " + currentSize + "  " + progress + "  " + networkSpeed);
    }

    @Override
    public void onError(Call call, @Nullable Response response, @Nullable Exception e) {
        System.out.println("onError");
        super.onError(call, response, e);
    }
}
