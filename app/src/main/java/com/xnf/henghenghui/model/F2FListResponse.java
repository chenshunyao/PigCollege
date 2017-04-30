package com.xnf.henghenghui.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class F2FListResponse {
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

        private String qtId;


        private String qtTitle;


        private String lastReplyTime;


        private String answerExpertCount;


        public void setQtId(String qtId){

            this.qtId = qtId;

        }

        public String getQtId(){

            return this.qtId;

        }

        public void setQtTitle(String qtTitle){

            this.qtTitle = qtTitle;

        }

        public String getQtTitle(){

            return this.qtTitle;

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
