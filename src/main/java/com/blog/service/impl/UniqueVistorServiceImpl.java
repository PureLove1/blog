package com.blog.service.impl;

import com.blog.common.Result;
import com.blog.constant.RedisKeyPrefix;
import com.blog.service.UniqueVisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.blog.constant.RedisKeyPrefix.DATE_UV;
import static com.blog.constant.RedisKeyPrefix.RANGE_UV;

/**
 * @Author PureLove1
 * @Date 2023/6/26
 */
@Service
public class UniqueVistorServiceImpl implements UniqueVisitorService {

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 获取范围uv redisKey
	 * @param start
	 * @param end
	 * @return
	 */
	private String getRangeUVKey(Date start, Date end) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String startStr = simpleDateFormat.format(start);
		String endStr = simpleDateFormat.format(end);
		String rangeUVKey = RANGE_UV + startStr + "_" + endStr;
		return rangeUVKey;
	}

	/**
	 * 获取日uv redisKey
	 * @param date
	 * @return
	 */
	private String getDateUVKey(Date date) {
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String dateStr = simpleDateFormat.format(date);
		String dateUVKey = DATE_UV + dateStr;
		return dateUVKey;
	}

	/**
	 * 添加uv记录
	 * @param remoteAddr
	 */
	@Override
	public void addVisitRecord(String remoteAddr) {
		String dateUVKey = getDateUVKey(null);
		redisTemplate.opsForHyperLogLog().add(dateUVKey, remoteAddr);
	}

	/**
	 * 获取日uv
	 * @param date
	 * @return
	 */
	@Override
	public Result getUV(Date date) {
		Long size = redisTemplate.opsForHyperLogLog().size(getDateUVKey(date));
		return Result.ok(size);
	}

	/**
	 * 获取范围uv
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public Result getRangeUV(Date start, Date end) {
		List<String> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		//设置起始日期时间
		calendar.setTime(start);
		while (!calendar.getTime().after(end)) {
			String data = getDateUVKey(calendar.getTime());
			list.add(data);
			calendar.add(Calendar.DATE, 1);
		}
		//合并数据
		String redisKey = getRangeUVKey(start,end);
		redisTemplate.opsForHyperLogLog().union(redisKey, list.toArray());
		Long size = redisTemplate.opsForHyperLogLog().size(redisKey);
		//返回统计结果
		return Result.ok(size);
	}
}
