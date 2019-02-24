package com.sy.mingding.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.sy.mingding.Bean.Project;
import com.sy.mingding.Constants.Constants;
import com.sy.mingding.Interface.ConfirmDialogInterface;
import com.sy.mingding.Utils.BeanUtils.ProjectUtil;
import com.sy.mingding.Utils.BeanUtils.TodoUtil;
import com.sy.mingding.Utils.BeanUtils.UserUtil;

public class DialogUtil {


    private static ProgressDialog waitingDialog;
    private static Project newProject;

    public static void showProjectInputDialog(final View view, final String event_id) {
        /*@setView 装入一个EditView
         */
        final EditText editText;
        editText = new EditText(view.getContext());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(view.getContext());
        inputDialog.setTitle("我是一个输入Dialog").setView(editText);
        switch (event_id) {
            case Constants.EVENT_ADD_PROJECT:
                inputDialog.setTitle("请输入你所要创建的项目名称").setView(editText);
                break;

            default:
                break;
        }


        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (event_id) {
                            case Constants.EVENT_ADD_PROJECT:
                                newProject = ProjectUtil.addProject(view, editText.getText().toString());
                                break;

                        }
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public static void showAddTodoInputDialog(final View view, final Project project) {
        /*@setView 装入一个EditView
         */
        final EditText editText;
        editText = new EditText(view.getContext());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(view.getContext());
        inputDialog.setTitle("请输入你所要创建的TODO名称").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TodoUtil.addTodo(view, editText.getText().toString(), project, UserUtil.get_user());
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public static void showNormalInputDialog(final View view, String title,@NonNull final ConfirmDialogInterface confirmDialogInterface ){

        final EditText editText;
        editText = new EditText(view.getContext());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(view.getContext());
        inputDialog.setTitle(title).setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDialogInterface.onConfirmClickListener(editText.getText().toString());
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDialogInterface.onCancelClickListener(editText.getText().toString());
                    }
                }).show();
    }


    public static void showWaitingDialog(Context context, String title, String msg) {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
        waitingDialog = new ProgressDialog(context);
        if (title != null) {
            waitingDialog.setTitle(title);
        }

        waitingDialog.setMessage(msg);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }

    public static void closeWaitingDialog() {


        waitingDialog.dismiss();
    }
}
