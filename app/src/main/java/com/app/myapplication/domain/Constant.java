package com.app.myapplication.domain;

/**
 * Created by WUJINGWEI on 2018/1/12.
 */

public class Constant {
    /*url公共*/
    public static final String HTTP_HEAD = "http://";
    public static final String WY_NODE_SERVICE_API = "/app/api";
    public static final String NODE_SERVICE_API = "/app/api/video";
    public static final String WATER_NODE_SERVICE_API = "/app/api/water";
    public static final String OPERATION_NODE_SERVICE_API = "/app/api/operation";
    public static final String LAW_ENFORCEMENT_NODE_SERVICE_API = "/app/api/mobilelaw";
    public static final String AIR_NODE_SERVICE_API = "/app/api/air";
    //测试环境
    /*public static final String NODE_SERVICE_IP = "36.7.84.128";
    public static final String NODE_SERVICE_PORT = "3000";*/
    //临时环境
    /*public static final String NODE_SERVICE_IP = "192.168.210.49";
    public static final String NODE_SERVICE_PORT = "9699";*/


    //生产环境
    public static final String NODE_SERVICE_IP = "36.7.84.128";
    public static final String NODE_SERVICE_PORT = "9699";

    public static final String ACTIVITY_URI = "scheme";
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
    public static final String OFFLINE = "离线";
    public static final String ERROR = "故障";
    public static final String APP_NAME = "app名称";
    public static final String CRASH_TIME = "崩溃时间";
    public static final String ANDROID_VERSION = "android版本号";
    public static final String ANDROID_API_VERSION = "android API版本号";
    public static final String PHONE_MANUFACTURER = "手机制造商";
    public static final String PHONE_TYPE = "手机型号";
    public static final String STACK_TRACE_INFO = "堆栈信息";

    /**
     * 污染源类型
     */
    public final static String POLLUTION_TYPE_WATER = "water";
    public final static String POLLUTION_TYPE_GAS = "gas";
    public final static String POLLUTION_WATER_TITLE = "废水";
    public final static String POLLUTION_GAS_TITLE = "废气";

    /**
     * 插件id
     */
    public final static String PLUGIN_VIDEO = "video";
    public final static String PLUGIN_WATER = "water";
    public final static String PLUGIN_CLOUD = "cloud";
    public final static String PLUGIN_AIR = "air";
    public final static String PLUGIN_OPERATION = "operations";
    public final static String PLUGIN_LAW_ENFOR = "lawEnfor";
    public static final String SP_PLUGIN_VERSION_KEY = "_version";


    /*用户服务方法参数*/
    public static final String PARAM_USERID = "userID";//用户名
    public static final String PARAM_PASSWORD = "password";//密码
    public static final String PARAM_NEW_PASSWORD = "newPassword";//新密码
    public static final String PARAM_OLD_PASSWORD = "oldPassword";//旧密码

    /*用户服务内部存储*/
    public static final String EDIT_USER_INFO = "USER_INFO";//用户信息
    public static final String EDIT_USERID = "USERID";//登录用户名
    public static final String EDIT_USER_NAME = "USERNAME";//登录用户名
    public static final String EDIT_USER_ROLE_ID = "roleId";//用户角色id
    public static final String EDIT_PASSWORD = "PASSWORD";//登录密码
    public static final String EDIT_REMEMBER_PASSWORD = "REMEMBER_PASSWORD";//记住密码
    public static final String EDIT_AUTO_LOGIN = "AUTO_LOGIN";//自动登录
    public static final String JSESSIONID = "JSESSIONID";
    public static final String LSESSIONID = "LSESSIONID";
    public static final String LOGININFO = "loginInfo";

    /*服务配置*/
    public static final String LOGIN = "Login";
    public static final String MODIFY_PASSWORD = "ModifyPassword";

    /**
     * 首頁
     */
    public static final String INDEX_MAP = "GetAppMapLocationInfo";
    public static final String PLUGIN_LIST = "GetPluginInfo";
    public static final String APP_UPGRADE_INFO = "GetAppUpdateInfo";
    public static final String WRITE_LOG = "WriteLog";
    public static final String uploadClientId = "uploadClientId";//提交cid
    public static final String deleteClientId = "deleteClientId";//提交cid
    public static final String logout = "Logout";//退出登录

    /**
     * 获取配置页参数
     */
    public static final String GET_TEMPORARY_DATA = "ReadParam";
    public static final String UPLOAD_TEMPORARY_DATA = "WriteParam";

    public static final float MAX_BOTTOM_DIALOG_HEIGH = 285.0f;

}
