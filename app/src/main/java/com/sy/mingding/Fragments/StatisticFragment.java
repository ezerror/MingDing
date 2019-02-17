package com.sy.mingding.Fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sy.mingding.Adapter.TimeLineAdapter;
import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.Bean.Timing;
import com.sy.mingding.R;
import com.sy.mingding.Utils.LogUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class StatisticFragment extends BaseFragment {

    private View mRootView;
    private RecyclerView mRvTimeLine;


    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        mRootView = layoutInflater.inflate(R.layout.fragment_statistic,container,false);
        initView(layoutInflater,container);
        initData();
        return mRootView;
    }

    private void initView(LayoutInflater layoutInflater, ViewGroup container) {

        mRvTimeLine = mRootView.findViewById(R.id.rv_timeline);
    }
    private void initData() {

        mRvTimeLine.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //获取timing
        BmobQuery<Timing> TimingBmobQuery = new BmobQuery<>();
        TimingBmobQuery.include("todo,todo.project");
        TimingBmobQuery.findObjects(new FindListener<Timing>() {
            @Override
            public void done(List<Timing> object, BmobException e) {
                if (e == null) {
                    TimeLineAdapter mTimeLineAdapter = new TimeLineAdapter();
                    mRvTimeLine.setAdapter(mTimeLineAdapter);
                    mTimeLineAdapter.setData(object);
                    mTimeLineAdapter.notifyDataSetChanged();

                    LogUtil.e("BMOBUTIL", "成功");

                } else {
                    LogUtil.e("BMOBUTIL", "shibai");
                }
            }
        });

    }

}
