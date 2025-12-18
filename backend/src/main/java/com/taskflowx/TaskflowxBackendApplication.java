package com.taskflowx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskflowxBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskflowxBackendApplication.class, args);
		System.out.println("Taskflowx Backend is running...");
	}

}
