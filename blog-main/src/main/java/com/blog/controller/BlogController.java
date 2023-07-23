package com.blog.controller;

import com.blog.annotation.HasAnyRole;
import com.blog.common.Result;
import com.blog.pojo.Blog;
import com.blog.service.BlogService;
import com.blog.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static com.blog.constant.StatusCode.USER_REQUIRED_PARAMETER_IS_NULL_ERROR;
import static com.blog.constant.UserRole.ROLE_VIP;
import static com.blog.util.StringUtil.isNotBlank;

/**
 * @author 贺畅
 * @date 2023/6/21
 */
@Api("博文管理")
@RestController
@RequestMapping("/blog")
public class BlogController {

	@Autowired
	private BlogService blogService;

	private static AtomicInteger count = new AtomicInteger(1);

	/**
	 * WITH_ID和WITHOUT_ID用于标识Blog对象是否携带ID进行判断
	 */
	private static final Integer WITH_ID = 1;
	private static final Integer WITHOUT_ID = null;
	/**
	 * 最小单页大小
	 */
	private static final int MIN_PAGE_SIZE = 1;

	/**
	 * 最大单页大小
	 */
	private static final int MAX_PAGE_SIZE = 100;

	/**
	 * 最小页码
	 */
	private static final int MIN_PAGE = MIN_PAGE_SIZE;

	private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

	/**
	 * @param blog
	 * @return
	 */
	@ApiOperation("博文发布")
	@PostMapping
	@HasAnyRole(ROLE_VIP)
	public Result addBlog(@RequestBody Blog blog) {
		if (Util.isNotNull(blog, WITHOUT_ID)) {
			return blogService.addBlog(blog);
		} else {
			return Result.error("必填数据不得为空");
		}
	}

	/**
	 * @param blog
	 * @return
	 */
	@ApiOperation("更新博文")
	@PutMapping
	@HasAnyRole(ROLE_VIP)
	public Result updateBlog(@RequestBody Blog blog) {
		if (!Util.isNotNull(blog, WITH_ID)) {
			return Result.error("参数错误，必填参数不得为空");
		}
		return blogService.updateBlog(blog);
	}

	/**
	 * @param pageSize
	 * @param currentPage
	 * @param startTime
	 * @return
	 */
	@ApiOperation("获取博文列表分页")
	@GetMapping
	public Result getBlogList(@RequestParam("pageSize") Long pageSize,
	                          @RequestParam("currentPage") Long currentPage,
	                          @RequestParam("startTime") String startTime) {
		if (pageSize == null || pageSize < MIN_PAGE_SIZE || currentPage == null || currentPage < MIN_PAGE || startTime == null) {
			return Result.error("错误的分页参数");
		}
		LocalDateTime parse = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return blogService.getBlogList(currentPage, pageSize, parse);
	}

	/**
	 * @param id
	 * @return
	 */
	@ApiOperation("根据id查询博文")
	@GetMapping("/{id}")
	public Result getBlogById(@PathVariable String id) {
		if (id == null) {
			return Result.error("错误的参数");
		}
		try {
			long l = Long.parseLong(id);
			if (l <= 0) {
				return Result.error("错误的参数");
			}
			return blogService.getBlogById(l);
		} catch (NumberFormatException e) {
			return Result.error("错误的参数");
		}
	}


	/**
	 * @param tag
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@ApiOperation("根据标签分页查询博文")
	@GetMapping("/tag")
	public Result getBlogByTag(@RequestParam("tag") String tag, @RequestParam("pageSize") Integer pageSize,
	                           @RequestParam("currentPage") Integer currentPage) {
		if (!StringUtil.isNotBlank(tag)) {
			return Result.error("错误的参数");
		}
		if (pageSize == null || pageSize < MIN_PAGE_SIZE || currentPage == null || currentPage < MIN_PAGE) {
			return Result.error("错误的分页参数");
		}
		return blogService.getBlogByTag(tag, pageSize, currentPage);
	}

	/**
	 * @param title
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@ApiOperation("根据标题分页查询博文")
	@GetMapping("/title")
	public Result getBlogByTitle(@RequestParam("title") String title, @RequestParam("pageSize") Integer pageSize,
	                             @RequestParam("currentPage") Integer currentPage) {
		if (!StringUtil.isNotBlank(title)) {
			return Result.error("错误的参数");
		}
		if (pageSize == null || pageSize < MIN_PAGE_SIZE || currentPage == null || currentPage < MIN_PAGE) {
			return Result.error("错误的分页参数");
		}
		return blogService.getBlogByTitle(title, pageSize, currentPage);
	}

	/**
	 * @param pageSize
	 * @return
	 */
	@ApiOperation("查询最新博文标题")
	@GetMapping("/newest")
	public Result getNewestTitle(@RequestParam("pageSize") Integer pageSize) {
		if (pageSize < MIN_PAGE_SIZE) {
			return Result.error("错误的分页大小");
		} else if (pageSize > MAX_PAGE_SIZE) {
			//超出100页返回空数据
			return Result.ok();
		}
		return blogService.getNewestTitle(pageSize);
	}

	/**
	 * @param blogId
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@ApiOperation("根据合集分页查询博文")
	@GetMapping("/collection")
	public Result getBlogListByCollection(@RequestParam("blogId") Long blogId, @RequestParam("pageSize") Integer pageSize,
	                                      @RequestParam("currentPage") Integer currentPage) {
		if (pageSize == null || pageSize < MIN_PAGE_SIZE || currentPage == null || currentPage < MIN_PAGE) {
			return Result.error("错误的分页参数");
		}
		if (blogId == null) {
			return Result.error("错误的参数", USER_REQUIRED_PARAMETER_IS_NULL_ERROR);
		}
		return blogService.getBlogListByCollection(blogId, pageSize, currentPage);
	}

	private static class Util{
		/**
		 * 用于判断博文对象是否为null
		 * @param blog
		 * @return
		 */
		private static boolean isNotNull(Blog blog, Integer mode) {
			if (blog == null) {
				return false;
			}
			if (blog.getCollection() != null && blog.getCollection()) {
				if (!isNotBlank(blog.getCollectionName())) {
					return false;
				}
			}
			if (mode == null) {
				return blog.getTags() != null && blog.getContent() != null && blog.getTitle() != null;
			}
			return blog.getTags() != null && blog.getContent() != null && blog.getTitle() != null && blog.getId() != null;
		}

	}
}
