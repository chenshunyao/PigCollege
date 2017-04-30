package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/6/11.
 */
public class MeetingDetailResponseModel {
    private ResponseInfo response;
    public class ResponseInfo {
        private int succeed;
        private int array;
        private int total;
        private MeetingContent content;

        public int getSucceed() {
            return succeed;
        }

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }

        public int getArray() {
            return array;
        }

        public void setArray(int array) {
            this.array = array;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public MeetingContent getContent() {
            return content;
        }

        public void setContent(MeetingContent content) {
            this.content = content;
        }
    }
    public static class MeetingContent {
        //会评Id
        private String cId;
        //会评链接
        private String cURL;
        //截图
        private String thumbnailURL;
        //会评状态
        private String status;
        //会评讲师Id
        private String authorId;
        private String authorName;
        //会评讲师单位及职称
        private String authorCompany;
        //会评讲师照片
        private String authorPhoto;
        //会评标题
        private String title;
        //会评地点
        private String address;
        //会评时间
        private String beginTime;
        //会评结束时间
        private String endTime;
        //会评内容
        private String content;
        //在线人数
        private String onlineNum;

        private String isConcern;

        public String getcId() {
            return cId;
        }

        public void setcId(String cId) {
            this.cId = cId;
        }

        public String getcURL() {
            return cURL;
        }

        public void setcURL(String cURL) {
            this.cURL = cURL;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }

        public void setThumbnailURL(String thumbnailURL) {
            this.thumbnailURL = thumbnailURL;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public String getAuthorCompany() {
            return authorCompany;
        }

        public void setAuthorCompany(String authorCompany) {
            this.authorCompany = authorCompany;
        }

        public String getAuthorPhoto() {
            return authorPhoto;
        }

        public void setAuthorPhoto(String authorPhoto) {
            this.authorPhoto = authorPhoto;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getOnlineNum() {
            return onlineNum;
        }

        public void setOnlineNum(String onlineNum) {
            this.onlineNum = onlineNum;
        }

        public String getIsConcern() {
            return isConcern;
        }

        public void setIsConcern(String isConcern) {
            this.isConcern = isConcern;
        }
    }

    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }
}
