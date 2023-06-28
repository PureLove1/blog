package com.blog.config;

import com.blog.interceptor.AuthenticationInterceptor;
import com.blog.interceptor.UniqueVisitorCountInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * springMVC配置
 * @author 贺畅
 * @date 2022/11/28
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    private UniqueVisitorCountInterceptor uniqueVisitorCountInterceptor;
	/**
	 * 静态资源处理
	 * @param registry
	 */
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
        .addResourceLocations("classpath:/templates/");
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //  授权拦截
        registry.addInterceptor(uniqueVisitorCountInterceptor).addPathPatterns("/**");
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/login/**","/logout");
    }

    /**
     * 跨域配置（用于前后端分离）
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 允许路径
                .addMapping("/**")
                // 允许源
//                .allowedOrigins("http://localhost:8081")
                .allowedOriginPatterns("*")
                // 允许凭证
                .allowCredentials(true)
                // 允许的请求方式
                .allowedMethods("GET","HEAD","PUT","POST","DELETE","OPTIONS");
    }
}
