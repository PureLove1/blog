package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.common.Result;
import com.blog.pojo.Collection;

/**
 *
 */
public interface CollectionService extends IService<Collection> {

	Result listAll();
}
