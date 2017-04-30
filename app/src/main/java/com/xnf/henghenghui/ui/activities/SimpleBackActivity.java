package com.xnf.henghenghui.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;

import com.xnf.henghenghui.R;
import com.xnf.henghenghui.model.SimpleBackPage;
import com.xnf.henghenghui.ui.base.BaseActivity2;
import com.xnf.henghenghui.ui.base.BaseFragment2;
import com.xnf.henghenghui.ui.interf.OnSendClickListener;

import java.lang.ref.WeakReference;

public class SimpleBackActivity extends BaseActivity2  implements OnSendClickListener{

    public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
    public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
    private static final String TAG = "FLAG_TAG";
    protected WeakReference<Fragment> mFragment;
    protected int mPageValue = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mPageValue == -1) {
            mPageValue = getIntent().getIntExtra(BUNDLE_KEY_PAGE, 0);
        }
        initFromIntent(mPageValue, getIntent());
    }

    protected void initFromIntent(int pageValue, Intent data) {
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }
        SimpleBackPage page = SimpleBackPage.getPageByValue(pageValue);
        if (page == null) {
            throw new IllegalArgumentException("can not find page by value:"
                    + pageValue);
        }

        setActionBarTitle(page.getTitle());

        try {
            Fragment fragment = (Fragment) page.getClz().newInstance();

            Bundle args = data.getBundleExtra(BUNDLE_KEY_ARGS);
            if (args != null) {
                fragment.setArguments(args);
            }

            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, fragment, TAG);
            trans.commitAllowingStateLoss();

            mFragment = new WeakReference<Fragment>(fragment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "generate fragment error. by value:" + pageValue);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mFragment.get() instanceof TweetsFragment) {
//            setActionBarTitle("话题");
//        }
    }


//    /**
//     * 发送话题
//     */
//    private void sendTopic() {
//        Bundle bundle = new Bundle();
//        bundle.putInt(TweetPubFragment.ACTION_TYPE,
//                TweetPubFragment.ACTION_TYPE_TOPIC);
//        bundle.putString("tweet_topic", "#"
//                + ((TweetsFragment) mFragment.get()).getTopic() + "# ");
//        UIHelper.showTweetActivity(this, SimpleBackPage.TWEET_PUB, bundle);
//    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.get() != null
                && mFragment.get() instanceof BaseFragment2) {
            BaseFragment2 bf = (BaseFragment2) mFragment.get();
            if (!bf.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN
                && mFragment.get() instanceof BaseFragment2) {
            ((BaseFragment2) mFragment.get()).onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView() {}

    @Override
    public void initData() {}

    @Override
    public void onClickSendButton(Editable str) {

    }

    @Override
    public void onClickFlagButton() {}
}
