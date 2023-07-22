package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.pojo.BlogTag;

/**
 *
 */
public interface BlogTagService extends IService<BlogTag> {
	int customDelete(Boolean deleted,Long blogId,Long tagId);
}
