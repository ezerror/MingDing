package com.sy.mingding.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sy.mingding.R;
import com.sy.mingding.Utils.BeanUtils.TimingUtil;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.widget.CountdownView;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;



/**
 * @Author: ez
 * @Time: 2019/2/14 2:12
 * @Description: HomePage For CountdownView
 */

public class TimerActivity extends AppCompatActivity {

    private CountdownView countdownView;
    private Button btnStart;
    private Button btnPause;
    private Button btnStop;
    public boolean isPause=false;
    public boolean isStop=true;
    private int time = 0;//表盘时间
    private int timing =0;//准备倒计时的时间
    private Date startTime;
    private Date endTime;
    private Timer mTimer;
    private String mTodoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);
        init();
    }

    private void init() {
        mTodoId = getIntent().getStringExtra("todo_id");


        //TODO:
        countdownView = findViewById(R.id.view_countdown);
        btnStart = findViewById(R.id.btn_start);
        btnPause=findViewById(R.id.btn_pause);
        btnStop=findViewById(R.id.btn_stop);
        // 设置倒计时时长
        countdownView.setCountdown(time);
        // 设置倒计时改变监听
        countdownView.setOnCountdownListener(new CountdownView.OnCountdownListener() {
            @Override
            public void countdown(int time) {
                TimerActivity.this.time = time*60;
            }
        });

        // 开始倒计时监听
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置监听关闭
                startTimer();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });
    }
    public void startTimer(){
        Calendar calendar= Calendar.getInstance();
        startTime=calendar.getTime();
        LogUtil.d("TimeActivity","startTime"+startTime);
        countdownView.setCountdownFlag(false);
        btnStart.setVisibility(View.INVISIBLE);
        btnPause.setVisibility(View.VISIBLE);
        isPause=false;
        timing =time;
        if(isStop) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isPause) {
                                countdownView.setCountdown(time);
                                if (time == 0) {
                                    mTimer.cancel();
                                    isStop = true;
                                    btnStart.setVisibility(View.VISIBLE);
                                    btnPause.setVisibility(View.INVISIBLE);
                                    countdownView.setCountdownFlag(true);
                                    if(timing !=0){
                                        Calendar calendar= Calendar.getInstance();
                                        endTime=calendar.getTime();
                                        LogUtil.d("TimeActivity","endTime"+endTime);
                                        TimingUtil.addTiming(mTodoId, timing,startTime,endTime);
                                    }
                                } else {
                                    time--;
                                }
                            }
                        }
                    });
                }
            }, 1000, 1000);

            isStop=false;
        }
    }
    public  void stopTimer(){
        timing =0;
        time=0;
        countdownView.setCountdown(time);
        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.INVISIBLE);
        isStop=true;
        countdownView.setCountdownFlag(true);
        mTimer.cancel();
    }

    public void pauseTimer() {
        isPause=true;
        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.INVISIBLE);
    }
}