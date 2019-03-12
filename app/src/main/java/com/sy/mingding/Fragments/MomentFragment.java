package com.sy.mingding.Fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sy.mingding.Activity.ImagePreviewActivity;
import com.sy.mingding.Adapter.MomentAdapter;
import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.Bean.Moment;
import com.sy.mingding.Bean.User;
import com.sy.mingding.Constants.P;
import com.sy.mingding.Interface.OnItemPictureClickListener;
import com.sy.mingding.R;
import com.sy.mingding.Utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MomentFragment extends BaseFragment {


    private View mRootView;
    private RecyclerView mRvMoment;
    private MomentAdapter mMomentAdapter;
    private List<Moment> mData = new ArrayList<>();
    private int itemPosition;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        mRootView = layoutInflater.inflate(R.layout.fragment_moment,container,false);
        initView(layoutInflater,container);
        initEvent();
        return mRootView;

    }

    private void initEvent() {
        getMomentData();
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {

        mMomentAdapter = new MomentAdapter(getContext(),mData,new OnItemPictureClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemPictureClick(int item,int position, String url, List<String> urlList, ImageView imageView) {
                itemPosition = item;
                Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                intent.putStringArrayListExtra("imageList", (ArrayList<String>) urlList);
                intent.putExtra(P.START_ITEM_POSITION, itemPosition);
                intent.putExtra(P.START_IAMGE_POSITION, position);
                ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, imageView.getTransitionName());
                startActivity(intent, compat.toBundle());
            }
        });
        mRvMoment = mRootView.findViewById(R.id.rv_moment);
        mRvMoment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRvMoment.setAdapter(mMomentAdapter);
    }


    private void getMomentData() {
        if (BmobUser.isLogin()) {
            BmobQuery<Moment> query = new BmobQuery<>();
            query.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));
            query.order("createdAt");
            //包含作者信息
            query.include("user");
            query.findObjects(new FindListener<Moment>() {

                @Override
                public void done(List<Moment> object, BmobException e) {

                    if (e == null) {
                        mData = object;
                        upTodoUI(mData);
                        mMomentAdapter.notifyDataSetChanged();
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


    private void upTodoUI(List<Moment> data) {
        mMomentAdapter.setData(data);
    }




}
