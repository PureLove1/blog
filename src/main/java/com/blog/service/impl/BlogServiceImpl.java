package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.common.Result;
import com.blog.common.UserHolder;
import com.blog.common.exception.CustomException;
import com.blog.constant.StatusCode;
import com.blog.mapper.*;
import com.blog.pojo.*;
import com.blog.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.blog.constant.RedisKeyPrefix.BLOG_CACHE;

/**
 * 博客业务实现类
 * @author 贺畅
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
		implements BlogService {
	private static final Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

	@Autowired
	private CollectionMapper collectionMapper;

	@Autowired
	private BlogMapper blogMapper;

	@Autowired
	private TagMapper tagMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private BlogTagMapper blogTagMapper;

	@Autowired
	private BlogCollectionMapper blogCollectionMapper;

	@Autowired
	private UserMapper userMapper;

	/**
	 * 博客发布
	 * @param blog
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result addBlog(Blog blog) {
		User currentUser = UserHolder.getCurrentUser();
		if (currentUser == null) {
			return Result.error("用户尚未登陆", StatusCode.USER_LOGIN_ERROR);
		}
		List<Tag> tags = blog.getTags();
		for (int i = 0; i < tags.size(); i++) {
			Tag tag = tags.get(i);
			//查询标签是否存在
			Tag tag1 = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getContent, tag.getContent()));
			//标签不存在
			if (tag1 == null) {
				//插入标签
				int insert = tagMapper.insert(tag);
				tags.set(i, tag);
				logger.info("插入后的tag{}", tag);
				//标签创建失败抛出异常
				if (insert != 1) {
					throw new CustomException("标签创建失败");
				}
			}
			tags.set(i, tag1);
		}
		//查询登录用户信息
		User user = userMapper.selectById(currentUser.getId());
		blog.setUserId(user.getId());
		blog.setUserName(user.getUserName());
		if (blog.getCollection()) {
			//是集合
			Collection collection = new Collection();
			collection.setName(blog.getCollectionName());
			collection.setUserId(currentUser.getId());
			//检查集合是否存在
			Collection collection1 = collectionMapper.selectOne(
					new LambdaQueryWrapper<Collection>()
							.eq(Collection::getUserId, collection.getUserId())
							.eq(Collection::getName, collection.getName()));
			//集合不存在
			if (collection1 == null) {
				//插入集合
				int insert = collectionMapper.insert(collection);
				if (insert != 1) {
					throw new CustomException("博客合集关联创建失败");
				}
			}
			if (!save(blog)) {
				throw new CustomException("博客插入失败");
			}
			//设置博客集合关联
			BlogCollection blogCollection = new BlogCollection();
			Long id = blog.getId();
			blogCollection.setBlogId(id);
			//如果集合已存在就使用原来集合的id
			blogCollection.setCollectionId(
					collection.getId() == null ? collection1.getId() : collection.getId());
			if (blogCollectionMapper.insert(blogCollection) != 1) {
				throw new CustomException("博客合集关联插入失败");
			}
		}
		//通过id判断是否完成了博客的插入
		if (blog.getId() == null) {
			if (!save(blog)) {
				throw new CustomException("博客发布失败");
			}
		}
		//最后插入博客和标签的关联
		for (Tag tag : tags) {
			BlogTag blogTag = new BlogTag();
			blogTag.setBlogId(blog.getId());
			blogTag.setTagId(tag.getId());
			int insert = blogTagMapper.insert(blogTag);
			if (insert != 1) {
				throw new CustomException("博客发布失败");
			}
		}
		return Result.ok("发布成功", blog.getId());
	}

	/**
	 * 查询博文列表
	 * @param currentPage
	 * @param pageSize
	 * @param startTime
	 * @return
	 */
	@Override
	public Result getBlogList(Long currentPage, Long pageSize, LocalDateTime startTime) {
		currentPage = (currentPage - 1) * pageSize;
		List<Blog> blogList = blogMapper.getBlogList(currentPage, pageSize, startTime);
		for (Blog blog : blogList) {
			List<Tag> tags = blog.getTags();
		}
		return Result.ok(blogList);
	}

	/**
	 * 根据id查询博文
	 *
	 * @param id
	 * @return
	 */
	@Override
	public Result getBlogById(Long id) {
		//查询博客缓存
		Object o = redisTemplate.opsForValue().get(BLOG_CACHE + id);
		if (o != null) {
			Blog cachedBlog = (Blog) o;
			//添加访问数
			updateViewNum(id);
			return Result.ok(cachedBlog);
		}
		//缓存未命中，查询数据库并添加缓存
		Blog byId = getById(id);
		if (byId == null) {
			return Result.error("博文不存在");
		}
		redisTemplate.opsForValue().set(BLOG_CACHE + id, byId, 30, TimeUnit.MINUTES);
		updateViewNum(id);
		return Result.ok(byId);
	}

	/**
	 * 通过标签查询博文
	 *
	 * @param tag
	 * @return
	 */
	@Override
	public Result getBlogByTag(String tag, Integer pageSize, Integer currentPage) {
		//查询对应内容的标签
		Tag tagObj = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getContent, tag));
		if (tagObj == null) {
			return Result.ok();
		}
		Long id = tagObj.getId();
		//获取标签后查询标签关联
		List<BlogTag> blogTags = blogTagMapper.selectList(
				new LambdaQueryWrapper<BlogTag>()
						.eq(BlogTag::getTagId, id)
						.last("limit " + pageSize * (currentPage - 1) + "," + pageSize));
		if (blogTags == null || blogTags.isEmpty()) {
			return Result.ok();
		}
		//通过中间表blog_tag得到博客id
		ArrayList<Long> blogIdList = new ArrayList<>();
		for (BlogTag blogTag : blogTags) {
			blogIdList.add(blogTag.getBlogId());
		}
		//查询对应博客信息
		List<Blog> blogListByIds = blogMapper.getBlogListByIds(blogIdList,null,null,null);
		if (blogListByIds == null || blogListByIds.isEmpty()) {
			return Result.ok();
		}
		return Result.ok(blogListByIds);
	}

	/**
	 * 查询最新博客标题
	 * @param pageSize
	 * @return
	 */
	@Override
	public Result getNewestTitle(Integer pageSize) {
		List<Blog> list = list(new LambdaQueryWrapper<Blog>()
				.select(Blog::getId, Blog::getTitle)
				.orderByDesc(Blog::getCreateTime).last("limit " + pageSize));
		return Result.ok(list);
	}

	/**
	 * 根据合集查询博客列表
	 * @param blogId
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@Override
	public Result getBlogListByCollection(Long blogId, Integer pageSize, Integer currentPage) {
		//查询博客id是否为合集，是的话返回博客合集关联
		BlogCollection blogCollection = blogCollectionMapper.selectOne(new LambdaQueryWrapper<BlogCollection>().eq(BlogCollection::getBlogId, blogId));
		if (blogCollection == null) {
			return Result.ok();
		}
		//通过博客合集关联得到合集id，并通过合集id得到合集对应的所有博客id
		List<BlogCollection> blogCollections = blogCollectionMapper.selectList(new LambdaQueryWrapper<BlogCollection>()
				.eq(BlogCollection::getCollectionId, blogCollection.getCollectionId())
				.last("limit " + pageSize * (currentPage - 1) + "," + pageSize));
		if (blogCollections == null || blogCollections.isEmpty()) {
			return Result.ok();
		}
		ArrayList<Long> ids = new ArrayList<>(pageSize);
		for (BlogCollection bc : blogCollections) {
			ids.add(bc.getBlogId());
		}
		//根据博客id查询列表
		List<Blog> blogListByIds = blogMapper.getBlogListByIds(ids,null,null,null);
		return Result.ok(blogListByIds);
	}

	/**
	 * 标题模糊匹配查询
	 * @param title
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@Override
	public Result getBlogByTitle(String title, Integer pageSize, Integer currentPage) {
		//使用右模糊匹配，避免索引失效
		List<Blog> blogListByIds = blogMapper.getBlogListByIds(
				null,title,(currentPage - 1) * pageSize,pageSize);
		if (blogListByIds == null || blogListByIds.isEmpty()) {
			return Result.ok();
		}
		return Result.ok(blogListByIds);
	}

	/**
	 * 异步修改浏览量
	 * @param id
	 */
	@Async
	public void updateViewNum(Long id) {
		while (true) {
			UpdateWrapper<Blog> wrapper = new UpdateWrapper<Blog>()
					.eq("id", id).setSql("view_num = view_num+1");
			int update = blogMapper.update(null, wrapper);
			if (update == 1) {
				return;
			}
		}

	}

}




