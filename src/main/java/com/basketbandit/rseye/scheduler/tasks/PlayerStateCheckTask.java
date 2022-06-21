package com.basketbandit.rseye.scheduler.tasks;


import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.scheduler.Task;
import com.basketbandit.rseye.socket.MapSocketHandler;

public class PlayerStateCheckTask implements Task {
    public PlayerStateCheckTask() {
    }

    @Override
    public void run() {
        try {
            Application.players.forEach((s, player) -> {
                if(player.loginStateChanged()) {
                    MapSocketHandler.broadcastUpdate("login_state", player.info());
                }
            });
        } catch(Exception e) {
            log.error("Unable to broadcast update, reason: {}", e.getMessage(), e);
        }
    }

}
