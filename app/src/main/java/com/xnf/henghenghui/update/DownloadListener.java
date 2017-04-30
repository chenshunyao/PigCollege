package com.xnf.henghenghui.update;

public interface DownloadListener {
    public void downLoadProgress(DownloadTask task);
    public void downLoadComplete(DownloadTask task);
}
