package com.basketbandit.rseye.scheduler.jobs;

import com.basketbandit.rseye.scheduler.Job;
import com.basketbandit.rseye.scheduler.tasks.PlayerStateCheckTask;

import java.util.concurrent.TimeUnit;

public class UpdateJob extends Job {
    private final PlayerStateCheckTask task;

    public UpdateJob(PlayerStateCheckTask task) {
        super(0, 5, TimeUnit.SECONDS);
        this.task = task;
    }

    @Override
    public void run() {
        handleTask(task);
    }
}
