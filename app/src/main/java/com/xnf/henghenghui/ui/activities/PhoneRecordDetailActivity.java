package com.xnf.henghenghui.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chatuidemo.ui.VoiceCallActivity;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.util.DateUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.PostRequest;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpCommitAnwserResponse;
import com.xnf.henghenghui.model.HttpUploadImageResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.ChildRecyclerView;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.ImageUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.MD5Calculator;
import com.xnf.henghenghui.util.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhoneRecordDetailActivity extends BaseActivity implements View.OnClickListener, EMMessageListener {

    private ImageView mBackImg;
    private TextView mTitle;
    private ListView mList;
    private View mHeadView;
    private int pagesize = Integer.MAX_VALUE;
    private PhoneRecordAdapter mAdapter;

    private ProgressDialog dialog;

    private String toChatUsername;
    protected EMConversation conversation;
    private boolean isMessageListInited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {
        initDialog();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_phone_record_detail);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.phone_record_detail_title);
        mList = (ListView)findViewById(R.id.list);

        toChatUsername = getIntent().getExtras().getString("userId");

        mHeadView = getLayoutInflater().inflate(R.layout.head_phone_record_detail,null);
        ImageView headAvatar = (ImageView)mHeadView.findViewById(R.id.avatar);
        TextView headName = (TextView)mHeadView.findViewById(R.id.name);
        ViewGroup headPhone = (ViewGroup)mHeadView.findViewById(R.id.btn_phone);
        EaseUserUtils.setUserAvatar(this, toChatUsername, headAvatar);
        EaseUserUtils.setUserNick(toChatUsername, headName);
//        EaseUserUtils.setUserNick("abcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefg", headName);
        headPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!EMClient.getInstance().isConnected()) {
                    Toast.makeText(PhoneRecordDetailActivity.this, R.string.not_connect_to_server, Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(PhoneRecordDetailActivity.this, VoiceCallActivity.class)
                            .putExtra("username", toChatUsername)
                            .putExtra("isComingCall", false));
                }
            }
        });
        mList.addHeaderView(mHeadView);

        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
