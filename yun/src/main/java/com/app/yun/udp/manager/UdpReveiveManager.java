package com.app.yun.udp.manager;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.app.myapplication.BaseApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UdpReveiveManager {
    private static final String TAG = "UdpReveiveManager";
    private static UdpReveiveManager instance;
    private DatagramSocket receiveDatagramSocket;
    private static boolean isReceiving = true;
    private WifiManager.MulticastLock lock;

    private ReceiveThread receiveThread = new ReceiveThread("receive_thread");
    public void init(){
        if (!receiveThread.isStarted){
            try {
                receiveThread.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Log.d(TAG,"already start");
        }
    }

    private class ReceiveThread extends Thread {

        private boolean isStarted = false;

        public ReceiveThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            Log.d(TAG, "start parse thread");
            isStarted = true;
            isReceiving = true;
            while (true){
                Log.i(TAG, "run: isReceiving = " + isReceiving);
                if (isReceiving){
                    byte[] message1 = new byte[2048];
                    try {
                        DatagramPacket datagramPacket = new DatagramPacket(message1, message1.length);
                        try {
                            lock.acquire();
                            receiveDatagramSocket.receive(datagramPacket);
                            String strMsg = new String(datagramPacket.getData()).trim();
                            lock.release();
                            Log.d(TAG, "ReceiveThread收到数据 strMsg = " + strMsg);
                            UdpAnalysisManager.getInstance().handle(strMsg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private UdpReveiveManager() {
        if (receiveDatagramSocket == null || receiveDatagramSocket.isClosed()){
            try {
                receiveDatagramSocket = new DatagramSocket(null);
                receiveDatagramSocket.setReuseAddress(true);
                receiveDatagramSocket.bind(new InetSocketAddress(40931));
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        if (lock == null) {
            this.lock = ((WifiManager) BaseApplication.mBaseApplication.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createMulticastLock("UDPwifi");
        }
        init();
    }

    public static UdpReveiveManager getInstance() {
        if (instance == null) {
            synchronized (UdpReveiveManager.class) {
                if (instance == null) {
                    instance = new UdpReveiveManager();
                }
            }
        }
        return instance;
    }

   /* public void handleReceiveMsg(){
        Log.i(TAG, "handleReceiveMsg: ");
        isReceiving = true;
        if (receiveDatagramSocket != null || !receiveDatagramSocket.isClosed()){
            try {
                Log.i(TAG, "handleReceiveMsg: 重新设置receiveDatagramSocket");
                receiveDatagramSocket = new DatagramSocket(null);
                receiveDatagramSocket.setReuseAddress(true);
                receiveDatagramSocket.setSoTimeout(2000);
                receiveDatagramSocket.bind(new InetSocketAddress(40931));
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopReceiveMsg(){
        Log.i(TAG, "stopReceiveMsg: ");
        isReceiving = false;
    }*/
}
