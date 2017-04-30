package com.xnf.henghenghui.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.adapter.EaseConversationAdapter;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.util.DateUtils;
import com.xnf.henghenghui.R;

import java.util.Date;
import java.util.List;

/**
 *
 * @author markmjw
 * @date 2013-10-08
 */
public class PhoneRecordAdapter extends EaseConversationAdapter {

    public PhoneRecordAdapter(Context context, int resource, List<EMConversation> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_phone_record, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.messageRight = (TextView) convertView.findViewById(R.id.message_right);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(R.id.list_itease_layout);
            holder.motioned = (TextView) convertView.findViewById(R.id.mentioned);
            convertView.setTag(holder);
        }
        holder.list_itease_layout.setBackgroundResource(R.drawable.ease_mm_listitem);

        // get conversation
        EMConversation conversation = getItem(position);
        // get username or group id
        String username = conversation.getUserName();
//        username = "abcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefg";
//        if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
//            String groupId = conversation.getUserName();
//            if(EaseAtMessageHelper.get().hasAtMeMsg(groupId)){
//                holder.motioned.setVisibility(View.VISIBLE);
//            }else{
//                holder.motioned.setVisibility(View.GONE);
//            }
//            // group message, show group avatar
//            holder.avatar.setImageResource(R.drawable.ease_group_icon);
//            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
//            holder.name.setText(group != null ? group.getGroupName() : username);
//        } else if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
//            holder.avatar.setImageResource(R.drawable.ease_group_icon);
//            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
//            holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
//            holder.motioned.setVisibility(View.GONE);
//        }else {
            EaseUserUtils.setUserAvatar(getContext(), username, holder.avatar);
            EaseUserUtils.setUserNick(username, holder.name);
            holder.motioned.setVisibility(View.GONE);
//        }

//        int count = getUnreadCount(conversation);
//        if (count > 0) {
//            // show unread message count
//            holder.unreadLabel.setText(String.valueOf(count));
//            holder.unreadLabel.setVisibility(View.VISIBLE);
//        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
//        }

        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = getLastPhoneMessage(conversation);
            if(lastMessage == null) {
                lastMessage = conversation.getLastMessage();
            }
            String content = null;
            if(cvsListHelper != null){
                content = cvsListHelper.onSetItemSecondaryText(lastMessage);
            }
//            holder.message.setText(EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
//                    TextView.BufferType.SPANNABLE);
            EMMessage.Type type = lastMessage.getType();
            if(type == EMMessage.Type.TXT){
                EMTextMessageBody txtBody = (EMTextMessageBody) lastMessage.getBody();
                if(lastMessage.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    holder.messageRight.setText(txtBody.getMessage());
                    holder.time.setText(R.string.phone_record_voice_call);
                }else if(lastMessage.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    holder.messageRight.setText(txtBody.getMessage());
                    holder.time.setText(R.string.phone_record_video_call);
                }
            }
            if(content != null){
                holder.messageRight.setText(content);
            }
            holder.message.setText(getContext().getString(R.string.phone_record_last_time_label,
                    DateUtils.getTimestampString(new Date(lastMessage.getMsgTime()))));
//            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
//                holder.msgState.setVisibility(View.VISIBLE);
//            } else {
//                holder.msgState.setVisibility(View.GONE);
//            }
        }

        //set property
        holder.name.setTextColor(primaryColor);
        holder.message.setTextColor(secondaryColor);
        holder.time.setTextColor(timeColor);
        holder.messageRight.setTextColor(secondaryColor);
        if(primarySize != 0)
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if(secondarySize != 0)
            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
        if(timeSize != 0)
            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        return convertView;
    }

    private EMMessage getLastPhoneMessage(EMConversation conversation){
        List<EMMessage> msglist = conversation.getAllMessages();
        EMMessage message = null;
        for (int i = msglist.size()-1;i >=0;i--) {
            message = msglist.get(i);
            if(isPhone(message)){
                break;
            }
        }
        return message;
    }

    public static boolean isPhone(EMMessage message) {
        boolean rs = false;
        switch (message.getType()) {
            case TXT:
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    rs = true;
                }else if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    rs = true;
                }
                break;
        }
        return rs;
    }

    public static int getUnreadCount(EMConversation conversation){
        int count = 0;
        List<EMMessage> msglist = conversation.getAllMessages();
        EMMessage message = null;
        for (int i = msglist.size()-1;i >=0;i--) {
            message = msglist.get(i);
            switch (message.getType()) {
                case TXT:
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false) && message.isUnread()){
                        count++;
                    }else if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false) && message.isUnread()){
                        count++;
                    }
                    break;
            }
        }
        return count;
    }
}
