package com.basketbandit.rseye.scheduler.jobs;

import com.basketbandit.rseye.scheduler.Job;
import com.basketbandit.rseye.scheduler.tasks.PingWebSocketTask;

import java.util.concurrent.TimeUnit;

public class PingJob extends Job {
    private final PingWebSocketTask pingWebSocketTask;

    public PingJob(PingWebSocketTask pingWebSocketTask) {
        super(0, 60, TimeUnit.SECONDS);
        this.pingWebSocketTask = pingWebSocketTask;
    }

    @Override
    public void run() {
        handleTask(pingWebSocketTask);
    }
}
