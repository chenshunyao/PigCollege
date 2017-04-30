package com.xnf.henghenghui.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/16.
 * 哼哼热点专题列表
 */

public class HttpSubjectListResponse {

    private Response response;
    public void setResponse(Response response) {
        this.response = response;
    }
    public Response getResponse() {
        return response;
    }

    public class Content {

        private String topicId;
        private String topicPhoto;
        private String topicTitle;
        private String topicDesc;
        private String topicDateTime;
        private String praiseCount;
        public void setTopicid(String topicid) {
            this.topicId = topicid;
        }
        public String getTopicid() {
            return topicId;
        }

        public void setTopicphoto(String topicphoto) {
            this.topicPhoto = topicphoto;
        }
        public String getTopicphoto() {
            return topicPhoto;
        }

        public void setTopictitle(String topictitle) {
            this.topicTitle = topictitle;
        }
        public String getTopictitle() {
            return topicTitle;
        }

        public void setTopicdesc(String topicdesc) {
            this.topicDesc = topicdesc;
        }
        public String getTopicdesc() {
            return topicDesc;
        }

        public void setTopicdatetime(String topicdatetime) {
            this.topicDateTime = topicdatetime;
        }
        public String getTopicdatetime() {
            return topicDateTime;
        }

        public void setPraisecount(String praisecount) {
            this.praiseCount = praisecount;
        }
        public String getPraisecount() {
            return praiseCount;
        }

    }

    public class Response {

        private int succeed;
        private int arrayflag;
        private int totalRow;
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
            this.totalRow = totalrow;
        }
        public int getTotalrow() {
            return totalRow;
        }

        public void setContent(ArrayList<Content> content) {
            this.content = content;
        }
        public ArrayList<Content> getContent() {
            return content;
        }

    }
}
