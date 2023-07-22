package com.blog.constant;

/**
 * @author 贺畅
 * @date 2022/12/16
 * 通用常量
 */
public class CommonConstant {

	/**
	 * 登录请求路径
	 */
	public static final String LOGIN_REQUEST_PATH = "/login";

	/**
	 * 登录请求路径
	 */
	public static final String LOGOUT_REQUEST_PATH = "/logout";

	/**
	 * 验证码获取
	 */
	public static final String GET_IMAGE_CODE_PATH = "/getCode";

	/**
	 * 邮箱验证码获取
	 */
	public static final String GET_EMAIL_CODE_PATH = "/getEmailCode";

	/**
	 * 放行白名单
	 */
	public static final String[] URL_WHITELIST = {
			LOGIN_REQUEST_PATH,
			GET_IMAGE_CODE_PATH,
			GET_EMAIL_CODE_PATH,
			LOGOUT_REQUEST_PATH
	};
}
