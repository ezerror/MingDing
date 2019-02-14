package com.sy.mingding.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sy.mingding.Activity.TimerActivity;
import com.sy.mingding.Bean.Todo;
import com.sy.mingding.R;
import com.sy.mingding.imple.Item;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private List<Todo> mData=new ArrayList<>();
    private ItemClickListener mClickListener;

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
        holder.setButton(position);


    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private EasyFlipView mEasyFlipView;
        private TextView mTodoNameFrontTv;
        private TextView mTodoNameBackTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setData(Todo todo){
            mTodoNameFrontTv = itemView.findViewById(R.id.todo_name_front_tv);
            mTodoNameFrontTv.setText(todo.getTodoName());
            mTodoNameBackTv = itemView.findViewById(R.id.todo_name_back_tv);
            mTodoNameBackTv.setText(todo.getTodoName());
            mEasyFlipView = itemView.findViewById(R.id.flipView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mEasyFlipView.flipTheView();
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        /**
         * 设置TODO按钮GO
         *
         * @param position the position
         */
        public void setButton(final int position) {
            AppCompatImageButton todoGoBtn = itemView.findViewById(R.id.todo_btn_go);
            todoGoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item.OnItemClickListener listener=new Item.OnItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Toast.makeText(itemView.getContext(),"GO --"+mData.get(index).getTodoName(),Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(itemView.getRootView().getContext(),TimerActivity.class);
                            intent.putExtra("todo_id", mData.get(position).getObjectId());
                            itemView.getRootView().getContext().startActivity(intent);
                        }
                    };
                    listener.onItemClick(position);
                }
            });

        }
    }
    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

