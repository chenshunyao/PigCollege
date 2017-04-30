package com.xnf.henghenghui.model;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/2/19.
 */
public class HotArticleModel extends Entity {
    private  String articleId;
    private String title;
    private String image;
    private String desc;
    private String mUrl;
    private String time;
    private String zuanNum;
    private String commentNum;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
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

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
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
        return "HotArticleModel{" +
                "articleId='" + articleId + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", desc='" + desc + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", time='" + time + '\'' +
                ", zuanNum='" + zuanNum + '\'' +
                ", commentNum='" + commentNum + '\'' +
                '}';
    }
}
