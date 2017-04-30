package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/5/15.
 */
public class ExpertQsItemModel {
    private String expertQsId;

    private String expertQsNum;

    private String expertQsLastestTime;

    private String qsDescription;

    public ExpertQsItemModel() {
    }

    public String getExpertQsLastestTime() {
        return expertQsLastestTime;
    }

    public void setExpertQsLastestTime(String expertQsLastestTime) {
        this.expertQsLastestTime = expertQsLastestTime;
    }

    public String getExpertQsId() {
        return expertQsId;
    }

    public void setExpertQsId(String expertQsId) {
        this.expertQsId = expertQsId;
    }

    public String getExpertQsNum() {
        return expertQsNum;
    }

    public void setExpertQsNum(String expertQsNum) {
        this.expertQsNum = expertQsNum;
    }

    public String getQsDescription() {
        return qsDescription;
    }

    public void setQsDescription(String qsDescription) {
        this.qsDescription = qsDescription;
    }

    @Override
    public String toString() {
        return "ExpertQsItemModel{" +
                "expertQsLastestTime='" + expertQsLastestTime + '\'' +
                ", expertQsId='" + expertQsId + '\'' +
                ", expertQsNum='" + expertQsNum + '\'' +
                ", qsDescription='" + qsDescription + '\'' +
                '}';
    }
}
