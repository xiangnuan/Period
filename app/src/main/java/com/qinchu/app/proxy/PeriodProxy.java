package com.qinchu.app.proxy;

import android.text.format.DateUtils;
<<<<<<< HEAD
=======

import com.orhanobut.logger.Logger;
import com.qinchu.app.db.UserProxy;
import com.qinchu.app.entity.User;
>>>>>>> origin/master

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

        int count = mUser.getCount();

        //如果startDate小于0,则表明没有设置信息,没有意义,返回false
<<<<<<< HEAD
        if (startDate < 0 || period <= 0 || count <= 0) {
=======
        if (mUser.getStartDate() == null || mUser.getPeriod() <= 0 || count <= 0) {
>>>>>>> origin/master
            return false;
        }

        Calendar calendarTarget = Calendar.getInstance();
        calendarTarget.set(year, month, day);

<<<<<<< HEAD
        long target = calendarTarget.getTimeInMillis();
        int datCount = Math.abs((int) ((startDate - target) / DateUtils.DAY_IN_MILLIS));
        int currentMonth = datCount % (count + period);
        if (currentMonth <= count) {
=======
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(startDate);

        long target = calendarTarget.getTimeInMillis();
        int datCount = Math.abs((int) ((startDate - target) / DateUtils.DAY_IN_MILLIS));

        Logger.d("isInPerid",
                "year:" + year
                        + "\nmonth:" + month
                        + "\nday:" + day
                        + "\nstart:" + calendarStart.getTime().toLocaleString()
                        + "\ndatCount:" + datCount
        );


        int currentMonth = datCount % (count + mUser.getPeriod());
        if (currentMonth < count) {
>>>>>>> origin/master
            return true;
        }
        return false;
    }
}
