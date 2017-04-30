package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpAnwserListResponse {

    Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {
        private int succeed;

        private int arrayflag;

        private int totalRow;

        private String errorCode;

        private String errorInfo;

        private LinkedList<Content> content;

        public LinkedList<Content> getContent() {
            return content;
        }

        public void setContent(LinkedList<Content> content) {
            this.content = content;
        }

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

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(String errorInfo) {
            this.errorInfo = errorInfo;
        }
    }

    public class Content {
        String aqId;
        String aqContent;
        String aqDateTime;
        String aqUserId;
        String aqUserName;
        String aqUserPhoto;
        String title;
        String praiseCount;
        String aqPhoto;

        public String getAqUserName() {
            return aqUserName;
        }

        public void setAqUserName(String aqUserName) {
            this.aqUserName = aqUserName;
        }

        public String getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(String praiseCount) {
            this.praiseCount = praiseCount;
        }

        public String getAqPhoto() {
            return aqPhoto;
        }

        public void setAqPhoto(String aqPhoto) {
            this.aqPhoto = aqPhoto;
        }

        public String getAqId() {
            return aqId;
        }

        public void setAqId(String aqId) {
            this.aqId = aqId;
        }

        public String getAqContent() {
            return aqContent;
        }

        public void setAqContent(String aqContent) {
            this.aqContent = aqContent;
        }

        public String getAqDateTime() {
            return aqDateTime;
        }

        public void setAqDateTime(String aqDateTime) {
            this.aqDateTime = aqDateTime;
        }

        public String getAqUserId() {
            return aqUserId;
        }

        public void setAqUserId(String aqUserId) {
            this.aqUserId = aqUserId;
        }

        public String getAqUserPhoto() {
            return aqUserPhoto;
        }

        public void setAqUserPhoto(String aqUserPhoto) {
            this.aqUserPhoto = aqUserPhoto;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
