package com.app.yun.adpter;

import com.app.yun.R;
import com.app.yun.bean.SystemResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.reactivex.rxjava3.annotations.Nullable;

/**
 * 体系中一级列表(左侧) recyclerview 适配器
 */
public class SystemLeftAdapter extends BaseQuickAdapter<SystemResult, BaseViewHolder> {

    public SystemLeftAdapter(int layoutResId, @Nullable List<SystemResult> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemResult item) {
        helper.setText(R.id.tv_system_left_title, item.getName())
                .setBackgroundColor(R.id.tv_system_left_title, item.isSelected(true) ? 0xffffffff : 0xffeeeeee);
    }
}
