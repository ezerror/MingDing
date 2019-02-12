package com.sy.mingding.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sy.mingding.Bean.Project;
import com.sy.mingding.Bean.Todo;
import com.sy.mingding.R;
import com.sy.mingding.Utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private List<Todo> mData=new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_todolist,null,false);
        return new ViewHolder(itemView);
    }

    public void setData(List<Todo> todoList) {

        if (mData != null) {
            mData.clear();
            mData.addAll(todoList);
        }
        //更新一下UI
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

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
        public void setData(Todo todo){
            TextView todoNameTv=itemView.findViewById(R.id.todo_name_tv);
            todoNameTv.setText(todo.getTodoName());

        }
    }
}
