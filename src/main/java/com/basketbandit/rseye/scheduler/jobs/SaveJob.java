package com.basketbandit.rseye.scheduler.jobs;

import com.basketbandit.rseye.scheduler.Job;
import com.basketbandit.rseye.scheduler.tasks.SavePlayerDataTask;

import java.util.concurrent.TimeUnit;

public class SaveJob extends Job {
    private final SavePlayerDataTask savePlayerDataTask;

    public SaveJob(SavePlayerDataTask savePlayerDataTask) {
        super(0, 5, TimeUnit.MINUTES);
        this.savePlayerDataTask = savePlayerDataTask;
    }

    @Override
    public void run() {
        handleTask(savePlayerDataTask);
    }
}
