package com.blog.util;

/**
 * @author PureLove1
 * @date 2022/12/22
 */
public class StringUtil {
	/**
	 * 判断是否为Null或全为空格的字符串
	 *
	 * @param string
	 * @return
	 */
	public static boolean isNotBlank(String string) {
		return string != null && !"".equals(string.trim());
	}

	/**
	 * 判断是否为Null
	 *
	 * @param string
	 * @return
	 */
	public static boolean isNotNull(String string) {
		return string != null;
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param string
	 * @return
	 */
	public static boolean isNotEmpty(String string) {
		return string != null && string.length() != 0;
	}

}
