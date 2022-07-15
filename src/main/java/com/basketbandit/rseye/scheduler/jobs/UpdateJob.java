package com.basketbandit.rseye.scheduler.jobs;

import com.basketbandit.rseye.scheduler.Job;
import com.basketbandit.rseye.scheduler.tasks.EventLogClearTask;
import com.basketbandit.rseye.scheduler.tasks.PlayerStateCheckTask;

import java.util.concurrent.TimeUnit;

public class UpdateJob extends Job {
    private final PlayerStateCheckTask playerStateCheckTask;
    private final EventLogClearTask eventLogClearTask;

    public UpdateJob(PlayerStateCheckTask playerStateCheckTask, EventLogClearTask eventLogClearTask) {
        super(0, 5, TimeUnit.SECONDS);
        this.playerStateCheckTask = playerStateCheckTask;
        this.eventLogClearTask = eventLogClearTask;
    }

    @Override
    public void run() {
        handleTask(playerStateCheckTask, eventLogClearTask);
    }
}
