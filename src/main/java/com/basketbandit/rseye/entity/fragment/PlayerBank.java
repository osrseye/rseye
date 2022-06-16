package com.basketbandit.rseye.entity.fragment;

import com.basketbandit.rseye.entity.Item;

import java.util.ArrayList;

public record PlayerBank(Integer value, ArrayList<Item> slots) {
}
