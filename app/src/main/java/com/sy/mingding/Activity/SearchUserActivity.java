package com.sy.mingding.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.orhanobut.logger.Logger;
import com.sy.mingding.Adapter.SearchUserAdapter;
import com.sy.mingding.Base.BaseActivity;
import com.sy.mingding.Base.BaseModel;
import com.sy.mingding.Bean.User;
import com.sy.mingding.Model.UserModel;
import com.sy.mingding.R;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**新朋友
 * @author :smile
 * @project:NewFriendActivity
 * @date :2016-01-25-18:23
 */
public class SearchUserActivity extends BaseActivity {
    EditText et_find_name;
    SwipeRefreshLayout sw_refresh;
    Button btn_search;
    RecyclerView rc_view;
    LinearLayoutManager layoutManager;
    SearchUserAdapter adapter;

    @Override
    protected void initView() {
        User user=BmobUser.getCurrentUser(User.class);
        et_find_name=findViewById(R.id.et_find_name);
        btn_search=findViewById(R.id.btn_search);
        rc_view=findViewById(R.id.rc_view);
        sw_refresh=findViewById(R.id.sw_refresh);
        adapter = new SearchUserAdapter();
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        rc_view.setAdapter(adapter);
        super.initView();
    }


    private void initEvent(){
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initView();
        initEvent();
    }

    public void query() {
        String name = et_find_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toast("请填写用户名");
            sw_refresh.setRefreshing(false);
            return;
        }
        UserModel.getInstance().queryUsers(name, BaseModel.DEFAULT_LIMIT,
                new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            sw_refresh.setRefreshing(false);
                            adapter.setData(list);
                          //  adapter.notifyDataSetChanged();
                        } else {
                            sw_refresh.setRefreshing(false);
                            adapter.setData(null);
                            toast(e.getMessage());
                          //  adapter.notifyDataSetChanged();
                        }
                    }
                }
        );
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }



}
