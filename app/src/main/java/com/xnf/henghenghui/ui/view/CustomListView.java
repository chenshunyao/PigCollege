package com.xnf.henghenghui.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xnf.henghenghui.R;

/**
 * Created by Administrator on 2016/5/14.
 */
public class CustomListView extends ListView implements AbsListView.OnScrollListener {

    private ProgressBar moreProgressBar;
    private TextView loadMoreView;
    private View moreView;
    private OnLoadListener loadListener;

    public CustomListView(Context context) {
        super(context);
        init(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        setOnScrollListener(this);
        moreView = LayoutInflater.from(context).inflate(R.layout.listfooter_more,null);
        moreView.setVisibility(View.VISIBLE);
        moreProgressBar = (ProgressBar) moreView.findViewById(R.id.pull_to_refresh_progress);
        loadMoreView = (TextView) moreView.findViewById(R.id.load_more);
        /*moreView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onLoad();
            }
        });*/
        addFooterView(moreView);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void setonLoadListener(OnLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public interface OnLoadListener {
        public void onLoad();
    }

    private void onLoad() {
        if (loadListener != null) {
            moreProgressBar.setVisibility(View.VISIBLE);
            loadMoreView.setText(getContext().getString(R.string.more));
            loadListener.onLoad();
        }
    }

    public void onLoadComplete() {
        moreProgressBar.setVisibility(View.GONE);
        loadMoreView.setText(getContext().getString(R.string.more));
    }

    @SuppressWarnings("deprecation")
    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);
    }
}
