package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.Question;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @author 贺畅
 * @Entity com.blog.pojo.Question
 */
@Repository
public interface QuestionMapper extends BaseMapper<Question> {
	ArrayList<String> getQuestionType();
}




