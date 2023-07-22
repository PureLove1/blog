package com.blog.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 贺畅
 * @date 2022/12/23
 */
@Component
public class MybatisPlusObjectHandler implements MetaObjectHandler {

	/**
	 * 插入操作，自动填充
	 * @param metaObject
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		//需要填充的属性名    填充的值
		// 由于各个对象的属性值不同，在填充前需要对源对象是否具有该属性进行判断
		// 否则找不到对应的setter方法mybatis-plus会报错
		if (metaObject.hasSetter("createTime")) {
			metaObject.setValue("createTime", LocalDateTime.now());
		}
		if (metaObject.hasSetter("updateTime")){
			metaObject.setValue("updateTime", LocalDateTime.now());
		}
		if (metaObject.hasSetter("viewNum")) {
			metaObject.setValue("viewNum", 0);
		}
	}

	/**
	 * 更新操作，自动填充
	 *
	 * @param metaObject
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		if (metaObject.hasSetter("updateTime")) {
			metaObject.setValue("updateTime", LocalDateTime.now());
		}
	}
}
