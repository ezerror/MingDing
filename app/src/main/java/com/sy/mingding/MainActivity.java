

package com.sy.mingding;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sy.mingding.Activity.LoginActivity;
import com.sy.mingding.Activity.MainViewActivity;
import com.sy.mingding.Activity.RegisterActivity;
import com.sy.mingding.widget.FullScreenVideoView;


public class MainActivity extends FragmentActivity {


    private FullScreenVideoView mVideoView;
    private TextView mLoginButton;
    private TextView mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);
        mVideoView = (FullScreenVideoView) this.findViewById(R.id.videoView);
        playVideoView();
        mLoginButton = findViewById(R.id.login_button_tv);
        mRegisterButton = findViewById(R.id.register_button_tv);
        initEvent();

    }

    private void initEvent() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void playVideoView() {
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //播放
        mVideoView.start();
        //循环播放
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVideoView.start();
            }
        });
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        playVideoView();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        mVideoView.stopPlayback();
        super.onStop();
    }


}
