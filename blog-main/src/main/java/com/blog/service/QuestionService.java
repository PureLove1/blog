package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.common.Result;
import com.blog.pojo.Question;

/**
 *
 */
public interface QuestionService extends IService<Question> {
	Result getQuestionType();
}
