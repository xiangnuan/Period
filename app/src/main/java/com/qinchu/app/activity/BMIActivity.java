package com.qinchu.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.qinchu.app.R;
import com.qinchu.app.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class BMIActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        ButterKnife.inject(this);

        setSupportActionBar(mToolBar);

        getSupportActionBar().setTitle("BMI分析");

        mToolBar.setLogoDescription("秦楚的APP");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationIcon(R.mipmap.ic_launcher);
    }

}
