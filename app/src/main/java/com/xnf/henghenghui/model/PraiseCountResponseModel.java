package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/5/18.
 */
public class PraiseCountResponseModel {

    private ResponseInfo response;
    public class ResponseInfo {
        private int succeed;
        private int array;
        private int total;
        private Content content;

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

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }
    }

    public static class Content{
        private String KeyId;
        private String praiseCount;
        private String browseCount;
        private String replyCount;

        public String getKeyId() {
            return KeyId;
        }

        public void setKeyId(String keyId) {
            KeyId = keyId;
        }

        public String getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(String praiseCount) {
            this.praiseCount = praiseCount;
        }

        public String getBrowseCount() {
            return browseCount;
        }

        public void setBrowseCount(String browseCount) {
            this.browseCount = browseCount;
        }

        public String getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(String replyCount) {
            this.replyCount = replyCount;
        }
    }

    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }
}
