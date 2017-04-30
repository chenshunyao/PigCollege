package com.xnf.henghenghui.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/3.
 */
public class CourseModel extends Entity implements Serializable,Parcelable{

    public CourseModel(){}
    public CourseModel(Parcel dest) {
        id = dest.readInt();
        cId = dest.readString();
        cTitle = dest.readString();
        cUri = dest.readString();
        cPlayTime = dest.readInt();
        cPrice = dest.readFloat();
        cImageUrl = dest.readString();
        cType = dest.readInt();
        cDuration = dest.readLong();
        cDescription = dest.readString();
        cZhan = dest.readInt();
        cCommentCount = dest.readInt();
        cExtend = dest.readString();
        cVid = dest.readString();
    }
    private String cId;
    private String cTitle;
    private String cUri;
    private int cPlayTime;
    private float cPrice;
    private String cImageUrl;
    private int cType;
    private long cDuration;
    private String cDescription;
    private int cZhan;
    private int cCommentCount;
    private String cExtend;
    private String cVid;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcTitle() {
        return cTitle;
    }

    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }

    public String getcUri() {
        return cUri;
    }

    public void setcUri(String cUri) {
        this.cUri = cUri;
    }

    public int getcPlayTime() {
        return cPlayTime;
    }

    public void setcPlayTime(int cPlayTime) {
        this.cPlayTime = cPlayTime;
    }

    public float getcPrice() {
        return cPrice;
    }

    public void setcPrice(float cPrice) {
        this.cPrice = cPrice;
    }

    public String getcImageUrl() {
        return cImageUrl;
    }

    public void setcImageUrl(String cImageUrl) {
        this.cImageUrl = cImageUrl;
    }

    public int getcType() {
        return cType;
    }

    public void setcType(int cType) {
        this.cType = cType;
    }

    public long getcDuration() {
        return cDuration;
    }

    public void setcDuration(long cDuration) {
        this.cDuration = cDuration;
    }

    public String getcDescription() {
        return cDescription;
    }

    public void setcDescription(String cDescription) {
        this.cDescription = cDescription;
    }

    public int getcZhan() {
        return cZhan;
    }

    public void setcZhan(int cZhan) {
        this.cZhan = cZhan;
    }

    public int getcCommentCount() {
        return cCommentCount;
    }

    public void setcCommentCount(int cCommentCount) {
        this.cCommentCount = cCommentCount;
    }

    public String getcExtend() {
        return cExtend;
    }

    public void setcExtend(String cExtend) {
        this.cExtend = cExtend;
    }

    public String getcVid() {
        return cVid;
    }

    public void setcVid(String cVid) {
        this.cVid = cVid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cId);
        dest.writeString(cTitle);
        dest.writeString(cUri);
        dest.writeInt(cPlayTime);
        dest.writeFloat(cPrice);
        dest.writeString(cImageUrl);
        dest.writeInt(cType);
        dest.writeLong(cDuration);
        dest.writeString(cDescription);
        dest.writeInt(cZhan);
        dest.writeInt(cCommentCount);
        dest.writeString(cExtend);
        dest.writeString(cVid);
    }

    public static final Creator<CourseModel> CREATOR = new Creator<CourseModel>() {

        @Override
        public CourseModel[] newArray(int size) {
            return new CourseModel[size];
        }

        @Override
        public CourseModel createFromParcel(Parcel source) {
            return new CourseModel(source);
        }
    };
}
