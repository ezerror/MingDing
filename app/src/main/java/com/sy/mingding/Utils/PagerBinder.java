package com.sy.mingding.Utils;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.sy.mingding.R;

public class PagerBinder {

    private static Menu mNavigationMenu;

    public static void bind(final BottomNavigationView bottomNavigationView, final ViewPager viewPager){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_todo:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_statistic:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_settings:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

        mNavigationMenu = bottomNavigationView.getMenu();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                MenuItem NavigationMenuItem;
                NavigationMenuItem= mNavigationMenu.getItem(i);
                NavigationMenuItem.setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }
}
