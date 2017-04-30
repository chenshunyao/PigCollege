package com.xnf.henghenghui.ui.view;

import android.os.Parcel;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.xnf.henghenghui.ui.utils.UIHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyURLSpan extends URLSpan {
    /**
     * Regular expression to match all IANA top-level domains for WEB_URL. List
     * accurate as of 2011/07/18. List taken from:
     * http://data.iana.org/TLD/tlds-alpha-by-domain.txt This pattern is
     * auto-generated by frameworks/ex/common/tools/make-iana-tld-pattern.py
     */
    public static final String TOP_LEVEL_DOMAIN_STR_FOR_WEB_URL = "(?:"
            + "(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
            + "|(?:biz|b[abdefghijmnorstvwyz])"
            + "|(?:cat|com|coop|c[acdfghiklmnoruvxyz])"
            + "|d[ejkmoz]"
            + "|(?:edu|e[cegrstu])"
            + "|f[ijkmor]"
            + "|(?:gov|g[abdefghilmnpqrstuwy])"
            + "|h[kmnrtu]"
            + "|(?:info|int|i[delmnoqrst])"
            + "|(?:jobs|j[emop])"
            + "|k[eghimnprwyz]"
            + "|l[abcikrstuvy]"
            + "|(?:mil|mobi|museum|m[acdeghklmnopqrstuvwxyz])"
            + "|(?:name|net|n[acefgilopruz])"
            + "|(?:org|om)"
            + "|(?:pro|p[aefghklmnrstwy])"
            + "|qa"
            + "|r[eosuw]"
            + "|s[abcdeghijklmnortuvyz]"
            + "|(?:tel|travel|t[cdfghjklmnoprtvwz])"
            + "|u[agksyz]"
            + "|v[aceginu]"
            + "|w[fs]"
            + "|(?:\u03b4\u03bf\u03ba\u03b9\u03bc\u03ae|\u0438\u0441\u043f\u044b\u0442\u0430" +
            "\u043d\u0438\u0435|\u0440\u0444|\u0441\u0440\u0431|\u05d8\u05e2\u05e1\u05d8|\u0622" +
            "\u0632\u0645\u0627\u06cc\u0634\u06cc|\u0625\u062e\u062a\u0628\u0627\u0631|\u0627" +
            "\u0644\u0627\u0631\u062f\u0646|\u0627\u0644\u062c\u0632\u0627\u0626\u0631|\u0627" +
            "\u0644\u0633\u0639\u0648\u062f\u064a\u0629|\u0627\u0644\u0645\u063a\u0631\u0628" +
            "|\u0627\u0645\u0627\u0631\u0627\u062a|\u0628\u06be\u0627\u0631\u062a|\u062a\u0648" +
            "\u0646\u0633|\u0633\u0648\u0631\u064a\u0629|\u0641\u0644\u0633\u0637\u064a\u0646" +
            "|\u0642\u0637\u0631|\u0645\u0635\u0631|\u092a\u0930\u0940\u0915\u094d\u0937\u093e" +
            "|\u092d\u093e\u0930\u0924|\u09ad\u09be\u09b0\u09a4|\u0a2d\u0a3e\u0a30\u0a24|\u0aad" +
            "\u0abe\u0ab0\u0aa4|\u0b87\u0ba8\u0bcd\u0ba4\u0bbf\u0baf\u0bbe|\u0b87\u0bb2\u0b99" +
            "\u0bcd\u0b95\u0bc8|\u0b9a\u0bbf\u0b99\u0bcd\u0b95\u0baa\u0bcd\u0baa\u0bc2\u0bb0" +
            "\u0bcd|\u0baa\u0bb0\u0bbf\u0b9f\u0bcd\u0b9a\u0bc8|\u0c2d\u0c3e\u0c30\u0c24\u0c4d" +
            "|\u0dbd\u0d82\u0d9a\u0dcf|\u0e44\u0e17\u0e22|\u30c6\u30b9\u30c8|\u4e2d\u56fd|\u4e2d" +
            "\u570b|\u53f0\u6e7e|\u53f0\u7063|\u65b0\u52a0\u5761|\u6d4b\u8bd5|\u6e2c\u8a66|\u9999" +
            "\u6e2f|\ud14c\uc2a4\ud2b8|\ud55c\uad6d|xn\\-\\-0zwm56d|xn\\-\\-11b5bs3a9aj6g|xn" +
            "\\-\\-3e0b707e|xn\\-\\-45brj9c|xn\\-\\-80akhbyknj4f|xn\\-\\-90a3ac|xn" +
            "\\-\\-9t4b11yi5a|xn\\-\\-clchc0ea0b2g2a9gcd|xn\\-\\-deba0ad|xn\\-\\-fiqs8s|xn" +
            "\\-\\-fiqz9s|xn\\-\\-fpcrj9c3d|xn\\-\\-fzc2c9e2c|xn\\-\\-g6w251d|xn\\-\\-gecrj9c|xn" +
            "\\-\\-h2brj9c|xn\\-\\-hgbk6aj7f53bba|xn\\-\\-hlcj6aya9esc7a|xn\\-\\-j6w193g|xn" +
            "\\-\\-jxalpdlp|xn\\-\\-kgbechtv|xn\\-\\-kprw13d|xn\\-\\-kpry57d|xn\\-\\-lgbbat1ad8j" +
            "|xn\\-\\-mgbaam7a8h|xn\\-\\-mgbayh7gpa|xn\\-\\-mgbbh1a71e|xn\\-\\-mgbc0a9azcg|xn" +
            "\\-\\-mgberp4a5d4ar|xn\\-\\-o3cw4h|xn\\-\\-ogbpf8fl|xn\\-\\-p1ai|xn\\-\\-pgbs0dh|xn" +
            "\\-\\-s9brj9c|xn\\-\\-wgbh1c|xn\\-\\-wgbl6a|xn\\-\\-xkc2al3hye2a|xn" +
            "\\-\\-xkc2dl3a5ee0h|xn\\-\\-yfro4i67o|xn\\-\\-ygbi2ammx|xn\\-\\-zckzah|xxx)"
            + "|y[et]" + "|z[amw]))";
    /**
     * Good characters for Internationalized Resource Identifiers (IRI). This
     * comprises most common used Unicode characters allowed in IRI as detailed
     * in RFC 3987. Specifically, those two byte Unicode characters are not
     * included.
     */
    public static final String GOOD_IRI_CHAR = "a-zA-Z0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF";

    /**
     * Regular expression pattern to match most part of RFC 3987
     * Internationalized URLs, aka IRIs. Commonly used Unicode characters are
     * added.
     */

    public static final Pattern WEB_URL2 = Pattern
            .compile("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;" +
                    ":/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");

    public static final Pattern WEB_URL = Pattern
            .compile("((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\" +
                    ".\\+\\!\\*\\'\\(\\)"
                    + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
                    + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
                    + "((?:(?:["
                    + GOOD_IRI_CHAR
                    + "]["
                    + GOOD_IRI_CHAR
                    + "\\-]{0,64}\\.)+" // named host
                    + TOP_LEVEL_DOMAIN_STR_FOR_WEB_URL
                    + "|(?:(?:25[0-5]|2[0-4]" // or ip address
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]"
                    + "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9])))"
                    + "(?:\\:\\d{1,5})?)" // plus option port number
                    + "(\\/(?:(?:["
                    + GOOD_IRI_CHAR
                    + "\\;\\/\\?\\:\\@\\&\\=\\#\\~" // plus option query params
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
                    + "(?:\\b|$)"); // and finally, a word boundary or end of
    // input. This is to stop foo.sure from
    // matching as foo.su

    public static final Pattern EMAIL_ADDRESS = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    public static final Pattern EMOJI = Pattern
            .compile("\\[(([\u4e00-\u9fa5]+)|([a-zA-z]+))\\]");
    public static final Pattern EMOJI_NUMBERS = Pattern
            .compile("\\[[(0-9)]+\\]");

    public static final Pattern RECORD_SOUND = Pattern.compile(".*[#语音分享#].*");

    public MyURLSpan(Parcel src) {
        super(src);
    }

    public MyURLSpan(String url) {
        super(url);
    }

    @Override
    public void onClick(View widget) {
//        URLsUtils urls = URLsUtils.parseURL(getURL());
//        if (urls != null) {
//            UIHelper.showLinkRedirect(widget.getContext(), urls.getObjType(),
//                    urls.getObjId(), urls.getObjKey());
//        } else {
           UIHelper.openBrowser(widget.getContext(), getURL());
//        }
    }

    public static void parseLinkText(TextView widget, Spanned spanhtml) {
        CharSequence text = widget.getText();
        if (text instanceof Spannable) {
            Spannable sp = (Spannable) widget.getText();
            String str = spanhtml.toString();

            if (!str.contains("#") && !str.contains("@") && !str.contains("://")) {
                return;
            }

            Matcher m = null;
            // 检查出所有链接
            if (str.contains("://")) {
                m = WEB_URL.matcher(text);
                while (m.find()) {
                    int start = m.start();
                    int e = m.end();
                    sp.setSpan(new MyURLSpan(m.group()), start, e,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            // 检查出所有EMAIL地址
            if (str.contains("@")) {
                m = EMAIL_ADDRESS.matcher(text);
                while (m.find()) {
                    int start = m.start();
                    int e = m.end();
                    sp.setSpan(new EmailSpan(m.group()), start, e,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            int end = text.length();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            URLSpan[] htmlurls = spanhtml.getSpans(0, end, URLSpan.class);

            if (urls.length == 0 && htmlurls.length == 0) {
                widget.setText(sp);
                return;
            }

            SpannableStringBuilder style = new SpannableStringBuilder(text);
            // style.clearSpans();// 这里会清除之前所有的样式
            for (URLSpan url : urls) {
                if (!isNormalUrl(url)) {
                    style.removeSpan(url);// 只需要移除的URL样式之前，再重新设置
                    NoLinkURLSpan span = new NoLinkURLSpan(url.getURL());
                    style.setSpan(span, sp.getSpanStart(url),
                            sp.getSpanEnd(url), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    continue;
                }
                style.removeSpan(url); // 只需要移除之前的URL样式，再重新设置
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
                style.setSpan(myURLSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (URLSpan url : htmlurls) {
                style.removeSpan(url);// 只需要移除之前的URL样式，再重新设置
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
                style.setSpan(myURLSpan, spanhtml.getSpanStart(url),
                        spanhtml.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            widget.setText(style);
        }
    }

    public static boolean isNormalUrl(URLSpan url) {
        String urlStr = url.getURL();
        return !urlStr.endsWith(".sh");
    }
}
