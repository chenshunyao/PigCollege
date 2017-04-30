package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpMasterQAListResponse
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
        String qtTitle;
        String qtCatagroryId;
        String qtCatagroryName;
        String lastReplyTime;
        String answerExpertCount;

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

        public String getQtCatagroryId() {
            return qtCatagroryId;
        }

        public void setQtCatagroryId(String qtCatagroryId) {
            this.qtCatagroryId = qtCatagroryId;
        }

        public String getQtCatagroryName() {
            return qtCatagroryName;
        }

        public void setQtCatagroryName(String qtCatagroryName) {
            this.qtCatagroryName = qtCatagroryName;
        }

        public String getLastReplyTime() {
            return lastReplyTime;
        }

        public void setLastReplyTime(String lastReplyTime) {
            this.lastReplyTime = lastReplyTime;
        }

        public String getAnswerExpertCount() {
            return answerExpertCount;
        }

        public void setAnswerExpertCount(String answerExpertCount) {
            this.answerExpertCount = answerExpertCount;
        }
    }

}
