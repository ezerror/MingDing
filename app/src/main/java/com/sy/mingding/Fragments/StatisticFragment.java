package com.sy.mingding.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.R;
import com.sy.mingding.Utils.DataUtil;

public class StatisticFragment extends BaseFragment {

    private View mRootView;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        mRootView = layoutInflater.inflate(R.layout.fragment_statistic,container,false);
        TEST(layoutInflater,container);
        return mRootView;
    }


    //TODO:待删除
    private void TEST(LayoutInflater layoutInflater, ViewGroup container) {
        Button getData =mRootView.findViewById(R.id.btn_getData);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtil.getWeekDateBegin();
                DataUtil.getWeekDateEnd();

            }
        });
        Button bmobgetData =mRootView.findViewById(R.id.btn_bmob_getData);
        bmobgetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
