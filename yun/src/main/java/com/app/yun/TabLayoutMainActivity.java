package com.app.yun;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
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


    private FragmentManager fragmentManager;
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

        /**
         *  解决viewpage切换fragment时数据丢失问题
         *  1、设置ViewPage缓存界面数
         *  说明：若果我们设定mOffscreenPageLimit＝１，则当我们我们滑到第三个页面的时候，
         *  第一个页面的视图将被销毁，当我们滑到第一个页面时，第三个页面的视图会被销毁。
         *  但滑到第二个页面时，三个界面的视图都不会销毁。
         */
       // viewPager.setOffscreenPageLimit(title.length-1);

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

    private void initData() {
    }


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
    /**
     *  解决viewpage切换fragment时数据丢失问题
     *  2、将FragmentPagerAdapter修改继承FragmentStatePagerAdapter
     *  说明：FragmentPagerAdapter创建完fragment就不会销毁，所以会导致再次进入就不会重新创建，
     *  FragmentStatePagerAdapter则不会
     */
    public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                return new SecondFragment();
            } else if (position == 2) {
                return new ThirdFragment();
            } else if (position == 3) {
                return new FourthFragment();
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

