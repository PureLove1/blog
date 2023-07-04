package com.blog.util;

import com.blog.pojo.Tag;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author PureLove1
 * @author 贺畅
 */
public class RandomCodeUtil {

	/**
	 * 生成随机验证码
	 *
	 * @return
	 */
	public static String randomCode() {
		// 避免 Random 实例被多线程使用，虽然共享该实例是线程安全的，但会因竞争同一 seed导致的性能下降
		int i = ThreadLocalRandom.current().nextInt(0, 999999);
		return String.valueOf(i);
	}

	/**
	 * 生成随机验证码
	 *
	 * @return
	 */
	public static boolean randomBoolean() {
		int i = ThreadLocalRandom.current().nextInt(0, 1);
		return i == 0;
	}

	/***
	 * 生成固定长度随机中文，kuojung
	 * @param n 中文个数
	 * @return 中文串
	 */
	public static String getRandomChineseString(int n) {
		if (n > 20) {
			n = ThreadLocalRandom.current().nextInt(100, 200);
		}
		String zh_cn = "";
		String str = "";
		// Unicode中汉字所占区域\u4e00-\u9fa5,将4e00和9fa5转为10进制
		int start = Integer.parseInt("4e00", 16);
		int end = Integer.parseInt("9fa5", 16);

		for (int ic = 0; ic < n; ic++) {
			// 随机值
			int code = (new Random()).nextInt(end - start + 1) + start;
			// 转字符
			str = new String(new char[]{(char) code});
			zh_cn = zh_cn + str;
		}
		return zh_cn;
	}


	public static List<Tag> getRandomTags() {
		String content = "";
		int i = ThreadLocalRandom.current().nextInt(0, 15);
		switch (i) {
			case 0:
				content = "Java";
				break;
			case 1:
				content = "Spring";
				break;
			case 2:
				content = "Spring Boot";
				break;
			case 3:
				content = "MySQL";
				break;
			case 4:
				content = "Redis";
				break;
			case 5:
				content = "Maven";
				break;
			case 6:
				content = "Rabbit MQ";
				break;
			case 7:
				content = "TCP";
				break;
			case 8:
				content = "Linux";
				break;
			case 9:
				content = "Git";
				break;
			case 10:
				content = "JDBC";
				break;
			case 11:
				content = "Spring Cloud";
				break;
			case 12:
				content = "异常处理";
				break;
			case 13:
				content = "幂等性";
				break;
			case 14:
				content = "SpringMVC";
				break;
			case 15:
				content = "测试";
				break;
			default:
				break;
		}
		Tag tag = new Tag();
		tag.setContent(content);
		ArrayList<Tag> tags = new ArrayList<>(1);
		tags.add(tag);
		return tags;
	}

	/**
	 * 生成随机合集
	 *
	 * @return
	 */
	public static String randomCollectionName() {
		int i = ThreadLocalRandom.current().nextInt(0, 15);
		switch (i) {
			case 0:
				return "Java虚拟机";
			case 1:
				return "SpringMVC面试题";
			case 2:
				return "Java面试";
			case 3:
				return "MySQL面试题";
			case 4:
				return "SQL查询优化";
			case 5:
				return "JVM";
			case 6:
				return "Redis";
			case 7:
				return "MongoDB";
			case 8:
				return "Spring";
			case 9:
				return "VUE";
			case 10:
				return "ElementUI";
			case 11:
				return "Axios";
			case 12:
				return "Docker";
			case 13:
				return "Seata";
			case 14:
				return "RabbitMQ";
			case 15:
				return "Kafka";
			default:
				return "Java面试";
		}
	}

}
