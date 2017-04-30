package com.xnf.henghenghui.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.L;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.logic.Face2FaceManager;
import com.xnf.henghenghui.model.F2FCategoryResponse;
import com.xnf.henghenghui.model.QACategory;
import com.xnf.henghenghui.ui.activities.F2FListActivity;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.util.CodeUtil;

import java.util.ArrayList;
import java.util.List;

public class F2FCatergoryFragment extends BaseFragment2{
    private ListView mF2FCategoryList;
    private QATypeAdapter mAdapter;
    private List<QACategory> mCategoryList;

    private int curSelect = 0;

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "Face2FaceFragment";

    public static F2FCatergoryFragment getInstance(Bundle bundle) {
        F2FCatergoryFragment f2fFragment = new F2FCatergoryFragment();
        f2fFragment.setArguments(bundle);
        return f2fFragment;
    }

    private int resId[] = new int[]{
            R.drawable.f2f_zhongzhu,R.drawable.f2f_zhubing,R.drawable.f2f_xuzhong,R.drawable.f2f_guanli,
            R.drawable.f2f_yingyang,R.drawable.f2f_qita,R.drawable.f2f_jiance
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mF2FCategoryList = (ListView)view.findViewById(R.id.qa_type_list);

    }

    @Override
    protected void setUIHandler() {
        super.setUIHandler();
        Face2FaceManager.setHandler(mHandler);
    }

    @Override
    public void initData() {
        super.initData();
        //showWaitDialog("加载中......");
        Face2FaceManager.getF2FCatergory(LoginUserBean.getInstance().getLoginUserid(),mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f2fcategory, container, false);
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

    private class QATypeAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<QACategory> infoList;
        private QATypeViewHolder holder;
        public QATypeAdapter(Context context, List<QACategory> infoList){
            this.inflater = LayoutInflater.from(context);
            this.infoList = infoList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(R.layout.f2f_qacategory_item, null);
                holder = new QATypeViewHolder();
                holder.icon = (ImageView)convertView.findViewById(R.id.qa_category_icon);
                holder.categoryName = (TextView) convertView
                        .findViewById(R.id.qa_categoryname_textview);
                holder.categoryDesc =(TextView)convertView.findViewById(R.id.qa_category_desc_textview);
                holder.qsNum = (TextView)convertView.findViewById(R.id.qa_num_textview);
                convertView.setTag(holder);
            } else{
                holder = (QATypeViewHolder) convertView.getTag();
            }
            QACategory category = infoList.get(position);
            holder.icon.setImageResource(category.getIconId());
            holder.categoryName.setText(category.getCategoryName());
            holder.categoryDesc.setText(category.getCategoryDesc());
            holder.qsNum.setText("今日："+String.valueOf(category.getQsNum()));
            return convertView;
        }

        private class QATypeViewHolder {
            private ImageView icon;
            private TextView categoryName;
            private TextView categoryDesc;
            private TextView qsNum;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_F2F_CATEGORY: {
                if(getActivity()==null || getActivity().isFinishing()){
                    return false;
                }
                String response = (String)msg.obj;
                L.d("yuhuan","response:"+response);
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    F2FCategoryResponse category = gson.fromJson(response,F2FCategoryResponse.class);
                    mCategoryList = new ArrayList<QACategory>();
                    int size = category.getResponse().getContent().size();
                    for (int i= 0;i<size;i++){
                        QACategory  category1 = new QACategory();
                        if(i<=resId.length-1){
                            category1.setIconId(resId[i]);
                        }else{
                            category1.setIconId(resId[resId.length-1]);
                        }

                        try{
                            category1.setCategoryId(category.getResponse().getContent().get(i).getQtCategoryId());
                            category1.setCategoryName(category.getResponse().getContent().get(i).getQtCategoryName());
                            category1.setCategoryDesc(category.getResponse().getContent().get(i).getQtCategoryDesc());
                            category1.setQsNum(Integer.parseInt(category.getResponse().getContent().get(i).getQtAskCount()));
                        }catch (Exception e){
                            category1.setQsNum(0);
                        }
                        mCategoryList.add(category1);
                    }
                    mAdapter = new QATypeAdapter(getActivity(),mCategoryList);
                    mF2FCategoryList.setAdapter(mAdapter);
                    mF2FCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            curSelect = i;
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), F2FListActivity.class);
                            intent.putExtra("CATEGORY_ID",mCategoryList.get(i).getCategoryId());
                            intent.putExtra("CATEGORY_NAME",mCategoryList.get(i).getCategoryName());
                            startActivity(intent);
                            //Toast.makeText(getActivity(),"Item:"+mCategoryList.get(i).getCategoryName(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    mAdapter.notifyDataSetChanged();
                }
            }
            break;
            default:
                break;
        }
        return false;
    }
}
