package com.xnf.henghenghui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/25.
 */
public class ConferenceCommentListModel {
    private ResponseInfo response;
    public class ResponseInfo {
        private int succeed;
        private int arrayflag;
        private int totalRow;
        private List<ConferenceCommentContent> content = new ArrayList<ConferenceCommentContent>();

        public int getSucceed() {
            return succeed;
        }

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }

        public int getArrayflag() {
            return arrayflag;
        }

        public void setArrayflag(int arrayflag) {
            this.arrayflag = arrayflag;
        }

        public int getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(int totalRow) {
            this.totalRow = totalRow;
        }

        public List<ConferenceCommentContent> getContent() {
            return content;
        }

        public void setContent(List<ConferenceCommentContent> content) {
            this.content = content;
        }
    }

    public static class ConferenceCommentContent{
        private String comtId;
        private String content;
        private String photos;
        private String comtUserId;
        private String userPhoto;
        private String comtNikeName;
        private String comtDate;

        //跟帖消息
        private List<Comt> gComts = new ArrayList<Comt>();

        public String getComtId() {
            return comtId;
        }

        public void setComtId(String comtId) {
            this.comtId = comtId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public String getComtNikeName() {
            return comtNikeName;
        }

        public void setComtNikeName(String comtNikeName) {
            this.comtNikeName = comtNikeName;
        }

        public String getComtDate() {
            return comtDate;
        }

        public void setComtDate(String comtDate) {
            this.comtDate = comtDate;
        }

        public List<Comt> getgComts() {
            return gComts;
        }

        public void setgComts(List<Comt> gComts) {
            this.gComts = gComts;
        }
    }

    public static class Comt{
        private String gComtId;
        private String gComtUserID;
        private String gContent;
        private String gComtDate;

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
    }
    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }
}
