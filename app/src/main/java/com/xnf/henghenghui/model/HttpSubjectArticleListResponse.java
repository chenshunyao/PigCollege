package com.xnf.henghenghui.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/16.
 * 
 */

public class HttpSubjectArticleListResponse {

    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public class Content {

        private String artId;
        private String artPhoto;
        private String artTitle;
        private String artDesc;
        private String artDateTime;
        private String praiseCount;
        private String commentCount;

        public void setArtid(String artId) {
            this.artId = artId;
        }

        public String getArtid() {
            return artId;
        }

        public void setArtphoto(String artPhoto) {
            this.artPhoto = artPhoto;
        }

        public String getArtphoto() {
            return artPhoto;
        }

        public void setArttitle(String artTitle) {
            this.artTitle = artTitle;
        }

        public String getArttitle() {
            return artTitle;
        }

        public void setArtdesc(String artDesc) {
            this.artDesc = artDesc;
        }

        public String getArtdesc() {
            return artDesc;
        }

        public void setArtdatetime(String artDateTime) {
            this.artDateTime = artDateTime;
        }

        public String getArtdatetime() {
            return artDateTime;
        }

        public void setPraisecount(String praiseCount) {
            this.praiseCount = praiseCount;
        }

        public String getPraisecount() {
            return praiseCount;
        }

        public void setCommentcount(String commentCount) {
            this.commentCount = commentCount;
        }

        public String getCommentcount() {
            return commentCount;
        }

    }

    public class Response {

        private int succeed;
        private int arrayflag;
        private int totalrow;
        private ArrayList<Content> content;

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
            this.totalrow = totalrow;
        }

        public int getTotalrow() {
            return totalrow;
        }

        public void setContent(ArrayList<Content> content) {
            this.content = content;
        }

        public ArrayList<Content> getContent() {
            return content;
        }

    }
}
