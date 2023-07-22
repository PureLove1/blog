package com.blog.util;

import com.alibaba.fastjson.JSON;
import com.blog.common.JwtClaims;
import com.blog.common.UserHolder;
import com.blog.pojo.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token工具类
 *
 * @author PureLove1
 */
public class JwtTokenUtil {
	private final static Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	//	private static SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private static final String KEY = "9273928361_6385754865_8936946385";

	private static final SecretKey SECRET_KEY = new SecretKeySpec(KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	/**
	 * cookie存储令牌
	 *
	 * @param jwtClaims 需要封装的用户信息
	 * @return
	 */
	public static String createJwt(JwtClaims jwtClaims) {
		// 标头
		Map<String, Object> headMap = new HashMap<>(2);
		headMap.put("type", "JWT");
		headMap.put("alg", "HS256");

		// 自定义信息
		Map<String, Object> claims = new HashMap<>(1);
		claims.put("id", jwtClaims.getId());
		// 过期时间 一个小时
		long l = System.currentTimeMillis() + (3600000);
		Date date = new Date(l);

		//创建jwt
		JwtBuilder jwtBuilder = Jwts.builder().setHeader(headMap)
				.setClaims(claims)
				.setExpiration(date);
		return jwtBuilder.signWith(SECRET_KEY).compact();
	}

	/**
	 * cookie存储令牌
	 *
	 * @param jwtClaims 需要封装的用户信息
	 * @return
	 */
	public static String createRefreshToken(JwtClaims jwtClaims) {
		// 标头
		Map<String, Object> headMap = new HashMap<>(2);
		headMap.put("type", "JWT");
		headMap.put("alg", "HS256");

		// 自定义信息
		Map<String, Object> claims = new HashMap<>(1);
		claims.put("id", jwtClaims.getId());
		// 过期时间
		long l = System.currentTimeMillis() + (7200000);
		Date date = new Date(l);

		//创建jwt
		JwtBuilder jwtBuilder = Jwts.builder().setHeader(headMap)
				.setClaims(claims)
				.setExpiration(date);
		return jwtBuilder.signWith(SECRET_KEY).compact();
	}

	public static String parseJwtToken(String token) throws ExpiredJwtException {
		//根据算法签名解析 可能会解析失败
		Jwt parsedJwt = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parse(token);
		//获取用户信息
		Claims claims = (Claims) parsedJwt.getBody();
		return JSON.toJSONString(claims);
	}

	/**
	 * 将新的refresh_token和access_token更新到响应头中
	 * @param accessToken
	 * @param refreshToken
	 * @param response
	 * @return
	 */
	public static boolean refreshToken(String accessToken, String refreshToken, HttpServletResponse response) {
		try {
			Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parse(accessToken);
		} catch (ExpiredJwtException e) {
			Claims claims = e.getClaims();
			//获取access_token的过期时间
			long accessTokenExp = claims.getExpiration().getTime();
			long newAccessTokenExp = accessTokenExp + 3600000;
			claims.setExpiration(new Date(newAccessTokenExp));
			// 标头
			Map<String, Object> headMap = new HashMap<>(2);
			headMap.put("type", "JWT");
			headMap.put("alg", "HS256");
			String newAccessToken = Jwts.builder().setHeader(headMap).setClaims(claims).signWith(SECRET_KEY).compact();
			Jwt refreshTokenJwt = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parse(refreshToken);
			Claims body = (Claims) refreshTokenJwt.getBody();
			long refreshTokenExp = body.getExpiration().getTime();
			long newRefreshTokenExp = refreshTokenExp + 3600000;
			body.setExpiration(new Date(newRefreshTokenExp));
			String newRefreshToken = Jwts.builder().setHeader(headMap).setClaims(body).signWith(SECRET_KEY).compact();
			//注意：必须设置Access-Control-Expose-Headers响应头，用来将响应头数据暴露给前端
			response.setHeader("Access-Control-Expose-Headers","Authorization,RefreshToken");
			response.setHeader("Authorization", newAccessToken);
			response.setHeader("RefreshToken", newRefreshToken);
		}
		return true;
	}

	/**
	 * 解析JWT并存储用户信息到ThreadLocal
	 */
	public static boolean parseTokenAndStoreUser(String token) throws ExpiredJwtException {
		String claimsString = parseJwtToken(token);
		if (claimsString == null) {
			return false;
		}
		if (!"".equals(claimsString)) {
			User user = JSON.parseObject(claimsString, User.class);
			user.setPassword("");
			UserHolder.setCurrentUser(user);
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		Jwt parse = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
				.parse("eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJpZCI6MSwiZXhwIjoxNjg2OTkyNzg0fQ.WXtoXt917BHHSVtsgT5rVFLT-JA2oZGNph_kDn_WZgA");
		Claims body = (Claims) parse.getBody();
		System.out.println("token过期时间"+body.getExpiration());
	}

}
