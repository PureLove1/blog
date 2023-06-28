package com.blog.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 贺畅
 * @date 2023/6/17
 * 用户未登录异常
 */
@Getter
@AllArgsConstructor
public class UserNotLoginException extends RuntimeException {
	private String message;
}
