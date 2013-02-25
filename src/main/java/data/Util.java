package data;

import java.text.SimpleDateFormat;

public class Util {
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd.MM.yyyy");

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
		long hour2 = time / msInHourL;
		long minute2 = (time % msInHourL) / msInMinL;
		double todayDouble = (double) hour2 + (double) minute2 / 60.0;
		return todayDouble;
	}

}
