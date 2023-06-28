package com.blog.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 贺畅
 * @date 2022/12/22
 */
@Getter
@AllArgsConstructor
public class UnauthorizedException extends RuntimeException {
	private String message;
}
