package com.app.yun;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.yun.fragment.FirstFragment;
import com.app.yun.fragment.FourthFragment;
import com.app.yun.fragment.SecondFragment;
import com.app.yun.fragment.ThirdFragment;
import com.google.android.material.tabs.TabLayout;

public class TabLayoutMainActivity extends AppCompatActivity {

    //未选中的Tab图片
    private int[] unSelectTabRes = new int[]{
            R.drawable.menu_home_normal
            , R.drawable.menu_information_normal
            , R.drawable.menu_home_normal
            , R.drawable.menu_home_normal
    };
    //选中的Tab图片
    private int[] selectTabRes = new int[]{
            R.drawable.menu_home_press
            , R.drawable.menu_information_press
            , R.drawable.menu_home_press
            , R.drawable.menu_home_press
    };
    //Tab标题
    private String[] title = new String[]{"首页", "跳转Activity", "弹窗","其他"};
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().hide();//隐藏掉整个ActionBar,因为manifest.xml中的theme已经设为NoActionBar所以这里在获取会报null object
        setContentView(R.layout.tablayout_main);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        viewPager =  findViewById(R.id.viewpager_content_view);
        tabLayout =  findViewById(R.id.tab_layout_view);

        //使用适配器将ViewPager与Fragment绑定在一起
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        //将TabLayout与ViewPager绑定
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < title.length; i++) {
            if (i == 0) {
                tabLayout.getTabAt(0).setIcon(selectTabRes[0]);
            } else {
                tabLayout.getTabAt(i).setIcon(unSelectTabRes[i]);
            }
        }
    }

    private void initData() {}

    private void initListener() {
        //TabLayout切换时导航栏图片处理
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中图片操作
                for (int i = 0; i < title.length; i++) {
                    if (tab == tabLayout.getTabAt(i)) {
                        tabLayout.getTabAt(i).setIcon(selectTabRes[i]);
                        viewPager.setCurrentItem(i);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {//未选中图片操作
                for (int i = 0; i < title.length; i++) {
                    if (tab == tabLayout.getTabAt(i)) {
                        tabLayout.getTabAt(i).setIcon(unSelectTabRes[i]);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    //自定义适配器
    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                return new SecondFragment();//娱乐
            } else if (position == 2) {
                return new ThirdFragment();//游戏
            } else if (position == 3) {
                return new FourthFragment();//我的
            }
            return new FirstFragment();//首页
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}

