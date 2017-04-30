package com.xnf.henghenghui.model;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HttpQaListResponse
{

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
        LinkedList<QuestionInfo> questionInfo;
        LinkedList<TopicInfo> topicInfo;
        LinkedList<AnswerInfo> answerInfo;
        LinkedList<ExpertsInfo> expertsInfo;
        LinkedList<EntryInfo> entryInfo;
        LinkedList<ArticleInfo> articleInfo;

        public LinkedList<QuestionInfo> getQuestionInfo() {
            return questionInfo;
        }

        public void setQuestionInfo(LinkedList<QuestionInfo> questionInfo) {
            this.questionInfo = questionInfo;
        }

        public LinkedList<TopicInfo> getTopicInfo() {
            return topicInfo;
        }

        public void setTopicInfo(LinkedList<TopicInfo> topicInfo) {
            this.topicInfo = topicInfo;
        }

        public LinkedList<AnswerInfo> getAnswerInfo() {
            return answerInfo;
        }

        public void setAnswerInfo(LinkedList<AnswerInfo> answerInfo) {
            this.answerInfo = answerInfo;
        }

        public LinkedList<ExpertsInfo> getExpertsInfo() {
            return expertsInfo;
        }

        public void setExpertsInfo(LinkedList<ExpertsInfo> expertsInfo) {
            this.expertsInfo = expertsInfo;
        }

        public LinkedList<EntryInfo> getEntryInfo() {
            return entryInfo;
        }

        public void setEntryInfo(LinkedList<EntryInfo> entryInfo) {
            this.entryInfo = entryInfo;
        }

        public LinkedList<ArticleInfo> getArticleInfo() {
            return articleInfo;
        }

        public void setArticleInfo(LinkedList<ArticleInfo> articleInfo) {
            this.articleInfo = articleInfo;
        }
    }

    public class QuestionInfo {
        String qId;
        String qTitle;
        String qCreateTime;

        public String getqId() {
            return qId;
        }

        public void setqId(String qId) {
            this.qId = qId;
        }

        public String getqTitle() {
            return qTitle;
        }

        public void setqTitle(String qTitle) {
            this.qTitle = qTitle;
        }

        public String getqCreateTime() {
            return qCreateTime;
        }

        public void setqCreateTime(String qCreateTime) {
            this.qCreateTime = qCreateTime;
        }
    }

    public class TopicInfo {
        String tId;
        String tcTitle;

        public String gettId() {
            return tId;
        }

        public void settId(String tId) {
            this.tId = tId;
        }

        public String getTcTitle() {
            return tcTitle;
        }

        public void setTcTitle(String tcTitle) {
            this.tcTitle = tcTitle;
        }
    }

    public class AnswerInfo {
        String questionId;
        String aqId;
        String aqContent;
        String aqDateTime;

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getAqId() {
            return aqId;
        }

        public void setAqId(String aqId) {
            this.aqId = aqId;
        }

        public String getAqContent() {
            return aqContent;
        }

        public void setAqContent(String aqContent) {
            this.aqContent = aqContent;
        }

        public String getAqDateTime() {
            return aqDateTime;
        }

        public void setAqDateTime(String aqDateTime) {
            this.aqDateTime = aqDateTime;
        }
    }

    public class ExpertsInfo {
        String expertsId;
        String expertsName;
        String expertsDesc;

        public String getExpertsId() {
            return expertsId;
        }

        public void setExpertsId(String expertsId) {
            this.expertsId = expertsId;
        }

        public String getExpertsName() {
            return expertsName;
        }

        public void setExpertsName(String expertsName) {
            this.expertsName = expertsName;
        }

        public String getExpertsDesc() {
            return expertsDesc;
        }

        public void setExpertsDesc(String expertsDesc) {
            this.expertsDesc = expertsDesc;
        }
    }

    public class EntryInfo {
        String entryId;
        String categoryId;
        String entryTitle;

        public String getEntryId() {
            return entryId;
        }

        public void setEntryId(String entryId) {
            this.entryId = entryId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getEntryTitle() {
            return entryTitle;
        }

        public void setEntryTitle(String entryTitle) {
            this.entryTitle = entryTitle;
        }
    }

    public class ArticleInfo {
        String artId;
        String artTitle;

        public String getArtId() {
            return artId;
        }

        public void setArtId(String artId) {
            this.artId = artId;
        }

        public String getArtTitle() {
            return artTitle;
        }

        public void setArtTitle(String artTitle) {
            this.artTitle = artTitle;
        }
    }

}
