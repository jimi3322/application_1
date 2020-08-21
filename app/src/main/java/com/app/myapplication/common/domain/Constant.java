package com.app.myapplication.common.domain;

/**
 * Created by WUJINGWEI on 2018/1/12.
 */

public class Constant {
    /*url公共*/
    public static final String HTTP_HEAD = "http://";
    public static final String WY_NODE_SERVICE_API = "/app/api";
    public static final String NODE_SERVICE_API = "/app/api/video";
    public static final String IOT_NODE_SERVICE_API = "/app/api/iot";
    public static final String SERVICE_API = "/app/api/air";
    //测试环境
    //public static final String NODE_SERVICE_IP = "36.7.84.128";
    //public static final String NODE_SERVICE_PORT = "3000";

    //生产环境
//    public static final String NODE_SERVICE_IP = "36.7.84.128";
//    public static final String NODE_SERVICE_PORT = "9699";

//        public static final String NODE_SERVICE_IP = "192.168.200.111";
//    public static final String NODE_SERVICE_PORT = "3000";
    //正式环境
    public static final String NODE_SERVICE_IP = "192.168.200.63";//36.7.84.128
    public static final String NODE_SERVICE_PORT = "3031";//3000


    /*公共符号标识*/
    public static final String ID_V = "V";//App版本标识V
    public static final String ID_ENTER = "\r\n";//回车换行
    public static final String ID_LEFT_SLASH = "/";//左斜杠
    public static final String ID_COLON_CN = "：";//中文冒号
    public static final String ID_COLON_EN = ":";//英文冒号
    public static final String ID_COLON_DOT = ".";
    public static final String ID_LEFT_PARENTHESIS = "(";//左圆括号
    public static final String ID_RIGHT_PARENTHESIS = ")";//右圆括号
    public static final String ID_LEFT_BRACKET = "[";//左中括号
    public static final String ID_RIGHT_BRACKET = "]";//右中括号
    public static final String ID_COMMA_EN = ",";//英文逗号
    public static final String ID_LINE = "_";//下划线
    public static final String ID_RIGHT_ARROW = ">";//右箭头（大于号）
    /*公共文字提示*/
    public static final String APP_NAME = "app名称";
    public static final String CRASH_TIME = "崩溃时间";
    public static final String ANDROID_VERSION = "android版本号";
    public static final String ANDROID_API_VERSION = "android API版本号";
    public static final String PHONE_MANUFACTURER = "手机制造商";
    public static final String PHONE_TYPE = "手机型号";
    public static final String STACK_TRACE_INFO = "堆栈信息";


    /**
     * 插件id
     */
    public final static String PLUGIN_VIDEO = "video";
    public final static String PLUGIN_WATER = "water";
    public final static String PLUGIN_CLOUD = "cloud";
    public final static String PLUGIN_AIR = "air";
    public final static String PLUGIN_OPERATION = "operations";
    public final static String PLUGIN_LAW_ENFOR = "lawEnfor";


    /*用户服务方法参数*/
    public static final String PARAM_USERID = "userID";//用户名
    public static final String PARAM_PASSWORD = "password";//密码
    public static final String PARAM_NEW_PASSWORD = "newPassword";//新密码
    public static final String PARAM_OLD_PASSWORD = "oldPassword";//旧密码

    /*用户服务内部存储*/
    public static final String EDIT_USERID = "USERID";//登录用户id
    public static final String EDIT_PASSWORD = "PASSWORD";//登录密码
    public static final String EDIT_REMEMBER_PASSWORD = "REMEMBER_PASSWORD";//记住密码
    public static final String EDIT_AUTO_LOGIN = "AUTO_LOGIN";//自动登录
    public static final String LOGININFO = "loginInfo";

    /*服务配置*/
    public static final String LOGIN = "Login";
    public static final String MODIFY_PASSWORD = "ModifyPassword";

    /**
     * 首頁
     */
    public static final String APP_UPGRADE_INFO = "GetAppUpdateInfo";
    public static final String WRITE_LOG = "WriteLog";
    public static final String logout = "Logout";//退出登录

    /**
     * 获取配置页参数
     */
    public static final float MAX_BOTTOM_DIALOG_HEIGH = 285.0f;

    /*
    图表
     */
    public static final String LSESSIONID = "LSESSIONID";
    public static final String GETQUALITYSTATISTICS = "GetQualityStatistics";
    public static final String SERVICE_IP = "192.168.210.49";
    public static final String SERVICE_PORT = "9699";
    public static final String CONDITION = "condition";
    public static final String TIME = "time";
    public static final String TYPE = "type";
    public static final String COMPONENTS = "components";
}
