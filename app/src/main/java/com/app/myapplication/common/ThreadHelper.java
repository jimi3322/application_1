package com.app.myapplication.common;

import android.os.Handler;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadHelper {

    private static final String TAG = ThreadHelper.class.getSimpleName();
    private static ThreadHelper instance ;
    public static ThreadHelper getInstance(){
        if(instance == null){
            instance = new ThreadHelper();
        }
        return instance;
    }
    private ThreadHelper(){
        executorService = Executors.newSingleThreadExecutor();
    }
    private ExecutorService executorService;
    private MyHandler handler = new MyHandler();
    private Handler mUiHandler = new Handler();
    public void post(Runnable runnable){
        executorService.execute(runnable);
    }
    public void postDelayed(Runnable runnable, long delayMillis){
        handler.postDelayExec(runnable,delayMillis);
    }
    public void postByUI(Runnable runnable){
        mUiHandler.post(runnable);
    }
    public void postDelayedByUI(Runnable runnable, long delayMillis){
        mUiHandler.postDelayed(runnable,delayMillis);
    }
    public void removeRunnableByUI(Runnable runnable){
        mUiHandler.removeCallbacks(runnable);
    }
    private class MyHandler extends Handler {
        private static final int EXEC_MES = 1001;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case EXEC_MES:
                    executorService.execute((Runnable) msg.obj);
                    break;
            }
        }

        public void postDelayExec(Runnable runnable, long delayMillis){
            Message msg = obtainMessage(EXEC_MES);
            msg.obj = runnable;
            sendMessageDelayed(msg,delayMillis);
        }
    }

}
