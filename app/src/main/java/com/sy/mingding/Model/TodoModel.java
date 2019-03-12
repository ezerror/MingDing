package com.sy.mingding.Model;

import android.content.Context;
import android.view.View;

import com.sy.mingding.Base.BaseApplication;
import com.sy.mingding.Bean.Project;
import com.sy.mingding.Bean.Todo;
import com.sy.mingding.Bean.User;
import com.sy.mingding.Constants.Constants;
import com.sy.mingding.Utils.DialogUtil;
import com.sy.mingding.Utils.LogUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class TodoModel {

    private static List<Todo> result;

    public static void deleteTodoFromSql(final String id, final Context context){
        Todo todo=new Todo();
        todo.setObjectId(id);
        DialogUtil.showWaitingDialog(context,null,"删除中···");
        todo.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    if (context != null) {
                        DialogUtil.closeWaitingDialog();
                    }
                    LogUtil.d("TodoModel---->:deleteTodoFromSql","成功删除Todo id:"+id);
                }else{
                    LogUtil.d("TodoModel---->:deleteTodoFromSql","error-->"+e.getErrorCode()+"   "+e.getMessage());
                }
            }
        });
    }
    public static void deleteTodoFromSql(final String id){
        Todo todo=new Todo();
        todo.setObjectId(id);
        todo.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    LogUtil.d("TodoModel---->:deleteTodoFromSql","成功删除Todo id:"+id);
                }else{
                    LogUtil.d("TodoModel---->:deleteTodoFromSql","error-->"+e.getErrorCode()+"   "+e.getMessage());
                }
            }
        });
    }

    public static void addTodo( final View view, final String todoName, final Project project, final User user){
       final Todo todo=new Todo();
       todo.setProject(project);
       todo.setUser(user);
       todo.setTodoName(todoName);
       todo.save(new SaveListener<String>() {
           @Override
           public void done(String s, BmobException e) {
               if (e == null) {
//                   Snackbar.make(view, "TODO添加成功：" + s, Snackbar.LENGTH_LONG).show();
                       BaseApplication.mHandler.sendEmptyMessage(Constants.HANDLER_ADD_TODO);

               } else {
                   LogUtil.e("BMOB", e.toString());
//                   Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
               }
           }
       });
    }

    public static void updateAllTodoSumTime(){

    }

    /**
     * 在添加Timing的时候 更新该TODO的总时间.
     * 在该方法使用{@link TimingModel#addTiming(String, Integer)}  }
     * @param todoid todo id
     * @param time   时间
     */

    public static void updateTodoSumTime(final String todoid, final Integer time){
        Todo todo=new Todo();
        todo.setObjectId(todoid);
        todo.increment("sumTime",time);
        todo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    //
                }{
                    //
                }
            }
        });
    }
}
