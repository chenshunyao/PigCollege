package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/5/5.
 */
public class AppUpdateResponseModel {

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

        private Content content;

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
        public void setContent(Content content){
            this.content = content;
        }
        public Content getContent(){
            return this.content;
        }
    }

    public class Content {
        private String hasNew;

        private String versionId;

        private String versionUrl;

        public void setHasNew(String hasNew){
            this.hasNew = hasNew;
        }
        public String getHasNew(){
            return this.hasNew;
        }
        public void setVersionId(String versionId){
            this.versionId = versionId;
        }
        public String getVersionId(){
            return this.versionId;
        }
        public void setVersionUrl(String versionUrl){
            this.versionUrl = versionUrl;
        }
        public String getVersionUrl(){
            return this.versionUrl;
        }

    }
}
