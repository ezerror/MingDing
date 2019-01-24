package com.sy.mingding.Fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.sy.mingding.Activity.UserActivity;
import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.R;
import com.sy.mingding.Utils.LogUtil;

public class TodoFragment extends BaseFragment {

    private View mRootView;
    private static final String TAG = "TodoFragment";



    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, final ViewGroup container) {
        mRootView = layoutInflater.inflate(R.layout.fragment_todo,container,false);

        //找到顶部控件
        Toolbar TodoTopBar =mRootView.findViewById(R.id.todo_top_bar);
        LogUtil.d(TAG,"线程名称-->"+Thread.currentThread().getName());
        TodoTopBar.inflateMenu(R.menu.todo_top_bar_menu);

        //BarSettings
        //TODO:


        //跳转到用户设置中
        TodoTopBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //Toast.makeText(container.getContext(), "大家好", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),UserActivity.class);
                startActivity(intent);

            }
        });



        mRootView.invalidate();

        return mRootView;

    }



}
