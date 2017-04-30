package com.xnf.henghenghui.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/16.
 * 哼哼热点专题详情
 */

public class HttpSubjectDetailResponse {

    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public class Content {

        private String topicId;
        private String topicName;
        private String topicContent;
        private String photos;
        private String authorName;
        private String publishDate;

        public String getPhotos() {
            return photos;
        }

        public void setPhotos(String photos) {
            this.photos = photos;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }

        public String getTopicContent() {
            return topicContent;
        }

        public void setTopicContent(String topicContent) {
            this.topicContent = topicContent;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }
    }

    public class Response {

        private int succeed;
        private int arrayflag;
        private int totalRow;
        private Content content;

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }

        public int getSucceed() {
            return succeed;
        }

        public void setArrayflag(int arrayflag) {
            this.arrayflag = arrayflag;
        }

        public int getArrayflag() {
            return arrayflag;
        }

        public void setTotalrow(int totalrow) {
            this.totalRow = totalrow;
        }

        public int getTotalrow() {
            return totalRow;
        }

        public void setContent(Content  content) {
            this.content = content;
        }

        public Content getContent() {
            return content;
        }

    }
}
