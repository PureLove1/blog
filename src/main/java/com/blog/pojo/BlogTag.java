package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName blog_tag
 */
@TableName(value ="blog_tag")
@Data
public class BlogTag implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long tagId;

    /**
     * 
     */
    private Long blogId;

    /**
     * 
     */
    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 
     */
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("is_deleted")
    private Boolean deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}