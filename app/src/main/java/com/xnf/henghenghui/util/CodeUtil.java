package com.xnf.henghenghui.util;


public class CodeUtil {

    private static final String TAG = "CodeUtil";

    public static final int RE_GET_TIME_USFUL_CODE = 0;
    public static final int GET_VCODE_SUCCESS = 1;
    public static final int CHECK_VCODE_SUCCESS = 2;
    public static final int CHECK_VCODE_FAILED = 3;
    public static final String FAV_FLAG_TYPE = "F";
    public static final String NO_START = "0";
    public static final String LIVING_CONFERENCE = "1";
    public static final String CONFERENCE_END = "2";

    public static class CmdCode {

        public static final int MSG_COMMON_HANDLE = 0xF0000001;

        public static class MsgTypeCode {

            public static final int UI_MSG = 0x10000000;

            public static final int NOTIFY_MSG = 0x20000000;

            public static final int MSG_LOGIN = 0x2000001;

            public static final int MSG_LOGIN_FAILED = 0x20000011;

            public static final int MSG_MODIFY_PASSWD = 0x2000003;

            public static final int MSG_CLASS_BANNER = 0x2000004;

            public static final int MSG_CLASS_HOTVIDEO_LIST = 0x2000005;

            public static final int MSG_CLASS_FREEVIDEO_LIST = 0x2000006;

            public static final int MSG_CLASS_DISCOUNTVIDEO_LIST = 0x2000007;

            public static final int MSG_CLASS_VIDEO_DETAIL = 0x2000008;

            public static final int MSG_CLASS_PRAISE_REPLY_COUNT = 0x2000013;

            public static final int MSG_CLASS_GIVE_PRAISE = 0x2000014;

            public static final int MSG_CLASS_MEETING_LIST = 0x2000015;

            public static final int MSG_CLASS_MEETING_DETAIL = 0x2000016;

            public static final int MSG_CLASS_LIVECONFERENCE_LIST = 0x2000017;

            public static final int MSG_CLASS_CONFERENCE_NOTE_LIST = 0x2000018;

            public static final int MSG_CLASS_CONFERENCE_NOTE_INFO = 0x2000022;

            public static final int MSG_CLASS_CONFERENCE_COMMENT_LIST = 0x2000019;

            public static final int MSG_CLASS_CONFERENCE_ADD_COMMENT = 0x2000020;

            public static final int MSG_CLASS_COURSE_FAV_OPT = 0x2000021;

            public static final int MSG_CLASS_CONCERN = 0x2000042;

            public static final int MSG_REGISTER = 0x2000009;

            public static final int MSG_CHECK_UPDATE= 0x200010;

            public static final int MSG_GET_VERIFY_CODE= 0x200011;

            public static final int MSG_CHECK_CODE= 0x200012;

            public static final int MSG_MODIFY_NORMALUSER_INFO= 0x200013;

            public static final int MSG_MODIFY_EXPERTUSER_INFO= 0x200014;
			
            public static final int MSG_QUESTION_CATEGORY = 0x2100000;

            public static final int MSG_UPLOAD_IMAGE = 0x2100001;

            public static final int MSG_COMMIT_QA = 0x2100002;

            public static final int MSG_HOT_MASTER = 0x2100003;

            public static final int MSG_LIST_MASTER = 0x2100004;

            public static final int MSG_DETAIL_MASTER = 0x2100005;

            public static final int MSG_GET_NORAMLUSER_INFO = 0x2100006;

            public static final int MSG_GET_EXPERTUSER_INFO = 0x2100007;

            public static final int MSG_GET_F2F_CATEGORY = 0x2100008;

            public static final int MSG_GET_TOPICLIST = 0x2100009;

            public static final int MSG_GET_F2F_LIST = 0x2100010;

            public static final int MSG_LIST_QA = 0x2100011;

            public static final int MSG_HOT_KEY = 0x2100012;

            public static final int MSG_MASTER_ANSWER_QUESTION = 0x2100013;

            public static final int MSG_USER_ASK_QUESTION = 0x2100014;

            public static final int MSG_ANWSER_QUESTION = 0x2100015;

            public static final int MSG_FEEDBACK = 0x2100016;

            public static final int MSG_QUESTION_INFO = 0x2100017;

            public static final int MSG_GET_TOPIC_DETAIL = 0x2100018;

            public static final int MSG_GET_F2F_QS_COMMENTLIST = 0x2100019;

            public static final int MSG_QUESTIONV2_INFO = 0x2100020;

            public static final int MSG_ANSWER_QUESTION_INFO = 0x2100021;

            public static final int MSG_GET_AUTH_STATUS = 0x2100022;

            public static final int MSG_CALL_EMPOWER = 0x2100023;

            public static final int  MSG_GET_INDEX_BANNER = 0x2100025;

            public static final int MSG_GET_TOPIC_ARTICLE_LIST = 0x2100026;

            public static final int  MSG_GET_FAVORITELIST_ARTICLE = 0x2100027;

            public static final int  MSG_GET_FAVORITELIST_VIDEO = 0x2100028;

            public static final int  MSG_GET_FAVORITELIST_MEETING = 0x2100029;

            public static final int  MSG_GET_FAVORITELIST_QUESTION = 0x2100030;

            public static final int  MSG_GET_FAVORITELIST_TOPIC= 0x2100031;

            public static final int  MSG_GET_FAVORITELIST_SUBJECT = 0x2100032;

            public static final int MSG_BAIKE_CATEGORY = 0x2100033;

            public static final int MSG_BAIKE_LIST = 0x2100034;

            public static final int MSG_BAIKE_INFO = 0x2100035;

            public static final int MSG_ARTICLE_DETAIL_INFO = 0x2100036;

            public static final int MSG_GET_SUBJECT_ARTICLE_LIST = 0x2100037;

            public static final int MSG_GET_SUBJECT_LIST = 0x2100038;

            public static final int MSG_GET_SUBJECT_DETAIL = 0x2100039;

            public static final int MSG_GET_HOTPT_HOTTP= 0x2100040;

            public static final int MSG_GET_PRAISE_BROWSER_REPLAY_COUNT= 0x2100041;

            public static int setUp(int orgCode, int orgMsgTypeCode) {
                int msgTypeCode = orgMsgTypeCode;
                if (!Mask.verify(Mask.MSG_TYPE_MASK, orgMsgTypeCode)) {
                    msgTypeCode = orgMsgTypeCode & Mask.MSG_TYPE_FILTER;
                }
                return (orgCode & Mask.MSG_TYPE_MASK) | msgTypeCode;
            }

        }

        public static class Mask {

            public static final int MSG_TYPE_MASK = 0x0FFFFFFF;

            public static final int MSG_TYPE_FILTER = ~MSG_TYPE_MASK;

            public static boolean verify(int mask, int code) {
                L.e(TAG, "verify mask = " + Integer.toHexString(mask)
                        + " code = " + Integer.toHexString(code)
                        + " (mask & code) = " + (mask & code));
                return (mask & code) == 0;
            }

        }

        public static boolean isUICode(int code) {
            return (Mask.MSG_TYPE_FILTER & code) == MsgTypeCode.UI_MSG;
        }

        public static boolean isNotifyCode(int code) {
            return (Mask.MSG_TYPE_FILTER & code) == MsgTypeCode.NOTIFY_MSG;
        }


        public static class UIMsgCode {


        }

        public static class NotifyMsgCode {




        }

    }


}
