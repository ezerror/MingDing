package com.sy.mingding.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.R;

public class StatisticFragment extends BaseFragment {

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView=layoutInflater.inflate(R.layout.fragment_statistic,container,false);
        return rootView;

    }
}
