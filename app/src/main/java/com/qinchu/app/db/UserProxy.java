package com.qinchu.app.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.qinchu.app.entity.User;


/**
 * Created by haoxiqiang on 15/3/20.
 */
public final class UserProxy {

    public static final String TABLENAME = "qc_user";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLENAME + "(" +
                    "'uid' INTEGER PRIMARY KEY NOT NULL," +
                    "'password' text," +
                    "'name' text," +
                    "'age' integer," +
                    "'height' integer," +
                    "'weight' integer," +
                    "'period' integer," +
                    "'count' integer," +
                    "'startDate' text" +
                    ");";

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DatabaseHelper.DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLENAME + ";");
            db.execSQL(CREATE_TABLE);
        }
    }

    public static void saveUser(User user) {
        SQLiteDatabase db = DatabaseHelper.getDatabase();
        Cursor cursor = null;
        try {
            cursor =
                    db.query(TABLENAME, new String[]{"uid"}, "uid = ?", new String[]{String.valueOf(user.getUid())}, null, null, null);
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", user.getUid());
            contentValues.put("password", user.getPassword());
            contentValues.put("name", user.getName());
            contentValues.put("age", user.getAge());
            contentValues.put("height", user.getHeight());
            contentValues.put("weight", user.getWeight());
            contentValues.put("period", user.getPeriod());
            contentValues.put("count", user.getCount());
            contentValues.put("startDate", user.getStartDate());

            if (cursor.getCount() == 0) {
                db.insert(TABLENAME, null, contentValues);
            } else {
                db.update(TABLENAME, contentValues, "uid = ?", new String[]{String.valueOf(user.getUid())});
            }
        } catch (Exception e) {
            Logger.d(e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

    }

    public static User getUser(int uid) {
        User user;
        SQLiteDatabase db = DatabaseHelper.getDatabase();
        Cursor cursor =
                db.query(TABLENAME, new String[]{"uid", "password", "name", "age", "height", "weight", "period", "count","startDate"}, "uid = ?", new String[]{String.valueOf(uid)}, null,
                        null, null);
        if (cursor.getCount() == 0) {
            user = null;
        } else {
            //切记这个moveToFirst不可以少,因为是要移动到第一条,否则在最后一条是没有内容的
            cursor.moveToFirst();
            user = new User();
            user.setUid(cursor.getInt(0));
            user.setPassword(cursor.getString(1));
            user.setName(cursor.getString(2));
            user.setAge(cursor.getInt(3));
            user.setHeight(cursor.getInt(4));
            user.setWeight(cursor.getInt(5));
            user.setPeriod(cursor.getInt(6));
            user.setCount(cursor.getInt(7));
            user.setStartDate(cursor.getString(8));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return user;
    }
}
