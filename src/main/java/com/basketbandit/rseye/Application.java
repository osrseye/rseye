package com.basketbandit.rseye;

import com.basketbandit.rseye.entity.Player;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class Application {
	public static ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

	public Application() {
		new AssetManager();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
