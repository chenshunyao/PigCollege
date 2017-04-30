package com.xnf.henghenghui.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class F2FCategoryResponse {

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

        private String qtCategoryId;


        private String qtCategoryName;


        private String qtCategoryDesc;

        private String qtAskCount;


        public void setQtCategoryId(String qtCategoryId){

            this.qtCategoryId = qtCategoryId;

        }

        public String getQtCategoryId(){

            return this.qtCategoryId;

        }

        public void setQtCategoryName(String qtCategoryName){

            this.qtCategoryName = qtCategoryName;

        }

        public String getQtCategoryName(){

            return this.qtCategoryName;

        }

        public void setQtCategoryDesc(String qtCategoryDesc){

            this.qtCategoryDesc = qtCategoryDesc;

        }

        public String getQtCategoryDesc(){

            return this.qtCategoryDesc;

        }

        public String getQtAskCount() {
            return qtAskCount;
        }

        public void setQtAskCount(String qtAskCount) {
            this.qtAskCount = qtAskCount;
        }
    }
}
