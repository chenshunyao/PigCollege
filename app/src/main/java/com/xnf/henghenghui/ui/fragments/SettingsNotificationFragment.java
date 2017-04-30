package com.xnf.henghenghui.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xnf.henghenghui.AppConfig;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.view.togglebutton.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/5/2.
 */
public class SettingsNotificationFragment extends BaseFragment2 {

    @InjectView(R.id.tb_accept)
    ToggleButton mTbAccept;
    @InjectView(R.id.tb_voice) ToggleButton mTbVoice;
    @InjectView(R.id.tb_vibration) ToggleButton mTbVibration;
    @InjectView(R.id.tb_app_exit) ToggleButton mTbAppExit;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_notifcation, container,
                false);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        setToggleChanged(mTbAccept, AppConfig.KEY_NOTIFICATION_ACCEPT);
        setToggleChanged(mTbVoice, AppConfig.KEY_NOTIFICATION_SOUND);
        setToggleChanged(mTbVibration, AppConfig.KEY_NOTIFICATION_VIBRATION);
        setToggleChanged(mTbAppExit, AppConfig.KEY_NOTIFICATION_DISABLE_WHEN_EXIT);

        view.findViewById(R.id.rl_accept).setOnClickListener(this);
        view.findViewById(R.id.rl_voice).setOnClickListener(this);
        view.findViewById(R.id.rl_vibration).setOnClickListener(this);
        view.findViewById(R.id.rl_app_exit).setOnClickListener(this);
    }

    public void initData() {
        setToggle(HengHengHuiAppliation.get(AppConfig.KEY_NOTIFICATION_ACCEPT, true), mTbAccept);
        setToggle(HengHengHuiAppliation.get(AppConfig.KEY_NOTIFICATION_SOUND, true), mTbVoice);
        setToggle(HengHengHuiAppliation.get(AppConfig.KEY_NOTIFICATION_VIBRATION, true), mTbVibration);
        setToggle(HengHengHuiAppliation.get(AppConfig.KEY_NOTIFICATION_DISABLE_WHEN_EXIT, true), mTbAppExit);
    }

    private void setToggleChanged(ToggleButton tb, final String key) {
        tb.setOnToggleChanged(new ToggleButton.OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                HengHengHuiAppliation.set(key, on);
            }
        });
    }

    private void setToggle(boolean value, ToggleButton tb) {
        if (value)
            tb.setToggleOn();
        else
            tb.setToggleOff();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.rl_accept:
                mTbAccept.toggle();
                break;
            case R.id.rl_voice:
                mTbVoice.toggle();
                break;
            case R.id.rl_vibration:
                mTbVibration.toggle();
                break;
            case R.id.rl_app_exit:
                mTbAppExit.toggle();
                break;
        }
    }
}
