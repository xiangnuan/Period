package com.qinchu.app.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by haoxiqiang on 15/4/7.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getTagName(), "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getTagName(), "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected String getTagName() {
        return this.getClass().getSimpleName();
    }
}
