package com.xnf.henghenghui.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/3.
 */
public class TopicListResponse {

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

        private String topicId;


        private String topicTitle;


        private String topicDesc;


        private String commentCount;


        private String topicCover;


        public void setTopicId(String topicId){

            this.topicId = topicId;

        }

        public String getTopicId(){

            return this.topicId;

        }

        public void setTopicTitle(String topicTitle){

            this.topicTitle = topicTitle;

        }

        public String getTopicTitle(){

            return this.topicTitle;

        }

        public void setTopicDesc(String topicDesc){

            this.topicDesc = topicDesc;

        }

        public String getTopicDesc(){

            return this.topicDesc;

        }

        public void setCommentCount(String commentCount){

            this.commentCount = commentCount;

        }

        public String getCommentCount(){

            return this.commentCount;

        }

        public void setTopicCover(String topicCover){

            this.topicCover = topicCover;

        }

        public String getTopicCover(){

            return this.topicCover;

        }

    }

}
