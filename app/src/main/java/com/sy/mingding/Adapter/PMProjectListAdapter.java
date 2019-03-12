package com.sy.mingding.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.sy.mingding.Bean.Project;
import com.sy.mingding.Bean.Todo;
import com.sy.mingding.Callback.TodoItemTouchCallback;
import com.sy.mingding.Model.ProjectModel;
import com.sy.mingding.R;
import com.sy.mingding.Utils.DialogUtil;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.imple.Item;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PMProjectListAdapter extends RecyclerView.Adapter<PMProjectListAdapter.ViewHolder>  {

    private List<Project> mData=new ArrayList<>();
    private List<Todo> todoData=new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pm_projectlist,viewGroup,false);
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

    public void addNewData(Project newproject) {
        mData.add(newproject);
        notifyItemInserted(mData.size()-1);
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




    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setData(Project project){
            TextView projectNameTv=itemView.findViewById(R.id.pm_list_tv);
            projectNameTv.setText(project.getProjectName());


            //查询


            if (BmobUser.isLogin()) {
                BmobQuery<Todo> query = new BmobQuery<>();
                LogUtil.d("QUERY---",project.getObjectId());
                query.addWhereEqualTo("project", new BmobPointer(project));
                query.order("createdAt");
                query.include("todoName");
                query.findObjects(new FindListener<Todo>() {

                    @Override
                    public void done(List<Todo> object, BmobException e) {

                        if (e == null) {
                            //todolistRv
                            final PMTodoListAdapter mPMTodoListAdapter = new PMTodoListAdapter();
                            RecyclerView todoListRv=itemView.findViewById(R.id.pm_list_rv);
                            todoListRv.setLayoutManager(new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.VERTICAL,false));
                            todoListRv.setAdapter(mPMTodoListAdapter);
                            LogUtil.d("QUERY---",object.toString());
                            todoData=object;
                            mPMTodoListAdapter.setData(todoData);
                            ItemTouchHelper helper= new ItemTouchHelper(new TodoItemTouchCallback(mPMTodoListAdapter));
                            helper.attachToRecyclerView(todoListRv);

                            //设置动画
                            int resId = R.anim.layout_animation_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(itemView.getRootView().getContext(), resId);
                            todoListRv.setLayoutAnimation(animation);

//                            BaseApplication.setOnHandlerListener(new BaseApplication.HandlerListener() {
//                                @Override
//                                public void heandleMessage(Message msg) {
//                                    switch (msg.what){
//                                        case Constants.HANDLER_ADD_TODO:
//                                            Todo data = (Todo) msg.obj;
//                                            LogUtil.e("HANDLER--","HANDLER--"+data);
//                                            mPMTodoListAdapter.addNewData(data);
//                                            break;
//                                        default:
//                                            break;
//                                    }
//
//                                }
//                            });



                        } else {
                            LogUtil.e("QUERY---",e.getMessage()+"--->"+e.getErrorCode());
                        }
                    }

                });
            } else {
                // Snackbar.make(view, "请先登录", Snackbar.LENGTH_LONG).show();
            }


            LogUtil.d("QUERY---","--------end");
        }


        public void setButton(final int position) {
            AppCompatImageButton btnAddTodo=itemView.findViewById(R.id.pm_btn_addTodo);
            AppCompatImageButton btnDelProject=itemView.findViewById(R.id.pm_btn_delProject);
            btnAddTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item.OnItemClickListener listener=new Item.OnItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Toast.makeText(itemView.getContext(),"TODO添加"+index,Toast.LENGTH_SHORT).show();
                            DialogUtil.showAddTodoInputDialog(itemView.getRootView(),mData.get(position));

                        }
                    };
                    listener.onItemClick(position);
                }
            });

            btnDelProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item.OnItemClickListener listener=new Item.OnItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Toast.makeText(itemView.getContext(),"PRoject删除"+index,Toast.LENGTH_SHORT).show();
                            ProjectModel.deleteProjectFromSql(mData.get(index),itemView.getRootView().getContext());
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
