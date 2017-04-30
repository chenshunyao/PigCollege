
package com.xnf.henghenghui.ui.fragments;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.ui.base.BaseFragment;

public class EmptyFragment extends BaseFragment{

    private static final String TAG = "EmptyFragment";

    public static EmptyFragment getInstance(Bundle bundle) {
        EmptyFragment settingFragment = new EmptyFragment();
        settingFragment.setArguments(bundle);
        return settingFragment;
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

    @Override
    protected String setFragmentTag() {
        return TAG;
    }

    @Override
    protected void initData(Bundle bundle) {
    }

    private void getPicNews() {
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container,
            Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_empty, container,
                false);
        return view;
    }

}
