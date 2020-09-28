package com.app.yun.adpter;

import com.app.yun.R;
import com.app.yun.bean.SystemResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.reactivex.rxjava3.annotations.Nullable;


/**
 * 体系中二级列表(右侧) recyclerview 适配器
 */
public class SystemRightAdapter extends BaseQuickAdapter<SystemResult.ChildrenBean, BaseViewHolder> {

    public SystemRightAdapter(int layoutResId) {
        super(layoutResId);
    }

    public SystemRightAdapter(int layoutResId, @Nullable List<SystemResult.ChildrenBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemResult.ChildrenBean item) {
        helper.setText(R.id.tv_system_right_title, item.getName());
    }
}
