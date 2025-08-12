package com.basketbandit.rseye.scheduler.tasks;

import com.basketbandit.rseye.DataManager;
import com.basketbandit.rseye.entity.ExperienceTracker;
import com.basketbandit.rseye.entity.LootTracker;
import com.basketbandit.rseye.scheduler.Task;

public class TrackerResetTask implements Task {
    @Override
    public void run() {
        DataManager.globalLootTracker = new LootTracker();
        DataManager.experienceTracker = new ExperienceTracker();
    }
}
