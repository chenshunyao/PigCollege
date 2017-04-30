package com.xnf.henghenghui.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.model.HttpBaikeListResponse;
import com.xnf.henghenghui.request.MyJsonCallBack;
import com.xnf.henghenghui.ui.activities.BaikeDetailActivity;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;

import org.json.JSONObject;

import java.util.List;

public class BaikeListFragment extends BaseFragment2 implements  View.OnClickListener,AdapterView.OnItemClickListener{

    protected static final String TAG = "BaikeListFragment";

    private EditText mEditSearch;
    private View mBtnSearch;

    private String lastSearchTxt = "";

    private ListView mBaikeList;
    private BaikeAdapter mAdapter;

    private ProgressDialog dialog;

    private OnFragmentInteractionListener mListener;

    private HttpBaikeListResponse mHttpBaikeListResponse;
    private String mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mBtnSearch = view.findViewById(R.id.img_baike_search);
        mEditSearch = (EditText) view.findViewById(R.id.edit_baike_list_search);
//        String keyword = getIntent().getStringExtra("keyword");
//        if(keyword == null) keyword = "";
        mEditSearch.setText("");
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String txt = mEditSearch.getText().toString().trim();
                    if (!txt.equals(lastSearchTxt)) {
                        loadList(true);
                    }

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditSearch.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        mBtnSearch.setOnClickListener(this);

        mBaikeList = (ListView)view.findViewById(R.id.baike_list);
//        mBaikeList.setAdapter(mAdapter);
//        if(mAdapter != null) {
//            mAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                String txt = mEditSearch.getText().toString().trim();
                if(!txt.equals(lastSearchTxt)){
                    loadList(true);
                }
                break;
            default:
                break;
        }
        //onPrepareOptionsMenu(menu);
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

//        mAdapter = new BaikeAdapter(getActivity(),mQaList);
//        mBaikeList.setAdapter(mAdapter);
        initDialog();
        mBaikeList.setOnItemClickListener(this);
    }

    private void initDialog(){
        dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        dialog.setMessage(getString(R.string.progress_doing));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baike_list, container, false);
        initView(view);
        initData();
        return view;
    }

    private void loadList(boolean sp){
        String txt = mEditSearch.getText().toString().trim();
        lastSearchTxt = txt;
        if(sp){
            if(dialog != null) {
                dialog.show();
            }
        }
        String userId = LoginUserBean.getInstance().getLoginUserid();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userId);
            jsonObj.put("categoryId", mType);
            jsonObj.put("keyWord", txt);
            jsonObj.put("startRowNum", "");
            jsonObj.put("endRowNum", "");
        } catch (Exception e) {
        }
        String jsonString = jsonObj.toString();
        OkHttpUtils.post(Urls.SERVER_URL + Urls.ACTION_BAIKE_LIST)
                .tag(Urls.ACTION_BAIKE_LIST)
                .postJson(BaseActivity.getRequestBody(jsonString))
                .execute(new MyJsonCallBack<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.d(TAG, "onResponse:" + s);
                        Message msg = new Message();
                        msg.what = CodeUtil.CmdCode.MsgTypeCode.MSG_BAIKE_LIST;
                        msg.obj = s;
//                        msg.obj = "{\"response\":{\"succeed\":1,\"arrayflag\":1,\"totalRow\":5,\"content\":[{\"categoryId\":\"ct11111111111\",\"categoryName\":\"传染病\",\"entryList\":[{\"entryId\":\"E11111111111\",\"entryName\":\"蓝耳病\"},{\"entryId\":\"E1111111223\",\"entryName\":\"红尾病\"},{\"entryId\":\"E1111111223\",\"entryName\":\"流感病\"}]}]}}";
                        mHandler.sendMessage(msg);
                    }
                });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_BAIKE_LIST: {
                String response = (String) msg.obj;
                if (Utils.getRequestStatus(response) == 1) {
                    Gson gson = new Gson();
                    mHttpBaikeListResponse = gson.fromJson(response, HttpBaikeListResponse.class);
                    List<HttpBaikeListResponse.Entry> list = mHttpBaikeListResponse.getResponse().getContent().get(0).getEntryList();
                    if(list == null){
                        mBaikeList.setAdapter(null);
                    }else {
                        mAdapter = new BaikeAdapter(getActivity(), list);
                        mBaikeList.setAdapter(mAdapter);
                    }
                } else {
                    mBaikeList.setAdapter(null);
                }
                if(dialog != null){
                    dialog.dismiss();
                }
                break;
            }
        }
        return false;
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
        HttpBaikeListResponse.Entry info = (HttpBaikeListResponse.Entry)mAdapter.getItem(i);
        Intent intent = new Intent(getActivity(), BaikeDetailActivity.class);
        intent.putExtra("bkid", info.getEntryId());
        intent.putExtra("bkname", info.getEntryName());
        Utils.start_Activity(getActivity(), intent);
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

    private class BaikeAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<HttpBaikeListResponse.Entry> infoList;
        private ViewHolder holder;
        public BaikeAdapter(Context context, List<HttpBaikeListResponse.Entry> infoList){
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
                convertView = inflater.inflate(R.layout.list_baike_item, null);
                holder = new ViewHolder();
//                holder.innerName = (TextView) convertView
//                        .findViewById(R.id.inner_name);
//                holder.innerIntro =(TextView)convertView.findViewById(R.id.inner_intro);
                holder.centerName = (TextView)convertView.findViewById(R.id.center_name);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }
            HttpBaikeListResponse.Entry info = infoList.get(position);
            holder.centerName.setText(info.getEntryName());
            return convertView;
        }

        private class ViewHolder {
            private TextView centerName;
        }
    }

    public void setData(Context context,String type){
        mType = type;
        loadList(false);
//        if(mType == 0){
//            mAdapter = new BaikeAdapter(context,mHttpQaListResponse.getResponse().getContent().getQuestionInfo());
//            if(mBaikeList != null) {
//                mBaikeList.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
//            }
//        } else if(mType == 1){
//            mAdapter = new BaikeAdapter(context,mHttpQaListResponse.getResponse().getContent().getTopicInfo());
//            if(mBaikeList != null) {
//                mBaikeList.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
//            }
//        } else if(mType == 2){
//            mAdapter = new BaikeAdapter(context,mHttpQaListResponse.getResponse().getContent().getAnswerInfo());
//            if(mBaikeList != null) {
//                mBaikeList.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
//            }
//        } else if(mType == 3){
//            mAdapter = new BaikeAdapter(context,mHttpQaListResponse.getResponse().getContent().getExpertsInfo());
//            if(mBaikeList != null) {
//                mBaikeList.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
//            }
//        }
    }
}
