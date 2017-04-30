package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpBaikeInfoResponse
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

        private Content content;

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
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
        String entryId;
        String categoryId;
        String entryTitle;
        String entrySource;
        String entryPhoto;
        String entryContent;

        public String getEntryId() {
            return entryId;
        }

        public void setEntryId(String entryId) {
            this.entryId = entryId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getEntryTitle() {
            return entryTitle;
        }

        public void setEntryTitle(String entryTitle) {
            this.entryTitle = entryTitle;
        }

        public String getEntrySource() {
            return entrySource;
        }

        public void setEntrySource(String entrySource) {
            this.entrySource = entrySource;
        }

        public String getEntryPhoto() {
            return entryPhoto;
        }

        public void setEntryPhoto(String entryPhoto) {
            this.entryPhoto = entryPhoto;
        }

        public String getEntryContent() {
            return entryContent;
        }

        public void setEntryContent(String entryContent) {
            this.entryContent = entryContent;
        }
    }

}
