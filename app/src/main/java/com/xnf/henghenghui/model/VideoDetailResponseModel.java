package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/5/2.
 */
public class VideoDetailResponseModel {
    private ResponseInfo response;
    public class ResponseInfo{
        private int succeed;
        private int array;
        private int total;
        private VideoContent content ;

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

        public VideoContent getContent() {
            return content;
        }

        public void setContent(VideoContent content) {
            this.content = content;
        }
    }

    public static class VideoContent {
        private String vId;
        private String videoURL;
        private String videoTitle;
        private String price;
        private String playCount;
        private String classFocus;
        private String classDesc;
        private String classTimeLong;
        private String teacherId;

        public String getvId() {
            return vId;
        }

        public void setvId(String vId) {
            this.vId = vId;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public void setVideoURL(String videoURL) {
            this.videoURL = videoURL;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public String getClassFocus() {
            return classFocus;
        }

        public void setClassFocus(String classFocus) {
            this.classFocus = classFocus;
        }

        public String getClassDesc() {
            return classDesc;
        }

        public void setClassDesc(String classDesc) {
            this.classDesc = classDesc;
        }

        public String getClassTimeLong() {
            return classTimeLong;
        }

        public void setClassTimeLong(String classTimeLong) {
            this.classTimeLong = classTimeLong;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }
    }
    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }
}
