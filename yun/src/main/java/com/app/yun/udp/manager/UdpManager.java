package com.app.yun.udp.manager;

import com.app.yun.udp.UdpUtil;

import java.net.DatagramSocket;
import java.util.HashMap;

import static android.util.Log.d;

public class UdpManager {

    private static UdpManager instance;
    private int SERVER_PORT = 11121;//服务端端口
    //private WifiManager.MulticastLock lock;
    //private InetAddress mInetAddress;
    private DatagramSocket sendSocket;
    //private DatagramSocket receiveSocket;
    private String serverIp;

    private UdpManager() {
    }

    public static UdpManager getInstance() {
        if (instance == null) {
            synchronized (UdpManager.class) {
                if (instance == null) {
                    instance = new UdpManager();
                }
            }
        }
        return instance;
    }

    public void setServerIp(String ip){
        this.serverIp = ip;
        init();
    }

    private void init(){
        if (sendSocket == null || sendSocket.isClosed()){
            try{
                sendSocket = new DatagramSocket();
                sendSocket.setSoTimeout(3000);
            }catch (Exception e){
                e.printStackTrace();
                sendSocket.close();
            }
        }
        d("serverIp = ", serverIp);
        UdpSendManager.getInstance().initData(serverIp,SERVER_PORT);
        UdpReveiveManager.getInstance().init();
    }


    public void sendMsg(HashMap<String, String> param) {
        final String message = UdpUtil.getMessage(param);
        if (sendSocket == null || sendSocket.isClosed()){
            init();
        }
        UdpSendManager.getInstance().sendMsg(sendSocket,message);
    }

}
