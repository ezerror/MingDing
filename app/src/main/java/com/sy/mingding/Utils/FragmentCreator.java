package com.sy.mingding.Utils;

import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.Constants.Constants;
import com.sy.mingding.Fragments.MomentFragment;
import com.sy.mingding.Fragments.StatisticFragment;
import com.sy.mingding.Fragments.TodoFragment;

import java.util.HashMap;
import java.util.Map;


public class FragmentCreator {


    private  static Map<Integer,BaseFragment> sCache =new HashMap<>();
    public static  BaseFragment getFragment(int index){
        BaseFragment baseFragment =sCache.get(index);
        if (baseFragment != null) {
            return baseFragment;
        }
        switch (index){
            case Constants.INDEX_TODO:
                baseFragment =new TodoFragment();
                break;
            case Constants.INDEX_STATISTIC:
                baseFragment =new StatisticFragment();
                break;
            case Constants.INDEX_SETTINGS:
                baseFragment =new MomentFragment();
                break;
        }
        sCache.put(index,baseFragment);
        return baseFragment;
    }

}
