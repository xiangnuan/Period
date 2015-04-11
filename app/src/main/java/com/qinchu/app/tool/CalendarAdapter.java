package com.qinchu.app.tool;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.qinchu.app.R;
import com.qinchu.app.entity.Day;
import com.qinchu.app.proxy.PeriodProxy;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CalendarAdapter extends BaseAdapter {

    private final int normalDayColor = Color.parseColor("#FF222222");
    private final int otherDayColor = Color.parseColor("#FFBBBBBB");
    private Context context;
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几
    private int lastDaysOfMonth = 0; // 上一个月的总天数
    private Day[] dayNumber = new Day[35]; // 一个gridview中的日期存入此数组中
    // 系统当前时间
    private Day sysDay = new Day();
    private Day showDay = new Day();
    private int chooseColor = Color.parseColor("#FFA6D3");
    PeriodProxy periodProxy;

    private CalendarExtend mCalendarExtend = CalendarExtend.getInstance();

    private CalendarAdapter(Context _context) {
        this.context = _context;

        periodProxy = PeriodProxy.newInstance();

        sysDay.year = mCalendarExtend.get(Calendar.YEAR);
        sysDay.month = mCalendarExtend.get(Calendar.MONTH);
        sysDay.monthDay = mCalendarExtend.get(Calendar.DAY_OF_MONTH);
    }

    public CalendarAdapter(Context _context, int jumpMonth) {
        this(_context);

        int targetY = sysDay.year;
        int targetM = sysDay.month + jumpMonth;

        if (targetM > 0) {
            // 往下一个月滑动,stepMonth=11,则是12月,是11几倍就是越过几年
            targetY = sysDay.year + targetM / 12;
            targetM = targetM % 12;
        } else if (targetM < 0) {
            // 往上一个月滑动
            targetY = (sysDay.year-1) + targetM / 11;
            targetM = 12 + targetM % 12;
        }
        showDay.year = targetY;
        showDay.month = targetM;
        showDay.monthDay = sysDay.monthDay;

        initDaysOfMonth(targetY, targetM);
    }

    /**
     * 得到某年的某月的天数且这月的第一天是星期几
     */
    public void initDaysOfMonth(int year, int month) {
        /**
         * 某月的总天数
         */
        daysOfMonth = mCalendarExtend.getDaysOfMonth(year, month);
        /**
         * 某月第一天为星期几
         */
        dayOfWeek = mCalendarExtend.getWeekdayOfMonthFirstDay(year, month);
        /**
         * 上一个月的总天数
         */
        int lastM;
        int lastY;
        if (month == 0) {
            lastM = 11;
            lastY = year - 1;
        } else {
            lastM = month - 1;
            lastY = year;
        }
        lastDaysOfMonth = mCalendarExtend.getDaysOfMonth(lastY, lastM);

        Logger.e("daysOfMonth:" + daysOfMonth
                        + "\nyear:" + year
                        + "\nmonth:" + month
                        + "\ndayOfWeek:" + dayOfWeek
                        + "\nlastDaysOfMonth:" + lastDaysOfMonth
        );

        getWeek(year, month);
    }

    /**
     * 将一个月中的每一天的值添加入数组dayNuber中
     */

    private void getWeek(int year, int month) {
        for (int i = 0; i < dayNumber.length; i++) {
            Day day = new Day();
            if (i < 7 && dayOfWeek != 1) {
                /**
                 *  补上前一个月的最后几天
                 */
                day.monthDay = lastDaysOfMonth - (dayOfWeek - 2) + i;

                if (month == 0) {
                    day.year = year - 1;
                    day.month = 11;
                } else {
                    day.year = year;
                    day.month = month - 1;
                }
            } else if (i < (daysOfMonth + dayOfWeek)) { // 本月
                day.monthDay = i;
                day.month = month;
                day.year = year;
            } else {
                /**
                 * 下一个月
                 */
                day.monthDay = i % daysOfMonth;
                if (month == 11) {
                    day.year = year + 1;
                    day.month = 0;
                } else {
                    day.year = year;
                    day.month = month + 1;
                }
            }
            dayNumber[i] = day;
        }
    }

    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Day getItem(int position) {
        return dayNumber[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Day itemDay = dayNumber[position];

        boolean isInPeriod = periodProxy.isInPerid(itemDay.year, itemDay.month, itemDay.monthDay);

        holder.footer.setBackgroundColor(isInPeriod ? chooseColor : Color.WHITE);

        holder.dayTV.setText(String.valueOf(itemDay.monthDay));
        if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
            //周日或者周六
            if (position % 7 == 1 || position % 7 == 0) {

            }
            //今天
            holder.dayTV.setTextColor(sysDay.equals(itemDay) ? Color.RED : normalDayColor);
        } else {
            holder.dayTV.setTextColor(isInPeriod ? Color.WHITE : otherDayColor);
        }

        return convertView;
    }

<<<<<<< HEAD
    // 得到某年的某月的天数且这月的第一天是星期几
    public void getCalendar(int year, int month) {
        daysOfMonth = mCalendarExtend.getDaysOfMonth(year, month); // 某月的总天数
        dayOfWeek = mCalendarExtend.getWeekdayOfMonthFirstDay(year, month); // 某月第一天为星期几
        lastDaysOfMonth = mCalendarExtend.getDaysOfMonth(year, month - 1); // 上一个月的总天数
//        ILog.d("DAY:  isLeapyear:" + isLeapyear + "   daysOfMonth:" + daysOfMonth + "  dayOfWeek:" + dayOfWeek + "  lastDaysOfMonth:" + lastDaysOfMonth);
        getWeek(year, month);
    }

    // 将一个月中的每一天的值添加入数组dayNuber中
    private void getWeek(int year, int month) {
        int j = 1;
        // 得到当前月的所有日程日期(这些日期需要标记)
        for (int i = 0; i < dayNumber.length; i++) {
            Day day = new Day();
            day.year = year;
            if (i < dayOfWeek) { // 前一个月
                int temp = lastDaysOfMonth - dayOfWeek + 1;
                day.monthDay = temp + i;
                if (month == 0) {
                    day.year = year - 1;
                    day.month = 11;
                } else {
                    day.month = month - 1;
                }
            } else if (i < daysOfMonth + dayOfWeek) { // 本月
                day.monthDay = i - dayOfWeek + 1;
                day.month = month;
            } else { // 下一个月
                day.monthDay = j;
                if (month == 11) {
                    day.year = year + 1;
                    day.month = 0;
                } else {
                    day.month = month + 1;
                }
                j++;
            }
            dayNumber[i] = day;
        }
    }

    /**
     * 当前系统时间
     *
     * @return
     */
    public Day getSysDay() {
        return sysDay;
=======
    public Day getShowMonth() {
        return showDay;
>>>>>>> origin/master
    }

    static class ViewHolder {
        @InjectView(R.id.daydesc)
        TextView dayTV;
        @InjectView(R.id.footer)
        View footer;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
