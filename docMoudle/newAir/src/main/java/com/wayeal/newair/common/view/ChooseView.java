package com.wayeal.newair.common.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wayeal.newair.R;

public class ChooseView extends RelativeLayout {

    private Context mContext;
    private Boolean expand = false;         //是否扩展
    private TextView mValue;
    private ImageView mArrow;
    private OnChooseClickListener mClickListener;
    private RelativeLayout mItem;

    public ChooseView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_choose,null,false);
        addView(view);
        mValue = view.findViewById(R.id.value);

        mArrow = view.findViewById(R.id.arrow);
        mItem = view.findViewById(R.id.item);
        mItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand){
                    //展开状态 进行关闭
                    expand = false;
                    if (mClickListener != null){
                        mClickListener.onShrink();
                    }
                    ViewCompat.animate(mArrow).setDuration(200)
                            .setInterpolator(new DecelerateInterpolator())
                            .rotation(0f)
                            .start();
                }else {
                    //关闭状态 进行展开
                    expand = true;
                    if (mClickListener != null){
                        mClickListener.onExpand();
                    }
                    ViewCompat.animate(mArrow).setDuration(200)
                            .setInterpolator(new DecelerateInterpolator())
                            .rotation(180f)
                            .start();
                }
            }
        });
    }

    public void setValue(String value){
        this.mValue.setText(value);
    }

    public void setExpandListener(OnChooseClickListener listener){
        this.mClickListener = listener;
    }

    public interface OnChooseClickListener{
        void onExpand();        //展开
        void onShrink();        //收缩
    }

    public void setExpand(Boolean willExpand) {
        if (expand  && !willExpand){
            expand = false;
            ViewCompat.animate(mArrow).setDuration(200)
                    .setInterpolator(new DecelerateInterpolator())
                    .rotation(0f)
                    .start();
        }else if (!expand && willExpand){
            expand = true;
            ViewCompat.animate(mArrow).setDuration(200)
                    .setInterpolator(new DecelerateInterpolator())
                    .rotation(180f)
                    .start();
        }
    }

    public void hideArrow(){
        this.mArrow.setVisibility(View.GONE);
    }

    public void showArrow(){
        this.mArrow.setVisibility(View.VISIBLE);
    }
}
