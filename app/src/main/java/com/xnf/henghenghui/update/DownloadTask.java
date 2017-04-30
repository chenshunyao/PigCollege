package com.xnf.henghenghui.update;
public class DownloadTask {
    private String url = null;
    private Long totalSize = null;
    private Long downloadedSize = null;
    private String taskName = null;
    private String location = null;
    private boolean start = false;
    private boolean isLoadOver = false;
    private int id = -1;

    public String getLocation() {
        return location;
    }
    public boolean isStart() {
        return start;
    }
    public void setStart(boolean start) {
        this.start = start;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Long getTotalSize() {
        return totalSize;
    }

    public synchronized int getId() {
        return id;
    }
    public synchronized void setId(int id) {
        this.id = id;
    }
    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }
    public Long getDownloadedSize() {
        return downloadedSize;
    }
    public void setDownloadedSize(Long downloadedSize) {
        this.downloadedSize = downloadedSize;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public boolean isLoadOver() {
        return isLoadOver;
    }
    public void setLoadOver(boolean isLoadOver) {
        this.isLoadOver = isLoadOver;
    }


}
