package com.blog.service;

import com.blog.common.Result;
import com.blog.pojo.Collection;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface CollectionService extends IService<Collection> {

	Result listAll();
}
