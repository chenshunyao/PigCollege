package com.xnf.henghenghui.ui.utils;

import android.os.Message;

public interface HandlerInterface {
    void handleCommonMsg(Message msg);

    boolean handleNotifyMessage(Message msg);

    boolean handleUIMessage(Message msg);

    boolean handleMessage(Message msg);
}
