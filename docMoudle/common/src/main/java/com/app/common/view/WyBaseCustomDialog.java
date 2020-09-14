package com.app.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.common.utils.CommonUtil;
import com.app.wayee.common.R;


/**
 * Created by Administrator on 2018/1/31.
 */

public class WyBaseCustomDialog extends Dialog {
    private TextView mOkTv, mCancelTv;//底部按钮
    private TextView mTitleTv;//消息标题文本
    private TextView mMessageTv;//消息提示文本
    private FrameLayout mContentFL;
    private String mTitleStr;//从外界设置的title文本
    private String mMessageStr;//从外界设置的消息文本
    private View topLineV;
    private View mBottomView;

    private View.OnClickListener mCancelListener;//取消按钮被点击了的监听器
    private View.OnClickListener  mOkLinstener;//确定按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, View.OnClickListener onNoOnclickListener) {
        if (!TextUtils.isEmpty(str)) {
            mCancelTv.setText(str);
        }
        this.mCancelListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, View.OnClickListener onYesOnclickListener) {

        if (!TextUtils.isEmpty(str)) {
            mOkTv.setText(str);
        }
        this.mOkLinstener = onYesOnclickListener;
    }

    public WyBaseCustomDialog(Context context) {
        super(context, R.style.custom_dialog_style);
    }

    //onCreate方法在show之后触发
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOkLinstener != null) {
                    mOkLinstener.onClick(v);
                }
            }
        });

        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mCancelListener != null) {
                    mCancelListener.onClick(v);
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (mTitleStr != null) {
            mTitleTv.setText(mTitleStr);
        }
        if (mMessageStr != null) {
            mMessageTv.setText(mMessageStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mOkTv = findViewById(R.id.okTV);
        mCancelTv =  findViewById(R.id.cancelTV);
        mTitleTv =  findViewById(R.id.titleTV);
        mMessageTv =  findViewById(R.id.messageTV);
        mContentFL = findViewById(R.id.content);
        topLineV = findViewById(R.id.topLine);
        mBottomView = findViewById(R.id.bottomBtns);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitleStr = title;
        mTitleTv.setText(mMessageStr);
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        mMessageStr = message;
        mMessageTv.setText(mMessageStr);
        mMessageTv.setVisibility(View.VISIBLE);
    }

    public void setMessageCenter() {
        mMessageTv.setGravity(Gravity.CENTER);
    }

    public void setDialogContentView(View v){
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(params);
        mContentFL.removeAllViews();
        mContentFL.addView(v);
    }


    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width=  CommonUtil.dip2px(250.0f);
        layoutParams.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);
    }

    /**
     * 只展示content
     */
    public void setOnlyContentShow(){
        setCanceledOnTouchOutside(true);
        topLineV.setVisibility(View.GONE);
        mTitleTv.setVisibility(View.GONE);
        mMessageTv.setVisibility(View.GONE);
        mBottomView.setVisibility(View.GONE);
    }

}