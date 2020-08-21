package com.app.yun.udp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class IotUdpThreadPool {
    private static final String TAG = "IotUdpThreadPool";
    private static final int CORE_POOL_SIZE = 0;//核心线程池大小
    private static final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;//非核心线程的最大值
    private static final int KEEP_ALIVE = 60;//保持存活时间，当线程数大于corePoolSize的空闲线程能保持的最大时间。
    private static final AtomicLong SEQ_SEED = new AtomicLong(0);//主要获取添加任务
    private static boolean isMonitor = false;

    private static final ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE
            , KEEP_ALIVE
            , TimeUnit.SECONDS
            , new SynchronousQueue<Runnable>());

    private static final ThreadPoolExecutor mThreadPoolCopy = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE
            , KEEP_ALIVE
            , TimeUnit.SECONDS
            , new SynchronousQueue<Runnable>());

    private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();



    public static ThreadPoolExecutor getThreadPool() {
        //todo 测试
//        monitor();
        return mThreadPool;
    }

    public static ThreadPoolExecutor getThreadPoolCopy(){
        return mThreadPoolCopy;
    }

    public static ExecutorService getSingleThreadPool(){
        return singleThreadExecutor;
    }


    public static void monitor(final Handler handler) {
        if (!isMonitor){
            isMonitor = true;
            Log.d(TAG, "monitor: 监控IotUdpThreadPool情况");
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (mThreadPool != null) {
                            int queueSize = mThreadPoolCopy.getQueue().size();
                            Log.d(TAG, "当前排队线程数：" + queueSize);
                            int activeCount = mThreadPoolCopy.getActiveCount();
                            Log.d(TAG, "当前活动线程数：" + activeCount);
                            long completedTaskCount = mThreadPoolCopy.getCompletedTaskCount();
                            Log.d(TAG, "执行完成线程数：" + completedTaskCount);
                            long taskCount = mThreadPoolCopy.getTaskCount();
                            Log.d(TAG, "总线程数：" + taskCount);

                            Message message = handler.obtainMessage();
                            message.what = 9999;
                            message.arg1 = activeCount;
                            handler.sendMessage(message);
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
