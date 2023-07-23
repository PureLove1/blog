package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.common.Result;
import com.blog.mapper.QuestionMapper;
import com.blog.pojo.Question;
import com.blog.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

	@Autowired
	private QuestionMapper questionMapper;

	@Override
	public Result getQuestionType() {
		return Result.ok(questionMapper.getQuestionType());
	}
}




