package com.qinchu.app.tool;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarExtend extends GregorianCalendar {

    public CalendarExtend(TimeZone timezone, Locale locale) {
        super(timezone, locale);
        setTimeInMillis(System.currentTimeMillis());
    }

    /**
     * Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * default {@code Locale} and default {@code TimeZone}, set to the current date and time.
     */
    public static synchronized CalendarExtend getInstance() {
        return getInstance(TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * given {@code Locale} and default {@code TimeZone}, set to the current date and time.
     */
    public static synchronized CalendarExtend getInstance(Locale locale) {
        return getInstance(TimeZone.getDefault(), locale);
    }

    /**
     * Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * default {@code Locale} and given {@code TimeZone}, set to the current date and time.
     */
    public static synchronized CalendarExtend getInstance(TimeZone timezone) {
        return getInstance(timezone, Locale.getDefault());
    }

    /**
     * Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * given {@code Locale} and given {@code TimeZone}, set to the current date and time.
     */
    public static synchronized CalendarExtend getInstance(TimeZone timezone, Locale locale) {
        return new CalendarExtend(timezone, locale);
    }

    /**
     * @param year
     * @param month [0...11]
     * @return
     */

    public int getDaysOfMonth(int year, int month) {
        int dayCount;
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                dayCount = 31;
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                dayCount = 30;
                break;
            case 1:
                if (isLeapYear(year)) {
                    dayCount = 29;
                } else {
                    dayCount = 28;
                }
                break;
            default:
                dayCount = 0;
                new IllegalArgumentException("month must in 0...11");
                break;

        }
        return dayCount;
    }

    /**
     * get the weekday by year-month
     * 1...7  星期日...星期六
     * SUNDAY.....SATURDAY
     *
     * @param year
     * @param month
     * @return
     */
    public int getWeekdayOfMonthFirstDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
