package com.app.yun.contract;

import com.app.myapplication.mvp.IView;
import com.app.yun.bean.ProjectPageItem;

import java.util.List;

public interface ProjectContract {

    interface View extends IView {
        void onProjectTabs(List<ProjectPageItem> projectPageItemList);
    }

    interface Presenter {
        void getProjectTabs();
    }
}
