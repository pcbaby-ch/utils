/**
 *
 */
package com.ack.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * filter初始化配置
 * 
 * @author chenzhao @date Oct 21, 2019
 */
@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean authorizationFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new CrosDomainFilter());// 添加过滤器
		registration.addUrlPatterns("/*");
		registration.setName("CrosDomainFilter");
		registration.setOrder(1);// 设置优先级
		return registration;
	}
}
