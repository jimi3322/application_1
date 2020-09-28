package com.app.yun.adpter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.app.yun.R;
import com.app.yun.bean.ProjectResult;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.reactivex.rxjava3.annotations.Nullable;

public class ProjectRecyclerAdapter extends BaseQuickAdapter<ProjectResult.DatasBean, BaseViewHolder> {

    public ProjectRecyclerAdapter(int layoutResId, @Nullable List<ProjectResult.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectResult.DatasBean item) {
        helper.setText(R.id.tv_project_title, item.getTitle());
        helper.setText(R.id.tv_project_author, item.getAuthor() + "\t\t" + item.getNiceDate());
        String envelopePic = item.getEnvelopePic();
        if (TextUtils.isEmpty(envelopePic)) {
            helper.setGone(R.id.iv_project_img,false);  // 隐藏图片占位
        }else {
            Glide.with(mContext)
                    .load(envelopePic)
                    .centerCrop()
                    .into((ImageView) helper.getView(R.id.iv_project_img));
        }
    }
}
