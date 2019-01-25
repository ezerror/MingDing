package com.sy.mingding.Fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.sy.mingding.Activity.SettingActivity;
import com.sy.mingding.Base.BaseFragment;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.Utils.UserUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class TodoFragment extends BaseFragment {

    private View mRootView;
    private static final String TAG = "TodoFragment";
    private CircleImageView mProfileImage;


    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, final ViewGroup container) {
        mRootView = layoutInflater.inflate(R.layout.fragment_todo,container,false);

        mProfileImage = mRootView.findViewById(R.id.profile_image_todo);
        Toolbar TodoTopBar =mRootView.findViewById(R.id.todo_top_bar);
        LogUtil.d(TAG,"线程名称-->"+Thread.currentThread().getName());
        TodoTopBar.inflateMenu(R.menu.todo_top_bar_menu);


        //BarSettings
        //TODO:


        //跳转到用户设置中
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(container.getContext(), "大家好", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
            }
        });


        refresh();
        mRootView.invalidate();

        return mRootView;

    }

    private void refresh() {
        User user =UserUtil.get_user();
        if(user.getIcon()!=null) {
            Picasso.with(this.getContext())
                    .load(user.getIcon().getUrl())
                    .placeholder(R.drawable.ic_profile_boy)
                    .into(mProfileImage);
        }
    }

    @Override
    public void onResume() {
        refresh();
        mRootView.invalidate();
        super.onResume();
    }
}
