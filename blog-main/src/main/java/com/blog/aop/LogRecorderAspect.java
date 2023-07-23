package com.blog.aop;

import com.blog.common.Result;
import com.blog.common.WebLog;
import com.blog.util.IPUtil;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.blog.constant.StatusCode.OK;
import static com.blog.util.StringUtil.isNotEmpty;

/**
 * @Author PureLove1
 * @Date 2023/7/5
 */
@Aspect
@Component
public class LogRecorderAspect {

	/**
	 * 允许的最大请求处理时间
	 */
	private static final int ALLOWED_MAX_PROCESS_TIME = 1000;

	private static final String RESULT_OK = "OK";

	private static final Logger logger = LoggerFactory.getLogger(LogRecorderAspect.class);

	/**
	 * controller切点
	 */
	@Pointcut("execution(* com.blog.controller.*.*(..))")
	public void controllerPointcut() {
	}


	/**
	 * 记录用户controller访问信息
	 *
	 * @param joinPoint
	 * @return
	 */
	@Around("controllerPointcut()")
	public Object aroundControllerExecute(ProceedingJoinPoint joinPoint) throws Throwable {
		//请求开始时间
		long startTime = System.currentTimeMillis();
		//获取当前请求对象
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		//记录请求信息
		WebLog webLog = new WebLog();
		Object result = joinPoint.proceed();
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		if (method.isAnnotationPresent(ApiOperation.class)) {
			ApiOperation log = method.getAnnotation(ApiOperation.class);
			webLog.setDescription(log.value());
		}
		long endTime = System.currentTimeMillis();
		//设置日志信息
		webLog.setUsername(request.getRemoteUser());
		webLog.setIp(IPUtil.getRemoteAddr(request));
		webLog.setMethod(request.getMethod());
		webLog.setParameter(getParameter(method, joinPoint.getArgs()));
		Result resultObj = (Result) result;
		String statusCode = resultObj.getStatusCode();
		webLog.setResult(OK.equals(statusCode) ? RESULT_OK : result);
		int spendTime = (int) (endTime - startTime);
		webLog.setSpendTime(spendTime);
		webLog.setStartTime(startTime);
		webLog.setUri(request.getRequestURI());
		webLog.setUrl(request.getRequestURL().toString());
		//对象用于后续logstash收集使用
		if (spendTime < ALLOWED_MAX_PROCESS_TIME) {
			logger.info("{}", webLog.getLogString());
		} else {
			logger.warn("{}", webLog.getLogString());
		}
		return result;
	}

	/**
	 * 根据方法和传入的参数获取请求参数
	 */
	private List<Object> getParameter(Method method, Object[] args) {
		List<Object> argList = new ArrayList<>();
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			//将RequestBody注解修饰的参数作为请求参数
			RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
			if (requestBody != null) {
				argList.add(args[i]);
			}
			//将RequestParam注解修饰的参数作为请求参数
			RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
			if (requestParam != null) {
				Map<String, Object> map = new HashMap<>();
				String key = parameters[i].getName();
				if (!isNotEmpty(requestParam.value())) {
					key = requestParam.value();
				}
				if (args[i] != null) {
					map.put(key, args[i]);
					argList.add(map);
				}
			}
		}
		if (argList.size() == 0) {
			return null;
		} else {
			return argList;
		}
	}
}
