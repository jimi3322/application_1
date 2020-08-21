package com.app.yun.udp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

public class UdpUtil {

    /**   yyyyMMddHHmmssSSS
     *    str : QN=
     *
     *
     */

    public static String getMessage(HashMap<String, String> param) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        StringBuffer str = new StringBuffer();
        str.append("QN=" + format.format(new Date()) + ";" + "ST=32;");

        if (param != null && param.containsKey("CN")) {
            str.append("CN=" + param.get("CN") + ";");
        }
        if (param != null && param.containsKey("dataIndex")){
            str.append("dataIndex=" + param.get("dataIndex") + ";");
        }
        str.append("PW=123456;");
        if (param != null && param.containsKey("MN")) {
            str.append("MN=" + param.get("MN") + ";");
        }
        str.append("Flag=5;CP=&&");
        try {
            str.append("ip=" + getIpAddress() + ";");
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (param != null) {
            for (String key : param.keySet()) {
                if (key == "CN" || key == "MN" || key == "dataIndex") {
                    continue;
                }
                str.append(key + "=" + param.get(key) + ";");
            }
        }
        if (str.toString().endsWith(";")) {
            str.deleteCharAt(str.length() - 1);
        }
        str.append("&&");
        return str.toString();
    }

    public static String getIpAddress() throws SocketException {
        String ipaddress = "";
        for (Enumeration<NetworkInterface> en = NetworkInterface
                .getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    ipaddress = inetAddress.getHostAddress().toString();
                    //过滤掉ipv6
                    if (!ipaddress.contains("::")) {//ipV6的地址
                        return ipaddress;
                    }
                }
            }
        }
        return ipaddress;
    }
}
