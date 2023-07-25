package com.blog.interceptor;

import com.blog.service.UniqueVisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.util.IPUtil.getRemoteAddr;

/**
 * @Author PureLove1
 * @Date 2023/6/26
 * UV统计拦截
 */
@Component
@Slf4j
public class UniqueVisitorCountInterceptor implements HandlerInterceptor {

	@Autowired
	private UniqueVisitorService uniqueVisitorService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//获取远程IP
		String remoteAddr = getRemoteAddr(request);
		uniqueVisitorService.addVisitRecord(remoteAddr);
		return true;
	}
}
