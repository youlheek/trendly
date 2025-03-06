package com.rebootcrew.trendly.trendly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.rebootcrew.trendly")
public class TrendlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrendlyApplication.class, args);
	}

}
