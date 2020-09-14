package com.app.common.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.wayee.common.R;

import net.cachapa.expandablelayout.ExpandableLayout;

public class CommonTitleActivity extends CommonBaseActivity implements View.OnClickListener {
    private View mTitleBar;
    private ImageView mTitleImageView;
    private TextView mTitleTextView;
    private Button mBackwardbButton;
    private Button mForwardButton;
    private FrameLayout mRightView;
    private FrameLayout mContentLayout;
    private ExpandableLayout mExpandableLayout;
    private TextView mTitleHintTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//软键盘输入框不被遮挡
        setupViews();   //加载 activity_title 布局 ，并获取标题及两侧按钮
    }

    private void setupViews() {
        super.setContentView(R.layout.common_activity_title);
        mTitleBar = findViewById(R.id.layout_titlebar);
        mTitleImageView = findViewById(R.id.image_title);
        mTitleTextView = findViewById(R.id.text_title);
        mContentLayout = findViewById(R.id.layout_content);
        mBackwardbButton = findViewById(R.id.button_backward);
        mForwardButton = findViewById(R.id.button_forward);
        mRightView = findViewById(R.id.fl_right);
        mExpandableLayout = findViewById(R.id.expand_title_hint);
        mTitleHintTextView = findViewById(R.id.text_hint);
    }

    /**
     * 是否显示标题栏提示视图
     *
     * @param hintResId 标题栏文字ID
     * @param show      true则显示，false隐藏
     */
    protected void showTitleHintView(int hintResId, boolean show) {
        if (mExpandableLayout == null || mTitleHintTextView == null) return;
        if (show) {
            mTitleHintTextView.setText(hintResId);
            mExpandableLayout.expand();
        } else {
            mExpandableLayout.collapse();
        }
    }

    /**
     * 设置titlebar的背景色
     * @param color
     */
    protected void setTitleBarBackColor(int color){
        mTitleBar.setBackgroundColor(color);
    }

    /**
     * 是否显示标题图标
     *
     * @param drawable 图片资源
     * @param show     true则显示
     */
    protected void showTitleImageView(Drawable drawable, boolean show) {
        if (mTitleImageView != null) {
            if (show) {
                mTitleImageView.setImageDrawable(drawable);
                mTitleImageView.setVisibility(View.VISIBLE);
            } else {
                mTitleImageView.setVisibility(View.GONE);
            }
        } // else ignored
    }

    /**
     * 设置头部可见
     */
    public void showTitleBar(){
        mTitleBar.setVisibility(View.VISIBLE);
    }

    /**
     * 设置头部可见
     */
    public void hideTitleBar(){
        mTitleBar.setVisibility(View.GONE);
    }

    /**
     * 是否显示返回按钮
     *
     * @param backwardResid 文字
     * @param show          true则显示
     * @param drawableLeft  左方图片资源
     */
    protected void showBackwardView(int backwardResid, boolean show, Drawable drawableLeft) {
        if (mBackwardbButton != null) {
            if (show) {
                if(backwardResid<=0){
                    mBackwardbButton.setText("");
                }else{
                    mBackwardbButton.setText(backwardResid);
                }
                mBackwardbButton.setVisibility(View.VISIBLE);
                if (drawableLeft != null) {
                    drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                    mBackwardbButton.setCompoundDrawables(drawableLeft, null, null, null);
                }
            } else {
                mBackwardbButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    /**
     * 获取展示title的view
     * @return
     */
    protected TextView getTitleTextView(){
        return mTitleTextView;
    }

    /**
     * 提供是否显示提交按钮
     *
     * @param forwardResId  文字id  不展示文字 id = 0
     * @param show          true则显示
     * @param drawableRight 右方图片资源
     */
    protected void showForwardView(int forwardResId, boolean show, Drawable drawableRight) {
        if (mForwardButton != null) {
            if (show) {
                mRightView.removeAllViews();
                if(forwardResId>0){
                    mForwardButton.setText(forwardResId);
                }else{
                    mForwardButton.setText("");
                }
                mForwardButton.setVisibility(View.VISIBLE);
                if (drawableRight != null)
                    drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
                mForwardButton.setCompoundDrawables(null, null, drawableRight, null);
            } else {
                mForwardButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    /**
     * 设置右侧按钮文字的颜色
     * @param colorRes
     */
    protected void setForwardTextColor(int colorRes){
        mForwardButton.setTextColor(getResources().getColor(colorRes));
    }

    /**
     * 提供是否显示提交按钮
     *
     * @param resId
     */
    protected void showRightView(int resId) {
        if (resId >0) {
            mForwardButton.setVisibility(View.GONE);
            View view  = LayoutInflater.from(CommonTitleActivity.this).inflate(resId,null);
            mRightView.removeAllViews();
            mRightView.addView(view);
            mRightView.setVisibility(View.VISIBLE);
        }
    }
    protected void showRightView(View view) {
        if (view!=null ) {
            mForwardButton.setVisibility(View.GONE);
            mRightView.setVisibility(View.VISIBLE);
            mRightView.removeAllViews();
            mRightView.addView(view);
        }
    }

    /**
     * 返回按钮点击后触发
     *
     * @param backwardView
     */
    protected void onBackward(View backwardView) {
        finish();
    }

    /**
     * 提交按钮点击后触发
     *
     * @param forwardView
     */
    protected void onForward(View forwardView) {
        //Toast.makeText(this, "点击提交", Toast.LENGTH_LONG).show();
    }

    /**
     * 标题栏提示区域点击后触发
     *
     * @param titleHintView
     */
    protected void onTitleHint(View titleHintView) {

    }

    //设置标题内容
    @Override
    public void setTitle(int titleId) {
        mTitleTextView.setText(titleId);
    }

    //设置标题内容
    @Override
    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }

    //设置标题文字颜色
    @Override
    public void setTitleColor(int textColor) {
        mTitleTextView.setTextColor(textColor);
    }


//    取出FrameLayout并调用父类removeAllViews()方法
    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResID, mContentLayout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
    }


    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     * 按钮点击调用的方法
     */
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button_backward){
            onBackward(v);
        }else if(v.getId() == R.id.button_forward){
            onForward(v);
        }else if(v.getId() == R.id.expand_title_hint){
            onTitleHint(v);
        }
    }
}
