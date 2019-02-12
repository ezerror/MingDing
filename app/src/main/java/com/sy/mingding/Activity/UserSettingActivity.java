package com.sy.mingding.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;
import com.sy.mingding.Utils.ActivityManager;
import com.sy.mingding.Utils.Constants;
import com.sy.mingding.Utils.ContentUriUtil;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.Utils.BeanUtils.UserUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettingActivity extends Activity implements View.OnClickListener {

    private Toolbar mUserTopBar;
    private Button mUserLogoutButton;
    private AppCompatButton mNickNameAlterButton;
    private AlertDialog.Builder builder;
    private View mRootContentView;
    private TextView mNickNameTV;
    private TextView mUserNameTV;
    private ProgressDialog progressDialog;
    private CircleImageView mProfile_image;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        setContentView(R.layout.activity_user_settings);
        initView();
        initEvent();

    }

    private void initView() {
        mUserTopBar = findViewById(R.id.user_top_bar);
        mUserLogoutButton = findViewById(R.id.user_logout_btn);
        mNickNameAlterButton = findViewById(R.id.alter_nickname_btn);
        mNickNameTV = findViewById(R.id.nickname);
        mUserNameTV = findViewById(R.id.username);
        mRootContentView = getWindow().getDecorView().findViewById(android.R.id.content);
        mProfile_image = findViewById(R.id.profile_image_usersettings);

    }

    private void initEvent() {
        //返回键
        mUserTopBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUserLogoutButton.setOnClickListener(this);
        mNickNameAlterButton.setOnClickListener(this);
        mProfile_image.setOnClickListener(this);
        refresh();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_logout_btn:
                BmobUser.logOut();
                ActivityManager.exit();
                Intent intent = new Intent(UserSettingActivity.this, StartActivity.class);
                startActivity(intent);
                break;
            case R.id.alter_nickname_btn:
                alterNickname();
                break;
            case R.id.profile_image_usersettings:
                Intent image_intent = new Intent(Intent.ACTION_GET_CONTENT);
                image_intent.setType("image/*");
                startActivityForResult(image_intent, Constants.REQUEST_GALLERY);
                break;

        }
    }

    private void alterNickname() {
        final EditText editText = new EditText(this);
        builder = new AlertDialog.Builder(this).setTitle("修改昵称").setView(editText).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserUtil.User_update(mRootContentView, editText.getText().toString(), "nickname");
                        showWaiting();
                    }
                });
        builder.create().show();

    }

    private void showWaiting() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在应用更改...");
        progressDialog.setIndeterminate(true);// 是否形成一个加载动画  true表示不明确加载进度形成转圈动画  false 表示明确加载进度
        progressDialog.setCancelable(false);//点击返回键或者dialog四周是否关闭dialog  true表示可以关闭 false表示不可关闭
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
                progressDialog.hide();

            }
        }, 500);//1秒后执行Runnable中的run方法

    }

    private void refresh() {
        User user = UserUtil.get_user();
        LogUtil.d(this, "nickname-->" + user.getNickname());
        mNickNameTV.setText(user.getNickname());
        mUserNameTV.setText(user.getUsername());
        if(user.getIcon()!=null) {
            Picasso.with(this)
                    .load(user.getIcon().getUrl())
                    .placeholder(R.drawable.ic_profile_boy)
                    .into(mProfile_image);
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case Constants.REQUEST_GALLERY:
                    Uri uri = data.getData();
                    String path = getImageUrlWithAuthority(this,uri);
                    LogUtil.d("ddddddd",path);

                    ContentResolver cr = this.getContentResolver();
                    try {
                        mBitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        /* 将Bitmap设定到ImageView */
                        mProfile_image.setImageBitmap(mBitmap);

                        final BmobFile profile_icon=new BmobFile(new File(ContentUriUtil.getPath(this,uri).toString()));
                        profile_icon.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                    LogUtil.d("上传文件成功:" , profile_icon.getFileUrl());
                                    UserUtil.User_update_icon(mRootContentView,profile_icon);

                                }else{
                                    LogUtil.d("上传文件失败：" , e.getMessage());
                                }
                            }
                        });



                    }
                    catch (FileNotFoundException e) {
                        LogUtil.e("onActivityResult", e.getMessage());
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





    //获取谷歌图片的地址
    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 将图片流读取出来保存到手机本地相册中
     **/
    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
