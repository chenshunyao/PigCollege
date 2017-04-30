package com.xnf.henghenghui.cityselection;

/**
 * Created by Administrator on 2016/4/20.
 */
public class City {
    private String name;
    private String pinyi;
    private String areaCode;

    public City(String name, String pinyi,String code) {
        super();
        this.name = name;
        this.pinyi = pinyi;
        this.areaCode = code;
    }

    public City() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyi() {
        return pinyi;
    }

    public void setPinyi(String pinyi) {
        this.pinyi = pinyi;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
