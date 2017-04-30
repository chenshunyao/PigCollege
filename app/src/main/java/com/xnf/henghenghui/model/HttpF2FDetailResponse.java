package com.xnf.henghenghui.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */

public class HttpF2FDetailResponse {
    private Response response;

    public void setResponse(Response response){
        this.response = response;
    }
    public Response getResponse(){
        return this.response;
    }

    public class Response {
        private int succeed;

        private int arrayflag;

        private int totalRow;

        private List<Content> content ;

        public void setSucceed(int succeed){
            this.succeed = succeed;
        }
        public int getSucceed(){
            return this.succeed;
        }
        public void setArrayflag(int arrayflag){
            this.arrayflag = arrayflag;
        }
        public int getArrayflag(){
            return this.arrayflag;
        }
        public void setTotalRow(int totalRow){
            this.totalRow = totalRow;
        }
        public int getTotalRow(){
            return this.totalRow;
        }
        public void setContent(List<Content> content){
            this.content = content;
        }
        public List<Content> getContent(){
            return this.content;
        }

    }

    public class Content {
        private String qtTitle;

        private String qtId;

        private String qtCategoryId;

        private String qtPhotos;

        private String qtDesc;

        private String qtContent;

        private List<QtAnswer> qtAnswer ;

        public void setQtTitle(String qtTitle){
            this.qtTitle = qtTitle;
        }
        public String getQtTitle(){
            return this.qtTitle;
        }
        public void setQtId(String qtId){
            this.qtId = qtId;
        }
        public String getQtId(){
            return this.qtId;
        }
        public void setQtCategoryId(String qtCategoryId){
            this.qtCategoryId = qtCategoryId;
        }
        public String getQtCategoryId(){
            return this.qtCategoryId;
        }
        public void setQtPhotos(String qtPhotos){
            this.qtPhotos = qtPhotos;
        }
        public String getQtPhotos(){
            return this.qtPhotos;
        }
        public void setQtDesc(String qtDesc){
            this.qtDesc = qtDesc;
        }
        public String getQtDesc(){
            return this.qtDesc;
        }
        public void setQtContent(String qtContent){
            this.qtContent = qtContent;
        }
        public String getQtContent(){
            return this.qtContent;
        }
        public void setQtAnswer(List<QtAnswer> qtAnswer){
            this.qtAnswer = qtAnswer;
        }
        public List<QtAnswer> getQtAnswer(){
            return this.qtAnswer;
        }

    }

    public class QtAnswer {
        private String aqId;

        private String aqContent;

        private String aqUserId;

        private String aqUserName;

        private String aqDateTime;

        public void setAqId(String aqId){
            this.aqId = aqId;
        }
        public String getAqId(){
            return this.aqId;
        }
        public void setAqContent(String aqContent){
            this.aqContent = aqContent;
        }
        public String getAqContent(){
            return this.aqContent;
        }
        public void setAqUserId(String aqUserId){
            this.aqUserId = aqUserId;
        }
        public String getAqUserId(){
            return this.aqUserId;
        }
        public void setAqUserName(String aqUserName){
            this.aqUserName = aqUserName;
        }
        public String getAqUserName(){
            return this.aqUserName;
        }
        public void setAqDateTime(String aqDateTime){
            this.aqDateTime = aqDateTime;
        }
        public String getAqDateTime(){
            return this.aqDateTime;
        }

    }
}
