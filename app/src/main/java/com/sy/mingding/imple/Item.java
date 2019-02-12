package com.sy.mingding.imple;

public class Item {
    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int index);
    }


    public void setOnItemClickListener2(OnItemClickListener onItemClickListener) {
        mOnItemClickListener=onItemClickListener;
    }
};



