package com.app.yun.udp.listener;

public interface IResultInfoListner<T> extends IInfoListener{
    void onResult(T t);
}
