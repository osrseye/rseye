package com.basketbandit.rseye;

import com.basketbandit.rseye.entity.Combat;
import com.basketbandit.rseye.entity.Player;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class Application {
	public static ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
	public static CopyOnWriteArrayList<Combat> combatFeed = new CopyOnWriteArrayList<>(); // inefficient but we aren't expecting high throughput

	public Application() {
		new AssetManager();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
