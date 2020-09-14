package com.app.common.view;

/**
 * Created by WUJINGWEI on 2018/10/15.
 */

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * 跑马灯文本类
 */
public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写isFocused方法，让其一直返回true
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {

    }
}
