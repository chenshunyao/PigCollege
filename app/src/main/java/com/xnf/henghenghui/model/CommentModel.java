package com.xnf.henghenghui.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/21.
 */
public class CommentModel implements Serializable{
    private String comtId;
    private String commentName;
    private int vCourseId;
    private String commentUri;
    private String commentDesc;
    private String commentFrom;
    private int userId;
    private String photos;
    private String comtUserId;
    private String userPhoto;
    private String comtDate;
    private String gComts;
    private String gComtId;
    private String gComtUserID;
    private String gContent;
    private String gComtDate;
    private String extend;

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public int getvCourseId() {
        return vCourseId;
    }

    public void setvCourseId(int vCourseId) {
        this.vCourseId = vCourseId;
    }

    public String getCommentUri() {
        return commentUri;
    }

    public void setCommentUri(String commentUri) {
        this.commentUri = commentUri;
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc;
    }

    public String getCommentFrom() {
        return commentFrom;
    }

    public void setCommentFrom(String commentFrom) {
        this.commentFrom = commentFrom;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getComtId() {
        return comtId;
    }

    public void setComtId(String comtId) {
        this.comtId = comtId;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getComtUserId() {
        return comtUserId;
    }

    public void setComtUserId(String comtUserId) {
        this.comtUserId = comtUserId;
    }

    public String getComtDate() {
        return comtDate;
    }

    public void setComtDate(String comtDate) {
        this.comtDate = comtDate;
    }

    public String getgComts() {
        return gComts;
    }

    public void setgComts(String gComts) {
        this.gComts = gComts;
    }

    public String getgComtId() {
        return gComtId;
    }

    public void setgComtId(String gComtId) {
        this.gComtId = gComtId;
    }

    public String getgComtUserID() {
        return gComtUserID;
    }

    public void setgComtUserID(String gComtUserID) {
        this.gComtUserID = gComtUserID;
    }

    public String getgContent() {
        return gContent;
    }

    public void setgContent(String gContent) {
        this.gContent = gContent;
    }

    public String getgComtDate() {
        return gComtDate;
    }

    public void setgComtDate(String gComtDate) {
        this.gComtDate = gComtDate;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
