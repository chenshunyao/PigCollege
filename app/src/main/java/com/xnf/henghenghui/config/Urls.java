package com.xnf.henghenghui.config;

public class Urls {

    /**
     * 哼哼会服务器接口地址
     */
    public static final String SERVER_IP = "http://www.zhu213.com:8080/";
    public static final String SERVER_URL_ = "http://www.zhu213.com:8080/HenghengServer/";
    public static final String SERVER_URL = SERVER_URL_+"client/";


    /**
     * 用户登录Action
     */
    public static final String ACTION_LOGIN = "login.action";
    /**
     * 获取首页广告的Action
     */
    public static final String ACTION_INDEXBANNER= "indexBanner.action";
    /*
     * 获取验证码
     */
    public static final String ACTION_GETVERIFYCODE = "getVerifyCode.action";

    /*
    * 校验验证码
    */
    public static final String ACTION_CHECKVERIFYCODE = "checkVerifyCode.action";
    /*
    * 修改密码
    */
    public static final String ACTION_MODIFYPASSWORD = "modifyPassword.action";
    /*
   *用户注册Action
   */
    public static final String ACTION_REGISTER = "register.action";

    /**
     * 用户积分action
     */
    public static final String ACTION_USERPOINYS = "userPoints.action";
    /**
     * 获取普通用户账户信息Action
     */
    public static final String ACTION_GET_NORMALUSER_INFO = "getBreederInfo.action";
    /**
     * 获取专家用户账户信息Action
     */
    public static final String ACTION_GET_EXPERTUSER_INFO = "getExpertInfo.action";
//    /**
//     * 获取用户提问的问题接口Action
//     */
//    public static final String ACTION_USER_ASK_QUESTION = "userAskQuestion.action";
    /**
     * 用户解答的问题Action
     */
    public static final String ACTION_USER_ANSWER_QUESTION = "userAnswerQuestion.action";
    /**
     * 首页搜索Action
     */
    public static final String ACTION_INDEX_SEARCH = "indexSearch.action";
    /**
     * 百科分类Action
     */
    public static final String ACTION_BAIKE_CATEGORY = "entryCategory.action";
    /**
     * 百科列表Action
     */
    public static final String ACTION_BAIKE_LIST = "entryContentList.action";
    /**
     * 获取百科信息的Action
     */
    public static final String ACTION_BAIKE_INFO = "entryDetail.action";
    /**
     * 首页搜索热词Action
     */
    public static final String ACTION_HOT_KEY = "hotKeyWordList.action";
    /**
     * 在线提问Action
     */
    public static final String ACTION_ONLINE_ASKQUESTION = "onlineAskQuestion.action";
    /**
     * 回答问题Action
     */
    public static final String ACTION_ANWSER_QUESTION = "commitReply.action";
    /**
     * 问题分类的Action
     */
    public static final String ACTION_QUESTION_CATEGORY = "questionCategory.action";
    /**
     * 获取问题信息的Action
     */
    public static final String ACTION_QUESTION_INFO = "questionInfo.action";
    /**
     * 获取问题信息的Action
     */
    public static final String ACTION_QUESTIONV2_INFO = "questionInfoV2.action";
    /**
     * 获取问题信息的Action
     */
    public static final String ACTION_ANSWER_QUESTION_INFO = "answerQuestion.action";
    /**
     * 获取专家列表Action
     */
    public static final String ACTION_EXPERTS_INFO_LIST = "expertsInfoList.action";
    /**
     * 获取专家列表详细Action
     */
    public static final String ACTION_EXPERTS_INFO = "expertsDetail.action";
    /**
     * 获取专家回答问题列表Action
     */
    public static final String ACTION_EXPERTS_ANSWER_QUESTION = "expertAnswerQuestion.action";
    /**
     * 获取用户提问题列表Action
     */
    public static final String ACTION_USER_ASK_QUESTION = "breederAskQuestion.action";
    //TODO 这里还差两个接口
    /**
     * 获取专家面对面分类的Action
     */
    public static final String ACTION_EXPERTFACETOFACE = "expertFaceToFace.action";
    /**
     * 获取专家面对面的问题列表
     */
    public static final String ACTION_FACETOFACE_QUESTION_LISY = "face2FaceQuestionList.action";
    /**
     * 获取专家问题列表
     */
    public static final String ACTION_MY_ANSWER_QUESTION_LISY = "myAnswerQuestionList.action";
    /**
     * 获取话题分类的Action
     */
    public static final String ACTION_TOPIC_DETAIL= "topicDetail.action";
    /**
     * 获取话题分类列表的Action
     */
    public static final String ACTION_TOPIC_LIST = "topicList.action";
    /**
     * 获取话题分类列表的Action
     */
    public static final String ACTION_TOPIC_ARTICLE_LIST = "topicArticleList.action";
    /**
     * 获取首页热点列表的Action
     */
    public static final String ACTION_HOT_ARTICLE_LIST = "hotArticleList.action";
    /**
     * 获取首页话题列表的Action
     */
    public static final String ACTION_HOT_QT_TP_LIST = "hotQtHotTp.action";
    /**
     * 发表话题
     */
    public static final String ACTION_PUBLISH_TOPIC = "publishTopic.action";
    /**
     * 千百问的Action
     */
    public static final String ACTION_KNOWALL_SEARCH = "knowallSearch.action";
    /**
     * 热点会评Action
     */
    public static final String ACTION_HOT_VIDEO = "hotVideo.action";
    /**
     * 热点会评Action
     */
    public static final String ACTION_MAKE_VIDEO_NOTE = "makeVideoNote.action";
    /**
     * 热点精选Action
     */
    public static final String ACTION_HOT_POINTS = "hotPoints.action";
    /**
     * 词条分类的Action
     */
    public static final String ACTION_ENTRY_CATEGORY = "entryCategory.action";
    /**
     * 养猪百科Action
     */
    public static final String ACTION_ANSWER_ANYTHING_SEARCH = "answerAnythingSearch.action";
    /**
     * 词条详情Action
     */
    public static final String ACTION_ENTRY_DETAIL = "entryDetail.action";
    /**
     * 添加词条Action
     */
    public static final String ACTION_ADD_ENTRY_INFO = "addEntryInfo.action";
    /**
     * 课堂广告轮播Action
     */
    public static final String ACTION_CLASS_BANNER = "classBanner.action";
    /**
     * 课堂视频列表Action
     */
    public static final String ACTION_CLASS_VIDEO_LIST = "classVideoList.action";
    /**
     * 课堂视频详情Action
     */
    public static final String ACTION_CLASS_VIDEO_DETAIL = "classVideoDetail.action";
    /**
     * 点赞Action
     */
    public static final String ACTION_GIVE_PRAISE = "givePraise.action";
    /**
     * 关注Action
     */
    public static final String ACTION_CONCERN = "concernOpt.action";
    /**
     * 评论操作Action
     */
    public static final String ACTION_GET_COMMENT_LIST = "commentList.action";
    /**
     * 跟帖Action
     */
    public static final String ACTION_GIVE_REPLY = "giveReply.action";
    /**
     * 跟帖Action
     */
    public static final String ACTION_CHECK_UPDATE = "checkApkVersion.action";
    /**
     * 点赞，评论等信息查询Action
     */
    public static final String ACTION_PRAISE_REPLY_COUNT = "praiseBrowseReplyCount.action";
	/**
	 *获取会评列表Action
     */
	public static final String ACTION_MEETING_INFO_LIST= "conferenceList.action";
	/**
     * 获取会评详情Action
     */
	public static final String ACTION_MEETING_INFO_DETAIL= "conferenceDetail.action";
    /**
     * 获取直播室列表Action
     */
    public static final String ACTION_LIVE_CONFERENCE_LIST = "liveConferenceList.action";
    /**
     * 获取听课笔记列表Action
     */
    public static final String ACTION_CONFERENCE_NOTE_LIST = "conferenceNoteList.action";
    /**
     * 获取会评笔记信息Action
     */
    public static final String ACTION_CONFERENCE_NOTE_INFO = "conferenceNoteInfo.action";
    /**
     * 获取会评评论列表Action
     */
    public static final String ACTION_CONFERENCE_COMMENT_LIST = "commentList.action";
    /**
     * 发布会评评论Action
     */
    public static final String ACTION_CONFERENCE_ADD_COMMENT = "publishComment.action";
    /**
     * 收藏操作Action
     */
    public static final String ACTION_COURSE_FAV_OPT = "favoriteOpt.action";
    /**
     * 支付宝付款回调Action
     */
    public static final String ACTION_ALIPAY_TO_NOTIFY = "/alipay/henghengServer/toNotify.action";

