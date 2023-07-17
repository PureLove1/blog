package com.blog.controller;

import com.blog.annotation.HasAnyRole;
import com.blog.common.Result;
import com.blog.service.CollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.blog.constant.UserRole.ROLE_VIP;

/**
 * @Author PureLove1
 * @Date 2023/6/23
 */
@Api("合集管理")
@RestController
@RequestMapping("/collection")
public class CollectionController {
	private static final Logger logger = LoggerFactory.getLogger(CollectionController.class);

	@Autowired
	private CollectionService collectionService;

	@ApiOperation("查询用户创建的全部合集")
	@GetMapping
	@HasAnyRole(ROLE_VIP)
	public Result listAll(){
		return collectionService.listAll();
	}

}
