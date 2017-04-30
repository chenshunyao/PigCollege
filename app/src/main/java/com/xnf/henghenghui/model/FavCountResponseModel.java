package com.xnf.henghenghui.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class FavCountResponseModel {

    private ResponseInfo response;
    public class ResponseInfo {
        private int succeed;
        private int arrayflag;
        private int totalRow;
        private List<Content> content;

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

        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }
    }

    public static class Content{
        private String keyFlag;
        private List<KeyData> keyDataList;

        public String getKeyFlag() {
            return keyFlag;
        }

        public void setKeyFlag(String keyFlag) {
            this.keyFlag = keyFlag;
        }

        public List<KeyData> getKeyDataList() {
            return keyDataList;
        }

        public void setKeyDataList(List<KeyData> keyDataList) {
            this.keyDataList = keyDataList;
        }
    }

    public static class KeyData{
        private String keyId;
        private String keyType;

        public String getKeyId() {
            return keyId;
        }

        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }

        public String getKeyType() {
            return keyType;
        }

        public void setKeyType(String keyType) {
            this.keyType = keyType;
        }
    }

    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }
}
