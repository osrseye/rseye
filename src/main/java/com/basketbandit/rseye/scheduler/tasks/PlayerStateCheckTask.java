package com.basketbandit.rseye.scheduler.tasks;


import com.basketbandit.rseye.DataManager;
import com.basketbandit.rseye.scheduler.Task;
import com.basketbandit.rseye.socket.MapSocketHandler;
import com.basketbandit.rseye.socket.UpdateType;

public class PlayerStateCheckTask implements Task {
    @Override
    public void run() {
        try {
            DataManager.players.forEach((_, player) -> {
                if(player.loginStateChanged()) {
                    MapSocketHandler.broadcastUpdate(UpdateType.LOGIN_UPDATE, player);
                }
            });
        } catch(Exception e) {
            log.error("Unable to broadcast update, reason: {}", e.getMessage(), e);
        }
    }
}
