package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpMasterAnwserQAListResponse
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
        String qtCatagroryId;
        String qtCatagrory;
        String qtDesc;
        String qtContent;
        String qtDateTime;
        String answerCount;
        LinkedList<Anwser> aqList;

        public String getQtId() {
            return qtId;
        }

        public void setQtId(String qtId) {
            this.qtId = qtId;
        }

        public String getQtDesc() {
            return qtDesc;
        }

        public void setQtDesc(String qtDesc) {
            this.qtDesc = qtDesc;
        }

        public String getQtCatagrory() {
            return qtCatagrory;
        }

        public void setQtCatagrory(String qtCatagrory) {
            this.qtCatagrory = qtCatagrory;
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

        public String getAnswerCount() {
            return answerCount;
        }

        public String getQtCatagroryId() {
            return qtCatagroryId;
        }

        public void setQtCatagroryId(String qtCatagroryId) {
            this.qtCatagroryId = qtCatagroryId;
        }

        public LinkedList<Anwser> getAqList() {
            return aqList;
        }

        public void setAqList(LinkedList<Anwser> aqList) {
            this.aqList = aqList;
        }

        public void setAnswerCount(String answerCount) {
            this.answerCount = answerCount;
        }
    }

    public class Anwser{
        String aqId;
        String aqContent;
        String aqDateTime;
        String aqUserId;
        String aqUserPhoto;

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
    }

}
