package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/3/3.
 */
public abstract class DataModel {
    public boolean isValidData(Object paramObject){
        if(paramObject == null){
            return false;
        }
        return true;
    }

    public abstract Object modelWithData(Object paramObject);
}
