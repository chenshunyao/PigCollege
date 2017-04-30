package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/5/18.
 */
public class GivePraiseModel {
    private ResponseInfo reponse;

    public class ResponseInfo {
        private int succeed;
        private int array;
        private int total;
        private PraiseContent content;

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

        public PraiseContent getContent() {
            return content;
        }

        public void setContent(PraiseContent content) {
            this.content = content;
        }
    }

    public static class PraiseContent {
        private String optCode;
        private String optMsg;

        public String getOptCode() {
            return optCode;
        }

        public void setOptCode(String optCode) {
            this.optCode = optCode;
        }

        public String getOptMsg() {
            return optMsg;
        }

        public void setOptMsg(String optMsg) {
            this.optMsg = optMsg;
        }
    }

    public ResponseInfo getReponse() {
        return reponse;
    }

    public void setReponse(ResponseInfo reponse) {
        this.reponse = reponse;
    }
}
