package com.sy.mingding.Interface;

import android.widget.ImageView;

import java.util.List;

public interface OnItemPictureClickListener{
    void onItemPictureClick(int itemPostion, int i, String url, List<String> urlList, ImageView imageView);
}
