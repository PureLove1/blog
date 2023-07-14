package com.blog.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author PureLove1
 * @Date 2023/7/5
 */
@Aspect
@Component
public class LogRecorderAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogRecorderAspect.class);

	/**
	 * 访问修饰符 返回值 包名.包名.包名…类名.方法名(参数列表)
	 * service切点
	 */
	@Pointcut("execution(* com.blog.service.*.*(..))")
	public void servicePointcut() {
	}

	/**
	 * controller切点
	 */
	@Pointcut("execution(* com.blog.controller.*.*(..))")
	public void controllerPointcut() {
	}

	/**
	 * 记录用户service访问信息
	 *
	 * @param joinPoint
	 */
	@Before("servicePointcut()")
	public void beforeServiceExecute(JoinPoint joinPoint) {
		recordUserAccess(joinPoint);
	}

	/**
	 * 记录用户controller访问信息
	 *
	 * @param joinPoint
	 */
	@Before("controllerPointcut()")
	public void beforeControllerExecute(JoinPoint joinPoint) {
		recordUserAccess(joinPoint);
	}

	/**
	 * 记录用户操作
	 * @param joinPoint
	 */
	private void recordUserAccess(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return;
		}
		HttpServletRequest request = attributes.getRequest();

		//获取IP
		String ip = request.getRemoteHost();
		//获取当前时间
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String time = formatter.format(now);
		Signature signature = joinPoint.getSignature();
		//获取访问类的全限定名
		String typeName = signature.getDeclaringTypeName();
		//获取访问方法
		String method = signature.getName();
		//获取参数
		Object[] args = joinPoint.getArgs();
		logger.info("用户IP：{}于{}访问了{}类的{}方法，参数列表为：{}", ip, time, typeName, method, args);
	}
}
