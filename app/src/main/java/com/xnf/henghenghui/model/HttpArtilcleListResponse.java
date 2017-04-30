package com.xnf.henghenghui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */

public class HttpArtilcleListResponse {
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

        private ArrayList<Content> content ;

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
        public void setContent(ArrayList<Content> content){
            this.content = content;
        }
        public ArrayList<Content> getContent(){
            return this.content;
        }

    }

    public class Content {
        private String artId ;

        private String artPhoto;

        private String artTitle ;

        private String artDesc ;

        private String artDateTime ;

        private String praiseCount;

        private String commentCount ;

        public void setArtId (String artId ){
            this. artId = artId ;
        }
        public String getArtId (){
            return this. artId ;
        }
        public void setArtPhoto(String artPhoto){
            this. artPhoto = artPhoto;
        }
        public String getArtPhoto(){
            return this. artPhoto;
        }
        public void setArtTitle (String artTitle ){
            this. artTitle = artTitle ;
        }
        public String getArtTitle (){
            return this. artTitle ;
        }
        public void setArtDesc (String artDesc ){
            this. artDesc = artDesc ;
        }
        public String getrtDesc (){
            return this.artDesc ;
        }
        public void setArtDateTime (String artDateTime ){
            this. artDateTime = artDateTime ;
        }
        public String getArtDateTime (){
            return this.artDateTime ;
        }
        public void setPraiseCount(String praiseCount){
            this. praiseCount = praiseCount;
        }
        public String getPraiseCount(){
            return this.praiseCount;
        }
        public void setCommentCount (String commentCount ){
            this. commentCount = commentCount ;
        }
        public String getCommentCount (){
            return this.commentCount ;
        }

    }


}


