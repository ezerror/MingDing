package com.sy.mingding.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sy.mingding.R;
import com.sy.mingding.Utils.CommonUtils;
import com.sy.mingding.Utils.LogUtil;

import java.util.ArrayList;

/**
 * @Author: ez
 * @Time: 2019/2/22 0:50
 * @Description: MOMENT编辑界面
 */
public class ReleaseMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private ArrayList<String> mList;

    private ArrayList<String> mRawList;
    private final LayoutInflater inflater;
    private static final int ITEM_TYPE_ONE = 0x00001;
    private static final int ITEM_TYPE_TWO = 0x00002;

    public ReleaseMsgAdapter(Activity context, ArrayList<String> mList) {
        this.context = context;
        this.mList = mList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setPadding(20, 0, 20, 0);
        switch (viewType) {
            case ITEM_TYPE_ONE:
                return new MyHolder(inflater.inflate(R.layout.item_release_message, parent, false));
            case ITEM_TYPE_TWO:
                return new MyTWOHolder(inflater.inflate(R.layout.item_release_message_two, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            bindItemMyHolder((MyHolder) holder, position);
        } else if (holder instanceof MyTWOHolder) {
            bindItemTWOMyHolder((MyTWOHolder) holder, position);
        }
    }

    private void bindItemTWOMyHolder(final MyTWOHolder holder, int position) {
        LogUtil.e("Adapter", listSize() + "");
        if (listSize() >= 9) {//集合长度大于等于9张时，隐藏 图片
            holder.imageview2.setVisibility(View.GONE);
        }
        holder.imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择图片
                CommonUtils.uploadPictures(context, 9, mRawList,0);
            }
        });
    }

    private void bindItemMyHolder(MyHolder holder, int position) {
        Glide.with(context)
                .load(mList.get(position))
                .centerCrop()
                .into(holder.imageview);
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return ITEM_TYPE_TWO;
        } else {
            return ITEM_TYPE_ONE;
        }
    }

    @Override
    public int getItemCount() {
        LogUtil.e("getItemCount", mList.size() + 1 + "");
        return mList.size() + 1;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private final ImageView imageview;

        public MyHolder(View itemView) {
            super(itemView);
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
        }
    }

    class MyTWOHolder extends RecyclerView.ViewHolder {
        private final ImageView imageview2;

        public MyTWOHolder(View itemView) {
            super(itemView);
            imageview2 = (ImageView) itemView.findViewById(R.id.imageview2);
        }
    }

    //对外暴露方法  。点击添加图片（类似于上啦加载数据）
    public void addMoreItem(ArrayList<String> loarMoreDatas) {
        if (mList != null) {
            mList.clear();
            mList.addAll(loarMoreDatas);
        }

        notifyDataSetChanged();
    }

    //得到集合长度
    public int listSize() {
        int size = mList.size();
        return size;
    }

    public void setRawPath(ArrayList<String> RawPath){
        mRawList=RawPath;
    }
}