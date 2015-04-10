package com.qinchu.app.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.qinchu.app.R;
import com.qinchu.app.base.BaseFragment;
import com.qinchu.app.entity.Day;
import com.qinchu.app.tool.CalendarAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends BaseFragment {

    private static int sensibility;
    /**
     * 每次滑动，增加或减去一个月,默认为0（即显示当前月）
     */
    private static int jumpMonth = 0;
    /**
     * 当前的年月，现在日历顶端
     */
    @InjectView(R.id.calendar_flipper)
    ViewFlipper mFlipper;
    TextView calendarTitle;

    private GestureDetector gestureDetector = null;
    private CalendarAdapter mAdapter = null;
    private GridView gridView = null;
    private int year_c = 0;
    private int month_c = 0;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View attachView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, attachView);
        return attachView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarTitle = (TextView) view.findViewById(R.id.calendarTitle);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        sensibility = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
        gestureDetector = new GestureDetector(this.getActivity(), new CalendarGestureListener());

        initFlipper();
    }

    private void initFlipper() {
        Time time = new Time();
        time.setToNow();
        year_c = time.year;
        month_c = time.month + 1;
        jumpMonth = 0;
        if (mFlipper.getChildCount() > 0) {
            mFlipper.removeAllViews();
        }
        mAdapter = new CalendarAdapter(this.getActivity(), jumpMonth, year_c, month_c);
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.none_animation));
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.none_animation));
        addGridView();
        gridView.setAdapter(mAdapter);
        mFlipper.addView(gridView, 0);
        resetTitle(mAdapter.getSysDay());
    }

    /**
     * 移动到下一个月
     */
    public void enterNextMonth(View v) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        mAdapter = new CalendarAdapter(this.getActivity(), jumpMonth, year_c, month_c);
        gridView.setAdapter(mAdapter);
        mFlipper.addView(gridView, mFlipper.getChildCount());
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_in_left));
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_out_left));
        mFlipper.showNext();
        mFlipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     */
    public void enterPrevMonth(View v) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月
        mAdapter = new CalendarAdapter(this.getActivity(), jumpMonth, year_c, month_c);
        gridView.setAdapter(mAdapter);
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_in_right));
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_out_right));
        mFlipper.addView(gridView, mFlipper.getChildCount());
        mFlipper.showPrevious();
        mFlipper.removeViewAt(0);
    }

    private void addGridView() {
        gridView = new GridView(this.getActivity());
        gridView.setNumColumns(7);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setHorizontalScrollBarEnabled(false);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setVerticalSpacing(2);
        gridView.setHorizontalSpacing(2);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        gridView.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
    }

    private void resetTitle(Day sysDay) {
        calendarTitle.setText(sysDay.toString());
    }

    private class CalendarGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > sensibility) {
                // 像左滑动
                enterNextMonth(null);
                return true;
            } else if (e1.getX() - e2.getX() < -sensibility) {
                // 向右滑动
                enterPrevMonth(null);
                return true;
            }
            return false;
        }
    }

}
