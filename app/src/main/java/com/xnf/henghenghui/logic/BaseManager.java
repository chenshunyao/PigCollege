package com.xnf.henghenghui.logic;

import com.xnf.henghenghui.ui.utils.HenghenghuiHandler;
import com.xnf.henghenghui.util.L;

import java.util.logging.Handler;

/**
 * Created by Administrator on 2016/1/22.
 */
public class BaseManager {

    public static int sRequestNum = 20;

    protected static HenghenghuiHandler mHandler;
    public static void setHandler(HenghenghuiHandler handler){
        mHandler=handler;
    }

    public static String getRequestBody(String jsonString){
        L.d("csy", "jsonString:" + jsonString);
        return "{"+"\"request\""+":" + jsonString + "}";
    }


}
