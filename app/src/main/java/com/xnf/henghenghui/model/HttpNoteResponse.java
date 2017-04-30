package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/10/15.
 */

public class HttpNoteResponse {
    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public class Response {

        private int succeed;
        private int arrayflag;
        private int totalRow;
        private Content content;

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

        public void setContent(Content content) {
            this.content = content;
        }

        public Content getContent() {
            return content;
        }

    }

    public class Content {

        private String cId;
        private String nId;
        private String nTitle;
        private String nContent;
        private String nDatetime;


        public String getcId() {
            return cId;
        }

        public void setcId(String cId) {
            this.cId = cId;
        }

        public String getnId() {
            return nId;
        }

        public void setnId(String nId) {
            this.nId = nId;
        }

        public String getnTitle() {
            return nTitle;
        }

        public void setnTitle(String nTitle) {
            this.nTitle = nTitle;
        }

        public String getnContent() {
            return nContent;
        }

        public void setnContent(String nContent) {
            this.nContent = nContent;
        }

        public String getnDatetime() {
            return nDatetime;
        }

        public void setnDatetime(String nDatetime) {
            this.nDatetime = nDatetime;
        }
    }
}
