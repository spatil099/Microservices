package com.sp.shop.notify_service;

import org.springframework.boot.SpringApplication;

public class TestNotifyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(NotifyServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
