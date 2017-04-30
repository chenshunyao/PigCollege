package com.xnf.henghenghui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.RequestHeaders;
//import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xnf.henghenghui.io.MyDiscCache;
import com.youku.cloud.player.YoukuPlayerConfig;
import com.youku.cloud.player.YoukuProfile;
import com.youku.cloud.utils.Logger;
//import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.PlatformConfig;
import com.xnf.henghenghui.cache.DataCleanManager;
import com.xnf.henghenghui.config.ConfigConstants;
import com.xnf.henghenghui.config.Constants;
import com.xnf.henghenghui.config.LoginUserBean;
import com.xnf.henghenghui.model.ExpertUserInfo;
import com.xnf.henghenghui.model.NormalUserInfo;
import com.xnf.henghenghui.model.User;
import com.xnf.henghenghui.model.ExpertUserInfo;
import com.xnf.henghenghui.ui.image.DisplayImageOption;
import com.xnf.henghenghui.io.DefaultThreadFactory;
import com.xnf.henghenghui.util.CyptoUtils;
import com.xnf.henghenghui.util.FileUtil;
import com.xnf.henghenghui.util.MethodsCompat;
import com.xnf.henghenghui.util.StringUtils;
//import com.youku.player.YoukuPlayerBaseConfiguration;
import com.xnf.henghenghui.util.NetworkUtil;

import org.kymjs.kjframe.Core;

import java.io.File;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2015/12/27.
 */
public class HengHengHuiAppliation extends BaseApplication {
    // private static final String TAG = "QixinApplication";
    private static HengHengHuiAppliation instance;
    //public static YoukuPlayerBaseConfiguration configuration;
    public static  String IMEI;
    //优酷sdk client_id
    public static final String CLIENT_ID_WITH_AD = "8c01dc6e51911d1f";
    //优酷sdk secret_id
    public static final String CLIENT_SECRET_WITH_AD = "b22b013918fca143e33eac9026a39546";

    public static final int PAGE_SIZE = 20;// 默认分页大小

    /**
     * 当前登录的用户id
     */
    private String loginUid;

    private boolean login;

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    //TODO 使用fresco 替代 universalimageloader
    private static final long STATS_CLOCK_INTERVAL_MS = 1000;// 内存状态等刷新延迟
    private static final int DEFAULT_MESSAGE_SIZE = 1024;// 默认StringBuilder的缓存区域
    private static final int BYTES_IN_MEGABYTE = 1024 * 1024;//Byte装MB

    public static HengHengHuiAppliation getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (CrashHandler.DEBUG) {
//            CrashHandler crashHandler = CrashHandler.getInstance();
//            crashHandler.init(getApplicationContext());
//        }
        instance = this;
        //优酷sdk初始化
        YoukuPlayerConfig.setLog(true);
        YoukuPlayerConfig.setClientIdAndSecret(CLIENT_ID_WITH_AD,CLIENT_SECRET_WITH_AD);
        YoukuPlayerConfig.onInitial(this);

        //Fresco.initialize(instance);
        FileUtil.isFolderExists("/sdcard/henghenghui/");
        FLog.setMinimumLoggingLevel(FLog.WARN);// 日志打印等级
        ConfigConstants.init(getResources());// 初始化默认图片（占位图，错误图）
        Fresco.initialize(this, ConfigConstants.getImagePipelineConfig(this));// 图片缓存初始化配置

        initImageLoader(getApplicationContext());

        initPlatFormConfig();

        DemoHelper.getInstance().init(getApplicationContext());

        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"2nnUlTAKoeXmyF3QIRbDiGx7");

        OkHttpUtils.getInstance()//
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)//全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)//全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);//全局的写入超时时间
                //.setCertificates(getAssets().open("srca.cer"), getAssets().open("zhy_server.cer"))//
                //.setCertificates(new Buffer().writeUtf8(CER_12306).inputStream());//设置自签名网站的证书

        RequestHeaders headers = new RequestHeaders();
        headers.put("X-Client-Type", "Android");
        OkHttpUtils.getInstance().addCommonHeader(headers); //全局公共头

        IMEI = NetworkUtil.getIMEI(this);


