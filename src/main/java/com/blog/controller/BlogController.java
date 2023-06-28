package com.blog.controller;

import com.blog.annotation.HasAnyRole;
import com.blog.common.Result;
import com.blog.constant.StatusCode;
import com.blog.constant.UserRole;
import com.blog.pojo.Blog;
import com.blog.service.BlogService;
import com.blog.util.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.blog.constant.StatusCode.USER_REQUIRED_PARAMETER_IS_NULL_ERROR;
import static com.blog.constant.UserRole.ROLE_VIP;
import static com.blog.util.StringUtil.isNotBlank;
import static java.lang.Boolean.TRUE;

/**
 * @author 贺畅
 * @date 2023/6/21
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
	@Autowired
	private BlogService blogService;

	private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

	/**
	 * 博文发布
	 *
	 * @param blog
	 * @return
	 */
	@PostMapping
	@HasAnyRole(ROLE_VIP)
	public Result addBlog(@RequestBody Blog blog) {
		if (isNotNull(blog)) {
			return blogService.addBlog(blog);
		} else {
			return Result.error("必填数据不得为空");
		}
	}

	/**
	 * 博文列表分页查询
	 *
	 * @param pageSize
	 * @param currentPage
	 * @param startTime
	 * @return
	 */
	@GetMapping
	public Result getBlogList(@Param("pageSize") Long pageSize,
	                          @Param("currentPage") Long currentPage,
	                          @Param("startTime") String startTime) {
		LocalDateTime parse = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		logger.info(parse.toString());
		if (pageSize == null || pageSize < 1 || currentPage == null || currentPage < 1) {
			return Result.error("错误的分页参数");
		}
		return blogService.getBlogList(currentPage, pageSize, parse);
	}

	/**
	 * 根据id查询博文
	 *
	 * @param id
	 * @param request
	 * @return
	 */
	@GetMapping("/{id}")
	public Result getBlogById(@PathVariable String id, HttpServletRequest request) {
		try {
			long l = Long.parseLong(id);
			if (l <= 0) {
				return Result.error("错误的参数");
			}
			return blogService.getBlogById(l);
		} catch (NumberFormatException e) {
			logger.warn("{}", e);
			return Result.error("错误的参数");
		}
	}

	/**
	 * 用于判断博文对象是否为null
	 *
	 * @param blog
	 * @return
	 */
	private boolean isNotNull(Blog blog) {
		if (blog == null) {
			return false;
		}
		if (blog.getCollection() != null && blog.getCollection()) {
			if (!isNotBlank(blog.getCollectionName())) {
				return false;
			}
		}
		return blog.getTags() != null && blog.getContent() != null && blog.getTitle() != null;
	}

	/**
	 * 根据标签分页查询
	 *
	 * @param tag
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@GetMapping("/tag")
	private Result getBlogByTag(@Param("tag") String tag, @Param("pageSize") Integer pageSize,
	                            @Param("currentPage") Integer currentPage) {
		if (!StringUtil.isNotBlank(tag)) {
			return Result.error("错误的参数");
		}
		if (pageSize == null || pageSize < 1 || currentPage == null || currentPage < 1) {
			return Result.error("错误的分页参数");
		}
		return blogService.getBlogByTag(tag, pageSize, currentPage);
	}

	/**
	 * 根据标题分页查询
	 *
	 * @param title
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@GetMapping("/title")
	private Result getBlogByTitle(@Param("title") String title, @Param("pageSize") Integer pageSize,
	                            @Param("currentPage") Integer currentPage) {
		if (!StringUtil.isNotBlank(title)) {
			return Result.error("错误的参数");
		}
		if (pageSize == null || pageSize < 1 || currentPage == null || currentPage < 1) {
			return Result.error("错误的分页参数");
		}
		return blogService.getBlogByTitle(title, pageSize, currentPage);
	}

	/**
	 * 查询最新博文列表
	 *
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/newest")
	private Result getNewestTitle(@Param("pageSize") Integer pageSize) {
		if (pageSize > 100 || pageSize < 1) {
			return Result.error("错误的展现数量");
		}
		return blogService.getNewestTitle(pageSize);

	}

	@GetMapping("/collection")
	public Result getBlogListByCollection(@Param("blogId") Long blogId, @Param("pageSize") Integer pageSize,
	                                      @Param("currentPage") Integer currentPage) {
		if (pageSize == null || pageSize < 1 || currentPage == null || currentPage < 1) {
			return Result.error("错误的分页参数");
		}
		if (blogId == null) {
			return Result.error("错误的参数", USER_REQUIRED_PARAMETER_IS_NULL_ERROR);
		}
		return blogService.getBlogListByCollection(blogId, pageSize, currentPage);
	}
}
