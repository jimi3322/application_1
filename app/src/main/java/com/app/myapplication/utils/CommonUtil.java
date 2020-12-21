package com.app.myapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;

import com.app.myapplication.BaseApplication;
import com.app.myapplication.WySharePCache;
import com.app.myapplication.common.domain.LoginData;
import com.app.myapplication.domain.Constant;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    private static final double EARTH_RADIUS = 6378137;
    public static int dip2px( float dpValue) {
        final float scale = BaseApplication.mBaseApplication.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip( float pxValue) {
        final float scale = BaseApplication.mBaseApplication.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 移动地图到指定经纬度位置
     *
     * @param ll        指定经纬度
     * @param zoomScale 缩放级别
     */
   /* public static void MoveMapToPosition(LatLng ll, float zoomScale, BaiduMap baiduMap) {
        //if (zoomScale < MIN_ZOOM_SCALE || zoomScale > MAX_ZOOM_SCALE) return;  //缩放范围3-19
        MapStatus mMapStatus = new MapStatus.Builder() //定义地图状态
                .target(ll)
                .zoom(zoomScale)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus); //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        baiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态
    }*/

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离：单位为米
     */
    public static double distanceOfTwoPoints(double lat1,double lng1,
                                             double lat2,double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 时间格式化
     */
    public static String timeFormat(Date date){
        if(date == null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 日期格式化
     */
    public static String dateFormat(Date date){
        if(date == null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date);
    }

    /**
     * map转成json
     */
    public static String mapTojson(HashMap<String, Object> params){
        JSONObject jsonObj = new JSONObject(params);
        return jsonObj.toString();
    }

    /**
     * map转成json
     */
    private static LoginData mLoginData;
    public static Boolean isLogin() {
        String loginInfo = WySharePCache.loadStringCach(Constant.LOGININFO);
        Gson gson = new Gson();
        if(TextUtils.isEmpty(loginInfo)){
            return false;
        }
        try {
            mLoginData = gson.fromJson(loginInfo,LoginData.class);
            if(mLoginData.result == 1){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }

    /**
     * 获取登录信息
     * @return
     */
    public static LoginData getLoginData() {
        //直接使用mLoginData，插件里面会有问题，所以每次都从sp中获取
        String loginInfo = WySharePCache.loadStringCach(Constant.LOGININFO);
        Gson gson = new Gson();
        try {
            mLoginData = gson.fromJson(loginInfo,LoginData.class);
            return mLoginData;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 初始化login信息
     * @param data
     */
    public static void initLoginData(LoginData data) {
        mLoginData = data;
        if(data == null){
            WySharePCache.removeShareCach(Constant.LOGININFO);
        }else{
            WySharePCache.saveStringCach(Constant.LOGININFO,new Gson().toJson(data));
        }
    }

    /**
     * 删除登录信息
     */
    public static void removeLoginData() {
        mLoginData = null;
        WySharePCache.removeShareCach(Constant.LOGININFO);
    }


    public static String getRegExpre(String type, int length){
        String lengthReg = "";
        if(length>0){
            lengthReg = "{0,"+length+"}$";
        }

        if(type == "1"){
            //数字，字母，下划线、汉字
            return "^[\\u4e00-\\u9fa5\\w\\n]"+lengthReg;

        }

        if(type == "2"){
            //数字、字母、下划线、中划线、小括号、汉字
            return "^[\\u4e00-\\u9fa5\\w-()（）\\n]"+lengthReg;

        }
        if(type == "4"){
            //电话号码校验

            /** 
             * 判断字符串是否符合手机号码格式 
             * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188 
             * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186 
             * 电信号段: 133,149,153,170,173,177,180,181,189 
             */
            return "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        }

        if(type == "5"){
            //数字、字母、下划线、中划线、小括号、汉字，人名以“、”号隔开
            return "^[\\u4e00-\\u9fa5\\w-()（）、\\n]"+lengthReg;
        }

        if(type == "6"){
            //数字、字母、下划线、小括号、汉字
            return "^[\\u4e00-\\u9fa5\\w()（）\\n]"+lengthReg;
        }
        return "";
    }


    //判断字符串是否满足正则表达式
    public static Boolean isMatcher(String str, String regular){
        Pattern pt = Pattern.compile( regular);
        Matcher mt = pt.matcher(str);
        return mt.matches();
    }

    /**
     * 获取id
     */
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * 将二进制流转换成bitmap
     */
    public static Bitmap getBitmapFromByte(byte[] streamByte){
        if(streamByte == null){
            return null;
        }
        return BitmapFactory.decodeByteArray(streamByte,0,streamByte.length);
    }

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String getMD5Str(String str) throws Exception {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new Exception("MD5加密出现错误，"+e.toString());
        }
    }

    /**
     * 压缩图片
     * */
    public static void compressBmpFileToTargetSize(File file, Long targetSize){
        if (file.length() > targetSize){
            int ratio = 2;
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            int targetWidth = options.outWidth / ratio;
            int targetHeight = options.outHeight / ratio;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            Bitmap resultBitmap = generateScaledBmp(bitmap,targetWidth,targetHeight,baos,quality);
            int count = 0;
            while (baos.size() > targetSize && count <= 10){
                targetWidth /= ratio;
                targetHeight /= ratio;
                count ++;
                baos.reset();
                resultBitmap = generateScaledBmp(resultBitmap,targetWidth,targetHeight,baos,quality);
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static Bitmap generateScaledBmp(Bitmap srcBmp
            ,int targetWidth
            ,int targetHeight
            ,ByteArrayOutputStream baos
            ,int quality){
        Bitmap result = Bitmap.createBitmap(targetWidth
                ,targetHeight
                ,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0,0,result.getWidth(),result.getHeight());
        canvas.drawBitmap(srcBmp,null,rect,null);
        if (!srcBmp.isRecycled()){
            srcBmp.recycle();
        }
        result.compress(Bitmap.CompressFormat.JPEG,quality,baos);
        return result;
    }
}
