package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/6/26.
 */
public class NoramlUserInfoResponse {

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
        private String userId;

        private String userName;

        private String nikeName;

        private String mobile;

        private String email;

       // private String city;

        private String address;

       // private String species;

      //  private String businessType;

        private String company;

        private String breedScope;

        private String photo;

        private String title;

        private String farmName;

        private String farmAddress;

        public String getUserPoints() {
            return userPoints;
        }

        public void setUserPoints(String userPoints) {
            this.userPoints = userPoints;
        }

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
        public void setNikeName(String nikeName){
            this.nikeName = nikeName;
        }
        public String getNikeName(){
            return this.nikeName;
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
//        public void setCity(String city){
//            this.city = city;
//        }
//        public String getCity(){
//            return this.city;
//        }
        public void setAddress(String address){
            this.address = address;
        }
        public String getAddress(){
            return this.address;
        }
//        public void setSpecies(String species){
//            this.species = species;
//        }
//        public String getSpecies(){
//            return this.species;
//        }
//        public void setBusinessType(String businessType){
//            this.businessType = businessType;
//        }
//        public String getBusinessType(){
//            return this.businessType;
//        }
        public void setCompany(String company){
            this.company = company;
        }
        public String getCompany(){
            return this.company;
        }
        public void setBreedScope(String breedScope){
            this.breedScope = breedScope;
        }
        public String getBreedScope(){
            return this.breedScope;
        }
        public void setPhoto(String photo){
            this.photo = photo;
        }
        public String getPhoto(){
            return this.photo;
        }

        public String getFarmAddress() {
            return farmAddress;
        }

        public void setFarmAddress(String farmAddress) {
            this.farmAddress = farmAddress;
        }

        public String getFarmName() {
            return farmName;
        }

        public void setFarmName(String farmName) {
            this.farmName = farmName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}



