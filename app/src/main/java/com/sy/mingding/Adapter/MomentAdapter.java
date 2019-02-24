package com.sy.mingding.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sy.mingding.Bean.Moment;
import com.sy.mingding.Interface.OnItemPictureClickListener;
import com.sy.mingding.R;
import com.sy.mingding.widget.view.NineGridTestLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ez
 * @Time: 2019/2/22 17:23
 * @Description: 功能描述
 */
public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.ViewHolder>{
    private List<Moment> mData=new ArrayList<>();
    private Context context;
    private OnItemPictureClickListener listener;

    public MomentAdapter(Context context, List<Moment> list,OnItemPictureClickListener listener) {
        this.mData = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MomentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_moment,viewGroup,false);
        return new MomentAdapter.ViewHolder(itemView);
    }

    public void setData(List<Moment> momentList) {

        if (mData != null) {
            mData.clear();
            mData.addAll(momentList);
        }
        //更新一下UI
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mimMomentProfile;
        private final TextView mTvMomentNickname;
        private final TextView mTvMomentContent;
//        private final ImageView mImg1;
//        private final ImageView mImg2;
//        private final ImageView mImg3;
//        private final ImageView mImg4;
//        private final ImageView mImg5;
//        private final ImageView mImg6;
//        private final ImageView mImg7;
//        private final ImageView mImg8;
//        private final ImageView mImg9;
//        private final List<ImageView> mImgList;
        private final LinearLayout mLLMomentImg;
        private final NineGridTestLayout mLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mimMomentProfile = (ImageView) itemView.findViewById(R.id.moment_profile);
            mTvMomentNickname = itemView.findViewById(R.id.moment_nickname);
            mTvMomentContent = itemView.findViewById(R.id.moment_content);
            mLLMomentImg = itemView.findViewById(R.id.linear_moment_img);
            mLayout = (NineGridTestLayout) itemView.findViewById(R.id.moment_picture);
//            mImg1 = itemView.findViewById(R.id.moment_img1);
//            mImg2 = itemView.findViewById(R.id.moment_img2);
//            mImg3 = itemView.findViewById(R.id.moment_img3);
//            mImg4 = itemView.findViewById(R.id.moment_img4);
//            mImg5 = itemView.findViewById(R.id.moment_img5);
//            mImg6 = itemView.findViewById(R.id.moment_img6);
//            mImg7 = itemView.findViewById(R.id.moment_img7);
//            mImg8 = itemView.findViewById(R.id.moment_img8);
//            mImg9 = itemView.findViewById(R.id.moment_img9);
//            mImgList = new ArrayList<>();
//            mImgList.add(0,mImg1);
//            mImgList.add(1,mImg2);
//            mImgList.add(2,mImg3);
//            mImgList.add(3,mImg4);
//            mImgList.add(4,mImg5);
//            mImgList.add(5,mImg6);
//            mImgList.add(6,mImg7);
//            mImgList.add(7,mImg8);
//            mImgList.add(8,mImg9);
        }


        public void setData(Moment moment) {
            //设置头像
            if(moment.getUser().getIcon()!=null){
                Picasso.with(itemView.getContext())
                        .load(moment.getUser().getIcon().getUrl())
                        .placeholder(R.drawable.ic_profile_boy)
                        .into(mimMomentProfile);
            }
            //设置昵称
            mTvMomentNickname.setText(moment.getUser().getNickname());
            //设置内容
            mTvMomentContent.setText(moment.getText());
            //设置图片
            List<String> urlList = new ArrayList<>();
            if (moment.getPhotoList() != null){
                for(int i=0;i<moment.getPhotoList().size();i++){
                   urlList.add(i,moment.getPhotoList().get(i).getUrl());

                }
            }
            mLayout.setIsShowAll(false); //当传入的图片数超过9张时，是否全部显示
            mLayout.setSpacing(5); //动态设置图片之间的间隔
            mLayout.setUrlList(urlList); //最后再设置图片url
            mLayout.setListener(listener);

//            if (moment.getPhotoList() != null) {
//                mLLMomentImg.setVisibility(View.VISIBLE);
//                for(int i=0;i<moment.getPhotoList().size();i++){
//                    //mImgList.get(i).setVisibility(View.VISIBLE);
//                    Picasso.with(itemView.getContext())
//                            .load(moment.getPhotoList().get(i).getUrl())
//                            .into(mImgList.get(i));
//                }
//                if(moment.getPhotoList().size()==1){
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(150),
//                            dp2px(320));
//                    mImg1.setLayoutParams(params);
//                }
//            }

        }
        public int dp2px(float dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    itemView.getContext().getResources().getDisplayMetrics());
        }
    }


}
