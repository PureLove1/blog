package com.blog.common;

import lombok.Data;

/**
 * @Author PureLove1
 * @Date 2023/7/17
 */
@Data
public class WebLog {
	/**
	 * 操作描述
	 */
	private String description;

	/**
	 * 操作用户
	 */
	private String username;

	/**
	 * 操作时间
	 */
	private Long startTime;

	/**
	 * 消耗时间
	 */
	private Integer spendTime;

	/**
	 * URI
	 */
	private String uri;

	/**
	 * URL
	 */
	private String url;

	/**
	 * 请求类型
	 */
	private String method;

	/**
	 * IP地址
	 */
	private String ip;

	/**
	 * 请求参数
	 */
	private Object parameter;

	/**
	 * 返回结果
	 */
	private Object result;


	public String getLogString() {
		return "用户-" + username + '\'' +
				",IP-" + ip + '\'' +
				"于" + startTime + '\'' +
				"执行了" + description + "操作" + '\'' +
				",请求URL-" + url + '\'' +
				",请求URI-" + uri + '\'' +
				",请求方法-" + method + '\'' +
				",请求参数-" + parameter + '\'' +
				",请求耗时-" + spendTime + "毫秒" + '\'' +
				",返回结果-" + result;
	}
}
