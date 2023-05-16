package com.jasonpyau.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    
    private DateFormat() {};

    public static String MMddyyyy() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    public static String MMddyyyyhhmmss() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String dateTime = simpleDateFormat.format(new Date());
        return dateTime;
    }

    public static String yyyyMMddHHmmss() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateTime = simpleDateFormat.format(new Date());
        return dateTime;
    }
}
