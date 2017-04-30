package com.xnf.henghenghui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/15.
 */
public class BannerResponseModel {
    private ResponseInfo response;
    public class ResponseInfo{
        private int succeed;
        private int array;
        private int total;
        private List<BannerContent> content = new ArrayList<BannerContent>();

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }

        public void setArray(int array) {
            this.array = array;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setContent(List<BannerContent> content) {
            this.content = content;
        }

        public int getSucceed() {
            return succeed;
        }

        public int getArray() {
            return array;
        }

        public int getTotal() {
            return total;
        }

        public List<BannerContent> getContents() {
            return content;
        }
    }
    public static class BannerContent{
        private String bannerId;
        private String bannerDesc;
        private String bannerImgURL;
        private String bannerHref;

        public String getBannerId() {
            return bannerId;
        }

        public void setBannerId(String bannerId) {
            this.bannerId = bannerId;
        }

        public String getBannerDesc() {
            return bannerDesc;
        }

        public void setBannerDesc(String bannerDesc) {
            this.bannerDesc = bannerDesc;
        }

        public String getBannerImgURL() {
            return bannerImgURL;
        }

        public void setBannerImgURL(String bannerImgURL) {
            this.bannerImgURL = bannerImgURL;
        }

        public String getBannerHref() {
            return bannerHref;
        }

        public void setBannerHref(String bannerHref) {
            this.bannerHref = bannerHref;
        }
    }

    public ResponseInfo getReponse() {
        return response;
    }

    public void setReponse(ResponseInfo reponse) {
        this.response = reponse;
    }

}
