package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/5/9.
 */
public class QACategory
{
    /**
     * 图标id
     */
    private int iconId;
    /**
     * 类别名称
     */
    private String categoryName;
    /**
     * 类别描述
     */
    private String categoryDesc;
    /**
     * 问题数目
     */
    private int qsNum;

    /**
     * 问题对应的id
     * @return
     */
    private String categoryId;

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getQsNum() {
        return qsNum;
    }

    public void setQsNum(int qsNum) {
        this.qsNum = qsNum;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
