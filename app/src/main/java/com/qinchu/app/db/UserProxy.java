package com.qinchu.app.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
                    "'count' integer" +
                    ");";

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DatabaseHelper.DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLENAME + ";");
            db.execSQL(CREATE_TABLE);
        }
    }

    public static void insertWeather(JSONObject weatherResult) {
        SQLiteDatabase db = DatabaseHelper.getDatabase();
        Cursor cursor = null;
        try {
            String date = weatherResult.getString("date").replace("-", "");
            cursor =
                    db.query(TABLENAME, new String[]{"date"}, "date = ?", new String[]{date}, null,
                            null, null);
            if (cursor.getCount() == 0) {
                JSONObject result = weatherResult.getJSONArray("results").getJSONObject(0);
                String currentCity = result.getString("currentCity");
                String pm25 = result.getString("pm25");
                JSONArray index = result.getJSONArray("index");
                JSONArray weather_data = result.getJSONArray("weather_data");

                ContentValues contentValues = new ContentValues();
                contentValues.put("date", date);
                contentValues.put("currentCity", currentCity);
                contentValues.put("pm25", pm25);
                contentValues.put("info", index.toString());
                contentValues.put("weather_data", weather_data.getJSONObject(0).toString());

                db.insert(TABLENAME, null, contentValues);
            }
        } catch (Exception e) {
            Logger.d(e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

    }

    public static Weather getTodayWeather() {
        Time time = new Time();
        time.setToNow();
        int month = time.month + 1;
        String todayDate = String.valueOf(time.year)
                + String.valueOf(month < 10 ? "0" + month : month)
                + String.valueOf(time.monthDay < 10 ? "0" + time.monthDay : time.monthDay);
        return getWeatherByDate(todayDate);
    }

    public static Weather getWeatherByDate(String dateParam) {
        Weather weather;
        SQLiteDatabase db = DatabaseHelper.getDatabase();
        Cursor cursor =
                db.query(TABLENAME, new String[]{"date", "currentCity", "pm25", "info",
                                "weather_data"}, "date = ?", new String[]{dateParam}, null,
                        null, null);
        if (cursor.getCount() == 0) {
            Logger.d("query todayDate:" + dateParam + "  is  0");
            weather = null;
            fetchWeatherContent(dateParam);
        } else {
            cursor.moveToFirst();
            weather = new Weather();
            weather.date = cursor.getString(0);
            weather.currentCity = cursor.getString(1);
            weather.pm25 = cursor.getString(2);
            try {
                weather.index = new JSONArray(cursor.getString(3));
                weather.weather_data = new JSONObject(cursor.getString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return weather;
    }
}
