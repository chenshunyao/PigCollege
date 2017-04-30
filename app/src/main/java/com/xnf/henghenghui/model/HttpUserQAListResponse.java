package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpUserQAListResponse
{

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
        String qtCatagrory;
        String qtTitle;
        String qtContent;
        String qtDateTime;
        String qtUserId;
        String qtPhotos;
        String answerCount;

        public String getQtId() {
            return qtId;
        }

        public void setQtId(String qtId) {
            this.qtId = qtId;
        }

        public String getQtCatagrory() {
            return qtCatagrory;
        }

        public void setQtCatagrory(String qtCatagrory) {
            this.qtCatagrory = qtCatagrory;
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

        public String getQtDateTime() {
            return qtDateTime;
        }

        public void setQtDateTime(String qtDateTime) {
            this.qtDateTime = qtDateTime;
        }

        public String getQtUserId() {
            return qtUserId;
        }

        public void setQtUserId(String qtUserId) {
            this.qtUserId = qtUserId;
        }

        public String getQtPhotos() {
            return qtPhotos;
        }

        public void setQtPhotos(String qtPhotos) {
            this.qtPhotos = qtPhotos;
        }

        public String getAnswerCount() {
            return answerCount;
        }

        public void setAnswerCount(String answerCount) {
            this.answerCount = answerCount;
        }
    }

}
