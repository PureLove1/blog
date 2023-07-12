package com.blog.mapper;

import com.blog.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.blog.pojo.Tag
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {
	/**
	 * 根据博文id获取标签列表
	 * @param blogId
	 * @return
	 */
	List<Tag> getTagByBlogId(long blogId);
}




