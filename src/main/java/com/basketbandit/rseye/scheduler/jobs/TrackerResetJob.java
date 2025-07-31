package com.basketbandit.rseye.scheduler.jobs;

import com.basketbandit.rseye.Utils;
import com.basketbandit.rseye.scheduler.Job;
import com.basketbandit.rseye.scheduler.tasks.LootTrackerResetTask;

import java.util.concurrent.TimeUnit;

public class TrackerResetJob extends Job {
    private final LootTrackerResetTask lootTrackerResetTask;

    public TrackerResetJob(LootTrackerResetTask lootTrackerResetTask) {
        super(Utils.minutesUntilMidnight(), 1440, TimeUnit.MINUTES);
        this.lootTrackerResetTask = lootTrackerResetTask;
    }

    @Override
    public void run() {
        handleTask(lootTrackerResetTask);
    }
}
