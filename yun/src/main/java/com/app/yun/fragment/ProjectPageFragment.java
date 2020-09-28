package com.app.yun.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.app.myapplication.base.BaseLazyFragment;
import com.app.myapplication.constants.Constants;
import com.app.myapplication.widget.LinearItemDecoration;
import com.app.yun.R;
import com.app.yun.adpter.ProjectRecyclerAdapter;
import com.app.yun.bean.ProjectResult;
import com.app.yun.contract.ProjectPageContract;
import com.app.yun.present.ProjectPagePresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ProjectPageFragment extends BaseLazyFragment<ProjectPagePresenter> implements ProjectPageContract.View {

    private RecyclerView recyclerView;
    private int id;
    private int page = 0;
    private ProjectRecyclerAdapter recyclerAdapter;
    private List<ProjectResult.DatasBean> mDataList = new ArrayList<>();
    private RefreshLayout refreshLayout;

    public static ProjectPageFragment newInstance(int id) {
        ProjectPageFragment homePageFragment = new ProjectPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        homePageFragment.setArguments(bundle);
        return homePageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
        }
    }

    @Override
    protected void loadData() {
        presenter.getProjects(id, page);
    }

    @Override
    protected ProjectPagePresenter createPresenter() {
        return new ProjectPagePresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_project_page;
    }

    @Override
    protected void initView(View rootView) {
        refreshLayout = rootView.findViewById(R.id.srl_project);
        recyclerView = rootView.findViewById(R.id.rv_home_page);
    }

    @Override
    protected void initData() {
        // 设置 ItemDecoration 作为分割线
        LinearItemDecoration itemDecoration = new LinearItemDecoration(mContext)
                .height(0.8f)    // 0.5dp
                .color(Color.parseColor("#aacccccc"))  // color 的 int 值，不是 R.color 中的值
                .margin(12, 12);  // 12dp
        recyclerView.addItemDecoration(itemDecoration);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.getProjects(id, page);
            }
        });

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onProjectList(ProjectResult projectResult) {
        page++;
        if (projectResult != null) {
            List<ProjectResult.DatasBean> datas = projectResult.getDatas();
            if (datas != null) {
                mDataList.addAll(datas);
                if (recyclerAdapter == null) {
                    recyclerAdapter = new ProjectRecyclerAdapter(R.layout.item_recycler_project, mDataList);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            gotoWebViewActivity(mDataList.get(position));
                        }
                    });
                } else {
                    recyclerAdapter.setNewData(mDataList);
                }
            } else {
                refreshLayout.setNoMoreData(true);
            }
        }
    }

    /**
     * 跳转到 WebViewActivity
     */
    private void gotoWebViewActivity(ProjectResult.DatasBean datasBean) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.URL, datasBean.getLink());
        bundle.putInt(Constants.ID, datasBean.getId());
        bundle.putString(Constants.AUTHOR, datasBean.getAuthor());
        bundle.putString(Constants.TITLE, datasBean.getTitle());
        ARouter.getInstance()
                .build("/web/WebViewActivity")
                .with(bundle)
                .navigation();
        getActivity().overridePendingTransition(R.anim.anim_web_enter, R.anim.anim_alpha);
    }
}
