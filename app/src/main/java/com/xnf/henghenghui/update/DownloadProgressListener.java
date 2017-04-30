package com.xnf.henghenghui.update;

public interface DownloadProgressListener {
    public void onDownloadSize(long size);
    public void onDownloadComplete(long size);
    public void onDownloadingDelTmpFile();
}
