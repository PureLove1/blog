package com.blog.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author PureLove1
 * @date 2022/12/20
 */
public class PasswordUtil {
	private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

	/**
	 * 密码校验
	 *
	 * @param password
	 * @param salt
	 * @param encodedPassword
	 * @return
	 */
	public static boolean validatePassword(String password, String salt, String encodedPassword)
			throws NoSuchAlgorithmException {
		logger.info(password);
		logger.info(salt);
		byte[] bytes = (password + salt).getBytes(UTF_8);
		MessageDigest sha = MessageDigest.getInstance("SHA");
		sha.update(bytes);
		byte[] resultBytes = sha.digest();
		String encode = new String(resultBytes,UTF_8);
		return encode.equals(encodedPassword);

	}

	/**
	 * 密码加盐加密
	 * @param password
	 * @param salt
	 * @return
	 */
	private static String encodePassword(String password, String salt) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance("SHA");
		String info = password + salt;
		byte[] srcBytes = info.getBytes(UTF_8);
		//使用srcBytes更新摘要
		sha.update(srcBytes);
		//完成哈希计算，得到result
		byte[] resultBytes = sha.digest();
		return new String(resultBytes, UTF_8);
	}

	/**
	 * 获取随机盐
	 *
	 * @return
	 */
	public static String getSalt() {
		return RandomStringUtils.random(5);
	}
}
