package com.app.common.utils;

import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by WUJINGWEI on 2018/1/2.
 */

public class ToastUtil {

    /**
     * 默认间隔毫秒数
     */
    private static int DEFAULT_INTERVAL = 3000;

    public static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, DEFAULT_INTERVAL);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }
}
