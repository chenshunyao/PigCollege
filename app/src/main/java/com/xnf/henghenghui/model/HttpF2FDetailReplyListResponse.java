package com.xnf.henghenghui.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */

public class HttpF2FDetailReplyListResponse {
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

        private List<F2fDetailReplyContent> content ;

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
        public void setContent(List<F2fDetailReplyContent> content){
            this.content = content;
        }
        public List<F2fDetailReplyContent> getContent(){
            return this.content;
        }

    }

}
