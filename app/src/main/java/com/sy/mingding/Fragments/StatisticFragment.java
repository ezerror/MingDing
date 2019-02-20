package com.sy.mingding.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.sy.mingding.Activity.CalAsynchronousActivity;
import com.sy.mingding.Activity.ProjectManageActivity;
import com.sy.mingding.Adapter.TimeLineAdapter;
import com.sy.mingding.Base.BaseApplication;
import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.Bean.Timing;
import com.sy.mingding.Dialog.BottomAddTimingDialog;
import com.sy.mingding.R;
import com.sy.mingding.Utils.Constants;
import com.sy.mingding.Utils.DataUtil;
import com.sy.mingding.Utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class StatisticFragment extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private RecyclerView mRvTimeLine;
    private LinearLayout mLyOpenTiming;
    private Intent mIntent;
    private LinearLayout mMLyOpenTodo;
    private LinearLayout mMLyOpenCanlendar;
    private PieChart mContributionPiechart;
    final Map<String,Integer> pieChartMap = new HashMap();
    final Map<String,Float> pieChartMapData = new HashMap();
    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        mRootView = layoutInflater.inflate(R.layout.fragment_statistic,container,false);
        initView(layoutInflater,container);
        initData();
        initEvent();
        onHandleFresh();



        initChart();
        return mRootView;
    }

    private void initChart() {
        //获取数据
        try {
            getStatisticData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //填充图表
        showChart();
    }

    private void showChart() {
        List<PieEntry> yVals = new ArrayList<>();
        int sum=0;
        for (Integer value : pieChartMap.values()) {
            sum+=value;
        }

        for (Map.Entry<String,Integer > entry : pieChartMap.entrySet()) {
            float data=(float)entry.getValue()/sum;
            yVals.add(new PieEntry(data, entry.getKey()));
        }

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#4A92FC"));
        colors.add(Color.parseColor("#ee6e55"));
        colors.add(Color.parseColor("#59dbe0"));
        colors.add(Color.parseColor("#f8b552"));
        colors.add(Color.parseColor("#87d288"));

        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        Description description = new Description();
        description.setText("");
        mContributionPiechart.setDescription(description);
        pieData.setDrawValues(false);
        mContributionPiechart.setData(pieData);
    }

    private void getStatisticData() throws JSONException {

        BmobQuery<Timing> timingStatisticQuery = new BmobQuery<>();
        //总和
        timingStatisticQuery.sum(new String[]{"time"});
        timingStatisticQuery.setHasGroupCount(true);
        timingStatisticQuery.include("todo");

        //获取今日
        timingStatisticQuery.addWhereGreaterThanOrEqualTo("startTime", new BmobDate(DataUtil.getTodayDate().get("first")));
        timingStatisticQuery.addWhereLessThanOrEqualTo("endTime", new BmobDate(DataUtil.getTodayDate().get("last")));
        //根据所给列分组统计
        timingStatisticQuery.groupby(new String[]{"todo"});
        //开始统计查询
        timingStatisticQuery.findStatistics(Timing.class, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    try {
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int sum=jsonObject.getInt("_sumTime");
                            String todoName="自由活动";
                            if(jsonObject.optJSONObject("todo")!=null){
                                JSONObject todoObject=jsonObject.getJSONObject("todo");
                                todoName =todoObject.getString("todoName");
                            }
                            pieChartMap.put(todoName,sum);
                        }

                        showChart();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnStatistics, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dateRefresh();
    }

    private void onHandleFresh() {
        BaseApplication.setOnHandlerListener(new BaseApplication.HandlerListener() {
            @Override
            public void heandleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.HANDLER_REFRESH_TIMELINE:
                        dateRefresh();
                        break;

                    default:break;

                }
            }
        });
    }

    private void initEvent() {
        mLyOpenTiming.setOnClickListener(this);
        mMLyOpenTodo.setOnClickListener(this);
        mMLyOpenCanlendar.setOnClickListener(this);
    }

    private void initView(LayoutInflater layoutInflater, ViewGroup container) {

        mRvTimeLine = mRootView.findViewById(R.id.rv_timeline);
        mLyOpenTiming = mRootView.findViewById(R.id.ll_op_timing);
        mMLyOpenTodo = mRootView.findViewById(R.id.ll_op_todo);
        mMLyOpenCanlendar = mRootView.findViewById(R.id.ll_op_calendar);
        mContributionPiechart = mRootView.findViewById(R.id.contribution_piechart);
    }
    private void initData() {

        mRvTimeLine.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        getData();
    }

    private void dateRefresh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("getStatisticData","刷新完成");
                getData();
                initChart();
                //刷新完成
            }

        }, 1000);
    }
    private void getData(){
        //获取timing
        BmobQuery<Timing> TimingBmobQuery = new BmobQuery<>();
        TimingBmobQuery.include("todo,todo.project");
        TimingBmobQuery.order("-createdAt");
        TimingBmobQuery.addWhereGreaterThanOrEqualTo("startTime", new BmobDate(DataUtil.getTodayDate().get("first")));
        TimingBmobQuery.addWhereLessThanOrEqualTo("endTime", new BmobDate(DataUtil.getTodayDate().get("last")));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_op_timing:
                BottomAddTimingDialog dialog = new BottomAddTimingDialog(getContext());
                dialog.show();
                break;
            case R.id.ll_op_todo:
                mIntent = new Intent(mRootView.getContext(),ProjectManageActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_op_calendar:
                mIntent = new Intent(mRootView.getContext(),CalAsynchronousActivity.class);
                startActivity(mIntent);
                break;
            default:break;
        }
    }



}
