package com.app.yun.udp.manager;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.app.myapplication.BaseApplication;
import com.app.myapplication.common.ThreadHelper;
import com.app.yun.udp.IotUdpThreadPool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpSendManager {

    private static final String TAG = "UdpSendManager";
    private static UdpSendManager instance;
    private WifiManager.MulticastLock lock;
    private InetAddress mInetAddress;
    private int mServerPort;


    public void initData(final String serverIp, final int serverPort){
        ThreadHelper.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (mInetAddress == null || !mInetAddress.getHostName().equals(serverIp)){
                    try {
                        mInetAddress = InetAddress.getByName(serverIp);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
                mServerPort = serverPort;
            }
        });
    }

    private UdpSendManager() {
        if (lock == null) {
            this.lock = ((WifiManager) BaseApplication.mBaseApplication.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createMulticastLock("UDPwifi");
        }
    }

    public static UdpSendManager getInstance() {
        if (instance == null) {
            synchronized (UdpSendManager.class) {
                if (instance == null) {
                    instance = new UdpSendManager();
                }
            }
        }
        return instance;
    }

    public void sendMsg(final DatagramSocket sendDatagramSocket, final String message){
        Log.i(TAG, "sendMsg: message = " + message);
        Log.i(TAG, "sendMsg: ip = " + mInetAddress);
        IotUdpThreadPool.getSingleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                int msg_length = message.length();
                byte[] messageByte = message.getBytes();
                DatagramPacket p = new DatagramPacket(messageByte, msg_length, mInetAddress, mServerPort);
                try {
                    sendDatagramSocket.send(p);
                    long endTime = System.currentTimeMillis();
                    Log.d(TAG, "sendMsg 发送数据耗时： " + (endTime - startTime));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
