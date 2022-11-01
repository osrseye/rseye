package com.basketbandit.rseye.entity.fragment;

import java.util.HashMap;

public record PlayerInformation(String username, String usernameEncoded, HashMap<String, String> position, HashMap<String, String> offsetPosition){
    public PlayerInformation() {
        this("", "", new HashMap<>(), new HashMap<>());
    }
}
