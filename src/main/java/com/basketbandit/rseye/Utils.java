package com.basketbandit.rseye;

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
}
