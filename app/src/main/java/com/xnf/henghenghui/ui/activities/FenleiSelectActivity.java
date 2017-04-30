package com.xnf.henghenghui.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class FenleiSelectActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackImg;
    private TextView mTitle;
    private ImageView mRightImg;
    private TextView mRightTxt;
    private ListView mFenleiList;
    private FenleiAdapter mAdapter;

    private List<String> mlist;

    private int currSelect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_fenlei_selection);
        mBackImg = (ImageView)findViewById(R.id.img_back);
        //mBackImg.setImageResource(R.drawable.green_back);
        mBackImg.setVisibility(View.VISIBLE);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(R.string.change_fenlei_title);
        mRightImg = (ImageView)findViewById(R.id.img_right);
//        mRightImg.setImageResource(R.drawable.master_detail_right);
        mRightImg.setVisibility(View.GONE);
        mRightTxt = (TextView)findViewById(R.id.txt_right);
        mRightTxt.setText(R.string.confirm);
        mRightTxt.setVisibility(View.GONE);

        mlist = new ArrayList<String>();
        mlist.add("所有分类");
        mlist.add("猪病");
        mlist.add("营养");
        mlist.add("育种");
        mlist.add("监测");
        mlist.add("其他");
        mFenleiList = (ListView)findViewById(R.id.fenlei_list);
        mAdapter = new FenleiAdapter(this,mlist);
        mFenleiList.setAdapter(mAdapter);
        mFenleiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currSelect = i;
                //mAdapter.notifyDataSetChanged();
                String txt = mlist.get(currSelect);
                Intent intent=new Intent();
                intent.putExtra("FENLEI_NAME", txt);
                intent.putExtra("FENLEI_ID", currSelect);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        currSelect = getIntent().getIntExtra("FENLEI_ID",0);
        mAdapter.notifyDataSetChanged();

        bindOnClickLister(this, mBackImg, mRightImg, mRightTxt);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_right:
                ;
                break;
            case R.id.txt_right:
                String txt = mlist.get(currSelect);
                Intent intent=new Intent();
                intent.putExtra("FENLEI_NAME", txt);
                intent.putExtra("FENLEI_ID", currSelect);
                setResult(RESULT_OK, intent);
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

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    private class FenleiAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<String> infoList;
        private MasterViewHolder holder;
        public FenleiAdapter(Context context,List<String> infoList){
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
                convertView = inflater.inflate(R.layout.list_fenlei_item, null);
                holder = new MasterViewHolder();
                holder.selectImg = (ImageView) convertView
                        .findViewById(R.id.select_image);
                holder.name = (TextView) convertView
                        .findViewById(R.id.fenlei_name);
                convertView.setTag(holder);
            } else{
                holder = (MasterViewHolder) convertView.getTag();
            }
            holder.name.setText(infoList.get(position));
            holder.selectImg.setVisibility(View.GONE);
            if(currSelect == position){
                holder.selectImg.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        private class MasterViewHolder{
            private ImageView selectImg;
            private TextView name;
        }
    }
}