//        conversation.markAllMessagesAsRead();
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
        isMessageListInited = false;
        mAdapter = new PhoneRecordAdapter();
        mList.setAdapter(mAdapter);
        refreshSelectLast();
        isMessageListInited = true;

        bindOnClickLister(this, mBackImg);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isMessageListInited) {
            refresh();
        }

        EaseUI.getInstance().pushActivity(this);
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
        //onPrepareOptionsMenu(menu);
    }
    @Override
    protected void setUI(Configuration newConfig, DisplayMetrics dm) {

    }

    @Override
    public boolean handleNotifyMessage(Message msg) {
        return false;
    }

    @Override
    public boolean handleUIMessage(Message msg) {
        return false;
    }

    private void initDialog(){
        dialog = new ProgressDialog(PhoneRecordDetailActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        dialog.setMessage(getString(R.string.progress_doing));
    }

    @Override
    public boolean handleMessage(Message msg) {
//        switch (msg.what) {
//            case CodeUtil.CmdCode.MsgTypeCode.MSG_UPLOAD_IMAGE: {
//                String response = (String)msg.obj;
//                if (Utils.getRequestStatus(response) == 1) {
//                    Gson gson = new Gson();
//                    HttpUploadImageResponse httpUploadImageResponse = gson.fromJson(response, HttpUploadImageResponse.class);
//                    String imgid = httpUploadImageResponse.getResponse().getContent().getFileMappingId();
//                    String imgPath = httpUploadImageResponse.getResponse().getContent().getFilePath();
//
//                    String userId = LoginUserBean.getInstance().getLoginUserid();
//                    JSONObject jsonObj = new JSONObject();
//                    try {
//                        jsonObj.put("userId", userId);
//                        jsonObj.put("qtId", mQtId);
//                        jsonObj.put("replyContent", mAnwser.getText());
//                        jsonObj.put("fileMappingId", imgid);
//                    } catch (Exception e) {
//                    }
//                    String jsonString = jsonObj.toString();
//                    OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_ANWSER_QUESTION)
//                            .tag(Urls.ACTION_ANWSER_QUESTION)
//                            .postJson(getRequestBody(jsonString))
//                            .execute(new MyJsonCallBack<String>() {
//                                @Override
//                                public void onResponse(String s) {
//                                    L.d(TAG, "onResponse:" + s);
//                                    Message msg = new Message();
//                                    msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_ANWSER_QUESTION;
//                                    msg.obj=s;
//                                    mHandler.sendMessage(msg);
//                                }
//                            });
//                }
//            }
//            case CodeUtil.CmdCode.MsgTypeCode.MSG_ANWSER_QUESTION: {
//                String response = (String)msg.obj;
//                if (Utils.getRequestStatus(response) == 1) {
//                    Gson gson = new Gson();
//                    HttpCommitAnwserResponse httpCommitAnwserResponse = gson.fromJson(response, HttpCommitAnwserResponse.class);
//                    if(httpCommitAnwserResponse.getResponse().getSucceed() == 1){
//                        ToastUtil.showToast(PhoneRecordDetailActivity.this,getString(R.string.op_success));
//                        //同步刷新界面
//                        QuestionDetailActivity.DATA_CHANGE = true;
//                        MyQuestionActivity.DATA_CHANGE = true;
//                        WhoAnwserQuestionActivity.DATA_CHANGE = true;
//                        //MasterDetailActivity.DATA_CHANGE = true;
//                        finish();
//                    }else{
//                        ToastUtil.showToast(PhoneRecordDetailActivity.this,getString(R.string.op_failed,
//                                httpCommitAnwserResponse.getResponse().getErrorCode(),
//                                httpCommitAnwserResponse.getResponse().getErrorInfo()));
//                    }
//                }
//                if(dialog != null) {
//                    dialog.dismiss();
//                }
//            }
//        }
        return false;
    }

    public void refresh(){
        if (mAdapter != null) {
            mAdapter.refresh();
        }
    }

    /**
     * refresh and jump to the last
     */
    public void refreshSelectLast(){
        if (mAdapter != null) {
            mAdapter.refreshSelectLast();
        }
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername)) {
                refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> messages) {
        if(isMessageListInited) {
            refresh();
        }
    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> messages) {
        if(isMessageListInited) {
            refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        if(isMessageListInited) {
            refresh();
        }
    }

    public static boolean isPhone(EMMessage message) {
        boolean rs = false;
        switch (message.getType()) {
            case TXT:
//                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    rs = true;
                }else if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    rs = true;
                }
                break;
        }
        return rs;
    }

    public static class ViewHolder {
        public TextView timeout;
        public TextView message;
        public TextView time;
        public ImageView sign;
    }

    public class PhoneRecordAdapter extends BaseAdapter {

        private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
        private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
        private static final int HANDLER_MESSAGE_SEEK_TO = 2;

        EMMessage[] messages = null;

        public PhoneRecordAdapter(){
        }

        public EMMessage getItem(int position) {
            if (messages != null && position < messages.length) {
                return messages[position];
            }
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        /**
         * get count of messages
         */
        public int getCount() {
            return messages == null ? 0 : messages.length;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            EMMessage message = getItem(position);
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.row_phone_record_detail, null);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.timeout = (TextView) convertView.findViewById(R.id.timeout);
                holder.message = (TextView) convertView.findViewById(R.id.message);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.sign = (ImageView) convertView.findViewById(R.id.sign);
                convertView.setTag(holder);
            }

            EMMessage.Type type = message.getType();
            EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
            if(type == EMMessage.Type.TXT){
                if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    holder.timeout.setText(txtBody.getMessage());
                    holder.time.setText(R.string.phone_record_voice_call);
                }else if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    holder.timeout.setText(txtBody.getMessage());
                    holder.time.setText(R.string.phone_record_video_call);
                }
            }
            holder.message.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));

            if(toChatUsername.equals(message.getFrom())){
                if(txtBody.getMessage().startsWith(getString(R.string.call_duration))) {
                    holder.sign.setImageResource(R.drawable.phone_record_in);
                }else{
                    holder.sign.setImageResource(R.drawable.phone_record_in_s);
                }
            }else{
                if(txtBody.getMessage().startsWith(getString(R.string.call_duration))) {
                    holder.sign.setImageResource(R.drawable.phone_record_out);
                }else{
                    holder.sign.setImageResource(R.drawable.phone_record_out_s);
                }
            }

            return convertView;
        }

        public void refresh() {
            if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
                return;
            }
            android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
            handler.sendMessage(msg);
        }

        /**
         * refresh and select the last
         */
        public void refreshSelectLast() {
            final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
            handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
            handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
            handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
            handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);
        }

        Handler handler = new Handler() {
            private void refreshList() {
                // you should not call getAllMessages() in UI thread
                // otherwise there is problem when refreshing UI and there is new message arrive
                java.util.List<EMMessage> var = conversation.getAllMessages();
                java.util.List<EMMessage> rs = new java.util.ArrayList<EMMessage>();
                EMMessage message = null;
                for (int i = var.size()-1;i >=0;i--) {
                    message = var.get(i);
                    if(isPhone(message)){
                        rs.add(message);
                    }
                }
                messages = rs.toArray(new EMMessage[rs.size()]);
//                conversation.markAllMessagesAsRead();
                notifyDataSetChanged();
            }

            @Override
            public void handleMessage(android.os.Message message) {
                switch (message.what) {
                    case HANDLER_MESSAGE_REFRESH_LIST:
                        refreshList();
                        break;
                    case HANDLER_MESSAGE_SELECT_LAST:
                        if (messages.length > 0) {
                            mList.setSelection(messages.length - 1);
                        }
                        break;
                    case HANDLER_MESSAGE_SEEK_TO:
                        int position = message.arg1;
                        mList.setSelection(position);
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
