package com.basketbandit.rseye.scheduler.jobs;

import com.basketbandit.rseye.Utils;
import com.basketbandit.rseye.scheduler.Job;
import com.basketbandit.rseye.scheduler.tasks.TrackerResetTask;

import java.util.concurrent.TimeUnit;

public class TrackerResetJob extends Job {
    private final TrackerResetTask trackerResetTask;

    public TrackerResetJob(TrackerResetTask trackerResetTask) {
        super(Utils.minutesUntilMidnight(), 1440, TimeUnit.MINUTES);
        this.trackerResetTask = trackerResetTask;
    }

    @Override
    public void run() {
        handleTask(trackerResetTask);
    }
}
