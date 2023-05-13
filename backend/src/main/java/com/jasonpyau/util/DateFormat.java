package com.jasonpyau.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    private DateFormat() {};

    public static String date() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    public static String dateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String dateTime = simpleDateFormat.format(new Date());
        return dateTime;
    }
}
