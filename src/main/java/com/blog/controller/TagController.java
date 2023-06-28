package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.common.Result;
import com.blog.pojo.Tag;
import com.blog.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 贺畅
 * @date 2023/6/21
 */
@RestController
@RequestMapping("/tag")
public class TagController {
	private static final Logger logger = LoggerFactory.getLogger(TagController.class);

	@Autowired
	private TagService tagService;

	@GetMapping
	public Result listAll(){
		return Result.ok(tagService.list());
	}

	/**
	 * 创建标签
	 * @param tag
	 * @return
	 */
	@PostMapping
	public Result addTag(@RequestBody Tag tag){
		int count = tagService.count(new LambdaQueryWrapper<Tag>().eq(Tag::getContent, tag.getContent()));
		if (count>0){
			return Result.error("该标签已存在");
		}
		if (tagService.save(tag)) {
			logger.info("标签创建{}",tag);
			return Result.ok("创建标签成功",tag);
		}
		return Result.error("创建标签失败");
	}
}
