package com.sy.mingding.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sy.mingding.Interface.ConfirmDialogInterface;
import com.sy.mingding.R;
import com.sy.mingding.Utils.BeanUtils.TimingUtil;
import com.sy.mingding.Utils.DialogUtil;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.widget.CountdownView;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: ez
 * @Time: 2019/2/18 21:27
 * @Description: 功能描述
 */
public class FreeTimerActivity extends AppCompatActivity {
    private CountdownView countdownView;
    private Button btnStart;
    private Button btnPause;
    private Button btnStop;
    public boolean isPause=false;
    public boolean isStop=true;
    private int time = 0;//表盘时间
    private Date startTime;
    private Date endTime;
    private Timer mTimer;
    private String mTodoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_timing);
        init();

    }
    private void init() {

        //TODO:
        countdownView = findViewById(R.id.free_countdown);
        btnStart = findViewById(R.id.btn_start);
        btnPause=findViewById(R.id.btn_pause);
        btnStop=findViewById(R.id.btn_stop);
        countdownView.setDrawDialFlag(false);
        // 设置时间
        countdownView.setCountdown(0);

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
        Calendar calendar = Calendar.getInstance();
        startTime= calendar.getTime();
        LogUtil.d("FreeTimerActivity","startTime"+startTime);
        btnStart.setVisibility(View.INVISIBLE);
        btnPause.setVisibility(View.VISIBLE);
        isPause=false;
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
                                time=time+1000;
                            }
                        }
                    });
                }
            }, 1000, 1000);

            isStop=false;
        }
    }
    public  void stopTimer(){
        if(time!=0){
            countdownView.setCountdown(0);
            btnStart.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.INVISIBLE);
            isStop=true;
            countdownView.setCountdownFlag(true);
            mTimer.cancel();
            DialogUtil.showNormalInputDialog(getWindow().getDecorView(), "请输入该TIMING名称", new ConfirmDialogInterface() {
                @Override
                public void onConfirmClickListener(String content) {
                    Toast.makeText(getBaseContext(), "放进去了", Toast.LENGTH_SHORT).show();
                    Calendar calendar = Calendar.getInstance();
                    endTime=calendar.getTime();
                    TimingUtil.addTimingFromFree(content,time,startTime,endTime);
                    time=0;
                }

                @Override
                public void onCancelClickListener(String content) {

                }
            });
        }


    }

    public void pauseTimer() {
        if(time!=0){
            isPause=true;
            btnStart.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.INVISIBLE);
        }
    }
}
