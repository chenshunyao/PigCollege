package com.xnf.henghenghui.model;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/2/19.
 */
public class SubjectArticleModel extends Entity {
    private String title;
    private Bitmap image;
    private String desc;
    private String mUrl;
    private String time;
    private String zuanNum;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
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
}
