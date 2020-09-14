package com.app.common.utils;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.app.baselib.BaseApplication;
import com.app.common.domain.Constant;
import com.app.wayee.common.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by WUJINGWEI on 2017/12/9.
 */

public class UpdateAppManager {

    private static final int DOWN_PROCESSING_MESSAGE = 1;
    private static final int DOWN_FINISHED_MESSAGE = 2;
    private static final int CAPACITY_KB = 1024 * 25; //支持最大20M的Apk文件

    private Context mContext;
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    private ProgressBar mProgress;
    private Thread downLoadThread;

    private int progress;
    private boolean interceptFlag = false;
    private String noticeDialogTitle = "";
    private UpdateActionCallback mCallback;
    //临时文件，下载时用，现在完成之后修改名字为saveFileFullName
    private String tempFileFullName = "";
    private String saveFileFullName = "";

    /**
     * 设置提示对话框标题
     *
     * @param noticeDialogTitle
     */
    public UpdateAppManager setNoticeDialogTitle(String noticeDialogTitle) {
        this.noticeDialogTitle = noticeDialogTitle;
        return this;
    }

    private String noticeDialogPositiveText = "";

    /**
     * 设置提示对话框确定按钮文本
     *
     * @param noticeDialogPositiveText
     */
    public UpdateAppManager setNoticeDialogPositiveText(String noticeDialogPositiveText) {
        this.noticeDialogPositiveText = noticeDialogPositiveText;
        return this;
    }

    private String noticeDialogNegativeText = "";

    /**
     * 设置提示对话框取消按钮文本
     *
     * @param noticeDialogNegativeText
     */
    public UpdateAppManager setNoticeDialogNegativeText(String noticeDialogNegativeText) {
        this.noticeDialogNegativeText = noticeDialogNegativeText;
        return this;
    }

    private String noticeDialogMainText = "";

    /**
     * 设置提示对话框主内容文本
     *
     * @param noticeDialogMainText
     */
    public UpdateAppManager setNoticeDialogMainText(String noticeDialogMainText) {
        this.noticeDialogMainText = noticeDialogMainText;
        return this;
    }

    private String downloadDialogTitle = "";

    /**
     * 设置下载对话框标题
     *
     * @param downloadDialogTitle
     */
    public void setDownloadDialogTitle(String downloadDialogTitle) {
        this.downloadDialogTitle = downloadDialogTitle;
    }

    private String downloadDialogNegativeText = "";

    /**
     * 设置下载对话框取消按钮文本
     *
     * @param downloadDialogNegativeText
     */
    public void setDownloadDialogNegativeText(String downloadDialogNegativeText) {
        this.downloadDialogNegativeText = downloadDialogNegativeText;
    }

    private String fileUrl = "";

    /**
     * 设置文件URL
     *
     * @param fileUrl
     */
    public UpdateAppManager setUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    private String savePath = "";

    /**
     * 设置下载包安装包路径
     *
     * @param savePath
     */
    public UpdateAppManager setSavePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    private String saveFileName = "";

    /**
     * 设置保存文件名（需包含后缀apk）
     *
     * @param saveFileName
     */
    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public UpdateAppManager setUpdateCallback(UpdateActionCallback callback){
        mCallback = callback;
        return this;
    }

    /**
     * 消息处理
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_PROCESSING_MESSAGE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_FINISHED_MESSAGE:
                    //非apk文件，下载完成后通知对应页面
                    if(mCallback!=null){
                        mCallback.onDownloadFinish();
                    }
                    if(downloadDialog!=null && downloadDialog.isShowing()){
                        downloadDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public UpdateAppManager(Context context) {
        this.mContext = context;
    }

    /**
     *  外部接口让主Activity调用,显示通知更新对话框
     */
    public UpdateAppManager showNoticeDialog() {
        Builder builder = new Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle(noticeDialogTitle);
        builder.setMessage(noticeDialogMainText);
        builder.setPositiveButton(noticeDialogPositiveText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(mCallback != null){
                    mCallback.onOkUpdate();
                }
//                showDownloadDialog(false);
            }
        });
        builder.setNegativeButton(noticeDialogNegativeText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(mCallback != null){
                    mCallback.onCancelUpdate();
                }
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
        return this;
    }

    /**
     * 显示下载进度对话框
     * mustDown 是否必须下载，首次安装插件，就必须下载，不展示“取消”按鈕
     * 插件升级，可以取消下载，展示“取消”按鈕
     */
    public UpdateAppManager showDownloadDialog(boolean mustDown) {
        Builder builder = new Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle(downloadDialogTitle);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.layout_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);

        builder.setView(v);
        if(!mustDown){
            builder.setNegativeButton(downloadDialogNegativeText, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    interceptFlag = true;
                }
            });
        }
        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
        return this;
    }

    /**
     * apk下载线程
     */
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            FileOutputStream fos = null;
            InputStream is = null;
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }

                tempFileFullName = savePath + File.separator+"temp"+ fileUrl.substring(fileUrl.lastIndexOf(Constant.ID_COLON_DOT));
                File downFile = new File(tempFileFullName);
                fos = new FileOutputStream(downFile);

                int count = 0;
                byte buf[] = new byte[CAPACITY_KB];

                do {
                    int readNum = is.read(buf);
                    count += readNum;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_PROCESSING_MESSAGE);
                    if (readNum <= 0) {
                        //删除原来的压缩文件
                        saveFileFullName = savePath + File.separator+fileUrl.substring(fileUrl.lastIndexOf(Constant.ID_LEFT_SLASH) + 1);
                        File saveFile = new File(saveFileFullName);
                        if(saveFile.exists()){
                            saveFile.delete();
                        }
                        //删除原来的解压后文件
                        String unZipFilePath = savePath + File.separator+ fileUrl.substring(fileUrl.lastIndexOf(Constant.ID_LEFT_SLASH) + 1,fileUrl.lastIndexOf(Constant.ID_COLON_DOT));
                        File unZipFile = new File(unZipFilePath);
                        if(unZipFile.exists()){
                            unZipFile.delete();
                        }
                        //将新下载的文件重命名
                        downFile.renameTo(saveFile);
                        //通知下载完成
                        mHandler.sendEmptyMessage(DOWN_FINISHED_MESSAGE);
                        break;
                    }
                    fos.write(buf, 0, readNum);
                } while (!interceptFlag);//点击取消就停止下载.
            } catch (MalformedURLException e) {

            } catch (IOException e) {

            }finally {
                try {
                    if(fos!=null){
                        fos.close();
                    }
                    if(is != null){
                        is.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 下载apk
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }


    public interface UpdateActionCallback{
        //升级弹窗，点了取消升级按钮
        public void onCancelUpdate();
        //确认升级
        public void onOkUpdate();
        //插件下载完成
        public void onDownloadFinish();
    }

}



