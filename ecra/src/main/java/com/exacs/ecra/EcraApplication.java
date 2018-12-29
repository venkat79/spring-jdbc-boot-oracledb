package com.exacs.ecra;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcraApplication {
	private static final Logger _logger = LoggerFactory.getLogger(EcraApplication.class);

	public static void main(String[] args) {
		_logger.debug("Start point of Spring Boot Application");
		SpringApplication.run(EcraApplication.class, args);

	}
}
