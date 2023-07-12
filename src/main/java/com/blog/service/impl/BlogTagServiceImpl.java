package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.pojo.BlogTag;
import com.blog.service.BlogTagService;
import com.blog.mapper.BlogTagMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag>
    implements BlogTagService{

	@Override
	public int customDelete(Boolean deleted, Long blogId, Long tagId) {
		return 0;
	}
}




