package com.sy.mingding.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.mingding.Activity.TimerActivity;
import com.sy.mingding.R;

/**
 * @Author: ez
 * @Time: 2019/2/17 18:31
 * @Description: 功能描述
 */
public class BottomAddTimingDialog extends BottomDialogBase implements View.OnClickListener {

    private CardView mCvFree;
    private TextView mTvTomato;
    private ImageView mImageTomato;
    private TextView mTvFree;
    private ImageView mImageFree;
    private int mSelectedState=0;
    private CardView mCvTodo;
    private ImageButton mBtnGo;
    private Intent mIntent;

    public BottomAddTimingDialog(Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate() {
        setContentView(R.layout.dialog_add_timing_bottom);
        initView();
        initEvent();


    }

    private void initEvent() {
        setTimingModelSelected(mSelectedState);
        mCvTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedState=0;
                setTimingModelSelected(mSelectedState);
            }
        });
        mCvFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedState=1;
                setTimingModelSelected(mSelectedState);
            }
        });
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go(mSelectedState);
            }
        });
    }

    private void Go(int selectedState) {
        if (selectedState==1){
            mIntent = new Intent(getContext(),TimerActivity.class);
            getContext().startActivity(mIntent);
            dismiss();
        }
    }

    private void initView() {
        mCvTodo = findViewById(R.id.cv_todo_timing);
        mCvFree = findViewById(R.id.cv_free_timing);
        mTvTomato = findViewById(R.id.tv_tomato);
        mImageTomato = findViewById(R.id.image_tomato);
        mTvFree = findViewById(R.id.tv_free);
        mImageFree = findViewById(R.id.image_free);
        mBtnGo = findViewById(R.id.dialog_add_timing_go);
    }

    private void setTimingModelSelected(int i) {
        mTvFree.setTextColor(Color.parseColor("#9198b2"));
        mImageFree.setBackgroundResource(R.drawable.ic_free);
        mTvTomato.setTextColor(Color.parseColor("#9198b2"));
        mImageTomato.setBackgroundResource(R.drawable.ic_tomato);
        switch (i) {
            case 0:
                mTvTomato.setTextColor(Color.parseColor("#22283d"));
                mImageTomato.setBackgroundResource(R.drawable.ic_tomato_selected);
                break;

            case 1:
                mTvFree.setTextColor(Color.parseColor("#22283d"));
                mImageFree.setBackgroundResource(R.drawable.ic_free_selected);
                break;
        }
    }

}
