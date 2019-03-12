package com.sy.mingding.Base;

import android.content.Context;

/**
 * @Author: ez
 * @Time: 2019/2/24 20:28
 * @Description: 功能描述
 */
public abstract class BaseModel {

    public int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;

    public static final int DEFAULT_LIMIT=20;

    public Context getContext(){
        return BaseApplication.INSTANCE();
    }
}
