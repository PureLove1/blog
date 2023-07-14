package com.blog;

import com.blog.common.UserHolder;
import com.blog.pojo.Blog;
import com.blog.pojo.User;
import com.blog.service.BlogService;
import com.blog.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1682033);
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8,
				8, 100000, TimeUnit.MILLISECONDS, queue);
		User byId = userService.getById(1);
		UserHolder.setCurrentUser(byId);
		for (int i = 0; i < 1682033; i++) {
			logger.error("{}创建成功", i + 1);
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
			Runnable runnable = () -> {
				UserHolder.setCurrentUser(byId);
				blogService.addBlog(blog);
			};
			threadPoolExecutor.execute(runnable);
		}
		;
		while (!threadPoolExecutor.isTerminated()) {
			Thread.sleep(10000);
		}
	}

	@Test
	void testTerminated() throws InterruptedException {

		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(Integer.MAX_VALUE);
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,
				1, 1000, TimeUnit.MILLISECONDS, queue, new ThreadPoolExecutor.CallerRunsPolicy());
		for (int i = 0; i < 20; i++) {
			int finalI = i;
			logger.error("{}创建成功", i + 1);
			Runnable runnable = () -> {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread() + "" + finalI);
			};
			threadPoolExecutor.execute(runnable);
		}
		;
		//threadPoolExecutor.shutdown();
//		System.out.println("--------------------shutdown被调用-----------------");
		//Thread.sleep(400);
		threadPoolExecutor.shutdownNow();
		System.out.println("--------------------shutdownNow被调用-----------------");
		threadPoolExecutor.execute(() -> {
			System.out.println("shudown后提交任务-----------------------------");
		});
		int j = 0;
		while (!threadPoolExecutor.isTerminated()) {

			System.out.println("循环" + j++);
			//Thread.sleep(10000);
		}

		threadPoolExecutor.execute(() -> {
			System.out.println("线程池关闭后提交任务-----------------------------");
		});
	}

	public static void main(String[] args) {
		OffsetDateTime offsetDateTime = LocalDateTime.now().atOffset(ZoneOffset.ofHours(8));
		System.out.println(offsetDateTime);
	}
}
