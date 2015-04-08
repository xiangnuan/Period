package com.qinchu.app.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by haoxiqiang on 15/4/8.
 */
public class BaseActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageToast.cancelAllToast();
    }
}
