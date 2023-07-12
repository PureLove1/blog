package com.blog.service;

import com.blog.pojo.BlogTag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface BlogTagService extends IService<BlogTag> {
	int customDelete(Boolean deleted,Long blogId,Long tagId);
}
