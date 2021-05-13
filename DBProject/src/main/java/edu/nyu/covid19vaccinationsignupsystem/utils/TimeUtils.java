package edu.nyu.covid19vaccinationsignupsystem.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    public static Date parseDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = null;
        try {
            parsed = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date(parsed.getTime());
        return date;
    }

    public static Timestamp now() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        Timestamp logTime = new Timestamp(calendar.getTimeInMillis());
        return logTime;
    }

    public static Timestamp parseTimeStamp(Timestamp timestamp) {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.setTimeInMillis(timestamp.getTime());
        Timestamp logTime = new Timestamp(calendar.getTimeInMillis());
        return logTime;
    }
}
