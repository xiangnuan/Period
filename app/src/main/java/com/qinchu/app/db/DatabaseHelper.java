package com.qinchu.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;
import com.qinchu.app.base.MainApplication;

/**
 * Created by haoxiqiang on 15/3/20.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "period.db";
    private static SQLiteDatabase mSQLiteDatabase = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteDatabase getDatabase() {
        return mSQLiteDatabase;
    }

    public synchronized static void init() {
        if (mSQLiteDatabase != null) {
            return;
        }
        try {
            DatabaseHelper helper = new DatabaseHelper(MainApplication.getAppContext());
            mSQLiteDatabase = helper.getWritableDatabase();
        } catch (Exception e) {

        }
    }

    public static void closeDatabase() {
        if (mSQLiteDatabase != null) {
            mSQLiteDatabase.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(UserProxy.CREATE_TABLE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();
        try {
            UserProxy.onUpgrade(db, oldVersion, newVersion);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            db.endTransaction();
        }
    }
}
