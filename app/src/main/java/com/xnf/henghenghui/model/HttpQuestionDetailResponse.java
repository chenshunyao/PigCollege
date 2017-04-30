package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpQuestionDetailResponse {

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
        String qtId;
        String qtTitle;
        String qtCategoryId;
        String qtPhotos;
        String qtContent;
        LinkedList<Answer> qtAnswer;

        public String getQtCategoryId() {
            return qtCategoryId;
        }

        public LinkedList<Answer> getQtAnswer() {
            return qtAnswer;
        }

        public void setQtCategoryId(String qtCategoryId) {
            this.qtCategoryId = qtCategoryId;
        }

        public void setQtAnswer(LinkedList<Answer> qtAnswer) {
            this.qtAnswer = qtAnswer;
        }

        public String getQtId() {
            return qtId;
        }

        public void setQtId(String qtId) {
            this.qtId = qtId;
        }

        public String getQtTitle() {
            return qtTitle;
        }

        public void setQtTitle(String qtTitle) {
            this.qtTitle = qtTitle;
        }

        public String getQtContent() {
            return qtContent;
        }

        public void setQtContent(String qtContent) {
            this.qtContent = qtContent;
        }

        public String getQtPhotos() {
            return qtPhotos;
        }

        public void setQtPhotos(String qtPhotos) {
            this.qtPhotos = qtPhotos;
        }

    }

    public class Answer {
        String aqId;
        String aqContent;
        String aqUserId;
        String aqUserName;

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

        public String getAqUserId() {
            return aqUserId;
        }

        public void setAqUserId(String aqUserId) {
            this.aqUserId = aqUserId;
        }

        public String getAqUserName() {
            return aqUserName;
        }

        public void setAqUserName(String aqUserName) {
            this.aqUserName = aqUserName;
        }

        public String getAqDateTime() {
            return aqDateTime;
        }

        public void setAqDateTime(String aqDateTime) {
            this.aqDateTime = aqDateTime;
        }

        String aqDateTime;
    }
}
