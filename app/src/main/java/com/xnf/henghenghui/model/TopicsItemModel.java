package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/5/10.
 */
public class TopicsItemModel {
    /**
     *
     */
    private String topicId;
    /**
     * 话题图片
     */
    private String topicIcon;
    /**
     * 话题名称
     */
    private String topicName;
    /**
     * 话题描述
     */
    private String topicDesc;
    /**
     * 参与人员数目
     */
    private long  participantsNum;

    public long getParticipantsNum() {
        return participantsNum;
    }

    public void setParticipantsNum(long participantsNum) {
        this.participantsNum = participantsNum;
    }

    public String getTopicDesc() {
        return topicDesc;
    }

    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
    }

    public String getTopicIcon() {
        return topicIcon;
    }

    public void setTopicIcon(String topicIcon) {
        this.topicIcon = topicIcon;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return "TopicsItemModel{" +
                "participantsNum=" + participantsNum +
                ", topicId='" + topicId + '\'' +
                ", topicIcon='" + topicIcon + '\'' +
                ", topicName='" + topicName + '\'' +
                ", topicDesc='" + topicDesc + '\'' +
                '}';
    }
}
