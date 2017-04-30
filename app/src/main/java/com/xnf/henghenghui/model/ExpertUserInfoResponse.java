package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/8/21.
 */

public class ExpertUserInfoResponse {

    private Response response;

    public void setResponse(Response response){
        this.response = response;
    }
    public Response getResponse(){
        return this.response;
    }

    public class Content {
        private String userId;

        private String userName;

        private String mobile;

        private String email;

        private String address;

        private String isRect;

        private String certType;

        private String company;

        private String titles;

        private String desc;

        private String professional;

        private String otherContact;

        private String photo;

        private String userPoints;

        public void setUserId(String userId){
            this.userId = userId;
        }
        public String getUserId(){
            return this.userId;
        }
        public void setUserName(String userName){
            this.userName = userName;
        }
        public String getUserName(){
            return this.userName;
        }
        public void setMobile(String mobile){
            this.mobile = mobile;
        }
        public String getMobile(){
            return this.mobile;
        }
        public void setEmail(String email){
            this.email = email;
        }
        public String getEmail(){
            return this.email;
        }
        public void setAddress(String address){
            this.address = address;
        }
        public String getAddress(){
            return this.address;
        }
        public void setIsRect(String isRect){
            this.isRect = isRect;
        }
        public String getIsRect(){
            return this.isRect;
        }
        public void setCertType(String certType){
            this.certType = certType;
        }
        public String getCertType(){
            return this.certType;
        }
        public void setCompany(String company){
            this.company = company;
        }
        public String getCompany(){
            return this.company;
        }
        public void setTitles(String titles){
            this.titles = titles;
        }
        public String getTitles(){
            return this.titles;
        }
        public void setDesc(String desc){
            this.desc = desc;
        }
        public String getDesc(){
            return this.desc;
        }
        public void setProfessional(String professional){
            this.professional = professional;
        }
        public String getProfessional(){
            return this.professional;
        }
        public void setOtherContact(String otherContact){
            this.otherContact = otherContact;
        }
        public String getOtherContact(){
            return this.otherContact;
        }
        public void setPhoto(String photo){
            this.photo = photo;
        }
        public String getPhoto(){
            return this.photo;
        }
        public void setUserPoints(String userPoints){
            this.userPoints = userPoints;
        }
        public String getUserPoints(){
            return this.userPoints;
        }

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
}
