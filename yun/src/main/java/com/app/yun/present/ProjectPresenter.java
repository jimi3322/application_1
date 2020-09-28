package com.app.yun.present;


import com.app.myapplication.base.BaseObserver;
import com.app.myapplication.mvp.BasePresenter;
import com.app.yun.apiservice.MainApiService;
import com.app.yun.bean.ProjectPageItem;
import com.app.yun.bean.ProjectTabItem;
import com.app.yun.contract.ProjectContract;
import com.app.yun.fragment.ProjectPageFragment;

import java.util.ArrayList;
import java.util.List;

public class ProjectPresenter extends BasePresenter<ProjectContract.View> implements ProjectContract.Presenter {


    /**
     * project 栏目
     */
    @Override
    public void getProjectTabs() {
        addSubscribe(create(MainApiService.class).getProjectTabs(), new BaseObserver<List<ProjectTabItem>>() {
            @Override
            protected void onSuccess(List<ProjectTabItem> data) {
                List<ProjectPageItem> projectPageItemList = createProjectPages(data);
                if (isViewAttached()) {
                    getView().onProjectTabs(projectPageItemList);
                }

            }
        });
    }

    private List<ProjectPageItem> createProjectPages(List<ProjectTabItem> projectItems) {
        if (projectItems == null || projectItems.size() == 0) {
            return new ArrayList<>();
        }
        List<ProjectPageItem> projectPageItemList = new ArrayList<>();
        for (ProjectTabItem projectItem : projectItems) {
            ProjectPageItem projectPageItem = new ProjectPageItem(projectItem.getId(),
                    projectItem.getName(), ProjectPageFragment.newInstance(projectItem.getId()));
            projectPageItemList.add(projectPageItem);
        }
        return projectPageItemList;
    }
}