//        SmileyParser.getInstance(getApplicationContext());
//
//        EmailManager emailManager = EmailManager.getInstance();
//        emailManager.init(this);
        /*configuration = new YoukuPlayerBaseConfiguration(this) {


            *//**
             * 通过覆写该方法，返回“正在缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的缓存界面
             *//*
            @Override
            public Class<? extends Activity> getCachingActivityClass() {
                // TODO Auto-generated method stub
                return null;
            }

            *//**
             * 通过覆写该方法，返回“已经缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的已缓存界面
             *//*

            @Override
            public Class<? extends Activity> getCachedActivityClass() {
                // TODO Auto-generated method stub
                return null;
            }

            *//**
             * 配置视频的缓存路径，格式举例： /appname/videocache/
             * 如果返回空，则视频默认缓存路径为： /应用程序包名/videocache/
             *
             *//*
            @Override
            public String configDownloadPath() {
                // TODO Auto-generated method stub

                //return "/myapp/videocache/";			//举例
                return null;
            }
        };*/
    }

    private void initPlatFormConfig(){
        //微信 appid appsecret
        PlatformConfig.setWeixin("wxfd8d3a4daa0311e4", "c160e2f6a617f32e40e226b1741df923");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105249491", "CTJjqkrDAdkUfNtg");
        //支付宝 appid
        PlatformConfig.setAlipay("2015111700822536");
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        Fresco.shutDown();// 关闭图片缓存～注意：一定要关闭，C++里new的空间
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.

        // MAX size of Image to save to memory,Screen Size;
        // default download thread 3

        // FOR CACHED IMAGE THREAD POOL
        final int CACHED_IMAGE_THREAD_POOL_SIZE = 1;
        final String CACHED_IMAGE_THREAD_POOL_NAME = "uil-cached";
        ThreadPoolExecutor cachedeExecutors= DefaultThreadFactory.createExecutor(CACHED_IMAGE_THREAD_POOL_SIZE, Thread.NORM_PRIORITY, QueueProcessingType.LIFO, CACHED_IMAGE_THREAD_POOL_NAME);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .taskExecutorForCachedImages(cachedeExecutors)
                .discCache(new MyDiscCache())
                .memoryCacheSize(8 * 1024 * 1024)
                        // 4M, remove this setting to use default : 1/8 APP Available
                        // Memory
                .defaultDisplayImageOptions(
                        DisplayImageOption.getHttpBuilder().build())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    //TODO 普通用户和专家用户的信息要分别存储
    /**
     * 保存专家登录信息
     *
     */
    public void saveExpertUserInfo(final ExpertUserInfo userInfo) {
        this.loginUid = userInfo.getUserId();
        this.login = true;
        setProperties(new Properties() {
            {

                setProperty("user.uid", String.valueOf(userInfo.getUserId()));
                if(userInfo.getUserName()!=null){
                    setProperty("user.userName", userInfo.getUserName());
                }else{
                    setProperty("user.userName", "");
                }
                if(userInfo.getMobile()!=null){
                    setProperty("user.mobile", userInfo.getMobile());
                }else{
                    setProperty("user.mobile", "");
                }
                if(userInfo.getEmail()!=null){
                    setProperty("user.email", userInfo.getEmail());
                }else{
                    setProperty("user.email", "");
                }
                if(userInfo.getAddress()!=null){
                    setProperty("user.address", userInfo.getAddress());
                }else{
                    setProperty("user.address", "");
                }
                if(userInfo.getIsRect()!=null){
                    setProperty("user.isRect", userInfo.getIsRect());
                }else{
                    setProperty("user.isRect", "");
                }
                if(userInfo.getCertType()!=null){
                    setProperty("user.certType", userInfo.getCertType());
                }else{
                    setProperty("user.certType", "");
                }
                if(userInfo.getCompany()!=null){
                    setProperty("user.company", userInfo.getCompany());
                }else{
                    setProperty("user.company", "");
                }
                if(userInfo.getTitles()!=null){
                    setProperty("user.titles", userInfo.getTitles());
                }else{
                    setProperty("user.titles", "");
                }
                if(userInfo.getDesc()!=null){
                    setProperty("user.desc", userInfo.getDesc());
                }else{
                    setProperty("user.desc", "");
                }
                if(userInfo.getProfessional()!=null){
                    setProperty("user.professional", userInfo.getProfessional());
                }else{
                    setProperty("user.professional", "");
                }
                if(userInfo.getOtherContact()!=null){
                    setProperty("user.otherContact", userInfo.getOtherContact());
                }else{
                    setProperty("user.otherContact", "");
                }
                if(userInfo.getFileMappingId()!=null){
                    setProperty("user.fileMappingId", userInfo.getFileMappingId());
                }else{
                    setProperty("user.fileMappingId", "");
                }
//                if(userInfo.getUserPoints()!=null){
//                    setProperty("user.userPoints", userInfo.getUserPoints());
//                }else{
//                    setProperty("user.userPoints", "");
//                }
                if(userInfo.getPhoto()!=null){
                    setProperty("user.photo", userInfo.getPhoto());
                }else{
                    setProperty("user.photo", "");
                }
            }
        });
    }

    /**
     * 更新专家用户信息
     *
     */
    @SuppressWarnings("serial")
    public void updateExpertUserInfo(final ExpertUserInfo userInfo) {
        setProperties(new Properties() {
            {
                if(userInfo.getUserName()!=null){
                    setProperty("user.userName", userInfo.getUserName());
                }else{
                    setProperty("user.userName", "");
                }
                if(userInfo.getMobile()!=null){
                    setProperty("user.mobile", userInfo.getMobile());
                }else{
                    setProperty("user.mobile", "");
                }
                if(userInfo.getEmail()!=null){
                    setProperty("user.email", userInfo.getEmail());
                }else{
                    setProperty("user.email", "");
                }
                if(userInfo.getAddress()!=null){
                    setProperty("user.address", userInfo.getAddress());
                }else{
                    setProperty("user.address", "");
                }
                if(userInfo.getIsRect()!=null){
                    setProperty("user.isRect", userInfo.getIsRect());
                }else{
                    setProperty("user.isRect", "");
                }
                if(userInfo.getCertType()!=null){
                    setProperty("user.certType", userInfo.getCertType());
                }else{
                    setProperty("user.certType", "");
                }
                if(userInfo.getCompany()!=null){
                    setProperty("user.company", userInfo.getCompany());
                }else{
                    setProperty("user.company", "");
                }
                if(userInfo.getTitles()!=null){
                    setProperty("user.titles", userInfo.getTitles());
                }else{
                    setProperty("user.titles", "");
                }
                if(userInfo.getDesc()!=null){
                    setProperty("user.desc", userInfo.getDesc());
                }else{
                    setProperty("user.desc", "");
                }
                if(userInfo.getProfessional()!=null){
                    setProperty("user.professional", userInfo.getProfessional());
                }else{
                    setProperty("user.professional", "");
                }
                if(userInfo.getOtherContact()!=null){
                    setProperty("user.otherContact", userInfo.getOtherContact());
                }else{
                    setProperty("user.otherContact", "");
                }
                if(userInfo.getFileMappingId()!=null){
                    setProperty("user.fileMappingId", userInfo.getFileMappingId());
                }else{
                    setProperty("user.fileMappingId", "");
                }
                if(userInfo.getUserPoints()!=null){
                    setProperty("user.userPoints", userInfo.getUserPoints());
                }else{
                    setProperty("user.userPoints", "");
                }
                if(userInfo.getPhoto()!=null){
                    setProperty("user.photo", userInfo.getPhoto());
                }else{
                    setProperty("user.photo", "");
                }
            }
        });
    }

    /**
     * 获得登录普通用户的信息
     *
     * @return
     */
    public ExpertUserInfo getLoginExpertUser() {
        ExpertUserInfo user = new ExpertUserInfo();
        user.setUserId(getProperty("user.uid"));
        user.setUserName(getProperty("user.userName"));
        user.setMobile(getProperty("user.mobile"));
        user.setEmail(getProperty("user.email"));
        user.setAddress(getProperty("user.address"));
        user.setCompany(getProperty("user.company"));
        user.setIsRect(getProperty("user.isRect"));
        user.setPhoto(getProperty("user.photo"));
        user.setCertType(getProperty("user.certType"));
        user.setTitles(getProperty("user.titles"));
        user.setDesc(getProperty("user.desc"));
        user.setProfessional(getProperty("user.professional"));
        user.setOtherContact(getProperty("user.otherContact"));
        user.setUserPoints(getProperty("user.userPoints"));
        user.setAvatarLocalPath(getProperty("user.avatarLocalPath"));
        return user;
    }



    /**
     * 清除登录信息
     */
    public void cleanLoginExpertInfo() {
        this.loginUid = "";
        this.login = false;
        removeProperty("user.uid","user.imei", "user.passWord", "user.regType",
                "user.userType", "user.userName","user.mobile","user.email",
                "user.address", "user.company", "user.titles","user.desc","user.professional",
                "user.fileMappingId","user.photo","user.platform");
    }


    /**
     * 保存普通登录信息
     *
     */
    public void saveNormalUserInfo(final NormalUserInfo userInfo) {
        this.loginUid = userInfo.getUserId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(userInfo.getUserId()));
                if(userInfo.getUserName()!=null){
                    setProperty("user.userName", userInfo.getUserName());
                }else{
                    setProperty("user.userName", "");
                }
                if(userInfo.getNikeName()!=null){
                    setProperty("user.nikeName", userInfo.getNikeName());
                }else{
                    setProperty("user.nikeName", "");
                }
                if(userInfo.getMobile()!=null){
                    setProperty("user.mobile",userInfo.getMobile());
                }else{
                    setProperty("user.mobile","");
                }
                if (userInfo.getEmail()!=null){
                    setProperty("user.email",userInfo.getEmail());
                }else{
                    setProperty("user.email","");
                }

                if(userInfo.getAddress()!=null){
                    setProperty("user.address", userInfo.getAddress());
                }
                else{
                    setProperty("user.address", "");
                }
                if(userInfo.getAddress()!=null){
                    setProperty("user.address", userInfo.getAddress());
                }
                else{
                    setProperty("user.address", "");
                }
                if(userInfo.getBreedScope()!=null){
                    setProperty("user.breedScope",userInfo.getBreedScope());
                }else{
                    setProperty("user.breedScope","");
                }
                if(userInfo.getPhoto()!=null){
                    setProperty("user.photo",userInfo.getPhoto());
                }else{
                    setProperty("user.photo","");
                }
                if(userInfo.getFarmName()!=null){
                    setProperty("user.farmName",userInfo.getFarmName());
                }else{
                    setProperty("user.farmName","");
                }

                if(userInfo.getFarmAddress() !=null){
                    setProperty("user.farmAddress",userInfo.getFarmAddress());
                }
                else{
                    setProperty("user.farmAddress","");
                }

                if(userInfo.getDuites() !=null){
                    setProperty("user.duites",userInfo.getDuites());
                }
                else{
                    setProperty("user.duites","");
                }
                if(userInfo.getCompany() !=null){
                    setProperty("user.company",userInfo.getCompany());
                }
                else{
                    setProperty("user.company","");
                }
//                if(userInfo.getAvatarLocalPath() !=null){
//                    setProperty("user.avatarLocalPath",userInfo.getAvatarLocalPath());
//                }
//                else{
//                    setProperty("user.avatarLocalPath","");
//                }
                if(userInfo.getFileMappingId() !=null){
                    setProperty("user.fileMappingId",userInfo.getFileMappingId());
                }
                else{
                    setProperty("user.fileMappingId","");
                }
            }
        });
    }

    public void updateUserInfo(final String tag, final String preInfo){
        setProperties(new Properties() {
            {
                if(preInfo !=null){
                    setProperty(tag,preInfo);
                }
                else{
                    setProperty(tag,"");
                }
            }
        });
    }
    /**
     * 更新普通用户信息
     *
     */
    @SuppressWarnings("serial")
    public void updateNormalUserInfo(final NormalUserInfo userInfo) {
        setProperties(new Properties() {
            {
                //setProperty("user.uid", String.valueOf(userInfo.getUserId()));
                if(userInfo.getUserName()!=null){
                    setProperty("user.userName", userInfo.getUserName());
                }else{
                    setProperty("user.userName", "");
                }
                if(userInfo.getNikeName()!=null){
                    setProperty("user.nikeName", userInfo.getNikeName());
                }else{
                    setProperty("user.nikeName", "");
                }
                if(userInfo.getMobile()!=null){
                    setProperty("user.mobile",userInfo.getMobile());
                }else{
                    setProperty("user.mobile","");
                }
                if (userInfo.getEmail()!=null){
                    setProperty("user.email",userInfo.getEmail());
                }else{
                    setProperty("user.email","");
                }

                //setProperty("user.city",userInfo.getCity());
                if(userInfo.getAddress()!=null){
                    setProperty("user.address", userInfo.getAddress());
                }
                else{
                    setProperty("user.address", "");
                }
                if(userInfo.getBreedScope()!=null){
                    setProperty("user.breedScope",userInfo.getBreedScope());
                }else{
                    setProperty("user.breedScope","");
                }
                if(userInfo.getPhoto()!=null){
                    setProperty("user.photo",userInfo.getPhoto());
                }else{
                    setProperty("user.photo","");
                }
                if(userInfo.getFarmName()!=null){
                    setProperty("user.farmName",userInfo.getFarmName());
                }else{
                    setProperty("user.farmName","");
                }

                if(userInfo.getFarmAddress() !=null){
                    setProperty("user.farmAddress",userInfo.getFarmAddress());
                }
                else{
                    setProperty("user.farmAddress","");
                }

                if(userInfo.getDuites() !=null){
                    setProperty("user.duites",userInfo.getDuites());
                }
                else{
                    setProperty("user.duites","");
                }
                if(userInfo.getCompany() !=null){
                    setProperty("user.company",userInfo.getCompany());
                }
                else{
                    setProperty("user.company","");
                }
//                if(userInfo.getAvatarLocalPath() !=null){
//                    setProperty("user.avatarLocalPath",userInfo.getAvatarLocalPath());
//                }
//                else{
//                    setProperty("user.avatarLocalPath","");
//                }
                if(userInfo.getFileMappingId() !=null){
                    setProperty("user.fileMappingId",userInfo.getFileMappingId());
                }
                else{
                    setProperty("user.fileMappingId","");
                }
            }
        });
    }

    /**
     * 获得登录普通用户的信息
     *
     * @return
     */
    public NormalUserInfo getLoginNormalUser() {
        NormalUserInfo user = new NormalUserInfo();
        user.setUserId(getProperty("user.uid"));
        user.setUserName(getProperty("user.userName"));
        user.setNikeName(getProperty("user.nikeName"));
        user.setMobile(getProperty("user.mobile"));
        user.setEmail(getProperty("user.email"));
        //user.setCity(getProperty("user.city"));
        user.setAddress(getProperty("user.address"));
        user.setCompany(getProperty("user.company"));
        //user.setSpecies(getProperty("user.species"));
        //user.setBusinessType(getProperty("user.businessType"));
        user.setBreedScope(getProperty("user.breedScope"));
        user.setFileMappingId(getProperty("user.fileMappingId"));
        user.setPhoto(getProperty("user.photo"));
        user.setDuites(getProperty("user.duites"));
        user.setFarmName(getProperty("user.farmName"));
        user.setFarmAddress(getProperty("user.farmAddress"));
        user.setAvatarLocalPath(getProperty("user.avatarLocalPath"));
        return user;
    }


    /**
     * 清除普通用户的登录信息
     */
    public void cleanLoginNormalUserInfo() {
        this.loginUid = "";
        this.login = false;
        removeProperty("user.uid","user.userName", "user.nikeName","user.mobile","user.email"
                ,"user.address", "user.company","user.breedScope","user.farmName" ,
                "user.farmAddress","user.photo","user.duites","user.avatarLocalPath");
    }

    /**
     * 清除专家的登录信息
     */
    public void cleanLoginExpertUserInfo() {
        this.loginUid = "";
        this.login = false;
        removeProperty("user.uid","user.userName","user.mobile","user.email"
                ,"user.address", "user.company","user.isRect","user.certType",
                "user.titles","user.desc","user.professional","user.otherContact",
                "user.userPoints","user.photo","user.avatarLocalPath");
    }

    public String getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        //TODO 根据登录的用户类型处理
        if(LoginUserBean.getInstance().getLoginUserType() == LoginUserBean.EXPERT_USER){
            cleanLoginExpertUserInfo();
        }else{
            cleanLoginNormalUserInfo();
        }
        //清除登陆信息
        clearLoginInfo();
        //ApiHttpClient.cleanCookie();
        this.cleanCookie();
        this.login = false;
        this.loginUid = "";

        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }

    public void saveLoginInfo(String username,String passwd){
        setProperty("loginUser",username);
        setProperty("passwd",passwd);
    }

    public void clearLoginInfo(){
        removeProperty("loginUser","passwd");
    }

    public String getLastLoginUser(){
        return getProperty("loginUser");
    }

    public  String getLastUserPasswd(){
        return getProperty("passwd");
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat
                    .getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        Core.getKJBitmap().cleanCache();
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

}

