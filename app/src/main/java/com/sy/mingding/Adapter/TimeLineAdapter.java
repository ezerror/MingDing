package com.sy.mingding.Adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sy.mingding.Bean.Timing;
import com.sy.mingding.R;
import com.sy.mingding.Utils.TimeUtil;
import com.sy.mingding.imple.Item;
import com.sy.mingding.widget.BottomAddTimingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ez
 * @Time: 2019/2/16 1:50
 * @Description: 功能描述
 */
public class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;
    private List<Timing> mData=new ArrayList<>();
    private View mItemView;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_TOP) {
            mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_add_timing,null,false);
        }
        if(viewType==TYPE_NORMAL){
            mItemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_default,null,false);
        }


        return new TimeLineAdapter.ViewHolder(mItemView);
    }

    public void setData(List<Timing> timingList) {

        if (mData != null) {
            mData.clear();
            mData.addAll(timingList);
        }
        mData.add(0,new Timing());
        //更新一下UI
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        if (getItemViewType(position) == TYPE_TOP) {
            itemHolder.mTvTopLine.setVisibility(View.INVISIBLE);
            itemHolder.mTvDot.setBackgroundResource(R.drawable.timelline_dot_first);
            itemHolder.setAddTimingButton(position);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            itemHolder.mTvTopLine.setVisibility(View.VISIBLE);
            itemHolder.mTvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
            itemHolder.setNormalData(mData.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTopLine, mTvDot;
        private final TextView mTvTimelineTitle;
        private final TextView mTvTimelineDuration;
        private final TextView mTvTimelineInterval;
        private final TextView mTvTimelineContent;
        private final ImageButton mBtnAddTiming;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
            mTvDot = (TextView) itemView.findViewById(R.id.tvDot);
            mTvTimelineTitle = (TextView)itemView.findViewById(R.id.tv_timeline_title);
            mTvTimelineDuration = (TextView)itemView.findViewById(R.id.tv_timeline_duration);
            mTvTimelineInterval = (TextView)itemView.findViewById(R.id.tv_timeline_interval);
            mTvTimelineContent = (TextView)itemView.findViewById(R.id.tv_item_content);
            mBtnAddTiming = itemView.findViewById(R.id.btn_add_timing);
        }

        @SuppressLint("SetTextI18n")
        public void setNormalData(Timing timing) {
            mTvTimelineTitle.setText(timing.getTodo().getProject().getProjectName());
            mTvTimelineDuration.setText(TimeUtil.getTimeFromInteger(timing.getTime()));
            mTvTimelineInterval.setText(TimeUtil.getTimeFromBmobDate(timing.getStartTime().getDate())+"~"+TimeUtil.getTimeFromBmobDate(timing.getEndTime().getDate()));
            mTvTimelineContent.setText(timing.getTodo().getTodoName());
        }

        public void setAddTimingButton(final int position) {
            mBtnAddTiming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item.OnItemClickListener listener=new Item.OnItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Toast.makeText(itemView.getContext(),"点击开启征程",Toast.LENGTH_SHORT).show();
                            BottomAddTimingDialog dialog = new BottomAddTimingDialog(itemView.getRootView().getRootView().getContext());
                            dialog.show();
                        }
                    };
                    listener.onItemClick(position);
                }
            });
        }
    }
}