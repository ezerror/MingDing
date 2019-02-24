package com.sy.mingding.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.sy.mingding.Adapter.PMProjectListAdapter;
import com.sy.mingding.Base.BaseApplication;
import com.sy.mingding.Bean.Project;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;
import com.sy.mingding.Utils.ActivityManager;
import com.sy.mingding.Constants.Constants;
import com.sy.mingding.Utils.DialogUtil;
import com.sy.mingding.Utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ProjectManageActivity extends Activity {


    private Toolbar mprojectManageBar;
    private RecyclerView mProjectManageRv;
    private PMProjectListAdapter mPMProjectListAdapter;
    private List<Project> mData = new ArrayList<>();
    private SwipeRefreshLayout mPMSwipeRefreshLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectmanage);
        ActivityManager.addActivity(this);
        initView();
        initEvent();


    }

    private void initEvent() {
        getProjectData();
        mPMSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshProjects();
            }
        });

        mprojectManageBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_add_project:
//                        Toast.makeText(mRootView.getContext(), "add", Toast.LENGTH_SHORT).show();
                                //TODO:addNewData
                                DialogUtil.showProjectInputDialog(getWindow().getDecorView(),Constants.EVENT_ADD_PROJECT);


                }
                return false;
            }
        });

        //添加项目 界面更新
        BaseApplication.setOnHandlerListener(new BaseApplication.HandlerListener() {
            @Override
            public void heandleMessage(Message msg) {
                LogUtil.e("HANDLER",""+msg.what);
                switch (msg.what) {
                    case Constants.HANDLER_ADD_PROJECT:
                        //完成主界面更新,拿到数据
                        if (msg.obj != null) {
                            Project data = (Project) msg.obj;
                            LogUtil.e("HANDLER--","HANDLER--"+data);
                            DialogUtil.showWaitingDialog(getWindow().getDecorView().getContext(),null,"正在添加");
                            mPMProjectListAdapter.addNewData(data);
                            DialogUtil.closeWaitingDialog();

                        }
                        break;
                    case Constants.HANDLER_ADD_TODO:
                            mPMProjectListAdapter.notifyDataSetChanged();

                        break;
                    default:break;

                }
            }
        });
    }

    private void initView() {
        mprojectManageBar = findViewById(R.id.project_manage_top_bar);
        mprojectManageBar.setTitleTextColor(getResources().getColor(R.color.white));
        mprojectManageBar.setTitle("项目管理");
        mprojectManageBar.inflateMenu(R.menu.projectmanage_top_bar_menu);

        //RecycleView 加载

        mPMProjectListAdapter = new PMProjectListAdapter();
        mProjectManageRv = findViewById(R.id.project_manage_list_rv);
        mProjectManageRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mProjectManageRv.setAdapter(mPMProjectListAdapter);
        mProjectManageRv.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);


        mPMSwipeRefreshLayout = findViewById(R.id.project_manage_swipe_refresh);
        mPMSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);


        //设置动画
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        mProjectManageRv.setLayoutAnimation(animation);

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
    private void upTodoUI(List<Project> data) {
        mPMProjectListAdapter.setData(data);
    }

    private void refreshProjects() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getProjectData();
                //刷新完成
                mPMSwipeRefreshLayout.setRefreshing(false);
            }

        }, 1000);

    }

}
