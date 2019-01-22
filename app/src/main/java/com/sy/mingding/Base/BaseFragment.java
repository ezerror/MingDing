package com.sy.mingding.Base;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {


    private View mRoorview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoorview = onSubViewLoaded(inflater,container);
        return mRoorview;
    }
    protected abstract View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container);

}
