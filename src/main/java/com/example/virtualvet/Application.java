package com.example.virtualvet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		// TODO: read about on delete, orphan removal, cascade.delete. | Read about transactions in Spring. | @Transactional - read about it | Read about @Component
		// TODO: do I need more ExceptionHandlers?
		// todo: read more about reflection (but not detailed)

	}

}
