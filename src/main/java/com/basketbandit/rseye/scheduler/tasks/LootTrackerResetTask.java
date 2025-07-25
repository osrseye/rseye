package com.basketbandit.rseye.scheduler.tasks;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.entity.LootTracker;
import com.basketbandit.rseye.scheduler.Task;

public class LootTrackerResetTask implements Task {
    @Override
    public void run() {
        Application.globalLootTracker = new LootTracker();
    }
}
