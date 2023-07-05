package com.blog.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
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
		//初始化线程池，
		//ArrayBlockingQueue是带有任务缓冲的，容量至少为1，
		//SynchronousQueue 是比较独特的队列，其本身是没有容量大小，比如我放一个数据到队列中，
		// 我是不能够立马返回的，我必须等待别人把我放进去的数据消费掉了，才能够返回。
		//为保证邮件发送的实时性，应当不使用缓冲队列
		//ArrayBlockingQueue queue = new ArrayBlockingQueue(1000);
		SynchronousQueue queue = new SynchronousQueue();
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
