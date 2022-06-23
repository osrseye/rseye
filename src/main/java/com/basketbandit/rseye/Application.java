package com.basketbandit.rseye;

import com.basketbandit.rseye.entity.event.CombatEvent;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.event.GrowthEvent;
import com.basketbandit.rseye.entity.event.QuestEvent;
import com.basketbandit.rseye.scheduler.ScheduleHandler;
import com.basketbandit.rseye.scheduler.jobs.UpdateJob;
import com.basketbandit.rseye.scheduler.tasks.PlayerStateCheckTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class Application {
	public static ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
	public static CopyOnWriteArrayList<CombatEvent> combatFeed = new CopyOnWriteArrayList<>(); // inefficient but we aren't expecting high throughput
	public static CopyOnWriteArrayList<GrowthEvent> growthFeed = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<QuestEvent> questFeed = new CopyOnWriteArrayList<>();

	public Application() {
		new AssetManager();
		ScheduleHandler.registerJob(new UpdateJob(new PlayerStateCheckTask()));
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
