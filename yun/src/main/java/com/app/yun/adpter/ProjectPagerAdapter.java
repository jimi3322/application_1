package com.app.yun.adpter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.yun.bean.ProjectPageItem;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class ProjectPagerAdapter extends FragmentPagerAdapter {

    private List<ProjectPageItem> projectPageItems;

    public ProjectPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPages(List<ProjectPageItem> pageItemList) {
        this.projectPageItems = pageItemList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        return projectPageItems.get(i).getFragment();
    }

    @Override
    public int getCount() {
        return projectPageItems == null ? 0 : projectPageItems.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return projectPageItems.get(position).getName();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }
}
