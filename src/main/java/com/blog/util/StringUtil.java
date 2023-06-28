package com.blog.util;

/**
 * @author PureLove1
 * @date 2022/12/22
 */
public class StringUtil {
	/**
	 * 判断是否为空串
	 * @param string
	 * @return
	 */
	public static boolean isNotBlank(String string){
		return string != null && !"".equals(string.trim());
	}

	/**
	 * 判断是否为Null
	 * @param string
	 * @return
	 */
	public static boolean isNotNull(String string){
		return string != null;
	}
}
