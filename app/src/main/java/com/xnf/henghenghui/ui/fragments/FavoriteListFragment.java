package com.xnf.henghenghui.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.CourseModel;
import com.xnf.henghenghui.model.HotArticleModel;
import com.xnf.henghenghui.model.HotSubjectModel;
import com.xnf.henghenghui.model.HttpArtilcleListResponse;
import com.xnf.henghenghui.model.HttpQaListResponse;
import com.xnf.henghenghui.model.HttpSubjectListResponse;
import com.xnf.henghenghui.model.MeetingListResponseModel;
import com.xnf.henghenghui.model.MeetingModel;
import com.xnf.henghenghui.model.SubjectArticleModel;
import com.xnf.henghenghui.model.VideoListResponseModel;
import com.xnf.henghenghui.ui.activities.ArticleDetailActivity2;
import com.xnf.henghenghui.ui.activities.CourseDetailActivity;
import com.xnf.henghenghui.ui.activities.CourseListActivity;
import com.xnf.henghenghui.ui.activities.MasterDetailActivity;
import com.xnf.henghenghui.ui.activities.MeetingActivity;
import com.xnf.henghenghui.ui.activities.MeetingDetailActivity;
import com.xnf.henghenghui.ui.activities.QuestionDetailActivity;
import com.xnf.henghenghui.ui.activities.SubjectActivity;
import com.xnf.henghenghui.ui.adapter.HotArticleAdapter;
import com.xnf.henghenghui.ui.adapter.HotSubjectAdapter;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.togglebutton.Util;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListFragment extends BaseFragment2 implements AdapterView.OnItemClickListener{

    private ListView mFavoriteList;
    private FavoriteListAdapter mAdapter;
    private View emptyView;

    private OnFragmentInteractionListener mListener;

    private HttpQaListResponse mHttpQaListResponse;
    private String mType;
    private static String TAG = "FavoriteListFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mFavoriteList = (ListView)view.findViewById(R.id.favorite_list);
        mFavoriteList.setAdapter(mAdapter);
        emptyView=view.findViewById(R.id.emptyview);
        mFavoriteList.setEmptyView(emptyView);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initData() {
        super.initData();
//        mQaList = new ArrayList<QAListItem>();
//        for(int i = 0;i < 10;i++){
//            QAListItem  category1 = new QAListItem();
//            category1.setQaName("种猪");
//            category1.setQaDesc("关于种猪相关的提问，如种猪的看护等");
//            mQaList.add(category1);
//        }

//        mAdapter = new FavoriteListAdapter(getActivity(),mQaList);
//        mFavoriteList.setAdapter(mAdapter);
        mFavoriteList.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        initView(view);
        initData();
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(mType.equals(Utils.TYPE_ARTICLE)){
            HotArticleModel info = (HotArticleModel) mAdapter.getItem(i);
            Intent intent = new Intent(getActivity(), ArticleDetailActivity2.class);
            intent.putExtra("ARTICLE_ID",info.getArticleId());
            Utils.start_Activity(getActivity(), intent);
        } else if(mType.equals(Utils.TYPE_VIDEO)){
            CourseModel course =  (CourseModel) mAdapter.getItem(i);
            int type = course.getcType();
            ArrayList<CourseModel> cour = (ArrayList<CourseModel>)mAdapter.vList;
            //Log.d("csy", "onitemclick course:" + course +";cour:"+cour+";type:"+type);
            Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
            Bundle bundle = new Bundle();
            if(type == 1){
                bundle.putParcelableArrayList("freecourse", cour);
            }else if(type == 2){
                bundle.putParcelableArrayList("hotcourse", cour);
            }else if(type == 3){
                bundle.putParcelableArrayList("discourse", cour);
            }else{
                bundle.putParcelableArrayList("favcourse", cour);
            }
            bundle.putSerializable("course", course);
            intent.putExtras(bundle);
            Utils.start_Activity(getActivity(),intent);
        } else if(mType.equals(Utils.TYPE_MEETING)){
            MeetingModel meeting = (MeetingModel)mAdapter.getItem(i);
            if(meeting == null)return;
            Intent intent = new Intent(getActivity(), MeetingDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("meeting", meeting);
            intent.putExtras(bundle);
            Utils.start_Activity(getActivity(), intent);
        } else if(mType.equals(Utils.TYPE_QUESTION)){
             //TODO
        }else if(mType.equals(Utils.TYPE_TOPIC)){
            //TODO
        }else if(mType.equals(Utils.TYPE_SUBJECT)){
            HotSubjectModel info = (HotSubjectModel) mAdapter.getItem(i);
            Intent intent = new Intent(getActivity(), SubjectActivity.class);
            intent.putExtra("SUBJECT_ID",info.getSubjectId());
            Utils.start_Activity(getActivity(), intent);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class FavoriteListAdapter extends BaseAdapter {
        private ArrayList<HotArticleModel> qList;
        private List<CourseModel> vList;
        private List<MeetingModel> meetingList;
        private List<HotSubjectModel> subjectList;
        public FavoriteListAdapter(Context context, ArrayList infoList){
            if(mType.equals(Utils.TYPE_ARTICLE)){
                this.qList = infoList;
            } else if(mType.equals(Utils.TYPE_VIDEO)){
                this.vList = infoList;
            } else if(mType.equals(Utils.TYPE_MEETING)){
                this.meetingList = infoList;
            } else if(mType.equals(Utils.TYPE_QUESTION)){

            }else if(mType.equals(Utils.TYPE_SUBJECT)){
                this.subjectList = infoList;
            }
        }

        @Override
        public int getCount() {
            if(mType.equals(Utils.TYPE_ARTICLE)){
                return qList.size();
            } else if(mType.equals(Utils.TYPE_VIDEO)){
                return vList.size();
            } else if(mType.equals(Utils.TYPE_MEETING)){
                return meetingList.size();
            } else if(mType.equals(Utils.TYPE_QUESTION)){
                //TODO
                return 0;
            } else if(mType.equals(Utils.TYPE_SUBJECT)){
                return subjectList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(mType.equals(Utils.TYPE_ARTICLE)){
                return qList.get(position);
            } else if(mType.equals(Utils.TYPE_VIDEO)){
                return vList.get(position);
            } else if(mType.equals(Utils.TYPE_MEETING)){
                return meetingList.get(position);
            } else if(mType.equals(Utils.TYPE_QUESTION)){
                //TODO
                return subjectList.get(position);
            }if(mType.equals(Utils.TYPE_SUBJECT)){
                return subjectList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(mType.equals(Utils.TYPE_ARTICLE)){
                ArticleViewHolder viewHolder;
                if(convertView == null){
                    viewHolder = new ArticleViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hot_article,parent,false);
                    viewHolder.imageView = (ImageView) convertView.findViewById(R.id.article_img);
                    viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.actilce_title);
                    viewHolder.descTextView = (TextView) convertView.findViewById(R.id.acticle_desc);
                    viewHolder.time = (TextView)convertView.findViewById(R.id.article_time);
                    viewHolder.praiseNum =(TextView)convertView.findViewById(R.id.article_priase_num);
                    viewHolder.commentNum =(TextView)convertView.findViewById(R.id.article_comment_num);
                    viewHolder.mFavoriteBtn =(TextView)convertView.findViewById(R.id.article_favorite);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder = (ArticleViewHolder)convertView.getTag();
                }
                HotArticleModel article = (HotArticleModel) qList.get(position);
                if (article.getImage() != null && !article.getImage().isEmpty()) {
                    //mImageLoader.displayImage(article.getImage(), viewHolder.imageView);
                    Picasso.with(getActivity()).load(article.getImage()).error(R.drawable.default_icon).
                            placeholder(R.drawable.default_icon).into(viewHolder.imageView);
                }  else {
                    viewHolder.imageView.setImageResource(R.drawable.default_icon);
                }
                viewHolder.titleTextView.setText(article.getTitle());
                viewHolder.descTextView.setText(article.getDesc());
                viewHolder.time.setText(article.getTime());
                viewHolder.praiseNum.setText("赞("+article.getZuanNum()+")");
                viewHolder.commentNum.setText("评论("+article.getCommentNum()+")");
            } else if(mType.equals(Utils.TYPE_VIDEO)){
                CourseModel mCourse;
                CourseViewHolder viewHolder;
                if(convertView == null){
                    viewHolder = new CourseViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_course_item,parent,false);
                    viewHolder.icon =(ImageView)convertView.findViewById(R.id.course_icon);
                    viewHolder.title =(TextView)convertView.findViewById(R.id.course_title);
                    viewHolder.price =(TextView)convertView.findViewById(R.id.course_price);
                    viewHolder.duration =(TextView)convertView.findViewById(R.id.course_duration);
                    viewHolder.time =(TextView)convertView.findViewById(R.id.course_time);
                    viewHolder.zhan =(TextView)convertView.findViewById(R.id.course_zhan);
                    viewHolder.comment =(TextView)convertView.findViewById(R.id.course_comment);
                    viewHolder.linear =(LinearLayout)convertView.findViewById(R.id.view_detail);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder = (CourseViewHolder) convertView.getTag();
                }
                mCourse = vList.get(position);
                //final CourseModel course = mCourse;
                //viewHolder.linear.setOnClickListener(this);

                if (mCourse.getcImageUrl() != null) {
                    Picasso.with(getActivity()).load(mCourse.getcImageUrl()).error(R.drawable.banner_default).
                            placeholder(R.drawable.banner_default).into(viewHolder.icon);
                } else {
                    viewHolder.icon.setImageResource(R.drawable.cover_default);
                }

                //mImageLoader.displayImage(mCourse.getcImageUrl(), viewHolder.icon);
                //viewHolder.icon.setImageResource(R.drawable.p6);
                viewHolder.title.setText(mCourse.getcTitle());
                if(mCourse.getcType() == 1){
                    viewHolder.price.setText(getString(R.string.course_free1));
                }else{
                    viewHolder.price.setText(getString(R.string.course_price, mCourse.getcPrice()));
                }
                viewHolder.duration.setText(getString(R.string.course_time_duration, mCourse.getcDuration()));
                viewHolder.time.setText(getString(R.string.course_play_count, mCourse.getcPlayTime()));
                viewHolder.zhan.setText(getString(R.string.praise_count, mCourse.getcZhan()));
                viewHolder.comment.setText(getString(R.string.comment_count, mCourse.getcCommentCount()));
            } else if(mType.equals(Utils.TYPE_MEETING)){
                MeetingViewHolder viewHolder;
                if(convertView == null){
                    viewHolder = new MeetingViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_meeting_item,parent,false);
                    viewHolder.icon =(ImageView)convertView.findViewById(R.id.meeting_icon);
                    viewHolder.title =(TextView)convertView.findViewById(R.id.meeting_title);
                    viewHolder.time =(TextView)convertView.findViewById(R.id.meeting_time);
                    viewHolder.endtime =(TextView)convertView.findViewById(R.id.meeting_endtime);
                    viewHolder.place =(TextView)convertView.findViewById(R.id.meeting_place);
                    viewHolder.status =(TextView)convertView.findViewById(R.id.meeting_status);
//                viewHolder.joincount =(TextView)convertView.findViewById(R.id.meeting_join);
                    //viewHolder.attention =(TextView)convertView.findViewById(R.id.meeting_attention);
                    //viewHolder.linear =(LinearLayout)convertView.findViewById(R.id.meeting_detail);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder = (MeetingViewHolder) convertView.getTag();
                }
                final MeetingModel mMeeting = meetingList.get(position);
                //viewHolder.linear.setOnClickListener(this);
                //viewHolder.icon.setImageResource(R.drawable.p6);

                if (mMeeting.getImgUrl() != null) {
                    Picasso.with(getActivity()).load(mMeeting.getImgUrl()).error(R.drawable.banner_default).
                            placeholder(R.drawable.banner_default).into(viewHolder.icon);
                } else {
                    viewHolder.icon.setImageResource(R.drawable.cover_default);
                }
                viewHolder.title.setText(mMeeting.getTitle());
                viewHolder.time.setText(getString(R.string.meeting_time, StringUtils.getShortTime(mMeeting.getTime())));
                viewHolder.endtime.setText(getString(R.string.meeting_end_time,StringUtils.getShortTime(mMeeting.getEndTime())));
                viewHolder.place.setText(getString(R.string.meeting_place,mMeeting.getPlace()));
                int status = Integer.valueOf(mMeeting.getStatus());
                if(status == 2){
                    viewHolder.status.setText(getString(R.string.meeting_status2));
                    viewHolder.status.setBackgroundResource(R.drawable.conference_status_0);
                }else if(status == 1){
                    viewHolder.status.setText(getString(R.string.meeting_status1));
                    viewHolder.status.setBackgroundResource(R.drawable.conference_status_1);
                }else if(status == 0){
                    viewHolder.status.setText(getString(R.string.meeting_status0));
                    viewHolder.status.setBackgroundResource(R.drawable.conference_status_0);
                }
            } else if(mType.equals(Utils.TYPE_QUESTION)){

            }if(mType.equals(Utils.TYPE_TOPIC)){

            }if(mType.equals(Utils.TYPE_SUBJECT)){
                SubjectViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new SubjectViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hot_subject, parent, false);
                    viewHolder.imageView = (ImageView) convertView.findViewById(R.id.article_img);
                    viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.actilce_title);
                    viewHolder.descTextView = (TextView) convertView.findViewById(R.id.acticle_desc);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.subject_time);
                    viewHolder.praiseNum = (TextView) convertView.findViewById(R.id.subjiect_priase_num);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (SubjectViewHolder) convertView.getTag();
                }
                HotSubjectModel subject = (HotSubjectModel) subjectList.get(position);
                //TODO test
//        String path = "http://120.76.73.10:8080/HenghengServer/attached/image/20161022/20161022212316_807.png";
//        subject.setImage(path);
                if (subject != null && subject.getImage() != null) {
                    Picasso.with(getActivity()).load(subject.getImage()).error(R.drawable.banner_default).
                            placeholder(R.drawable.banner_default).into(viewHolder.imageView);
                } else {
                    viewHolder.imageView.setImageResource(R.drawable.cover_default);
                }
                viewHolder.titleTextView.setText(subject.getTitle());
                viewHolder.descTextView.setText(subject.getDesc());
                viewHolder.time.setText(subject.getTime());
                viewHolder.praiseNum.setText("赞(" + subject.getZuanNum() + ")");
            }
            return convertView;
        }

        private class QAViewHolder {
            private TextView innerName;
            private TextView innerIntro;
            private TextView centerName;
        }
    }

    public void setData(Context context,String httpListResponse,String type){
        if(httpListResponse == null){
            mHttpQaListResponse = null;
            mFavoriteList.setAdapter(null);
            return;
        }
        mType = type;
        if(mType.equals(Utils.TYPE_ARTICLE)){
            ArrayList<HotArticleModel> mArticleList = new ArrayList<HotArticleModel>();
            if (Utils.getRequestStatus(httpListResponse) == 1) {
                Gson gson = new Gson();
                HttpArtilcleListResponse httpArtilcleListResponse = gson.fromJson(httpListResponse, HttpArtilcleListResponse.class);
                ArrayList<HttpArtilcleListResponse.Content> datas = new ArrayList<HttpArtilcleListResponse.Content>();
                datas  = httpArtilcleListResponse.getResponse().getContent();

                for(int i = 0;i<datas.size();i++){
                    HotArticleModel model = new HotArticleModel();
                    model.setArticleId(datas.get(i).getArtId());
                    model.setTitle(datas.get(i).getArtTitle());
                    model.setDesc(datas.get(i).getrtDesc());
                    model.setImage(datas.get(i).getArtPhoto());
                    model.setCommentNum(datas.get(i).getCommentCount());
                    model.setTime(datas.get(i).getArtDateTime());
                    //model.setmUrl(datas.get(i).);
                    model.setZuanNum(datas.get(i).getPraiseCount());
                    mArticleList.add(model);
                }
            }else{

            }
            if(mArticleList!=null && mArticleList.size()>0){
                mAdapter = new FavoriteListAdapter(context,mArticleList);
                if(mFavoriteList != null) {
                    mFavoriteList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } else if(mType.equals(Utils.TYPE_VIDEO)){
            Gson gson = new Gson();
            //L.d("csy","favoritelist response:"+httpListResponse);
            VideoListResponseModel videoListModel = gson.fromJson(httpListResponse,VideoListResponseModel.class);
            List<VideoListResponseModel.VideoContent> contents = videoListModel.getReponse().getContent();
            //L.d("csy", "videolist contents size:" + contents.size());
            ArrayList<CourseModel> mLists = new ArrayList<>();
            for(int i=0;i<contents.size();i++) {
                VideoListResponseModel.VideoContent content = contents.get(i);
                CourseModel course = new CourseModel();
                course.setcVid(content.getvId());

                //course.setcType(type);
                course.setcTitle(content.getVideoTitle());
                course.setcPlayTime(Integer.valueOf(content.getPlayCount()));
                if (content.getClassTimeLong() == null || "null".equalsIgnoreCase(content.getClassTimeLong())) {
                    course.setcDuration(0);
                } else {
                    course.setcDuration(Long.valueOf(content.getClassTimeLong()));
                }
                String price = content.getPrice();
                if (price == null || price.equals("null") || price.equals("")) {
                    course.setcPrice(0.0f);
                } else {
                    course.setcPrice(Float.valueOf(price));
                }
                course.setcImageUrl(content.getThumbnailURL());
                mLists.add(course);
            }
            if(mLists!=null && mLists.size()>0){
                mAdapter = new FavoriteListAdapter(context,mLists);
                if(mFavoriteList != null) {
                    mFavoriteList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }


        } else if(mType.equals(Utils.TYPE_MEETING)){
            L.d(TAG, "hot Response:" + httpListResponse);
            ArrayList<MeetingModel> mMeetings = new ArrayList<MeetingModel>();
            if(Utils.getRequestStatus(httpListResponse) == 1){
                L.d(TAG, "resp meetinglist success");
                mMeetings.clear();
                Gson gson = new Gson();
                MeetingListResponseModel model = gson.fromJson(httpListResponse,MeetingListResponseModel.class);
                List<MeetingListResponseModel.MeetingContent> lists = model.getReponse().getContent();
                L.d("csy","handleMessage lists size:"+lists.size());
                for(int i=0;i<lists.size();i++){
                    MeetingListResponseModel.MeetingContent content = lists.get(i);
                    MeetingModel meeting = new MeetingModel();
                    meeting.setcId(content.getcId());
                    meeting.setTitle(content.getTitle());
                    meeting.setTime(content.getBeginTime());
                    meeting.setEndTime(content.getEndTime());
                    meeting.setPlace(content.getAddress());
                    meeting.setImgUrl(content.getThumbnailURL());
                    meeting.setStatus(content.getStatus());
                    meeting.setJoinCount(100);
                    meeting.setAttention(500);
                    mMeetings.add(meeting);
                }
                L.d("csy","handleMessage mMeetings size:"+mMeetings.size());
                if(mMeetings != null && mMeetings.size() > 0){
                        /*if(mMeetingAdapter == null){
                            mMeetingAdapter = new MeetingListAdapter(getBaseContext(),mMeetings);
                            mListView.setAdapter(mMeetingAdapter);
                            //setListViewHeightBasedOnChildren(mListView);
                        }else{

                        }*/
                    mAdapter = new FavoriteListAdapter(context,mMeetings);
                    if(mFavoriteList != null) {
                        mFavoriteList.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }else{
                L.d(TAG,"resp meetinglist fail");
            }
        } else if(mType.equals(Utils.TYPE_QUESTION)){

        }if(mType.equals(Utils.TYPE_TOPIC)){

        }if(mType.equals(Utils.TYPE_SUBJECT)){
            ArrayList<HotSubjectModel> mSubjetcList = new ArrayList<>();
            Gson gson = new Gson();
            HttpSubjectListResponse responseModel = gson.fromJson(httpListResponse, HttpSubjectListResponse.class);
            ArrayList<HttpSubjectListResponse.Content> contents = responseModel.getResponse().getContent();
            //mDatas = new ArrayList<HotImageView>();
            for (int i = 0; i < contents.size(); i++) {
                HttpSubjectListResponse.Content content = contents.get(i);
                HotSubjectModel subject = new HotSubjectModel();
                subject.setSubjectId(content.getTopicid());
                subject.setTitle(content.getTopictitle());
                subject.setmUrl(content.getTopicphoto());
                subject.setDesc(content.getTopicdesc());
                subject.setZuanNum(content.getPraisecount());
                subject.setTime(content.getTopicdatetime());
                subject.setImage(content.getTopicphoto());
                mSubjetcList.add(subject);
            }
            mAdapter = new FavoriteListAdapter(context,mSubjetcList);
            if(mFavoriteList != null) {
                mFavoriteList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    class ArticleViewHolder{
        private ImageView imageView;
        private TextView titleTextView;
        private TextView descTextView;
        private TextView time;
        private TextView praiseNum;
        private TextView commentNum;
        private TextView mFavoriteBtn;
    }

    class SubjectViewHolder {
        private ImageView imageView;
        private TextView titleTextView;
        private TextView descTextView;
        private TextView time;
        private TextView praiseNum;
        private TextView mFavoriteBtn;
    }

    class CourseViewHolder{
        private ImageView icon;
        private TextView title;
        private TextView price;
        private TextView duration;
        private TextView time;
        private TextView zhan;
        private TextView comment;
        private LinearLayout linear;
    }

    class MeetingViewHolder{
        private ImageView icon;
        private TextView status;
        private TextView title;
        private TextView place;
        private TextView joincount;
        private TextView time;
        private TextView endtime;
        //private TextView attention;
        private LinearLayout linear;
    }

}
