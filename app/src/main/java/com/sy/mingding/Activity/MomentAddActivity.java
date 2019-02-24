package com.sy.mingding.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sy.mingding.Adapter.ReleaseMsgAdapter;
import com.sy.mingding.Interface.ImagePressInterface;
import com.sy.mingding.R;
import com.sy.mingding.Utils.BeanUtils.MomentUtil;
import com.sy.mingding.Utils.LogUtil;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.foamtrace.photopicker.PhotoPickerActivity.EXTRA_RESULT;

public class MomentAddActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView textCancel;
    private TextView textRelease;
    private RecyclerView mRec;
    private EditText etMesssage;
    private ArrayList<String> listImagePath;
    private ArrayList<String> mList = new ArrayList<>();
    private ReleaseMsgAdapter adapter;
    private ArrayList<String> mData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moment);
        initView();
        initEvent();
        setRecyclerview();

    }

    private void initEvent() {
        textCancel.setOnClickListener(this);
        textRelease.setOnClickListener(this);

    }

    private void initView() {
        textCancel = findViewById(R.id.tv_addmoment_cancel);
        textRelease = findViewById(R.id.tv_addmoment_release);
        etMesssage = findViewById(R.id.et_addmoment_message);
        mRec = findViewById(R.id.rv_release_moment);
    }

    private void setRecyclerview() {
        if (mList != null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            mRec.setLayoutManager(gridLayoutManager);
            adapter = new ReleaseMsgAdapter(MomentAddActivity.this, mList);
            mRec.setAdapter(adapter);
        }

    }

    //用户选中图片后，拿到回掉结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            listImagePath = data.getStringArrayListExtra(EXTRA_RESULT);
            compress(listImagePath);
        }
    }

    //压缩 拿到返回选中图片的集合url，然后转换成file文件
    public void compress(final ArrayList<String> list) {
        mData.clear();
        compressImage(list, new ImagePressInterface() {
            @Override
            public void onAllImagePressDoneListener(int num) {
                if (num == list.size()) {
                    adapter.addMoreItem(mData);
                }
            }
        });
        adapter.setRawPath(list);

    }

    //压缩
    private void compressImage(ArrayList<String> list, @NonNull final ImagePressInterface imagePressInterface) {
        final int[] num = {0};
        for (String imageUrl : list) {
            LogUtil.e(">>>>>>", imageUrl);
            File file = new File(imageUrl);

            Luban.with(this)//用的第三方的压缩，开源库  Luban 大家可以自行百度
                    .load(file)                     //传人要压缩的图片
                    .putGear(3)      //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() { //设置回调
                        @Override
                        public void onStart() {
                            //TODO 压缩开始前调用，可以在方法内启动 loading UI
                        }

                        @Override
                        public void onSuccess(final File file) {
                            num[0]++;
                            URI uri = file.toURI();
                            String[] split = uri.toString().split(":");
                            mData.add(split[1]);//压缩后返回的文件，带file字样，所以需要截取
                            imagePressInterface.onAllImagePressDoneListener(num[0]);
                            LogUtil.e(">>>>>>", uri + "????????????" + split[1]);

                        }

                        @Override
                        public void onError(Throwable e) {
                            //TODO 当压缩过去出现问题时调用
                        }
                    }).launch();//启动压缩
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addmoment_cancel:
                finish();
                break;
            case R.id.tv_addmoment_release:
                MomentUtil.addMomentItem(mData,etMesssage.getText().toString());
                finish();
                break;

        }
    }
}
