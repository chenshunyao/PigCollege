
package com.xnf.henghenghui.ui.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.R;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.config.Urls;
import com.xnf.henghenghui.logic.PersonalnfoManager;
import com.xnf.henghenghui.model.ExpertUserInfo;
import com.xnf.henghenghui.model.ExpertUserInfoResponse;
import com.xnf.henghenghui.model.NoramlUserInfoResponse;
import com.xnf.henghenghui.model.NormalUserInfo;
import com.xnf.henghenghui.model.SimpleBackPage;
import com.xnf.henghenghui.ui.activities.ExpertUserInfoActivity;
import com.xnf.henghenghui.ui.activities.IntegrationActivity;
import com.xnf.henghenghui.ui.activities.MainActivity;
import com.xnf.henghenghui.ui.activities.MyFavoriteActivity;
import com.xnf.henghenghui.ui.activities.NormalUserInfoActivity;
import com.xnf.henghenghui.ui.activities.MyQuestionActivity;
import com.xnf.henghenghui.ui.activities.SettingsActivity;
import com.xnf.henghenghui.ui.base.BaseFragment;
import com.xnf.henghenghui.ui.utils.UIHelper;
import com.xnf.henghenghui.ui.utils.Utils;
import com.xnf.henghenghui.ui.view.CustomProgressDialog;
import com.xnf.henghenghui.ui.view.RoundImageView;
import com.xnf.henghenghui.ui.view.ShareDialog;
import com.xnf.henghenghui.util.CodeUtil;
import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.NetworkUtil;
import com.xnf.henghenghui.util.PreferencesWrapper;
import com.xnf.henghenghui.util.ToastUtil;

public class MeFragment extends BaseFragment implements OnClickListener {

    private static final String TAG = "MeFragment";

    private TextView tvname;
    private TextView tv_accout;
    private RoundImageView mHeadImg;

    private TextView mTextViewContactUs;
    private TextView mTextViewFeedBack;
    private TextView mTextViewRecommend;

    private ViewGroup mColloectionLayout;
    private ViewGroup mAskQuestionLayout;
    private ViewGroup mReceiveQuestionLayout;
    private View mSignMyAskQuestion;
    private View mSignMyReceiveQuestion;
    private String personalInfo = "";

    private CustomProgressDialog progressDialog;


    public static MeFragment getInstance(Bundle bundle) {
        MeFragment settingFragment = new MeFragment();
        settingFragment.setArguments(bundle);
        return settingFragment;
    }

