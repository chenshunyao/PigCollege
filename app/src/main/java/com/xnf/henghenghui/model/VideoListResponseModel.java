package com.xnf.henghenghui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 */
public class VideoListResponseModel {
    private ResponseInfo response;
    public class ResponseInfo{
        private int succeed;
        private int array;
        private int total;
        private List<VideoContent> content = new ArrayList<VideoContent>();

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

        public List<VideoContent> getContent() {
            return content;
        }

        public void setContent(List<VideoContent> content) {
            this.content = content;
        }
    }
    public static class VideoContent{
        private String vId;
        private String videoTitle;
        private String videoURL;
        private String thumbnailURL;
        private String playCount;
        private String price;
        private String classTimeLong;

        public String getvId() {
            return vId;
        }

        public void setvId(String vId) {
            this.vId = vId;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public void setVideoURL(String videoURL) {
            this.videoURL = videoURL;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }

        public void setThumbnailURL(String thumbnailURL) {
            this.thumbnailURL = thumbnailURL;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public String getPrice() {
            return price == null ? "0" : price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getClassTimeLong() {
            return classTimeLong;
        }

        public void setClassTimeLong(String classTimeLong) {
            this.classTimeLong = classTimeLong;
        }
    }

    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }
}
