package com.xnf.henghenghui.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 * 首页返回的专题问题列表或者话题列表
 */

public class HttpMainPageF2FResponseModel {
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
        private List<QtInfo> qtInfo ;

        private List<TpInfo> tpInfo ;

        public void setQtInfo(List<QtInfo> qtInfo){
            this.qtInfo = qtInfo;
        }
        public List<QtInfo> getQtInfo(){
            return this.qtInfo;
        }
        public void setTpInfo(List<TpInfo> tpInfo){
            this.tpInfo = tpInfo;
        }
        public List<TpInfo> getTpInfo(){
            return this.tpInfo;
        }

    }

    public class TpInfo {
        private String id;

        private String title;

        private String desc;

        private String photo;

        private String lastReplyTime;

        private String answerExpertCount;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setDesc(String desc){
            this.desc = desc;
        }
        public String getDesc(){
            return this.desc;
        }
        public void setPhoto(String photo){
            this.photo = photo;
        }
        public String getPhoto(){
            return this.photo;
        }
        public void setLastReplyTime(String lastReplyTime){
            this.lastReplyTime = lastReplyTime;
        }
        public String getLastReplyTime(){
            return this.lastReplyTime;
        }
        public void setAnswerExpertCount(String answerExpertCount){
            this.answerExpertCount = answerExpertCount;
        }
        public String getAnswerExpertCount(){
            return this.answerExpertCount;
        }

    }

    public class QtInfo {
        private String id;

        private String title;

        private String desc;

        private String photo;

        private String lastReplyTime;

        private String answerExpertCount;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setDesc(String desc){
            this.desc = desc;
        }
        public String getDesc(){
            return this.desc;
        }
        public void setPhoto(String photo){
            this.photo = photo;
        }
        public String getPhoto(){
            return this.photo;
        }
        public void setLastReplyTime(String lastReplyTime){
            this.lastReplyTime = lastReplyTime;
        }
        public String getLastReplyTime(){
            return this.lastReplyTime;
        }
        public void setAnswerExpertCount(String answerExpertCount){
            this.answerExpertCount = answerExpertCount;
        }
        public String getAnswerExpertCount(){
            return this.answerExpertCount;
        }

    }
}
