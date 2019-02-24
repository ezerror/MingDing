package com.sy.mingding.Utils;

import android.app.Activity;
import android.content.Intent;

import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;

import java.util.ArrayList;

/**
 * @Author: ez
 * @Time: 2019/2/22 0:52
 * @Description: 功能描述
 */
public class CommonUtils {
    public static Intent uploadPictures(Activity activity, int number, ArrayList<String> imagePaths,int requestCode){
        //加载图片
        PhotoPickerIntent intent = new PhotoPickerIntent(activity);
        intent.setSelectModel(SelectModel.MULTI);//多选
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(number); // 最多选择照片数量，默认为9
        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
        intent.putExtra("type","photo");//选择方式；
        activity.startActivityForResult(intent,requestCode);
        return intent;
    }

    public static String getNameByPosition(int itemPosition, int i) {
        String name = itemPosition+"_"+Name.IMAGE_1;
        switch (i){
            case 0:
                name = itemPosition+"_"+Name.IMAGE_1;
                break;
            case 1:
                name = itemPosition+"_"+Name.IMAGE_2;
                break;
            case 2:
                name = itemPosition+"_"+Name.IMAGE_3;
                break;
            case 3:
                name = itemPosition+"_"+Name.IMAGE_4;
                break;
            case 4:
                name = itemPosition+"_"+Name.IMAGE_5;
                break;
            case 5:
                name = itemPosition+"_"+Name.IMAGE_6;
                break;
            case 6:
                name = itemPosition+"_"+Name.IMAGE_7;
                break;
            case 7:
                name = itemPosition+"_"+Name.IMAGE_8;
                break;
            case 8:
                name = itemPosition+"_"+Name.IMAGE_9;
                break;

        }
        return name;
    }
}
