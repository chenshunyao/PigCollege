package com.xnf.henghenghui.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.MeetingManager;
import com.xnf.henghenghui.model.ConferenceNoteListModel;
import com.xnf.henghenghui.model.LessonNoteModel;
import com.xnf.henghenghui.ui.activities.NoteInfoActivity;
import com.xnf.henghenghui.ui.base.BaseFragment3;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.MoreDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public class LessonNoteFragment extends BaseFragment3 implements View.OnClickListener,AdapterView.OnItemClickListener {

    private static final String TAG = "LessonNoteFragment";
    private ListView mListView;
    private List<LessonNoteModel> mNotes = new ArrayList<LessonNoteModel>();
    private String mId,mStatus;
    private LessonNoteAdapter mNoteAdapter;
    private ConferenceNoteThread mNoteThread;
    ProgressDialog mDialog;
    private TextView mLessonNoteStatus;
    private TextView mLessonNoteOnlineNum;
    private boolean mProgressShow;

    public static LessonNoteFragment getInstance(Bundle bundle){
        LessonNoteFragment noteFragment = new LessonNoteFragment();
        noteFragment.setArguments(bundle);
        return noteFragment;
    }

    @Override
    protected String setFragmentTag() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initloadingDialog();
        //MeetingManager.setHandler(mHandler);
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d("csy","LessonNoteFragment onStart");
    }

    @Override
    protected void lazyLoad() {
        MeetingManager.setHandler(mHandler);
        L.d("csy","LessonNoteFragment lazyload");
        if(mNoteThread == null){
            L.d("csy","LessonNoteFragment lazyload getdata");
            mDialog.show();
            mNoteThread = new ConferenceNoteThread();
            mNoteThread.start();
        }
        if(mLessonNoteOnlineNum != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("online_num", Activity.MODE_PRIVATE);
            mLessonNoteOnlineNum.setText(getString(R.string.conference_online_num, sharedPreferences.getString("MEETING_ONLINE_NUM", "0")));
        }
    }

    @Override
    protected void initData(Bundle bundle) {
        //mNotes = getLessonNotes();
        mId = getActivity().getIntent().getStringExtra("cId");
        mStatus = getActivity().getIntent().getStringExtra("status");
        L.d("csy", "initData lessonNote  mId:" + mId);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mNotes = new ArrayList<LessonNoteModel>();
        View view = inflater.inflate(R.layout.lesson_note_fragment_layout,container,false);
        mListView = (ListView)view.findViewById(R.id.lessonnote_list);
        mLessonNoteStatus =(TextView)view.findViewById(R.id.meeting_detail_status);
        mLessonNoteOnlineNum =(TextView)view.findViewById(R.id.meeting_detail_join);
        if(!TextUtils.isEmpty(mStatus)){
            //0:未开始；1：直播中；2：已结束
            if(CodeUtil.NO_START.equals(mStatus)){
                //L.d("csy_"+TAG,"0 mMeetingStatus setText:"+status);
                mLessonNoteStatus.setText(getString(R.string.meeting_status0));
            }else if(CodeUtil.LIVING_CONFERENCE.equals(mStatus)){
                //L.d("csy_"+TAG,"1 mMeetingStatus setText:"+status);
                mLessonNoteStatus.setText(getString(R.string.meeting_status1));
            }else if(CodeUtil.CONFERENCE_END.equals(mStatus)) {
                //L.d("csy_"+TAG,"2 mMeetingStatus setText:"+status);
                mLessonNoteStatus.setText(getString(R.string.meeting_status2));
            }
        }
        /*SharedPreferences sharedPreferences = getActivity().getSharedPreferences("online_num", Activity.MODE_PRIVATE);
        mLessonNoteOnlineNum.setText(getString(R.string.conference_online_num,sharedPreferences.getString("MEETING_ONLINE_NUM","0")));*/
        mNoteAdapter = new LessonNoteAdapter(getContext());
        mListView.setAdapter(mNoteAdapter);
        mListView.setOnItemClickListener(this);
        //mListView.setAdapter(new LessonNoteAdapter(getContext(),mNotes));
        return view;
    }

    class ConferenceNoteThread extends Thread{
        public volatile boolean exit = false;
        @Override
        public void run() {
            L.d("csy","ConferenceNoteThread run exit:"+exit+";mId:"+mId);
            if(!exit) {
                MeetingManager.requestNoteList(LoginUserBean.getInstance().getLoginUserid(), mId);
            }
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
            case CodeUtil.CmdCode.MsgTypeCode.MSG_CLASS_CONFERENCE_NOTE_LIST:
                String resp  =(String)msg.obj;
                L.d(TAG, "note list Response:" + resp);
                if(mDialog != null && mDialog.isShowing()){
                    mDialog.dismiss();
                }
                if(Utils.getRequestStatus(resp) == 1){
                    mNotes.clear();
                    Gson gson = new Gson();
                    ConferenceNoteListModel responseModel = gson.fromJson(resp,ConferenceNoteListModel.class);
                    List<ConferenceNoteListModel.ConferenceNoteContent> contents = responseModel.getReponse().getContent();
                    for(int i=0;i<contents.size();i++){
                        ConferenceNoteListModel.ConferenceNoteContent content = contents.get(i);
                        LessonNoteModel data = new LessonNoteModel();
                        data.setnId(content.getnId());
                        data.setcId(content.getcId());
                        data.setNoteName(content.getTitle());
                        data.setNoteImg(content.getThumbnailURL());
                        data.setNoteContent(content.getContent());
                        data.setNoteDate(content.getCreateTime());
                        data.setNoteAuthor(content.getAuthorId());
                        mNotes.add(data);
                    }
                    if(mNotes != null && mNotes.size() > 0){
                        /*mNoteAdapter = new LessonNoteAdapter(getContext(),mNotes);
                        mListView.setAdapter(mNoteAdapter);*/
                        mNoteAdapter.notifyDataSetChanged();
                    }
                }else{
                    L.d(TAG,"resp lesson note list fail");
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), NoteInfoActivity.class);
        intent.putExtra("nId", mNotes.get(position).getnId());
        Utils.start_Activity(getActivity(), intent);
    }

    class LessonNoteAdapter extends BaseAdapter implements View.OnClickListener{
        //List<LessonNoteModel> mList;
        LayoutInflater mLayoutInflater;
        Context mContext;

        public LessonNoteAdapter(Context context){
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            //mList = datas;
        }

        @Override
        public int getCount() {
            return mNotes.size();
        }

        @Override
        public Object getItem(int position) {
            return mNotes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LessonNoteHolderView holderView;
            if(convertView == null){
                holderView = new LessonNoteHolderView();
                convertView = mLayoutInflater.inflate(R.layout.lesson_note_item_layout,parent,false);
                holderView.NoteImg =(ImageView)convertView.findViewById(R.id.lesson_note_image);
                holderView.name =(TextView)convertView.findViewById(R.id.lesson_note_name);
                holderView.content =(TextView)convertView.findViewById(R.id.lesson_note_content);
                holderView.time =(TextView)convertView.findViewById(R.id.lesson_note_time);
                //holderView.authorId =(TextView)convertView.findViewById(R.id.lesson_note_author);
                //holderView.more =(TextView)convertView.findViewById(R.id.lesson_note_more);
                //holderView.more.setOnClickListener(this);
                convertView.setTag(holderView);
            }else{
                holderView = (LessonNoteHolderView)convertView.getTag();
            }
            LessonNoteModel lessonNote = mNotes.get(position);
            //holderView.NoteImg.setImageResource(R.drawable.upload_img1);
            if (!TextUtils.isEmpty(lessonNote.getNoteImg())) {
                mImageLoader.displayImage(lessonNote.getNoteImg(), holderView.NoteImg,mOptions);
                //Picasso.with(mContext).load(lessonNote.getNoteImg()).error(R.drawable.banner_default).
                //        placeholder(R.drawable.banner_default).into(holderView.NoteImg);
            }  else {
                holderView.NoteImg.setImageResource(R.drawable.subject_cover_default);
            }
            holderView.name.setText(lessonNote.getNoteName());
            holderView.content.setText(lessonNote.getNoteContent());
            /*if(!TextUtils.isEmpty(lessonNote.getNoteAuthor())){
                holderView.authorId.setText(lessonNote.getNoteAuthor());
            }*/
            if(TextUtils.isEmpty(lessonNote.getNoteDate())){
                holderView.time.setText(StringUtils.getDataTime("yyyy-MM-dd HH:mm:ss"));
            }else{
                holderView.time.setText(lessonNote.getNoteDate());
            }
            return convertView;
        }

        @Override
        public void onClick(View v) {
            /*switch(v.getId()){
                case R.id.lesson_note_more:
                    MoreDialog dialog = new MoreDialog(getActivity(),mContext);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    break;
                default:
                    break;
            }
        }*/
        }
    }

     class LessonNoteHolderView{
        private ImageView NoteImg;
        private TextView name;
        private TextView time;
        private TextView content;
        //private TextView authorId;
       /* private ImageView img1;
        private ImageView img2;
        private ImageView img3;
        private ImageView img4;*/
        //private TextView more;
    }

    private List<LessonNoteModel> getLessonNotes(){
        List<LessonNoteModel> datas = new ArrayList<LessonNoteModel>();
        for(int i=0;i<2;i++){
            LessonNoteModel note = new LessonNoteModel();
            if(i == 0){
                note.setNoteName("重点一:如何预防突发猪病");
            }else if(i == 1){
                note.setNoteName("重点二:会议精神概述");
            }
            note.setNoteContent(getString(R.string.compere_content));
            datas.add(note);
        }
        return datas;
    }
}
