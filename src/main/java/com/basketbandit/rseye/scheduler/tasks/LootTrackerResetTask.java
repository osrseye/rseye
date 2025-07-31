package com.basketbandit.rseye.scheduler.tasks;

import com.basketbandit.rseye.DataManager;
import com.basketbandit.rseye.entity.LootTracker;
import com.basketbandit.rseye.scheduler.Task;

public class LootTrackerResetTask implements Task {
    @Override
    public void run() {
        DataManager.globalLootTracker = new LootTracker();
    }
}
