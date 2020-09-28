package com.app.yun.contract;


import com.app.myapplication.mvp.IView;
import com.app.yun.bean.ProjectResult;

public interface ProjectPageContract {

    interface View extends IView {
        void onProjectList(ProjectResult projectResult);
    }

    interface Presenter {
        void getProjects(int id, int page);
    }
}
