package com.ack.config;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling // 开启定时任务
@EnableAsync // 开启多线程
@Component
public class MultiThreadScheduleTask {

	String equId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 25);

	@Async
	@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void insertCity() {
	}

	@Async
	@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void insertPv() {
	}

	public static void main(String[] args) {
		System.out.println(new Random().nextInt(2));
		System.out.println(5 + (int) (Math.random() * ((10 - 5) + 1)));
		System.out.println(new Date().getTime());
	}
}