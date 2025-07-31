package com.basketbandit.rseye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public Application() {
		new DataManager();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
