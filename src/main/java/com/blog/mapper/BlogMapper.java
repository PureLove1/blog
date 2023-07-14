package com.blog.mapper;

import com.blog.pojo.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Entity com.blog.pojo.Blog
 * @author
 */
@Repository
public interface BlogMapper extends BaseMapper<Blog> {

	List<Blog> getBlogList(Long start, Long end, LocalDateTime startTime);

	List<Blog> getBlogListByIds(List<Long> ids,String title,Integer start,Integer pageSize);
}




