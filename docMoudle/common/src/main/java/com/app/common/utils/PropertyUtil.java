package com.app.common.utils;

import android.content.Context;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by WUJINGWEI on 2018/1/11.
 */
public class PropertyUtil {
    private static final String APP_CONFIG_FILE_NAME = "appConfig.properties";

    /**
     * 获取配置
     *
     * @param c 上下文
     * @return 配置
     */
    public static Properties getProperties(Context c) {
        Properties props = new Properties();
        try {
            InputStream in = c.getAssets().open(APP_CONFIG_FILE_NAME);
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }


    /**
     * 保存配置
     *
     * @param c        上下文
     * @param keyName  目标键名
     * @param keyValue 写入键值
     * @return 保存结果：true成功，false失败
     */
    public static boolean setProperties(Context c, String keyName, String keyValue) {
        Properties props = new Properties();
        try {
            props.load(c.openFileInput(APP_CONFIG_FILE_NAME));
            OutputStream out = c.openFileOutput(APP_CONFIG_FILE_NAME, Context.MODE_PRIVATE);
            props.setProperty(keyName, keyValue);
            props.store(out, null);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
