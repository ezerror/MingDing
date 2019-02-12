package com.sy.mingding.Callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.sy.mingding.Adapter.PMTodoListAdapter;
import com.sy.mingding.Bean.Todo;
import com.sy.mingding.imple.ItemTouchMoveListener;

import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags;

public class TodoItemTouchCallback extends ItemTouchHelper.Callback {

    PMTodoListAdapter mAdapter ;


    public TodoItemTouchCallback(PMTodoListAdapter adapter) {
        this.mAdapter=adapter;

    }
    /**
     * 判断动作方向
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int flags = makeMovementFlags(dragFlags, swipeFlags);
        return flags;
    }

    /**
     * 拖拽回调
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {

            return false;
        }
       mAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());

        return true;
    }

    /**
     * 侧滑回调
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemRemove(viewHolder.getAdapterPosition());
    }

    /**
     * 长按拖拽开关
     *
     * @return 代表是否开启长按拖拽
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
}