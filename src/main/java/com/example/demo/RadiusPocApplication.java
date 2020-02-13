package com.example.demo;

import org.springframework.boot.SpringApplication;

/*
 * 
 * @Zakaria El KOTB
 */
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RadiusPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(RadiusPocApplication.class, args);
	}

}
