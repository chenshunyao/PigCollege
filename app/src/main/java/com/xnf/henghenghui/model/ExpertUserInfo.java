package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/3/27.
 * <p>
 * "userId": "18772921400",
 * "userName": "李专家",
 * "mobile": "18627701380",
 * "email": "luomihou@qq.com",
 * "address": "广东省广州市天河区金华路99号",
 * "isRect": "1",
 * "certType": "2",
 * "company": "湖北养殖业有限公司",
 * "titles": "高级顾问",
 * "desc": "李专家，擅长领域：生物安全纵深建设及疾病防控，高热病的防控、产房仔猪腹泻的防控、保育猪高残次控制、断奶猪腹泻的防控、五号病的防控和规模化猪场疑难杂症的防控。",
 * "professional": "猪病",
 * "otherContact": "",
 * "photo": "",
 * "userPoints": "0"
 */
public class ExpertUserInfo {

    private String userId;
    private String userName;
    private String mobile;
    private String email;
    private String address;
    private String isRect;
    private String certType;
    private String company;
    private String titles;
    private String desc;
    private String professional;
    private String otherContact;
    private String fileMappingId;
    private String userPoints;
    private String photo;
    private String avatarLocalPath;

    public ExpertUserInfo() {
    }

    public ExpertUserInfo(String address, String certType, String company, String desc, String email, String fileMappingId, String isRect, String mobile, String otherContact, String photo, String professional, String titles, String userId, String userName, String userPoints) {
        this.address = address;
        this.certType = certType;
        this.company = company;
        this.desc = desc;
        this.email = email;
        this.fileMappingId = fileMappingId;
        this.isRect = isRect;
        this.mobile = mobile;
        this.otherContact = otherContact;
        this.photo = photo;
        this.professional = professional;
        this.titles = titles;
        this.userId = userId;
        this.userName = userName;
        this.userPoints = userPoints;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileMappingId() {
        return fileMappingId;
    }

    public void setFileMappingId(String fileMappingId) {
        this.fileMappingId = fileMappingId;
    }

    public String getIsRect() {
        return isRect;
    }

    public void setIsRect(String isRect) {
        this.isRect = isRect;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtherContact() {
        return otherContact;
    }

    public void setOtherContact(String otherContact) {
        this.otherContact = otherContact;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(String userPoints) {
        this.userPoints = userPoints;
    }

    public String getAvatarLocalPath() {
        return avatarLocalPath;
    }

    public void setAvatarLocalPath(String avatarLocalPath) {
        this.avatarLocalPath = avatarLocalPath;
    }

    @Override
    public String toString() {
        return "ExpertUserInfo{" +
                "address='" + address + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", isRect='" + isRect + '\'' +
                ", certType='" + certType + '\'' +
                ", company='" + company + '\'' +
                ", titles='" + titles + '\'' +
                ", desc='" + desc + '\'' +
                ", professional='" + professional + '\'' +
                ", otherContact='" + otherContact + '\'' +
                ", fileMappingId='" + fileMappingId + '\'' +
                ", userPoints='" + userPoints + '\'' +
                ", photo='" + photo + '\'' +
                ", avatarLocalPath='" + avatarLocalPath + '\'' +
                '}';
    }
}
