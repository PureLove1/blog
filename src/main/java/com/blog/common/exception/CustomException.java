package com.blog.common.exception;

import lombok.Getter;

/**
 * 自定义异常类
 * @author 贺畅
 * @date 2022/11/28
 */
@Getter
public class CustomException extends RuntimeException {
	private String message;

	public CustomException(String message) {
		this.message = message;
	}
}
