package com.qinchu.app.fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.qinchu.app.R;
import com.qinchu.app.activity.LoginActivity;
import com.qinchu.app.activity.MainActivity;
import com.qinchu.app.base.BaseFragment;
import com.qinchu.app.base.MessageToast;
import com.qinchu.app.db.UserProxy;
import com.qinchu.app.entity.User;
import com.qinchu.app.proxy.SettingProxy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 设置界面
 */
public class SettingFragment extends BaseFragment {

    @InjectView(R.id.age)
    EditText ageEditText;
    @InjectView(R.id.height)
    EditText heightEditText;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setTitle("退出登录")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("退出登录你将失去已保存的信息,确认退出么")
                    .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int uid = SettingProxy.getUid();
                            SettingProxy.saveUid(-1);
                            UserProxy.logout(uid);
                            Intent intent = new Intent(SettingFragment.this.getActivity(), LoginActivity.class);
                            startActivity(intent);
                            SettingFragment.this.getActivity().finish();
                        }
                    })

                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();

            return true;
        }
        return super.onOptionsItemSelected(item);
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
        User user = UserProxy.getUser(SettingProxy.getUid());
        if (user != null) {
            ageEditText.setText(String.valueOf(user.getAge()));
            countEditText.setText(String.valueOf(user.getCount()));
            periodEditText.setText(String.valueOf(user.getPeriod()));
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

    @OnClick(R.id.save)
    public void save() {
        int uid = SettingProxy.getUid();
        //TODO 登录的话需要修改的UID,但是这里只是一个用户就先写死了
        boolean showMain = false;
        if (uid == -1) {
            //如果是第一次,那么保存完应该进入到MainFragment
            showMain = true;
            uid = 0x0329;
        }

        User user = new User();
        user.setUid(uid);
        user.setName("秦楚");
        //password 一般是不可以直接存储明文的,常见的简单的都是一般简单的处理一下明文然后取明文的MD5值,这里随便写一下
        user.setPassword(String.valueOf("秦楚".hashCode()));

        try {

            String errorMsg = null;

            String age = ageEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(age)) {
                user.setAge(Integer.valueOf(age));
            }

            String height = heightEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(height)) {
                user.setHeight(Integer.valueOf(height));
            }
            String count = countEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(count)) {
                user.setCount(Integer.valueOf(count));
            } else {
                errorMsg = "请输入每次姨妈持续时间";
            }
            String period = periodEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(period)) {
                user.setPeriod(Integer.valueOf(period));
            } else {
                errorMsg = "请输入姨妈周期";
            }

            String startDateString = user.getStartDate();
            if (startDateString == null) {
                errorMsg = "请输入上次姨妈开始时间";
            }

            if (errorMsg != null) {
                MessageToast.show(errorMsg, MessageToast.Style.ALERT);
                return;
            }

            UserProxy.saveUser(user);
            SettingProxy.saveUid(user.getUid());

            ((MainActivity) getActivity()).showControl(showMain);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
<<<<<<< HEAD
        int height = SettingProxy.getHeight();
        if (height != -1) {
            heightEditText.setText(String.valueOf(height));
        }
        long startDate = SettingProxy.getStartDate();
        if (startDate != -1) {
            Date date = new Date();
            date.setTime(startDate);
            startdateTextView.setText(simple_formatter.format(date));
=======
        User user = UserProxy.getUser(SettingProxy.getUid());
        if (user != null) {
            heightEditText.setText(String.valueOf(user.getHeight()));
            String startDateString = user.getStartDate();
            long startDate = Long.valueOf((startDateString == null ? "-1" : startDateString));
            if (startDate != -1) {
                Date date = new Date();
                date.setTime(startDate);
                startdateTextView.setText(simple_formatter.format(date));
            }
>>>>>>> origin/master
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
