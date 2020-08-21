package com.app.yun.udp.business;

public interface IMsgProcessor {

    void init();

    void onProcessMsg(String msg);

}
