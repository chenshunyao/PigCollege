package com.xnf.henghenghui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/10.
 */
public class MeetingListResponseModel {
    private ResponseInfo response;
    public class ResponseInfo {
        private int succeed;
        private int arrayflag;
        private int totalRow;
        private List<MeetingContent> content = new ArrayList<MeetingContent>();

        public int getSucceed() {
            return succeed;
        }

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }

        public int getArrayflag() {
            return arrayflag;
        }

        public void setArrayflag(int arrayflag) {
            this.arrayflag = arrayflag;
        }

        public int getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(int totalRow) {
            this.totalRow = totalRow;
        }

        public List<MeetingContent> getContent() {
            return content;
        }

        public void setContent(List<MeetingContent> content) {
            this.content = content;
        }
    }

    public static class MeetingContent{
        //会评id
        private String cId;
        //会评视频链接
        private String cURL;
        //会评截图url
        private String thumbnailURL;
        //会评状态
        private String status;
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
        //主持人昵称
        private String nikeName;
        //点赞数
        private String praiseCount;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNikeName() {
            return nikeName;
        }

        public void setNikeName(String nikeName) {
            this.nikeName = nikeName;
        }

        public String getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(String praiseCount) {
            this.praiseCount = praiseCount;
        }
    }

    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }
}
