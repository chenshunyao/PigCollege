package com.xnf.henghenghui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class LiveConferenceListModel {
    private ResponseInfo response;
    public class ResponseInfo {
        private int succeed;
        private int arrayflag;
        private int totalRow;
        private List<LiveConferenceContent> content = new ArrayList<LiveConferenceContent>();

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

        public List<LiveConferenceContent> getContent() {
            return content;
        }

        public void setContent(List<LiveConferenceContent> content) {
            this.content = content;
        }
    }

    public static class LiveConferenceContent{
        private String cId;
        private String commId;
        private String content;
        private String userId;
        private String nikeName;
        //评论用户头像
        private String userPhoto;
        //评论图片
        private String photos;
        private String publishTime;
        private String praiseCount;

        public String getcId() {
            return cId;
        }

        public void setcId(String cId) {
            this.cId = cId;
        }

        public String getCommId() {
            return commId;
        }

        public void setCommId(String commId) {
            this.commId = commId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public String getNikeName() {
            return nikeName;
        }

        public void setNikeName(String nikeName) {
            this.nikeName = nikeName;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public String getPhotos() {
            return photos;
        }

        public void setPhotos(String photos) {
            this.photos = photos;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(String praiseCount) {
            this.praiseCount = praiseCount;
        }
    }

    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }
}
