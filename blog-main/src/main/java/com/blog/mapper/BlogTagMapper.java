package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.BlogTag;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.blog.pojo.BlogTag
 */
@Repository
public interface BlogTagMapper extends BaseMapper<BlogTag> {
	int customDelete(Boolean deleted, Long blogId, Long tagId);
}




