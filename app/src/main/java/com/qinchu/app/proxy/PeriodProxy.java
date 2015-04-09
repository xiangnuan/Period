package com.qinchu.app.proxy;

import android.text.format.DateUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.qinchu.app.db.UserProxy;
import com.qinchu.app.entity.User;

import java.util.Calendar;

/**
 * Created by haoxiqiang on 15/4/7.
 */
public class PeriodProxy {

    private User mUser;

    private PeriodProxy() {

    }

    public static PeriodProxy newInstance() {
        PeriodProxy periodProxy = new PeriodProxy();
        periodProxy.mUser = UserProxy.getUser(SettingProxy.getUid());
        return periodProxy;
    }

    public boolean isInPerid(int year, int month, int day) {

        //如果startDate小于0,则表明没有设置信息,没有意义,返回false
        if (mUser.getStartDate() == null || mUser.getPeriod() <= 0 || mUser.getCount() <= 0) {
            return false;
        }

        long startDate = Long.valueOf(mUser.getStartDate());

        Calendar calendarTarget = Calendar.getInstance();
        calendarTarget.set(year, month, day);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(startDate);
        Logger.e("isInPerid",
                "year:" + year
                        + "\nmonth:" + month
                        + "\nday:" + day
                        + "\nstart:" + calendarStart.getTime().toLocaleString()
        );


        long target = calendarTarget.getTimeInMillis();
        int datCount = Math.abs((int) ((startDate - target) / DateUtils.DAY_IN_MILLIS));
        int currentMonth = datCount % (mUser.getCount() + mUser.getPeriod());
        if (currentMonth < mUser.getCount()) {
            return true;
        }
        return false;
    }
}
