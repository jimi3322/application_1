package com.app.common.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class PushInfo implements Parcelable {

    public static final String NOTIFICATION_ID_PREFIX = "99";
    public static final String BUNDLE_PUSH_INFO = "bundle_push_info";

    public int notificationId = 0; // notification ID
    public String taskId;
    public String pushId = "";      // 第三方SDK推送的消息ID
    public String title = "";
    public String type = "";
    public String desc = "";
    public long msgId = 0;
    //针对哪个插件发的消息
    public String pluginId="";

    public PushInfo() {
    }

    @Override
    public String toString() {
        return "PushInfo{" +
                "notificationId=" + notificationId +
                ", taskId='" + taskId + '\'' +
                ", pushId='" + pushId + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", msgId=" + msgId +
                ", pluginId='" + pluginId + '\'' +
                '}';
    }

    protected PushInfo(Parcel in) {
        notificationId = in.readInt();
        taskId = in.readString();
        pushId = in.readString();
        title = in.readString();
        type = in.readString();
        desc = in.readString();
        msgId = in.readLong();
        pluginId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(notificationId);
        dest.writeString(taskId);
        dest.writeString(pushId);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(desc);
        dest.writeLong(msgId);
        dest.writeString(pluginId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PushInfo> CREATOR = new Creator<PushInfo>() {
        @Override
        public PushInfo createFromParcel(Parcel in) {
            return new PushInfo(in);
        }

        @Override
        public PushInfo[] newArray(int size) {
            return new PushInfo[size];
        }
    };
}
