package com.sy.mingding.Interface;

/**
 * @Author: ez
 * @Time: 2019/2/21 22:25
 * @Description: Dialog 点击
 */
public interface ConfirmDialogInterface {
    //监听确认按钮点击事件
    void onConfirmClickListener(String content);
    //监听取消按钮点击事件
    void onCancelClickListener(String content);
}
