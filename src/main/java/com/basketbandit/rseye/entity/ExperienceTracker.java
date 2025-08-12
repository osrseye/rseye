package com.basketbandit.rseye.entity;

import com.basketbandit.rseye.entity.player.Skills;
import com.basketbandit.rseye.entity.player.Username;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ExperienceTracker {
    private final Skills globalTracker = new Skills();
    private final ConcurrentHashMap<String, Skills> individualTrackers = new ConcurrentHashMap<>();

    public Skills globalTracker() {
        return globalTracker;
    }

    public ConcurrentHashMap<String, Skills> individualTrackers() {
        return individualTrackers;
    }

    public Skills individualTracker(Username username) {
        return individualTrackers.get(username.encoded());
    }

    public void trackExperience(Username username, String skill, int experience) {
        if(!individualTrackers.containsKey(username.encoded())) {
            individualTrackers.put(username.encoded(), new Skills());
        }
        individualTrackers.get(username.encoded()).skills().get(skill).addExperience(experience);
        globalTracker.skills().get(skill).addExperience(experience);
    }

    public void trackExperience(Username username, HashMap<String, Integer> skills) {
        skills.forEach((skill, value) -> trackExperience(username, skill, value));
    }
}
