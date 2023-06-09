package com.jeongho.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ComfortDayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComfortDayApplication.class, args);
	}

}
