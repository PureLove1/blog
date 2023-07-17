package com.blog.controller;

import com.blog.common.Result;
import com.blog.constant.StatusCode;
import com.blog.service.UniqueVisitorService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.blog.constant.StatusCode.USER_REQUIRED_PARAMETER_IS_NULL_ERROR;
import static com.blog.constant.StatusCode.USER_WRONG_PARAMETER_ERROR;

/**
 * @Author PureLove1
 * @Date 2023/6/26
 */
@RestController
@RequestMapping("/uv")
public class UVController {

	@Autowired
	private UniqueVisitorService uniqueVisitorService;

	private static final Logger logger = LoggerFactory.getLogger(UVController.class);

	/**
	 * 单日uv统计
	 * @param date
	 * @return
	 */
	@ApiOperation("获取单日UV")
	@GetMapping
	public Result getDateUV(@Param("date") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date date) {
		if (date == null) {
			return Result.error("必填参数不得为空", USER_REQUIRED_PARAMETER_IS_NULL_ERROR);
		}
		if (date.after(new Date())) {
			return Result.error("错误的日期参数", USER_WRONG_PARAMETER_ERROR);
		}
		return uniqueVisitorService.getUV(date);
	}

	/**
	 * 范围uv统计
	 * @param start
	 * @param end
	 * @return
	 */
	@ApiOperation("获取日期范围UV")
	@GetMapping("/range")
	public Result getRangeUV(@Param("start") Date start, @Param("end") Date end) {
		if (start == null || end == null || end.before(start) || end.after(new Date())) {
			return Result.error("非法的参数", USER_WRONG_PARAMETER_ERROR);
		}
		return uniqueVisitorService.getRangeUV(start, end);
	}

}
