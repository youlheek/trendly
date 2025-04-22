package com.rebootcrew.trendly.trendly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
	scanBasePackages = "com.rebootcrew.trendly",
	exclude = {
		SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
	}
)
@EnableAsync
public class TrendlyApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrendlyApplication.class, args);
	}
}
