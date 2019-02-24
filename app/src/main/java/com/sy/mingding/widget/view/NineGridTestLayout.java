package com.sy.mingding.widget.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sy.mingding.Interface.OnItemPictureClickListener;
import com.sy.mingding.Utils.CommonUtils;

import java.util.List;

/**
 * 一般项目就实现NineGridLayout类即可，如果没有特殊需求，不要改动NineGridLayout类
 */
public class NineGridTestLayout extends NineGridLayout {

    private Context context;
    private int itemPosition;
    private OnItemPictureClickListener listener;

    public NineGridTestLayout(Context context) {
        this(context,null);
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void displayImage(int position,RatioImageView imageView, String url) {
        if(context!=null){
            Picasso.with(context).load(url).into(imageView);
            imageView.setTag(CommonUtils.getNameByPosition(itemPosition,position));
            imageView.setTransitionName(CommonUtils.getNameByPosition(itemPosition,position));
        }
    }

    @Override
    protected void onClickImage(int imageIndex, String url, List<String> urlList, ImageView imageView) {
        listener.onItemPictureClick(itemPosition,imageIndex,url,urlList,imageView);
    }


    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public void setListener(OnItemPictureClickListener listener) {
        this.listener = listener;
    }
}
