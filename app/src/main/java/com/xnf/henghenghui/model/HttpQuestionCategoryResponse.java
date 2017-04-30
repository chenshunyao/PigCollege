package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpQuestionCategoryResponse
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
        String qtCategoryId;
        String qtCategoryName;
        String qtCategoryDesc;

        public String getQtCategoryId() {
            return qtCategoryId;
        }

        public void setQtCategoryId(String qtCategoryId) {
            this.qtCategoryId = qtCategoryId;
        }

        public String getQtCategoryName() {
            return qtCategoryName;
        }

        public void setQtCategoryName(String qtCategoryName) {
            this.qtCategoryName = qtCategoryName;
        }

        public String getQtCategoryDesc() {
            return qtCategoryDesc;
        }

        public void setQtCategoryDesc(String qtCategoryDesc) {
            this.qtCategoryDesc = qtCategoryDesc;
        }
    }

}
