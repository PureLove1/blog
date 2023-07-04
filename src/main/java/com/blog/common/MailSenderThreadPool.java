package com.blog.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author PureLove1
 * @Date 2023/7/4
 */
public class MailSenderThreadPool {
	private static final Logger logger = LoggerFactory.getLogger(MailSenderThreadPool.class);
	private static ThreadPoolExecutor mailSenderExecutor;

	static {
		//初始化线程池
		ArrayBlockingQueue queue = new ArrayBlockingQueue(1000);
		mailSenderExecutor = new ThreadPoolExecutor(4,
				8,
				5,
				TimeUnit.SECONDS,
				queue,
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static void addSendMailTask(Runnable task){
		logger.info("邮件发送任务已提交");
		mailSenderExecutor.execute(task);
	}
}
