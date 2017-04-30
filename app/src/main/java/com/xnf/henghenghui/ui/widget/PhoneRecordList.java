package com.xnf.henghenghui.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.adapter.EaseConversationAdapter;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.ui.adapter.PhoneRecordAdapter;

import java.util.List;

/**
 *
 * @author markmjw
 * @date 2013-10-08
 */
public class PhoneRecordList extends EaseConversationList {

    public PhoneRecordList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhoneRecordList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(List<EMConversation> conversationList, EaseConversationListHelper helper){
        conversations = conversationList;
        if(helper != null){
            this.conversationListHelper = helper;
        }
        adapter = new PhoneRecordAdapter(context, 0, conversationList);
        adapter.setCvsListHelper(conversationListHelper);
        adapter.setPrimaryColor(primaryColor);
        adapter.setPrimarySize(primarySize);
        adapter.setSecondaryColor(secondaryColor);
        adapter.setSecondarySize(secondarySize);
        adapter.setTimeColor(timeColor);
        adapter.setTimeSize(timeSize);
        setAdapter(adapter);
    }
}
