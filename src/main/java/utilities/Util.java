package utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Util {
  public static final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
  public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

  /**
   * Time is <0.0-24.0>h
   * 
   * @return Time since midnight.
   */
  public static long timeFromDayDoubleToDayMs(double time) {
    final long msInHourL = 60 * 60 * 1000;
    final long msInMinL = 60 * 1000;

    double hour = Math.floor(time);
    double dec = time % 1;
    double minute = Math.round(dec * 60.0);

    long todayMs = msInHourL * (long) hour + msInMinL * (long) minute;

    return todayMs;
  }

  public static double timeFromDayMsToDayDouble(long time) {
    final long msInHourL = 60 * 60 * 1000;
    final long msInMinL = 60 * 1000;
    long hour = time / msInHourL;
    long minute = (time % msInHourL) / msInMinL;
    double todayDouble = (double) hour + (double) minute / 60.0;
    return todayDouble;
  }

  public static long dateToMsSinceEpoch(java.sql.Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeZone(TimeZone.getTimeZone("UTC"));//TODO je to treba?
    cal.setTime(date);
    return cal.getTimeInMillis();
  }

  public static java.sql.Date longToDate(long date) {//TODO test
    return new java.sql.Date(date);
  }

  public static String formatDateAndTime(long time) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return dateAndTimeFormat.format(cal.getTime());
  }
  
  public static String formatDate(long time) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return dateFormat.format(cal.getTime());
  }
  
  public static String formatTime(long time) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return timeFormat.format(cal.getTime());
  }
  
  public static long getPreviousDay(long time) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    cal.add(Calendar.DATE, -1);
    return cal.getTimeInMillis();
  }

}
