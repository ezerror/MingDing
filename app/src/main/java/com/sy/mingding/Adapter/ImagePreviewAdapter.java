package com.sy.mingding.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;

import com.squareup.picasso.Picasso;
import com.sy.mingding.Utils.CommonUtils;

import java.util.List;




/**
 * @Description:
 * @Author: Liangchaojie
 * @Create On 2018/3/30 10:33
 */
public class ImagePreviewAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageList;
    private int itemPosition;
    private PhotoView photoView;
    public ImagePreviewAdapter(Context context, List<String> imageList, int itemPosition) {
        this.context = context;
        this.imageList = imageList;
        this.itemPosition = itemPosition;
    }

    @Override
    public int getCount() {
        return imageList==null?0:imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoView image = new PhotoView(context);
        // 启用图片缩放功能

        image.enable();
        Picasso.with(context).load(imageList.get(position)).into(image);


        image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               image.setEnabled(false);
               ((Activity)context).onBackPressed();
           }
       });
        container.addView(image);
        return image;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        photoView = (PhotoView) object;
        photoView.setTag(CommonUtils.getNameByPosition(itemPosition,position));
        photoView.setTransitionName(CommonUtils.getNameByPosition(itemPosition,position));
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public PhotoView getPhotoView() {
        return photoView;
    }

}
