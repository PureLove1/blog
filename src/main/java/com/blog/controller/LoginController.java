package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.common.JwtClaims;
import com.blog.common.MailSenderThreadPool;
import com.blog.common.Result;
import com.blog.common.exception.CustomException;
import com.blog.pojo.TokenVO;
import com.blog.pojo.User;
import com.blog.service.UserService;
import com.blog.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 贺畅
 * @date 2023/6/22
 */
@RequestMapping("/login")
@RestController
public class LoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private RedisTemplate redisTemplate;

	@Value("${spring.mail.username}")
	private String from;

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


	@PostMapping
	public Result login(HttpServletRequest request) {
		String email = request.getParameter("email");
		String code = request.getParameter("code");
		Object o = redisTemplate.opsForValue().get(email);
		if (o == null) {
			return Result.error("验证码已失效或过期");
		} else {
			String storeCode = (String) o;
			User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
			if (one == null) {
				return Result.error("用户不存在");
			}
			System.out.println(storeCode);
			if (storeCode.equals(code)) {
				String token = JwtTokenUtil.createJwt(new JwtClaims(one));
				String refreshToken = JwtTokenUtil.createRefreshToken(new JwtClaims(one));
				TokenVO tokenVO = new TokenVO(one, token, refreshToken);
				delete(email);
				return Result.ok("登陆成功，正在跳转", tokenVO);
			}
			return Result.error("验证码错误或已失效");
		}
	}

	/**
	 * 获取邮箱验证码
	 *
	 * @return
	 * @throws MessagingException
	 */
	@PostMapping("/getEmailCode")
	public Result getEmailCode(ServletRequest servletRequest)
			throws CustomException, MessagingException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		String to = req.getParameter("to");
		logger.info("发送邮件至{}", to);
		User emailOwner = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, to));
		if (emailOwner == null) {
			return Result.error("该邮件地址尚未被注册");
		}
		Object o = redisTemplate.opsForValue().get(to);
		if (o != null) {
			return Result.error("验证码尚未过期，请查看您的邮箱");
		}
		Runnable task = () -> {
			//构建 MimeMessage（邮件的主体） Spring 提供了 MimeMessageHelper 帮助类
			String subject = "邮箱登陆验证码";
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = null;
			try {
				helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			//生成随机数
			int randomCode = (int) ((Math.random() * 9 + 1) * 100000);
			String codeString = String.valueOf(randomCode);
			Context context = new Context();
			context.setVariable("username", emailOwner.getUserName());
			context.setVariable("code", codeString);
			context.setVariable("to", to);
			logger.error("设置的验证码是" + codeString);
			//随机数放入redis，并设置过期时间五分钟
			String content = templateEngine.process("mailtemplate", context);
			try {
				helper.setFrom(from);
				helper.setTo(to);
				helper.setSubject(subject);
				//设置邮件内容支持HTML文件格式
				helper.setText(content, true);
			} catch (MessagingException e) {
				try {
					retry(javaMailSender, helper, from, to, subject, content);
					return;
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			//调用 send 方法
			javaMailSender.send(helper.getMimeMessage());
		};
		MailSenderThreadPool.addSendMailTask(task);
		return Result.ok("邮件发送成功，五分钟内有效");
	}

	/**
	 * 暂定重试机制
	 * @param sender
	 * @param helper
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @throws InterruptedException
	 */
	void retry(JavaMailSender sender, MimeMessageHelper helper, String from, String to, String subject, String content) throws InterruptedException {
		int maxRetryTimes = 3;
		int i = 1;
		logger.info("重试调用");
		while (i <= maxRetryTimes) {
			try {
				helper.setFrom(from);
				helper.setTo(to);
				helper.setSubject(subject);
				//设置邮件内容支持HTML文件格式
				helper.setText(content, true);
				sender.send(helper.getMimeMessage());
				return;
			} catch (MessagingException e) {
				if (i == maxRetryTimes) {
					logger.error("邮件发送失败，时间：{}，参数=发送人：{}，接收人：{}，主题：{}，内容：{}，异常堆栈：{}",
							new Date(),from,to,subject,content,e.getStackTrace());
				}
				i++;
				Thread.sleep(1000);
			}
		}
	}

	@Async
	public void delete(String key){
		System.out.println("删除方法被调用");
		while (!redisTemplate.delete(key)) {
			if (redisTemplate.opsForValue().get(key) != null) {
				System.out.println("继续删除");
				continue;
			} else {
				System.out.println("已经删除");
				return;
			}
		}
		;
		System.out.println("删除成功");
	}

}
