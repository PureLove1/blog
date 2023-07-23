package com.blog.controller;

import com.blog.common.Result;
import com.blog.service.QuestionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author PureLove1
 * @Date 2023/7/14
 */
@Api("测试管理")
@RequestMapping("/test")
@RestController
public class TestController {

	@Autowired
	private QuestionService questionService;

	@GetMapping("/type")
	public Result getByType(){
		return questionService.getQuestionType();
	}

}
