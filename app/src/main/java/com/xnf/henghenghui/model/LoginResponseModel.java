package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/4/5.
 */
public class LoginResponseModel {

    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }


    public class Content {
        private String userId;

        private String userType;

        private String userLevel;

        private String levelDesc;

        private String channelId;

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserType() {
            return this.userType;
        }

        public void setUserLevel(String userLevel) {
            this.userLevel = userLevel;
        }

        public String getUserLevel() {
            return this.userLevel;
        }

        public void setLevelDesc(String levelDesc) {
            this.levelDesc = levelDesc;
        }

        public String getLevelDesc() {
            return this.levelDesc;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelId() {
            return this.channelId;
        }

    }

    public class Response {
        private int succeed;

        private int arrayflag;

        private int totalRow;

        private Content content;

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }

        public int getSucceed() {
            return this.succeed;
        }

        public void setArrayflag(int arrayflag) {
            this.arrayflag = arrayflag;
        }

        public int getArrayflag() {
            return this.arrayflag;
        }

        public void setTotalRow(int totalRow) {
            this.totalRow = totalRow;
        }

        public int getTotalRow() {
            return this.totalRow;
        }

        public void setContent(Content content) {
            this.content = content;
        }

        public Content getContent() {
            return this.content;
        }

    }


}
