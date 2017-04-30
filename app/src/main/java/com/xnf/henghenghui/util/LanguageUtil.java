package com.xnf.henghenghui.util;

import java.util.Locale;

public class LanguageUtil {
	/**
	 * @return true :the locale language is Chinese
	 * 			false:the locale language is not Chinese
	 */
	public static int getLocaleCode() {
		Locale l = Locale.getDefault();
		String language = String.format("%s-%s", l.getLanguage(),
				l.getCountry());
		if (language.equalsIgnoreCase("zh-CN")) {
			return 0;
		} else if (language.equalsIgnoreCase("zh-TW")) {
			return 1;
		} else {
			return 999;
		}
	}
}
