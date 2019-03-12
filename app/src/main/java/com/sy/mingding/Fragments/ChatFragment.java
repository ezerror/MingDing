package com.sy.mingding.Fragments;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sy.mingding.Activity.SearchUserActivity;
import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends BaseFragment {

    private View mRootView;
    private static final String TAG = "ChatFragment";
    private CircleImageView mProfileImage;
    private Toolbar mTodoTopBar;
    private TextView mTodoTopBarTitle;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, final ViewGroup container) {
        mRootView = layoutInflater.inflate(R.layout.fragment_chat, container, false);
        User user=BmobUser.getCurrentUser(User.class);
        AppCompatImageButton add_friend=mRootView.findViewById(R.id.add_friend);
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SearchUserActivity.class);
                startActivity(intent);
            }
        });
        return mRootView;

    }

    //返回该页面时刷新一下
    @Override
    public void onResume() {
        mRootView.invalidate();
        super.onResume();
    }


}
