package com.sy.mingding.Utils.BeanUtils;

import com.sy.mingding.Bean.Moment;
import com.sy.mingding.Utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * @Author: ez
 * @Time: 2019/2/22 15:40
 * @Description: 功能描述
 */
public class MomentUtil {
    public static void addMomentItem(ArrayList<String> data, String message){
        final Moment mt=new Moment();
        mt.setText(message);
        mt.setUser(UserUtil.get_user());
        if (data.size() != 0){
            final String[] array = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                array[i] = data.get(i);
            }
            BmobFile.uploadBatch(array, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {
                    if (urls.size() == array.length) {
                        mt.setPhotoList(files);
                        mt.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null) {
                                    LogUtil.d("uploadLog","上传成功");
                                }   else {
                                    LogUtil.d("uploadLog","上传失败"+e.getMessage()+e.getErrorCode());
                                }
                            }
                        });
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }else {
            mt.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e != null) {
                        LogUtil.d("uploadLog","上传成功");
                    }   else {
                        LogUtil.d("uploadLog","上传失败");
                    }
                }
            });
        }
    }
}
