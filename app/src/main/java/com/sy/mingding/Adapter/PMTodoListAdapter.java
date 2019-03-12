package com.sy.mingding.Adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sy.mingding.Bean.Todo;
import com.sy.mingding.Model.TodoModel;
import com.sy.mingding.R;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.imple.Item;
import com.sy.mingding.imple.ItemTouchMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PMTodoListAdapter extends RecyclerView.Adapter<PMTodoListAdapter.ViewHolder> implements ItemTouchMoveListener {

    private List<Todo> mData = new ArrayList<>();



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pm_todolist, viewGroup, false);
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

    public void addNewData(Todo newtodo) {
        mData.add(newtodo);
        notifyItemInserted(mData.size()-1);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(position);
        holder.setData(mData.get(position));
        holder.setButton(position);

    }

    @Override

    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        LogUtil.e("TESTETETST", fromPosition + "   " + toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public boolean onItemRemove(int position) {
        return false;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Todo todo) {
            TextView pmtodoNameTv = itemView.findViewById(R.id.pm_todo_name_tv);
            pmtodoNameTv.setText(todo.getTodoName());
            //随机颜色
//            Random myRandom = new Random();
//            int ranColor = 0xff000000 | myRandom.nextInt(0x00ffffff);
//
//            RelativeLayout pm = itemView.findViewById(R.id.item_pm);
//            pm.setBackgroundColor(ranColor);

        }

        @SuppressLint("ClickableViewAccessibility")
        public void setButton(final int position) {
            Button todoAlterBtn = itemView.findViewById(R.id.todo_btn_alter);
            Button todoDelBtn = itemView.findViewById(R.id.todo_btn_delete);
            todoAlterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item.OnItemClickListener listener=new Item.OnItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Toast.makeText(itemView.getContext(),"哈哈哈"+index,Toast.LENGTH_SHORT).show();
                        }
                    };
                    listener.onItemClick(position);
                }
            });

            todoDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item.OnItemClickListener listener=new Item.OnItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Toast.makeText(itemView.getContext(),"嘻嘻嘻"+index,Toast.LENGTH_SHORT).show();
                            //获取id并删除,数据库删除
                            String id=mData.get(position).getObjectId();
                            TodoModel.deleteTodoFromSql(id,itemView.getContext());
                            //本地删除
                            mData.remove(index);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(0,getItemCount());



                        }
                    };
                    listener.onItemClick(position);
                }
            });

        }
    }



}


