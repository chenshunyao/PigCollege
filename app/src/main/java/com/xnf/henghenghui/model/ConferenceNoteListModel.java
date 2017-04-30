package com.xnf.henghenghui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/25.
 */
public class ConferenceNoteListModel {
    private ResponseInfo response;
    public class ResponseInfo {
        private int succeed;
        private int array;
        private int total;
        private List<ConferenceNoteContent> content = new ArrayList<ConferenceNoteContent>();

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

        public List<ConferenceNoteContent> getContent() {
            return content;
        }

        public void setContent(List<ConferenceNoteContent> content) {
            this.content = content;
        }
    }

    public static class ConferenceNoteContent{
        private String nId;
        private String cId;
        private String thumbnailURL;
        private String title;
        private String content;
        private String createTime;
        private String authorId;

        public String getnId() {
            return nId;
        }

        public void setnId(String nId) {
            this.nId = nId;
        }

        public String getcId() {
            return cId;
        }

        public void setcId(String cId) {
            this.cId = cId;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }

        public void setThumbnailURL(String thumbnailURL) {
            this.thumbnailURL = thumbnailURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }
    }

    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }
}
