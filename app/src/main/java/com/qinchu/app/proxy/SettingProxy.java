package com.qinchu.app.proxy;

import android.content.Context;
import android.content.SharedPreferences;

import com.qinchu.app.base.MainApplication;

/**
 * Created by haoxiqiang on 15/4/1.
 */
public class SettingProxy {

    private static final String SETTINGNAME = "qc-setting";
    private static final String UID = "qc-uid";
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

    public static int getUid() {
        return getSettingPreferences().getInt(UID, -1);
    }

    public static void saveUid(int uid) {
        save(UID, uid);
    }
}
