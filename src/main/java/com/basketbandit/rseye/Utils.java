package com.basketbandit.rseye;

public class Utils {
    public static String formatNumber(int num) {
        if(num > 9999999) return num / 1000000 + "M";
        if(num > 99999) return num / 1000 + "K";
        return num + "";
    }
}
