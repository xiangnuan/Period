package com.qinchu.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.qinchu.app.R;
import com.qinchu.app.base.BaseFragment;
import com.qinchu.app.fragment.AnalyseFragment;
import com.qinchu.app.fragment.MainFragment;
import com.qinchu.app.fragment.SettingFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {


    private static final String MAIN_TAG = "tag-main";
    private static final String SETTING_TAG = "tag-setting";
    private static final String ANALYSE_TAG = "tag-analyse";

    private Fragment mainFragment = new MainFragment();
    private Fragment analyseFragment = new AnalyseFragment();
    private Fragment settingFragment = new SettingFragment();

    @InjectView(R.id.toolbar)
    Toolbar mToolBar;

    private boolean comfirmExit = false;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            comfirmExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        setSupportActionBar(mToolBar);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        mToolBar.setLogoDescription("秦楚的APP");
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setNavigationIcon(R.mipmap.ic_launcher);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFragment, MAIN_TAG).commit();
    }

    @OnClick({R.id.calendar, R.id.tool, R.id.setting})
    public void control(View v) {
        int vId = v.getId();
        String tag;
        Fragment chooseFragment;
        switch (vId) {
            case R.id.tool:
                tag = ANALYSE_TAG;
                chooseFragment = analyseFragment;
                break;
            case R.id.setting:
                tag = SETTING_TAG;
                chooseFragment = settingFragment;
                break;
            case R.id.calendar:
            default:
                tag = MAIN_TAG;
                chooseFragment = mainFragment;
                break;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(mainFragment)
                    .hide(settingFragment)
                    .hide(analyseFragment)
                    .show(chooseFragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(mainFragment)
                    .hide(settingFragment)
                    .hide(analyseFragment)
                    .add(R.id.fragment_container, chooseFragment, tag)
                    .show(chooseFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (comfirmExit) {
            mHandler.removeCallbacks(mRunnable);
            this.finish();
        } else {
            comfirmExit = true;
            mHandler.postDelayed(mRunnable, 2000);
            Toast.makeText(this, "再次按返回键退出应用", Toast.LENGTH_SHORT).show();
        }
    }
}
