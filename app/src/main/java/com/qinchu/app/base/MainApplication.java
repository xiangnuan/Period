package com.qinchu.app.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.qinchu.app.BuildConfig;
import com.qinchu.app.db.DatabaseHelper;

import java.util.List;

/**
 * Created by haoxiqiang on 15/4/1.
 */
public class MainApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog()
                    .penaltyDeath().build());
        }
        super.onCreate();
        if (mContext == null) {
            mContext = this.getApplicationContext();
        }

        if (!isMainProcess()) {
            return;
        }

        DatabaseHelper.init();
    }

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (!isMainProcess()) {
            return;
        }
        DatabaseHelper.closeDatabase();
    }

    private boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
