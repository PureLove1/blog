package com.blog.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author PureLove1
 * @Date 2023/7/17
 */

@Configuration
@EnableOpenApi
public class SwaggerConfig {
	/**
	 * 创建API应用
	 * apiInfo() 增加API相关信息
	 * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
	 * 本例采用指定扫描的包路径来定义指定要建立API的目录。
	 * 然后我们启动项目，打开http://地址:端口/swagger-ui/index.html ip和port为自己本机的
	 *
	 * @return
	 */
	@Bean
	public Docket restApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("标准接口")
				.apiInfo(apiInfo("blog API", "1.0"))
				.useDefaultResponseMessages(true)
				.forCodeGeneration(false)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.blog.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	/**
	 * 创建该API的基本信息（这些基本信息会展现在文档页面中）
	 * 访问地址：http://地址:端口/swagger-ui/index.html
	 *
	 * @return
	 */
	private ApiInfo apiInfo(String title, String version) {
		return new ApiInfoBuilder()
				.title(title)
				.version(version)
				.build();
	}

	@Bean
	public BeanPostProcessor generateBeanPostProcessor(){
		return new BeanPostProcessor() {

			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
					customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
				}
				return bean;
			}

			private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
				List<T> copy = mappings.stream()
						.filter(mapping -> mapping.getPatternParser() == null)
						.collect(Collectors.toList());
				mappings.clear();
				mappings.addAll(copy);
			}

			@SuppressWarnings("unchecked")
			private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
				try {
					Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
					field.setAccessible(true);
					return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
		};
	}
}
