package com.blog.common.exception;

import lombok.Getter;

/**
 * @author 贺畅
 * @date 2022/12/22
 */
@Getter
public class UnauthorizedException extends RuntimeException {
	private String message;

	public UnauthorizedException(String message) {
		super(message);
	}
}
