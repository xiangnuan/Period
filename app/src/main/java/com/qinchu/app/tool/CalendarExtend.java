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
     * @param month
     * @return
     */

    public int getDaysOfMonth(int year, int month) {
        int daycount = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daycount = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daycount = 30;
                break;
            case 2:
                if (isLeapYear(year)) {
                    daycount = 29;
                } else {
                    daycount = 28;
                }

        }
        return daycount;
    }

    /**
     * get the weekday by year-month
     *
     * @param year
     * @param month
     * @return
     */
    public int getWeekdayOfMonthFirstDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }
}
