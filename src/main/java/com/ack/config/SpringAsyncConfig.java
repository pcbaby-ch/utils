package com.ack.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步调用线程池
 * @author chenzhao @date Oct 16, 2019
 */
@Configuration
@EnableAsync
public class SpringAsyncConfig {

	@Value("${maxPoolSize}")
	private int maxPoolSize;
	@Value("${corePoolSize}")
	private int corePoolSize;
	@Value("${queueCapacity}")
	private int queueCapacity;
	
	@Bean
	public Executor getThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(maxPoolSize);
		executor.setCorePoolSize(corePoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("ThreadPoolTaskExecutor");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		executor.initialize();
		return executor;
	}
}
