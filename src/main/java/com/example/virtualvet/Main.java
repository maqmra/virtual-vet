package com.example.virtualvet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		//TODO: add transactions
		// TODO: use UUID
		// TODO: read about on delete, orphan removal, cascade.delete

	}

}
