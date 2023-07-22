package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.mapper.TagMapper;
import com.blog.pojo.Tag;
import com.blog.service.TagService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




