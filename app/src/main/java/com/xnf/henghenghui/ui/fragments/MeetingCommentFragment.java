package com.xnf.henghenghui.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.MeetingManager;
import com.xnf.henghenghui.model.CommentModel;
import com.xnf.henghenghui.model.ConferenceCommentListModel;
import com.xnf.henghenghui.ui.activities.CommentListActivity;
import com.xnf.henghenghui.ui.base.BaseFragment3;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MeetingCommentFragment extends BaseFragment3 implements View.OnClickListener {

    private static final String TAG = "csy_MeetingCommentFragment";
    private static final String TYPE = "CF";
    private ListView mListView;
    private List<CommentModel> mComments;
    private String mId,mStatus;
    private MeetingCommentAdapter mCommentAdapter;
    private ConferenceCommentThread mCommentThread;
    private EditText mEditComment;
    private Button mBtnComment;
    private TextView mMeetingCommentStatus;
    private TextView mMeetingCommentOnlineNum;
    ProgressDialog mDialog;
    private boolean mProgressShow;

    public static MeetingCommentFragment getInstance(Bundle bundle){
        MeetingCommentFragment commentFragment = new MeetingCommentFragment();
        commentFragment.setArguments(bundle);
        return commentFragment;
    }
    @Override
    protected String setFragmentTag() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MeetingManager.setHandler(mHandler);
        initloadingDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d("csy","MeetingCommentFragment onStart");
    }

    @Override
    protected void lazyLoad() {
        MeetingManager.setHandler(mHandler);
        L.d("csy", "MeetingCommentFragment lazyload");
        if(mCommentThread == null){
            L.d("csy","MeetingCommentFragment lazyload getdata");
            mDialog.show();
            mCommentThread = new ConferenceCommentThread();
            mCommentThread.start();
        }
        if(mMeetingCommentOnlineNum != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("online_num", Activity.MODE_PRIVATE);
            mMeetingCommentOnlineNum.setText(getString(R.string.conference_online_num, sharedPreferences.getString("MEETING_ONLINE_NUM", "0")));
        }
    }

    @Override
    protected void initData(Bundle bundle) {
        //mComments = getMeetingComments();
        mId = getActivity().getIntent().getStringExtra("cId");
        mStatus = getActivity().getIntent().getStringExtra("status");
        L.d("csy", "initData conference comment list  mId:" + mId);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mComments = new ArrayList<CommentModel>();
        View view = inflater.inflate(R.layout.fragment_meeting_comment_layout,container,false);
        mListView =(ListView)view.findViewById(R.id.meeting_comment_list);
        mEditComment = (EditText)view.findViewById(R.id.meeting_comment_text);
        mBtnComment = (Button)view.findViewById(R.id.meeting_comment_add);
        mMeetingCommentStatus = (TextView)view.findViewById(R.id.meeting_detail_status);
        mMeetingCommentOnlineNum =(TextView)view.findViewById(R.id.meeting_detail_join);
        if(!TextUtils.isEmpty(mStatus)){
            //0:未开始；1：直播中；2：已结束
            if(CodeUtil.NO_START.equals(mStatus)){
                //L.d("csy_"+TAG,"0 mMeetingStatus setText:"+status);
                mMeetingCommentStatus.setText(getString(R.string.meeting_status0));
            }else if(CodeUtil.LIVING_CONFERENCE.equals(mStatus)){
                //L.d("csy_"+TAG,"1 mMeetingStatus setText:"+status);
                mMeetingCommentStatus.setText(getString(R.string.meeting_status1));
            }else if(CodeUtil.CONFERENCE_END.equals(mStatus)) {
                //L.d("csy_"+TAG,"2 mMeetingStatus setText:"+status);
                mMeetingCommentStatus.setText(getString(R.string.meeting_status2));
            }
        }
        //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("online_num", Activity.MODE_PRIVATE);
        //mMeetingCommentOnlineNum.setText(getString(R.string.conference_online_num,sharedPreferences.getString("MEETING_ONLINE_NUM","0")));
        mBtnComment.setOnClickListener(this);
        //mListView.setAdapter(new MeetingCommentAdapter(getContext(),mComments));
        return view;
    }

    class ConferenceCommentThread extends Thread{
        public volatile boolean exit = false;
        @Override
        public void run() {
            L.d("csy","ConferenceCommentThread run exit:"+exit+";mId:"+mId);
            if(!exit) {
                MeetingManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(), mId,mHandler);
            }
        }
    }

    @Override
    public boolean handleNotifyMessage(Message msg) {
        return false;
    }

    @Override
    public boolean handleUIMessage(Message msg) {
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what){
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_COMMENT_LIST:
                String resp  =(String)msg.obj;
                L.d(TAG, "comment list Response:" + resp);
                if(mDialog != null && mDialog.isShowing()){
                    mDialog.dismiss();
                }
                if(Utils.getRequestStatus(resp) == 1){
                    if(mComments != null && mComments.size() > 0){
                        mComments.clear();
                    }
                    Gson gson = new Gson();
                    ConferenceCommentListModel responseModel = gson.fromJson(resp,ConferenceCommentListModel.class);
                    List<ConferenceCommentListModel.ConferenceCommentContent> contents = responseModel.getReponse().getContent();
                    for(int i=0;i<contents.size();i++){
                        ConferenceCommentListModel.ConferenceCommentContent content = contents.get(i);
                        CommentModel comment = new CommentModel();
                        comment.setComtId(content.getComtId());
                        comment.setCommentDesc(content.getContent());
                        comment.setComtUserId(content.getComtUserId());
                        comment.setPhotos(content.getUserPhoto());
                        comment.setCommentName(content.getComtNikeName());
                        comment.setComtDate(content.getComtDate());
                        comment.setCommentFrom("湖北养户");
                        mComments.add(comment);
                    }
                    if(mComments != null && mComments.size() > 0){
                        if(mCommentAdapter == null){
                            mCommentAdapter = new MeetingCommentAdapter(getContext(),mComments);
                            mListView.setAdapter(mCommentAdapter);
                        }else{
                            mCommentAdapter.notifyDataSetChanged();
                        }
                    }
                }else{
                    L.d(TAG,"resp comment conference list fail");
                }
                break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_ADD_COMMENT:
                String response  =(String)msg.obj;
                L.d(TAG, "add comment Response:" + response);
                if(Utils.getRequestStatus(response) == 1){
                    Toast.makeText(getContext(),R.string.comment_success,Toast.LENGTH_SHORT).show();
                    MeetingManager.requestCommentList(LoginUserBean.getInstance().getLoginUserid(), mId,mHandler);
                }else{
                    Toast.makeText(getContext(),R.string.comment_fail,Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.meeting_comment_add:
                String content = mEditComment.getText().toString();
                L.d(TAG,"onclick content:"+content);
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(getContext(),R.string.add_comment_hint,Toast.LENGTH_SHORT).show();
                    return;
                }
                MeetingManager.requestAddComment(LoginUserBean.getInstance().getLoginUserid(), mId, TYPE, content,mHandler);
                mEditComment.getText().clear();
                break;
            default:
                break;
        }
    }

    private void initloadingDialog(){
        mProgressShow = true;
        mDialog = new ProgressDialog(getContext());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mProgressShow = false;
            }
        });
        mDialog.setMessage(getString(R.string.dl_waiting));
    }

    class MeetingCommentAdapter extends BaseAdapter{
        List<CommentModel> mDatas;
        LayoutInflater mLayoutInflater;

        public MeetingCommentAdapter(Context context,List<CommentModel> datas){
            mDatas = datas;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MeetingCommentHolderView holderView;
            if(convertView == null){
                holderView = new MeetingCommentHolderView();
                convertView = mLayoutInflater.inflate(R.layout.article_comment_item,parent,false);
                holderView.commenterImg =(ImageView)convertView.findViewById(R.id.comment_icon);
                holderView.name =(TextView)convertView.findViewById(R.id.comment_name);
                holderView.from =(TextView)convertView.findViewById(R.id.comment_from);
                holderView.commentContent =(TextView)convertView.findViewById(R.id.comment_content);
                holderView.comment_time = (TextView)convertView.findViewById(R.id.comment_time);
                convertView.setTag(holderView);
            }else{
                holderView = (MeetingCommentHolderView)convertView.getTag();
            }
            CommentModel comment = mDatas.get(position);
            if(!TextUtils.isEmpty(comment.getCommentName())) {
                holderView.name.setText(comment.getCommentName());
            }else{
                holderView.name.setText(R.string.comment_default_nickname);
			}
            if(comment.getPhotos() != null && !comment.getPhotos().isEmpty()){
                Picasso.with(getActivity()).load(comment.getPhotos()).error(R.drawable.index_zhuanti).
                        placeholder(R.drawable.index_zhuanti).into(holderView.commenterImg);
            }else{
                holderView.commenterImg.setImageResource(R.drawable.index_zhuanti);
            }
            holderView.comment_time.setText(comment.getComtDate());
            holderView.from.setText(comment.getCommentFrom());
            holderView.commentContent.setText(comment.getCommentDesc());
            return convertView;
        }
    }

    static class MeetingCommentHolderView{
        ImageView commenterImg;
        TextView name;
        TextView from;
        TextView commentContent;
        private TextView comment_time;
    }

    private List<CommentModel> getMeetingComments(){
        List<CommentModel> lists = new ArrayList<CommentModel>();
        for(int i=0;i<8;i++){
            CommentModel comment = new CommentModel();
            comment.setCommentName("小凯");
            comment.setCommentFrom("海南养户");
            comment.setCommentDesc("讲得太好了，对我有很大的帮助，以前的很多盲区，都发现了！感谢！");
            lists.add(comment);
        }
        return lists;
    }
}
