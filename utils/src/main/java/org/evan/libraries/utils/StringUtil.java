package org.evan.libraries.utils;


/**
 * 字符串工具工具
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {
	/**
	 * 取文本长度,中文算两个
	 *
	 * @param text
	 */
	public static int lengthForChinese(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length;
	}
}

	

