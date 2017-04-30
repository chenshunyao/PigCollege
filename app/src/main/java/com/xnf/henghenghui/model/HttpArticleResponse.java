package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/10/15.
 */

public class HttpArticleResponse {
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

        private String artId;
        private String artTitle;
        private String artContent;
        private String authorName;
        private String publishDate;
        private String isFavorite;

        public void setArtid(String artid) {
            this.artId = artid;
        }

        public String getArtid() {
            return artId;
        }

        public void setArttitle(String arttitle) {
            this.artTitle = arttitle;
        }

        public String getArttitle() {
            return artTitle;
        }

        public void setArtcontent(String artcontent) {
            this.artContent = artcontent;
        }

        public String getArtcontent() {
            return artContent;
        }

        public void setAuthorname(String authorname) {
            this.authorName = authorname;
        }

        public String getAuthorname() {
            return authorName;
        }

        public void setPublishdate(String publishdate) {
            this.publishDate = publishdate;
        }

        public String getPublishdate() {
            return publishDate;
        }

        public String getIsFavorite() {
            return isFavorite;
        }

        public void setIsFavorite(String isFavorite) {
            this.isFavorite = isFavorite;
        }
    }
}
