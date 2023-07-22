package com.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * token信息封装
 *
 * @author 贺畅
 * @date 2022/12/21
 */
@Data
@AllArgsConstructor
public class TokenVO {
	private String userId;
	private String token;
	private String refreshToken;

	public TokenVO(User user, String token, String refreshToken) {
		this.userId = user.getId().toString();
		this.token = token;
		this.refreshToken = refreshToken;
	}

	public TokenVO(long id, String token, String refreshToken) {
		this.userId = String.valueOf(id);
		this.token = token;
		this.refreshToken = refreshToken;
	}
}