    /**
     * 修改普通用户信息Action
     */
    public static final String ACTION_MODIFY_NORMALUSER_INFO = "/modifyBreederInfo.action";
    /**
     * 修改专家用户信息Action
     */
    public static final String ACTION_MODIFY_EXPERTUSER_INFO = "/modifyExpertInfo.action";
    /**
     * 上传图片Action
     */
    public static final String ACTION_UPLOAD_IMAGE = "clientFileUpload";
    /**
     * 意见反馈Action
     */
    public static final String ACTION_FEEDBACK = "CommentsFeedback.action";
    /**
     * 获取用户列表Action
     */
    public static final String ACTION_USER_CONTACT_LIST = "userContactList.action";

    /**
     * 获取用户认证状态
     */
    public static final String ACTION_GET_AUTH_STATUS= "getAuditResult.action";
    /**
     * 获取专家面对面问题详情
     */
    public static final String ACTION_GET_F2FQS_DETAIL_INFO ="questionInfo.action";
    /**
     * 获取专家面对面问题详情中的专家回答列表
     */
    public static final String ACTION_GET_F2FQS_DETAIL_REPLY_LIST ="answerQuestion.action";

    public static final String ACTION_RECORD_CALL_HISTORY ="recordCallHistory.action";

    public static final String ACTION_CALL_EMPOWER ="callEmpower.action";
    /**
     * 获取收藏列表数据
     */
    public static final String ACTION_GET_FAVORITELIST ="favoriteList.action";

    /**
     * 获取收藏列表数据(3月30日新增)
     */
    public static final String ACTION_GET_NEW_FAVORITELIST ="newFavoriteList.action";

    /**
     * 获取文章详情的Action
     */
    public static final String ACTION_ARTICLE_DETAIL_INFO = "articleDetailInfo.action";

    /**
     * 获取专题列表的Action
     */
    public static final String ACTION_SUBJECT_LIST = "topicArtList.action";
    /**
     * 获取专题中的文章列表Action
     */
    public static final String ACTION_SUBJECT_ARTICLE_LIST = "topicArtShowList.action";
    /**
     * 获取专题中的文章列表Action
     */
    public static final String ACTION_SUBJECT_DETAIL = "topicDetail.action";
    /**
     * 推送Channelid传输Action
     */
    public static final String ACTION_CHANNEL_ID = "updateUserChannelId.action";

    public static final String SHARE_ARTICLE_URL ="http://www.zhu213.com:8080/HenghengServer/articleDetail/share.action?artId=";


    public static final String SHARE_MEETING_URL = "http://www.zhu213.com:8080/HenghengServer/conference/share.action?cfId=";
}
