package com.app.yun.present;


import com.app.myapplication.base.BaseObserver;
import com.app.myapplication.mvp.BasePresenter;
import com.app.yun.apiservice.MainApiService;
import com.app.yun.bean.ProjectResult;
import com.app.yun.contract.ProjectPageContract;

public class ProjectPagePresenter extends BasePresenter<ProjectPageContract.View>
        implements ProjectPageContract.Presenter {

    @Override
    public void getProjects(int id, int page) {
        addSubscribe(create(MainApiService.class).getProjects(page, id),
                new BaseObserver<ProjectResult>(getView()) {

                    @Override
                    protected void onSuccess(ProjectResult data) {
                        if (isViewAttached()) {
                            getView().onProjectList(data);
                        }
                    }
                });
    }
}
