package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpMasterArtListResponse
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
        String artId;
        String artPhoto;
        String artTitle;
        String artDesc;
        String artDateTime;
        String praiseCount;
        String commentCount;

        public String getArtId() {
            return artId;
        }

        public void setArtId(String artId) {
            this.artId = artId;
        }

        public String getArtPhoto() {
            return artPhoto;
        }

        public void setArtPhoto(String artPhoto) {
            this.artPhoto = artPhoto;
        }

        public String getArtTitle() {
            return artTitle;
        }

        public void setArtTitle(String artTitle) {
            this.artTitle = artTitle;
        }

        public String getArtDesc() {
            return artDesc;
        }

        public void setArtDesc(String artDesc) {
            this.artDesc = artDesc;
        }

        public String getArtDateTime() {
            return artDateTime;
        }

        public void setArtDateTime(String artDateTime) {
            this.artDateTime = artDateTime;
        }

        public String getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(String praiseCount) {
            this.praiseCount = praiseCount;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }
    }

}
