package com.sy.mingding.Fragments;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sy.mingding.Activity.CalAsynchronousActivity;
import com.sy.mingding.Activity.CalendarActivity;
import com.sy.mingding.Activity.ProjectManageActivity;
import com.sy.mingding.Activity.SettingActivity;
import com.sy.mingding.Adapter.ProjectListAdapter;
import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.Bean.Project;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;
import com.sy.mingding.Utils.Constants;
import com.sy.mingding.Utils.DialogUtil;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.Utils.BeanUtils.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class TodoFragment extends BaseFragment {

    private View mRootView;
    private static final String TAG = "TodoFragment";
    private CircleImageView mProfileImage;
    private Toolbar mTodoTopBar;
    private RecyclerView mProjectListRv;
    private ProjectListAdapter mProjectListAdapter;
    private List<Project> mData = new ArrayList<>();
    private SwipeRefreshLayout mTodoSwipeRefreshLayout;
    private TextView mTodoTopBarTitle;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, final ViewGroup container) {
        initSubView(layoutInflater, container);
        initSubEvent(layoutInflater, container);

        //刷新
        ToolbarRefresh();
        mRootView.invalidate();
        return mRootView;

    }


    private void initSubEvent(LayoutInflater layoutInflater, ViewGroup container) {
        //LogUtil.d(TAG,"线程名称-->"+Thread.currentThread().getName());
        mTodoTopBar.inflateMenu(R.menu.todo_top_bar_menu);
        mTodoTopBarTitle.setText("项目");

        //ToolBarSettings
        mTodoTopBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            private Intent mIntent;

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.action_edit_project:
//                        Toast.makeText(mRootView.getContext(), "edit", Toast.LENGTH_SHORT).show();

                        mIntent = new Intent(mRootView.getContext(),ProjectManageActivity.class);
                        startActivity(mIntent);
                        break;
                    case R.id.action_calendar:
//                        Toast.makeText(mRootView.getContext(), "edit", Toast.LENGTH_SHORT).show();

                        mIntent=new Intent(mRootView.getContext(),CalAsynchronousActivity.class);
                        startActivity(mIntent);
                        break;
                }
                return false;
            }
        });


        //TODO:
        getProjectData();


        //跳转到用户设置中
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(container.getContext(), "大家好", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        mTodoSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshProjects();
            }
        });
    }

    private void refreshProjects() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getProjectData();
                //刷新完成
                mTodoSwipeRefreshLayout.setRefreshing(false);
            }

        }, 1000);

    }


    private void getProjectData() {
        if (BmobUser.isLogin()) {
            BmobQuery<Project> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", BmobUser.getCurrentUser(User.class));
            query.order("createdAt");
            //包含作者信息
            query.include("userId");
            query.findObjects(new FindListener<Project>() {

                @Override
                public void done(List<Project> object, BmobException e) {

                    if (e == null) {
                        mData = object;
                        upTodoUI(mData);
                        mProjectListAdapter.notifyDataSetChanged();
                        LogUtil.d("QUERY---", "--------ss" + object.toString());
                    } else {
                        LogUtil.d("QUERY---", "--------ee" + e.getErrorCode() + e.getMessage());
                    }
                }

            });
        } else {
            // Snackbar.make(view, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }


    private void initSubView(LayoutInflater layoutInflater, ViewGroup container) {
        mRootView = layoutInflater.inflate(R.layout.fragment_todo, container, false);
        mProfileImage = mRootView.findViewById(R.id.profile_image_todo);
        mTodoTopBar = mRootView.findViewById(R.id.todo_top_bar);
        mTodoTopBarTitle = mRootView.findViewById(R.id.todo_title_tv);


        //RecycleView相关设置
        mProjectListAdapter = new ProjectListAdapter();
        mProjectListRv = mRootView.findViewById(R.id.project_list_rv);
        mProjectListRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mProjectListRv.setAdapter(mProjectListAdapter);

        //SwipeRefresh
        mTodoSwipeRefreshLayout = mRootView.findViewById(R.id.todo_swipe_refresh);
        mTodoSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }


    //Toolbar的头像刷新
    private void ToolbarRefresh() {
        User user = UserUtil.get_user();
        if (user.getIcon() != null) {
            Picasso.with(this.getContext())
                    .load(user.getIcon().getUrl())
                    .placeholder(R.drawable.ic_profile_boy)
                    .into(mProfileImage);
        }
    }

    //返回该页面时刷新一下
    @Override
    public void onResume() {
        ToolbarRefresh();
        mRootView.invalidate();
        getProjectData();
        super.onResume();
    }

    private void upTodoUI(List<Project> data) {
        mProjectListAdapter.setData(data);
    }
}
