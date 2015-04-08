package com.qinchu.app.proxy;

import android.text.format.DateUtils;

import java.util.Calendar;

/**
 * Created by haoxiqiang on 15/4/7.
 */
public class PeriodProxy {

    private long startDate;
    private int period;
    private int count;

    private PeriodProxy(){

    }

    public static PeriodProxy newInstance(){
        PeriodProxy periodProxy = new PeriodProxy();
        periodProxy.startDate = SettingProxy.getStartDate();
        periodProxy.period = SettingProxy.getPeriod();
        periodProxy.count = SettingProxy.getCount();
        return periodProxy;
    }

    public boolean isInPerid(int year, int month, int day) {

        //如果startDate小于0,则表明没有设置信息,没有意义,返回false
        if (startDate < 0 || period <= 0 || count <= 0) {
            return false;
        }

        Calendar calendarTarget = Calendar.getInstance();
        calendarTarget.set(year, month, day);

        long target = calendarTarget.getTimeInMillis();
        int datCount = Math.abs((int) ((startDate - target) / DateUtils.DAY_IN_MILLIS));
        int currentMonth = datCount % (count + period);
        if (currentMonth <= count) {
            return true;
        }
        return false;
    }
}
