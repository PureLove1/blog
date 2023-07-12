package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 博客合集关联表
 * @author 贺畅
 * @TableName blog_collection
 */
@TableName(value ="blog_collection")
@Data
public class BlogCollection implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 博客id
     */
    private Long blogId;

    /**
     * 合集id
     */
    private Long collectionId;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}