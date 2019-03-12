package com.sy.mingding.Adapter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sy.mingding.Activity.UserInfoActivity;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author :smile
 * @project:NewFriendAdapter
 * @date :2016-04-27-14:18
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder>{

    private List<User> mData = new ArrayList<>();

    @NonNull
    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_user,viewGroup,false);
        return new SearchUserAdapter.ViewHolder(itemView);
    }

    public void setData(List<User> UserList) {
        mData.clear();
        if (null != UserList) {
            mData.addAll(UserList);
        }
        //更新一下UI
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setData(final User user) {
             ImageView avatar=itemView.findViewById(R.id.avatar);
             TextView name=itemView.findViewById(R.id.name);
             Button btn_add=itemView.findViewById(R.id.btn_check);


            if (user.getIcon() != null) {
                Picasso.with(itemView.getContext())
                        .load(user.getIcon().getUrl())
                        .into(avatar);
                name.setText(user.getUsername());
            }


             btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//查看个人详情
                    Bundle bundle = new Bundle();
                     bundle.putSerializable("u", user);
                     Intent intent = new Intent(itemView.getContext(),UserInfoActivity.class);
                     intent.putExtra(itemView.getContext().getPackageName(), bundle);
                     itemView.getContext().startActivity(intent);
                }
            });
        }
    }

}
