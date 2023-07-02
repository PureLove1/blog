package com.blog.interceptor;

import com.blog.service.UniqueVisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author PureLove1
 * @Date 2023/6/26
 * UV统计拦截
 */
@Component
public class UniqueVisitorCountInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(UniqueVisitorCountInterceptor.class);

	@Autowired
	private UniqueVisitorService uniqueVisitorService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//获取远程IP
		String remoteAddr = getRemoteAddr(request);
		//远程用户
		String remoteUser = request.getRemoteUser();
		//远程端口
		int remotePort = request.getRemotePort();
		//远程主机
		String remoteHost = request.getRemoteHost();
		//使用协议
		String protocol = request.getProtocol();
		//用户代理
		String userAgent = request.getHeader("User-Agent");
		logger.info("访问IP端口：{}:{}；远程用户名：{},远程主机名：{},使用协议：{},用户代理：{}，访问时间：{}",
				remoteAddr,remotePort,remoteUser,remoteHost,protocol,userAgent,new Date());
		uniqueVisitorService.addVisitRecord(remoteAddr);
		return true;
	}

	private String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
