package com.basketbandit.rseye.scheduler.tasks;

import com.basketbandit.rseye.scheduler.Task;
import com.basketbandit.rseye.socket.MapSocketHandler;

public class PingWebSocketTask implements Task {
    @Override
    public void run() {
        MapSocketHandler.broadcastPing();
    }
}
