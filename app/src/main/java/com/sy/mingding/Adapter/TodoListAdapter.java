package com.sy.mingding.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sy.mingding.Bean.Project;
import com.sy.mingding.R;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private List<Project> mData=new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_todolist,viewGroup,false);
        return new ViewHolder(itemView);
    }

    public void setData(List<Project> projectList) {

        if (mData != null) {
            mData.clear();
            mData.addAll(projectList);
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
        public void setData(Project project){
            TextView projectNameTv=itemView.findViewById(R.id.todo_list_tv);

            projectNameTv.setText(project.getProjectName());
        }
    }
}
