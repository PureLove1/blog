package com.blog.service;

import com.blog.common.Result;

import java.util.Date;

/**
 * @Author PureLove1
 * @Date 2023/6/26
 */
public interface UniqueVisitorService {

	void addVisitRecord(String remoteAddr);

	Result getUV(Date date);

	Result getRangeUV(Date start, Date end);
}
