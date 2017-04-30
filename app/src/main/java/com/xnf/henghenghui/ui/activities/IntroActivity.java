package com.xnf.henghenghui.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.Constants;
import com.xnf.henghenghui.ui.appintro.AppIntro;
import com.xnf.henghenghui.ui.fragments.PageSlideFragment;
import com.xnf.henghenghui.util.PreferencesWrapper;

public class IntroActivity  extends AppIntro {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(PageSlideFragment.newInstance(R.layout.intro));
        addSlide(PageSlideFragment.newInstance(R.layout.intro2));
        addSlide(PageSlideFragment.newInstance(R.layout.intro3));
        addSlide(PageSlideFragment.newInstance(R.layout.intro4));
        addSlide(PageSlideFragment.newInstance(R.layout.intro5));
    }

    private void loadLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        PreferencesWrapper.getInstance().setPreferenceBooleanValue(Constants.INTRO_SHOW_TAG,true);
        finish();
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onSkipPressed() {
        loadLoginActivity();
       // Toast.makeText(getApplicationContext(),getString(R.string.skip), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed() {
        loadLoginActivity();
    }

    @Override
    public void onSlideChanged() {
    }

    public void getStarted(View v){

    }

}
