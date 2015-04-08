package com.qinchu.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qinchu.app.activity.BMIActivity;
import com.qinchu.app.R;
import com.qinchu.app.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyseFragment extends BaseFragment {


    public AnalyseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analyse, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.bmi)
    public void bmi(View v) {
        Intent bmiIntent = new Intent(this.getActivity(), BMIActivity.class);
        startActivity(bmiIntent);
    }

}
