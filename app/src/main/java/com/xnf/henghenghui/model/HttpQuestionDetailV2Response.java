package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpQuestionDetailV2Response {

    Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {
        private int succeed;

        private int arrayflag;

        private int totalRow;

        private String errorCode;

        private String errorInfo;

        private Content content;

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

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

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(String errorInfo) {
            this.errorInfo = errorInfo;
        }
    }

    public class Content {
        String qtId;
        String qtTitle;
        String qtCategoryId;
        String qtCategoryName;
        String qtPhotos;
        String attackNum;
        String days;
        String temp;
        String illnesses;
        String immune;
        String medication;

        public void setQtCategoryId(String qtCategoryId) {
            this.qtCategoryId = qtCategoryId;
        }

        public String getQtCategoryName() {
            return qtCategoryName;
        }

        public void setQtCategoryName(String qtCategoryName) {
            this.qtCategoryName = qtCategoryName;
        }

        public String getAttackNum() {
            return attackNum;
        }

        public void setAttackNum(String attackNum) {
            this.attackNum = attackNum;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getIllnesses() {
            return illnesses;
        }

        public void setIllnesses(String illnesses) {
            this.illnesses = illnesses;
        }

        public String getImmune() {
            return immune;
        }

        public void setImmune(String immune) {
            this.immune = immune;
        }

        public String getMedication() {
            return medication;
        }

        public void setMedication(String medication) {
            this.medication = medication;
        }

        public String getQtCategoryId() {
            return qtCategoryId;
        }

        public String getQtId() {
            return qtId;
        }

        public void setQtId(String qtId) {
            this.qtId = qtId;
        }

        public String getQtTitle() {
            return qtTitle;
        }

        public void setQtTitle(String qtTitle) {
            this.qtTitle = qtTitle;
        }

        public String getQtPhotos() {
            return qtPhotos;
        }

        public void setQtPhotos(String qtPhotos) {
            this.qtPhotos = qtPhotos;
        }

    }

}
