package com.app.myapplication.common.utils.http;

public interface IResponseHandler {
    void onFailure(int statusCode, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
