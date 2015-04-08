package com.qinchu.app.proxy;

import android.content.Context;
import android.content.SharedPreferences;

import com.qinchu.app.base.MainApplication;

/**
 * Created by haoxiqiang on 15/4/1.
 */
public class SettingProxy {

    private static final String SETTINGNAME = "qc-setting";
    private static final String AGE = "qc-age";
    private static final String HEIGHT = "qc-height";
    private static final String FIRSTAGE = "qc-first-age";
    private static final String COUNT = "qc-count";
    private static final String STARTDATE = "qc-start-date";
    private static final String PERIOD = "qc-period";
    private static final String WEIGHT = "qc-weight";
    private static SharedPreferences mSharedPreferences;

    private static SharedPreferences getSettingPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = MainApplication.getAppContext().getSharedPreferences(SETTINGNAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    private static void save(String key, String value) {
        SharedPreferences.Editor edit = getSettingPreferences().edit();
        edit.putString(key, value);
        edit.apply();
    }

    private static void save(String key, int value) {
        SharedPreferences.Editor edit = getSettingPreferences().edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static int getAge() {
        return getSettingPreferences().getInt(AGE, -1);
    }

    public static void saveAge(int age) {
        save(AGE, age);
    }

    public static int getHeight() {
        return getSettingPreferences().getInt(HEIGHT, -1);
    }

    public static void saveHeight(int height) {
        save(HEIGHT, height);
    }

    public static int getFirstAge() {
        return getSettingPreferences().getInt(FIRSTAGE, -1);
    }

    public static void saveFirstAge(int firstAge) {
        save(FIRSTAGE, firstAge);
    }

    public static int getCount() {
        return getSettingPreferences().getInt(COUNT, -1);
    }

    public static void saveCount(int count) {
        save(COUNT, count);
    }

    public static int getPeriod() {
        return getSettingPreferences().getInt(PERIOD, -1);
    }

    public static void savePeriod(int period) {
        save(PERIOD, period);
    }

    public static long getStartDate() {
        return getSettingPreferences().getLong(STARTDATE, -1L);
    }

    public static void saveStartDate(long startDate) {
        SharedPreferences.Editor edit = getSettingPreferences().edit();
        edit.putLong(STARTDATE, startDate);
        edit.apply();
    }

    public static int getWeight() {
        return getSettingPreferences().getInt(WEIGHT, -1);
    }

    public static void saveWeight(int startDate) {
        save(WEIGHT, startDate);
    }
}
