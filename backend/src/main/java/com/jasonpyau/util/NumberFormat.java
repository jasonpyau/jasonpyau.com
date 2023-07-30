package com.jasonpyau.util;

import java.util.Locale;

public class NumberFormat {
    
    private NumberFormat() {}

    public static String shorten(Long num) {
        java.text.NumberFormat numberFormat = java.text.NumberFormat.getCompactNumberInstance(Locale.US, java.text.NumberFormat.Style.SHORT);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(num);
    }
}
