package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/5/9.
 */
public class QAListItem
{
//    /**
//     * 图标id
//     */
//    private int iconId;
    /**
     * 类别名称
     */
    private String qaName;
    /**
     * 类别描述
     */
    private String qaDesc;
//    /**
//     * 问题数目
//     */
//    private int qsNum;

    public String getQaDesc() {
        return qaDesc;
    }

    public void setQaDesc(String qaDesc) {
        this.qaDesc = qaDesc;
    }

    public String getQaName() {
        return qaName;
    }

    public void setQaName(String qaName) {
        this.qaName = qaName;
    }

}
