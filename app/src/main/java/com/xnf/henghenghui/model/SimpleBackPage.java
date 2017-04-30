package com.xnf.henghenghui.model;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.ui.fragments.AboutHenghenghuiFragment;
import com.xnf.henghenghui.ui.fragments.BrowserFragment;
import com.xnf.henghenghui.ui.fragments.FeedBackFragment;
import com.xnf.henghenghui.ui.fragments.SettingsNotificationFragment;

/**
 * Created by Administrator on 2016/5/1.
 */
public enum SimpleBackPage {

    ABOUT_Henghenghui(1, R.string.actionbar_title_about_henghenghui, AboutHenghenghuiFragment.class),

    BROWSER(2, R.string.app_name, BrowserFragment.class),

    SETTING_NOTIFICATION(3, R.string.actionbar_title_setting_notification,
            SettingsNotificationFragment.class),

    FEED_BACK(4, R.string.str_feedback_title, FeedBackFragment.class);

    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }
}
