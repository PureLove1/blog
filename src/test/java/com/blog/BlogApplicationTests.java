package com.blog;

import com.blog.common.UserHolder;
import com.blog.pojo.Blog;
import com.blog.pojo.User;
import com.blog.service.BlogService;
import com.blog.service.UserService;
import com.blog.util.RandomCodeUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

import static com.blog.util.RandomCodeUtil.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BlogApplicationTests {

	@Autowired
	private BlogService blogService;
	@Autowired
	private UserService userService;
	private static final Logger logger = LoggerFactory.getLogger(BlogApplicationTests.class);

	@Test
	void contextLoads() {
	}

	@Test
	void insertOneMilionData() throws InterruptedException {
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1000000);
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 50, 1000, TimeUnit.MILLISECONDS, queue);
		User byId = userService.getById(1);
		UserHolder.setCurrentUser(byId);
		for (int i = 0; i < 66182; i++) {
			int finalI = i;
			Blog blog = new Blog();
			String userName = byId.getUserName();
			blog.setUserName(userName);
			blog.setUserId(byId.getId());
			boolean b = randomBoolean();
			blog.setCollection(b);
			if (b) {
				//是合集
				blog.setCollectionName(randomCollectionName());
			}
			blog.setContent(getRandomChineseString(21));
			blog.setDescription(getRandomChineseString(20));
			blog.setTags(getRandomTags());
			blog.setTitle(getRandomChineseString(20));
			Runnable runnable=()->{
				UserHolder.setCurrentUser(byId);
				blogService.addBlog(blog);
			};
			threadPoolExecutor.execute(runnable);
		};
		while(!threadPoolExecutor.isTerminated()){
			Thread.sleep(10000);
		}
		logger.info("任务执行结束");
	}
}
