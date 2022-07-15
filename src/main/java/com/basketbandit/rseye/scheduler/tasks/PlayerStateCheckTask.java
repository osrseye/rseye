package com.basketbandit.rseye.scheduler.tasks;


import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.socket.UpdateType;
import com.basketbandit.rseye.scheduler.Task;
import com.basketbandit.rseye.socket.MapSocketHandler;

public class PlayerStateCheckTask implements Task {
    @Override
    public void run() {
        try {
            Application.players.forEach((s, player) -> {
                if(player.loginStateChanged()) {
                    MapSocketHandler.broadcastUpdate(UpdateType.LOGIN_UPDATE, player);
                }
            });
        } catch(Exception e) {
            log.error("Unable to broadcast update, reason: {}", e.getMessage(), e);
        }
    }
}
