package com.qinchu.app.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


public class Day implements Comparable, Parcelable, Cloneable {

    public static final Creator<Day> CREATOR
            = new Creator<Day>() {
        public Day createFromParcel(Parcel source) {
            Day day = new Day();
            day.readFromParcel(source);
            return day;
        }

        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
    public int monthDay = -1;
    public int month = -1;
    public int year = -1;

    @Override
    protected Day clone() {
        try {
            return (Day) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
            Day day = new Day();
            day.year = this.year;
            day.month = this.month;
            day.monthDay = this.monthDay;
            return day;
        }
    }

    @Override
    public int hashCode() {
        int result = monthDay;
        result = 31 * result + month;
        result = 31 * result + year;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Day) {
            if (this.hashCode() == o.hashCode()
                    && this.year == ((Day) o).year
                    && this.month == ((Day) o).month
                    && this.monthDay == ((Day) o).monthDay) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year)
                .append("年")
                .append(month+1 < 10 ? ("0" + String.valueOf(month+1)) : month+1)
                .append("月");
        return stringBuilder.toString();
    }

    public String getDate() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year)
                .append(month < 10 ? ("0" + String.valueOf(month)) : month)
                .append(monthDay < 10 ? ("0" + String.valueOf(monthDay)) : monthDay);
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(@NonNull Object another) {
        //19700101
        if (another instanceof Day) {
            return (year - ((Day) another).year) * 10000 + (month - ((Day) another).month) * 100 + (monthDay - ((Day) another).monthDay);
        }
        return -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(monthDay);
    }

    private void readFromParcel(Parcel source) {
        year = source.readInt();
        month = source.readInt();
        monthDay = source.readInt();
    }
}
