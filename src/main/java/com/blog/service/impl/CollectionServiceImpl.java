package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.annotation.HasAnyRole;
import com.blog.common.Result;
import com.blog.common.UserHolder;
import com.blog.pojo.Collection;
import com.blog.pojo.User;
import com.blog.service.CollectionService;
import com.blog.mapper.CollectionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.blog.constant.UserRole.ROLE_VIP;

/**
 *
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection>
    implements CollectionService{

	@Override
	@HasAnyRole(ROLE_VIP)
	public Result listAll() {
		User currentUser = UserHolder.getCurrentUser();
		if (currentUser==null){
			return Result.error("用户尚未登陆");
		}
		List<Collection> list = list(new LambdaQueryWrapper<Collection>().eq(Collection::getUserId,currentUser.getId()));
		return Result.ok(list);
	}
}




