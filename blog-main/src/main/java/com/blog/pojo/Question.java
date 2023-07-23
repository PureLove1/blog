package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName question
 */
@TableName(value ="question")
@Data
public class Question implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    private String question;

    private String answer;

    private String cone;

    private String ctwo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}