package com.basketbandit.rseye;

import java.util.Calendar;

public class Utils {
    public static String formatNumber(int num) {
        if(num > 9999999) return num / 1000000 + "M";
        if(num > 99999) return num / 1000 + "K";
        return num + "";
    }

    /**
     * Pascal ensures each word is capitalised, this is case we just want the first letter.
     * @param string String
     * @return String
     */
    public static String toPascal(String string) {
        if(string.length() < 2) {
            return string.toUpperCase();
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public static long minutesUntilMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (calendar.getTimeInMillis()-System.currentTimeMillis()) / 60000;
    }
}
