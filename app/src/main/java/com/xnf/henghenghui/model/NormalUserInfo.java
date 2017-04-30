package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/4/30.
 */
public class NormalUserInfo {

    private String userId;

    private String userName;

    private String nikeName;

    private String mobile;

    private String email;

   // private String city;

    private String address;

   // private String species;

  //  private String businessType;

    private String farmName;

    private String farmAddress;

    private String company;

    private String breedScope;

    private String fileMappingId;

    private String photo;

    private String avatarLocalPath;

    private String duites;

    private String userPoints;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBreedScope() {
        return breedScope;
    }

    public void setBreedScope(String breedScope) {
        this.breedScope = breedScope;
    }

    public String getDuites() {
        return duites;
    }

    public void setDuites(String duites) {
        this.duites = duites;
    }

    //    public String getBusinessType() {
//        return businessType;
//    }
//
//    public void setBusinessType(String businessType) {
//        this.businessType = businessType;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

//    public String getSpecies() {
//        return species;
//    }
//
//    public void setSpecies(String species) {
//        this.species = species;
//    }

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


    public String getFarmAddress() {
        return farmAddress;
    }

    public void setFarmAddress(String farmAddress) {
        this.farmAddress = farmAddress;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
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
        return "NormalUserInfo{" +
                "address='" + address + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", farmName='" + farmName + '\'' +
                ", farmAddress='" + farmAddress + '\'' +
                ", company='" + company + '\'' +
                ", breedScope='" + breedScope + '\'' +
                ", fileMappingId='" + fileMappingId + '\'' +
                ", photo='" + photo + '\'' +
                ", avatarLocalPath='" + avatarLocalPath + '\'' +
                ", duites='" + duites + '\'' +
                ", userPoints='" + userPoints + '\'' +
                '}';
    }
}
