package com.qinchu.app.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.DatePicker;

import com.qinchu.app.db.UserProxy;
import com.qinchu.app.entity.User;
import com.qinchu.app.proxy.SettingProxy;

import java.util.Calendar;

/**
 * Created by haoxiqiang on 15/4/7.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        User user = UserProxy.getUser(SettingProxy.getUid());
        user.setStartDate(String.valueOf(calendar.getTimeInMillis()));
        UserProxy.saveUser(user);
//        SettingProxy.saveStartDate(calendar.getTimeInMillis());
        Intent intent = new Intent();
        intent.setAction("com.qc.update.ui");
        LocalBroadcastManager.getInstance(this.getActivity()).sendBroadcast(intent);
    }
}
