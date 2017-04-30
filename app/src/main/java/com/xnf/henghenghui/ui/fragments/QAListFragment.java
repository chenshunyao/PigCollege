package com.xnf.henghenghui.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.HttpQaListResponse;
import com.xnf.henghenghui.model.QACategory;
import com.xnf.henghenghui.model.QAListItem;
import com.xnf.henghenghui.ui.activities.ArticleDetailActivity;
import com.xnf.henghenghui.ui.activities.ArticleDetailActivity2;
import com.xnf.henghenghui.ui.activities.BaikeDetailActivity;
import com.xnf.henghenghui.ui.activities.F2FListActivity;
import com.xnf.henghenghui.ui.activities.MasterDetailActivity;
import com.xnf.henghenghui.ui.activities.QuestionDetailActivity;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class QAListFragment extends BaseFragment2 implements AdapterView.OnItemClickListener{
    private TextView mNoneText;
    private ListView mQAList;
    private QAAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    private HttpQaListResponse mHttpQaListResponse;
    private int mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mNoneText = (TextView)view.findViewById(R.id.qa_txt_none);
        mQAList = (ListView)view.findViewById(R.id.qa_list);
        mQAList.setAdapter(mAdapter);
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

//        mAdapter = new QAAdapter(getActivity(),mQaList);
//        mQAList.setAdapter(mAdapter);
        mQAList.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qa_list, container, false);
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
        if(mType == 0){
            HttpQaListResponse.QuestionInfo info = (HttpQaListResponse.QuestionInfo)mAdapter.getItem(i);
            Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra("qtid", info.getqId());
            Utils.start_Activity(getActivity(), intent);
        } else if(mType == 1){

        } else if(mType == 2){
            HttpQaListResponse.AnswerInfo info = (HttpQaListResponse.AnswerInfo)mAdapter.getItem(i);
            Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra("qtid", info.getQuestionId());
            Utils.start_Activity(getActivity(), intent);
        } else if(mType == 3){
            HttpQaListResponse.ExpertsInfo info = (HttpQaListResponse.ExpertsInfo)mAdapter.getItem(i);
            Intent intent = new Intent(getActivity(), MasterDetailActivity.class);
            intent.putExtra("masterid",info.getExpertsId());
            Utils.start_Activity(getActivity(), intent);
        } else if(mType == 4){
            HttpQaListResponse.EntryInfo info = (HttpQaListResponse.EntryInfo)mAdapter.getItem(i);
            Intent intent = new Intent(getActivity(), BaikeDetailActivity.class);
            intent.putExtra("bkid",info.getEntryId());
            intent.putExtra("bkname",info.getEntryTitle());
            Utils.start_Activity(getActivity(), intent);
        } else if(mType == 5){
            HttpQaListResponse.ArticleInfo info = (HttpQaListResponse.ArticleInfo)mAdapter.getItem(i);
//            Intent intent = new Intent(getActivity(), MasterDetailActivity.class);
//            intent.putExtra("masterid",info.getExpertsId());
//            Utils.start_Activity(getActivity(), intent);
            Intent intent = new Intent(getActivity(), ArticleDetailActivity2.class);
            intent.putExtra("ARTICLE_ID",info.getArtId());
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

    private class QAAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<HttpQaListResponse.QuestionInfo> qList;
        private List<HttpQaListResponse.TopicInfo> tList;
        private List<HttpQaListResponse.AnswerInfo> aList;
        private List<HttpQaListResponse.ExpertsInfo> eList;
        private List<HttpQaListResponse.EntryInfo> enList;
        private List<HttpQaListResponse.ArticleInfo> arList;
        private QAViewHolder holder;
        public QAAdapter(Context context, List infoList){
            this.inflater = LayoutInflater.from(context);
            if(mType == 0){
                this.qList = infoList;
            } else if(mType == 1){
                this.tList = infoList;
            } else if(mType == 2){
                this.aList = infoList;
            } else if(mType == 3){
                this.eList = infoList;
            } else if(mType == 4){
                this.enList = infoList;
            } else if(mType == 5){
                this.arList = infoList;
            }
            this.context = context;
        }
//        public QAAdapter(Context context, List<HttpQaListResponse.TopicInfo> infoList){
//            this.inflater = LayoutInflater.from(context);
//            this.tList = infoList;
//            this.context = context;
//        }
//        public QAAdapter(Context context, List<HttpQaListResponse.AnswerInfo> infoList){
//            this.inflater = LayoutInflater.from(context);
//            this.aList = infoList;
//            this.context = context;
//        }
//        public QAAdapter(Context context, List<HttpQaListResponse.ExpertsInfo> infoList){
//            this.inflater = LayoutInflater.from(context);
//            this.eList = infoList;
//            this.context = context;
//        }

        @Override
        public int getCount() {
            if(mType == 0){
                return qList.size();
            } else if(mType == 1){
                return tList.size();
            } else if(mType == 2){
                return aList.size();
            } else if(mType == 3){
                return eList.size();
            } else if(mType == 4){
                return enList.size();
            } else if(mType == 5){
                return arList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(mType == 0){
                return qList.get(position);
            } else if(mType == 1){
                return tList.get(position);
            } else if(mType == 2){
                return aList.get(position);
            } else if(mType == 3){
                return eList.get(position);
            } else if(mType == 4){
                return enList.get(position);
            } else if(mType == 5){
                return arList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(R.layout.list_qa_item, null);
                holder = new QAViewHolder();
                holder.innerName = (TextView) convertView
                        .findViewById(R.id.inner_name);
                holder.innerIntro =(TextView)convertView.findViewById(R.id.inner_intro);
                holder.centerName = (TextView)convertView.findViewById(R.id.center_name);
                convertView.setTag(holder);
            } else{
                holder = (QAViewHolder) convertView.getTag();
            }
            if(mType == 0){
                HttpQaListResponse.QuestionInfo info = qList.get(position);
                holder.centerName.setText(info.getqTitle());
                holder.innerName.setVisibility(View.GONE);
                holder.innerIntro.setVisibility(View.GONE);
                holder.centerName.setVisibility(View.VISIBLE);
            } else if(mType == 1){
                HttpQaListResponse.TopicInfo info = tList.get(position);
                holder.centerName.setText(info.getTcTitle());
                holder.innerName.setVisibility(View.GONE);
                holder.innerIntro.setVisibility(View.GONE);
                holder.centerName.setVisibility(View.VISIBLE);
            } else if(mType == 2){
                HttpQaListResponse.AnswerInfo info = aList.get(position);
                holder.centerName.setText(info.getAqContent());
                holder.innerName.setVisibility(View.GONE);
                holder.innerIntro.setVisibility(View.GONE);
                holder.centerName.setVisibility(View.VISIBLE);
            } else if(mType == 3){
                HttpQaListResponse.ExpertsInfo info = eList.get(position);
                holder.innerName.setText(info.getExpertsName());
                holder.innerIntro.setText(info.getExpertsDesc());
                holder.innerName.setVisibility(View.VISIBLE);
                holder.innerIntro.setVisibility(View.VISIBLE);
                holder.centerName.setVisibility(View.GONE);
            } else if(mType == 4){
                HttpQaListResponse.EntryInfo info = enList.get(position);
                holder.centerName.setText(info.getEntryTitle());
                holder.innerName.setVisibility(View.GONE);
                holder.innerIntro.setVisibility(View.GONE);
                holder.centerName.setVisibility(View.VISIBLE);
            } else if(mType == 5){
                HttpQaListResponse.ArticleInfo info = arList.get(position);
                holder.centerName.setText(info.getArtTitle());
                holder.innerName.setVisibility(View.GONE);
                holder.innerIntro.setVisibility(View.GONE);
                holder.centerName.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        private class QAViewHolder {
            private TextView innerName;
            private TextView innerIntro;
            private TextView centerName;
        }
    }

    public void setData(Context context,HttpQaListResponse httpQaListResponse,int type){
        if(httpQaListResponse == null){
            mHttpQaListResponse = null;
            mQAList.setAdapter(null);
            mNoneText.setVisibility(View.VISIBLE);
            mQAList.setVisibility(View.GONE);
            return;
        }
        mHttpQaListResponse = httpQaListResponse;
        mType = type;
        if(mType == 0){
            mAdapter = new QAAdapter(context,mHttpQaListResponse.getResponse().getContent().getQuestionInfo());
            if(mQAList != null) {
                mQAList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        } else if(mType == 1){
            mAdapter = new QAAdapter(context,mHttpQaListResponse.getResponse().getContent().getTopicInfo());
            if(mQAList != null) {
                mQAList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        } else if(mType == 2){
            mAdapter = new QAAdapter(context,mHttpQaListResponse.getResponse().getContent().getAnswerInfo());
            if(mQAList != null) {
                mQAList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        } else if(mType == 3){
            mAdapter = new QAAdapter(context,mHttpQaListResponse.getResponse().getContent().getExpertsInfo());
            if(mQAList != null) {
                mQAList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        } else if(mType == 4){
            mAdapter = new QAAdapter(context,mHttpQaListResponse.getResponse().getContent().getEntryInfo());
            if(mQAList != null) {
                mQAList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        } else if(mType == 5){
            mAdapter = new QAAdapter(context,mHttpQaListResponse.getResponse().getContent().getArticleInfo());
            if(mQAList != null) {
                mQAList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
        if(mAdapter.getCount() > 0) {
            mNoneText.setVisibility(View.GONE);
            mQAList.setVisibility(View.VISIBLE);
        }else{
            mNoneText.setVisibility(View.VISIBLE);
            mQAList.setVisibility(View.GONE);
        }
    }
}
