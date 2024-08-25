package com.creativeuncommons.ProgressTrackingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan
public class ProgressTrackingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgressTrackingSystemApplication.class, args);
	}

}