    @Override
    protected void setUIHandler() {
        super.setUIHandler();
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
        switch (msg.what) {
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_NORAMLUSER_INFO: {
                L.d(TAG, "get Personal Info:" + msg.obj);
                personalInfo = (String) msg.obj;
                Gson gson = new Gson();
                NoramlUserInfoResponse normalUserInfo = gson.fromJson(personalInfo, NoramlUserInfoResponse.class);

                NoramlUserInfoResponse.Content content = normalUserInfo.getResponse().getContent();
                if (content != null) {
                    NormalUserInfo info = new NormalUserInfo();
                    info.setUserId(content.getUserId());
                    info.setUserName(content.getUserName());
                    info.setNikeName(content.getNikeName());
                    info.setMobile(content.getMobile());
                    info.setAddress(content.getAddress());
                    info.setEmail(content.getEmail());
                    //info.setCity(content.getCity());
                    info.setCompany(content.getCompany());
                    // info.setSpecies(content.getSpecies());
                    // info.setBusinessType(content.getBusinessType());
                    info.setBreedScope(content.getBreedScope());
                    info.setPhoto(content.getPhoto());
                    info.setFarmAddress(content.getFarmAddress());
                    info.setFarmName(content.getFarmName());
                    info.setDuites(content.getTitle());
                    HengHengHuiAppliation.getInstance().saveNormalUserInfo(info);
                    if (tvname != null) {
                        tvname.setText(content.getUserName());
                    }
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }
            break;
            case CodeUtil.CmdCode.MsgTypeCode.MSG_GET_EXPERTUSER_INFO: {
                L.d(TAG, "get Personal Info:" + msg.obj);
                personalInfo = (String) msg.obj;
                Gson gson = new Gson();
                ExpertUserInfoResponse expertUserInfoResponse = gson.fromJson(personalInfo, ExpertUserInfoResponse.class);
                ExpertUserInfoResponse.Content content = expertUserInfoResponse.getResponse().getContent();
                updateExpertInfo(content);
                if (tvname != null) {
                    tvname.setText(content.getUserName());
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected String setFragmentTag() {
        return TAG;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    private void showProgressDialog() {
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(this
                .getResources().getString(R.string.progress_getInfo_doing));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                // finish();
                // cancelUpdate = true;
            }
        });
        progressDialog.show();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container,
                            Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_me, container,
                false);
        tvname = (TextView) view.findViewById(R.id.tvname);
        tv_accout = (TextView) view.findViewById(R.id.tvmsg);
        mHeadImg = (RoundImageView) view.findViewById(R.id.head);
        mHeadImg.setType(RoundImageView.TYPE_CIRCLE);
        mTextViewRecommend = (TextView) view.findViewById(R.id.txt_recommend);
        mTextViewContactUs = (TextView) view.findViewById(R.id.txt_contact_us);
        mTextViewFeedBack = (TextView) view.findViewById(R.id.txt_feedback);
        mTextViewContactUs.setOnClickListener(this);
        mTextViewRecommend.setOnClickListener(this);
        mTextViewFeedBack.setOnClickListener(this);

        view.findViewById(R.id.view_user).setOnClickListener(this);
        view.findViewById(R.id.txt_my_integration).setOnClickListener(this);

        mColloectionLayout = (ViewGroup) view.findViewById(R.id.layout_my_collection);
        mColloectionLayout.setOnClickListener(this);
        mAskQuestionLayout = (ViewGroup) view.findViewById(R.id.layout_my_ask_question);
        mAskQuestionLayout.setOnClickListener(this);
        mReceiveQuestionLayout = (ViewGroup) view.findViewById(R.id.layout_my_receive_question);
        mReceiveQuestionLayout.setOnClickListener(this);

        mSignMyAskQuestion = view.findViewById(R.id.sign_my_ask_question);
        mSignMyReceiveQuestion = view.findViewById(R.id.sign_my_receive_question);

        if (LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.NORMAL_USER) {
            mReceiveQuestionLayout.setVisibility(View.GONE);
            mAskQuestionLayout.setVisibility(View.VISIBLE);
        } else {
            mReceiveQuestionLayout.setVisibility(View.VISIBLE);
            mAskQuestionLayout.setVisibility(View.GONE);
        }

        PersonalnfoManager.setHandler(mHandler);

        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            if (LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.EXPERT_USER) {
                if (HengHengHuiAppliation.getInstance().getLoginExpertUser() == null) {
                    PersonalnfoManager.getExpertUserInfo(LoginUserBean.getInstance().getLoginUserid());
                    showProgressDialog();
                }
            } else {
                if (HengHengHuiAppliation.getInstance().getLoginNormalUser() == null) {
                    PersonalnfoManager.getNormalUserInfo(LoginUserBean.getInstance().getLoginUserid());
                    showProgressDialog();
                }
            }
        } else {
            ToastUtil.showShort(getActivity(), R.string.network_isnot_available);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_user: {
                if (LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.EXPERT_USER) {
                    Intent intent = new Intent(getActivity(), ExpertUserInfoActivity.class);
                    intent.putExtra("PERSONAL_INFO", personalInfo);
                    Utils.start_Activity(getActivity(), intent);
                } else {
                    Intent intent = new Intent(getActivity(), NormalUserInfoActivity.class);
                    intent.putExtra("PERSONAL_INFO", personalInfo);
                    Utils.start_Activity(getActivity(), intent);
                }

            }
            break;
            case R.id.txt_my_integration:// 积分
            {
                Intent intent = new Intent(getActivity(), IntegrationActivity.class);
                Utils.start_Activity(getActivity(), intent);
            }
            break;
            case R.id.layout_my_collection:// 我的收藏
            {
                Intent intent = new Intent(getActivity(), MyFavoriteActivity.class);
                Utils.start_Activity(getActivity(), intent);
            }
            break;
            case R.id.layout_my_ask_question:// 我的提问
            {
                Intent intent = new Intent(getActivity(), MyQuestionActivity.class);
                intent.putExtra("type", MyQuestionActivity.TYPE_MY_QUESTION);
                Utils.start_Activity(getActivity(), intent);
            }
            break;
            case R.id.layout_my_receive_question: { //我收到的问题
                Intent intent = new Intent(getActivity(), MyQuestionActivity.class);
                intent.putExtra("type", MyQuestionActivity.TYPE_MY_ANWSER_QUESTION);
                Utils.start_Activity(getActivity(), intent);
            }
            break;
            case R.id.txt_contact_us: {
                String strMobile = "400-212-5566";
                //此处应该对电话号码进行验证。。
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strMobile));
                getActivity().startActivity(intent);
            }
            break;
            case R.id.txt_recommend: {
                handleShare();
            }
            break;
            case R.id.txt_feedback: {
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.FEED_BACK);
            }
            break;
            default:
                break;
        }
    }

    // 分享
    public void handleShare() {
        final ShareDialog dialog = new ShareDialog(getActivity(), getActivity());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);
        dialog.setShareInfo("Title", "ShareContent", "http://www.baidu.com");
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hengheng.question.push");
        getActivity().registerReceiver(pushBroadcaseReciever,filter);
        ((MainActivity) getActivity()).setToolBarVisiblity();
        String uid = LoginUserBean.getInstance().getLoginUserid();
        int pushSign = PreferencesWrapper.getInstance().getPreferenceIntValue("question" + uid);
        if (LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.EXPERT_USER) {
            ExpertUserInfo userInfo = HengHengHuiAppliation.getInstance().getLoginExpertUser();
            if (userInfo != null && tvname != null) {
                String name = userInfo.getUserName();
                if (name != null) {
                    tvname.setText(name);
                }
            }
            try {
                if (userInfo != null && userInfo.getAvatarLocalPath() != null) {
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeFile(userInfo.getAvatarLocalPath(), option);
                    L.d(TAG, "userInfo.getAvatarLocalPath():" + userInfo.getAvatarLocalPath());
                    mHeadImg.setImageBitmap(bitmap);
                } else if (userInfo != null && userInfo.getPhoto() != null) {
                    L.d(TAG, "userInfo.getPhoto():" + userInfo.getPhoto());
                    Picasso.with(getActivity()).load(userInfo.getPhoto()).error(R.drawable.shouyi2).
                            placeholder(R.drawable.shouyi2).into(mHeadImg);
                } else {
                    mHeadImg.setImageResource(R.drawable.shouyi2);
                }
            } catch (Exception error) {
                mHeadImg.setImageResource(R.drawable.shouyi2);
            }
            if(pushSign == 1) {
                mSignMyReceiveQuestion.setVisibility(View.VISIBLE);
            }else{
                mSignMyReceiveQuestion.setVisibility(View.GONE);
            }
        } else {
            NormalUserInfo userInfo = HengHengHuiAppliation.getInstance().getLoginNormalUser();
            if (userInfo != null && tvname != null) {
                String name = userInfo.getUserName();
                if (name != null) {
                    tvname.setText(name);
                }
            }
            if (userInfo != null && userInfo.getAvatarLocalPath() != null) {
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(userInfo.getAvatarLocalPath(), option);
                L.d(TAG, "userInfo.getAvatarLocalPath():" + userInfo.getAvatarLocalPath());
                mHeadImg.setImageBitmap(bitmap);
            } else if (userInfo != null && userInfo.getPhoto() != null) {
                L.d(TAG, "userInfo.getPhoto():" + userInfo.getPhoto());
                //mImageLoader.displayImage(userInfo.getPhoto(), mHeadImg);

                if(userInfo.getPhoto()!=null && !userInfo.getPhoto().isEmpty()){
                    Picasso.with(getActivity()).load(userInfo.getPhoto()).error(R.drawable.index_zhuanti).
                            placeholder(R.drawable.index_zhuanti).into(mHeadImg);
                }
                else{
                    mHeadImg.setImageResource(R.drawable.index_zhuanti);
                }
            } else {
                mHeadImg.setImageResource(R.drawable.index_zhuanti);
            }
            if(pushSign == 1) {
                mSignMyAskQuestion.setVisibility(View.VISIBLE);
            }else{
                mSignMyAskQuestion.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(pushBroadcaseReciever);
        super.onPause();
    }

    private BroadcastReceiver pushBroadcaseReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String uid = LoginUserBean.getInstance().getLoginUserid();
            int pushSign = PreferencesWrapper.getInstance().getPreferenceIntValue("question" + uid);
            if (LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.EXPERT_USER) {
                if(pushSign == 1) {
                    mSignMyReceiveQuestion.setVisibility(View.VISIBLE);
                }else{
                    mSignMyReceiveQuestion.setVisibility(View.GONE);
                }
            } else {
                if(pushSign == 1) {
                    mSignMyAskQuestion.setVisibility(View.VISIBLE);
                }else{
                    mSignMyAskQuestion.setVisibility(View.GONE);
                }
            }
        }
    };

    private void updateExpertInfo(ExpertUserInfoResponse.Content content) {
        if (content == null) {
            return;
        }
        //TODO 将该用户的信息传递至服务器
        ExpertUserInfo expertUserInfo = new ExpertUserInfo();
        expertUserInfo.setUserId(LoginUserBean.getInstance().getLoginUserid());
        if (content.getUserName() != null) {
            expertUserInfo.setUserName(content.getUserName());
        }
        expertUserInfo.setMobile(LoginUserBean.getInstance().getLoginUserid());
        if (content.getEmail() != null) {
            expertUserInfo.setEmail(content.getEmail());
        }

        // normalUserInfo.setCity(tv_city.getText().toString());
        if (content.getAddress() != null) {
            expertUserInfo.setAddress(content.getAddress());
        }
        if (content.getIsRect() != null) {
            expertUserInfo.setIsRect(content.getIsRect());
        }
        if (content.getCertType() != null) {
            expertUserInfo.setCertType(content.getCertType());
        }
        if (content.getCompany() != null) {
            expertUserInfo.setCompany(content.getCompany());
        }
        if (content.getTitles() != null) {
            expertUserInfo.setTitles(content.getTitles());
        }
        if (content.getDesc() != null) {
            expertUserInfo.setDesc(content.getDesc());
        }
        if (content.getProfessional() != null) {
            expertUserInfo.setProfessional(content.getProfessional());
        }
        if (content.getPhoto() != null) {
            expertUserInfo.setPhoto(content.getPhoto());
        }
        HengHengHuiAppliation.getInstance().saveExpertUserInfo(expertUserInfo);
    }
}
