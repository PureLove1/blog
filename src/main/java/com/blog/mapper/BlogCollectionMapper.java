package com.blog.mapper;

import com.blog.pojo.BlogCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author 贺畅
 * @Entity com.blog.pojo.BlogCollection
 */
@Repository
public interface BlogCollectionMapper extends BaseMapper<BlogCollection> {

	@Update("update blog_collection set is_deleted=#{deleted} where blog_id=#{id}")
	boolean customDelete(Long id, boolean deleted);
}




