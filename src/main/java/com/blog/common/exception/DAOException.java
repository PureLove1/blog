package com.blog.common.exception;

/**
 * @Author PureLove1
 * @Date 2023/6/29
 */
public class DAOException extends RuntimeException {
	public DAOException(String message) {
		super(message);
	}
}
