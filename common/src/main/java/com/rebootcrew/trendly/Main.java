package com.rebootcrew.trendly;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan
// @WebServlet, @WebFilter, @WebListener 등의 어노테이션을 자동으로 스캔하고 스프링 컨텍스트에 등록
@SpringBootApplication
@RequiredArgsConstructor
public class Main {
	public static void main(String[] args) {
		System.out.println("Hello world!");
		SpringApplication.run(Main.class, args);

	}
}