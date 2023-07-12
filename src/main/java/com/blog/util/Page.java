package com.blog.util;

import java.util.List;

/**
 * @Author PureLove1
 * @Date 2023/7/5
 * 分页封装类
 */
public class Page<E> {
	/**
	 * 总条数
	 */
	private int total;

	/**
	 * 数据
	 */
	private List<E> data;
}
