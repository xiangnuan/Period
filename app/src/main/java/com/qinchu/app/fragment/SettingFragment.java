package com.qinchu.app.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.qinchu.app.R;
import com.qinchu.app.base.BaseFragment;
import com.qinchu.app.proxy.SettingProxy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 设置界面
 */
public class SettingFragment extends BaseFragment {

    @InjectView(R.id.age)
    EditText ageEditText;
    @InjectView(R.id.height)
    EditText heightEditText;
    @InjectView(R.id.firstAge)
    EditText firstAgeEditText;
    @InjectView(R.id.count)
    EditText countEditText;
    @InjectView(R.id.period)
    EditText periodEditText;
    @InjectView(R.id.startdate)
    TextView startdateTextView;

    private static final SimpleDateFormat simple_formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    private DateReciver mDateReciver = new DateReciver();

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int age = SettingProxy.getAge();
        if (age != -1) {
            ageEditText.setText(String.valueOf(age));
        }
        int firstAge = SettingProxy.getFirstAge();
        if (firstAge != -1) {
            firstAgeEditText.setText(String.valueOf(firstAge));
        }
        int count = SettingProxy.getCount();
        if (count != -1) {
            countEditText.setText(String.valueOf(count));
        }
        int period = SettingProxy.getPeriod();
        if (period != -1) {
            periodEditText.setText(String.valueOf(period));
        }

        /**
         * 这里是new 了一个OnClickListener,也可以直接使用butterknife的@OnClick注解
         * butterknife的优点在于在编译期起作用,不会影响性能,简化一些重复代码,本质和findViewById完全相同
         */

        startdateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getChildFragmentManager(), "datepicker");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        int height = SettingProxy.getHeight();
        if (height != -1) {
            heightEditText.setText(String.valueOf(height));
        }
        long startDate = SettingProxy.getStartDate();
        if (startDate != -1) {
            Date date = new Date();
            date.setTime(startDate);
            startdateTextView.setText(simple_formatter.format(date));
        }

        /**
         * LocalBroadcastManager控制的广播,这里因为是启动的一个子DatePickerFragment设置时间,不会触发onResume方法,通过广播来触发
         * LocalBroadcastManager和普通的广播相比更加具有安全性,因为这样保证了广播只在本应用内起作用
         */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.qc.update.ui");
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(mDateReciver, intentFilter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /**
         * 如果setting界面隐藏了,那么就存储一次值
         */
        if (hidden) {
            try {
                String age = ageEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(age)) {
                    SettingProxy.saveAge(Integer.valueOf(age));
                }
                String height = heightEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(height)) {
                    SettingProxy.saveHeight(Integer.valueOf(height));
                }
                String firstAge = firstAgeEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(firstAge)) {
                    SettingProxy.saveFirstAge(Integer.valueOf(firstAge));
                }
                String count = countEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(count)) {
                    SettingProxy.saveCount(Integer.valueOf(count));
                }
                String period = periodEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(period)) {
                    SettingProxy.savePeriod(Integer.valueOf(period));
                }
            } catch (Exception e) {

            }
        }
        Log.e(getTagName(), "onHiddenChanged:" + hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * 广播一般在onResume注册,onPause取消注册,避免内存泄露
         */
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(mDateReciver);
    }

    class DateReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long startDate = SettingProxy.getStartDate();
            if (startDate != -1) {
                Date date = new Date();
                date.setTime(startDate);
                startdateTextView.setText(simple_formatter.format(date));
            }
        }
    }

}
