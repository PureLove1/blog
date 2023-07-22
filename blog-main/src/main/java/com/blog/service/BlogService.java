package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.common.Result;
import com.blog.pojo.Blog;

import java.time.LocalDateTime;

/**
 *
 */
public interface BlogService extends IService<Blog> {

	void updateViewNum(Long id);

	Result addBlog(Blog blog);

	Result getBlogList(Long currentPage, Long pageSize, LocalDateTime startTime);

	Result getBlogById(Long id);

	Result getBlogByTag(String tag,Integer pageSize,Integer currentPage);

	Result getNewestTitle(Integer pageSize);

	Result getBlogListByCollection(Long blogId,Integer pageSize,Integer currentPage);

	Result getBlogByTitle(String title, Integer pageSize, Integer currentPage);

	Result updateBlog(Blog blog);
}
