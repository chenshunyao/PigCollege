package com.xnf.henghenghui.model;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/2/19.
 */
public class HotSubjectModel extends Entity {
    private String subjectId;
    private String title;
    private String image;
    private String desc;
    private String mUrl;
    private String time;
    private String zuanNum;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getZuanNum() {
        return zuanNum;
    }

    public void setZuanNum(String zuanNum) {
        this.zuanNum = zuanNum;
    }

    @Override
    public String toString() {
        return "HotSubjectModel{" +
                "desc='" + desc + '\'' +
                ", title='" + title + '\'' +
                ", image=" + image +
                ", mUrl='" + mUrl + '\'' +
                ", time='" + time + '\'' +
                ", zuanNum='" + zuanNum + '\'' +
                '}';
    }
}
