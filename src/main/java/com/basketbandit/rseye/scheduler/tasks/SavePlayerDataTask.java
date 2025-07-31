package com.basketbandit.rseye.scheduler.tasks;

import com.basketbandit.rseye.DataManager;
import com.basketbandit.rseye.scheduler.Task;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SavePlayerDataTask implements Task {
    private final Gson gson = new Gson();

    @Override
    public void run() {
        DataManager.players.forEach((_, player) -> {
            String path = "./data/players/%s.json".formatted(player.username().encoded());
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(gson.toJson(player));
            } catch(IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
