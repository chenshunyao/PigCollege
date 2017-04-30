package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/5/15.
 * 专家面对面中的某个问题的回答页面对应的item
 */
public class ExpertReplyItemModel {

    /**
     * 专家图像
     */
    private String expertImg;
    /**
     * 评论的数目
     */
    private String expertReplyComentNum;
    /**
     * 点赞的数目
     */
    private String expertReplyLikeNum;
    /**
     * 问题描述
     */
    private String qsDescription;

    public ExpertReplyItemModel() {
    }

    public String getExpertImg() {
        return expertImg;
    }

    public void setExpertImg(String expertImg) {
        this.expertImg = expertImg;
    }

    public String getExpertReplyComentNum() {
        return expertReplyComentNum;
    }

    public void setExpertReplyComentNum(String expertReplyComentNum) {
        this.expertReplyComentNum = expertReplyComentNum;
    }

    public String getExpertReplyLikeNum() {
        return expertReplyLikeNum;
    }

    public void setExpertReplyLikeNum(String expertReplyLikeNum) {
        this.expertReplyLikeNum = expertReplyLikeNum;
    }

    public String getQsDescription() {
        return qsDescription;
    }

    public void setQsDescription(String qsDescription) {
        this.qsDescription = qsDescription;
    }

    @Override
    public String toString() {
        return "ExpertReplyItemModel{" +
                "expertImg='" + expertImg + '\'' +
                ", expertReplyComentNum='" + expertReplyComentNum + '\'' +
                ", expertReplyLikeNum='" + expertReplyLikeNum + '\'' +
                ", qsDescription='" + qsDescription + '\'' +
                '}';
    }
}
