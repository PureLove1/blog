package com.blog.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限不足，请求被拒绝
 * @author 贺畅
 * @date 2022/12/22
 */
@Getter
@AllArgsConstructor
public class AccessDenyException extends RuntimeException {

	private String message;

}
