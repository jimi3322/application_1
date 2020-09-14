package com.wayeal.newair.common;


public class AirConstants {

    public static final int RESPONSE_STATUS_SUCCESS = 1;
    public static final int RESPONSE_STATUS_FAILURE = -1;

    public static final String TYPE = "type";
    public static final String CONDITION = "condition";
    public static final String AREA = "area";          //area的key值
    public static final String STARTDATE = "StartDate";          //排序开始时间key
    public static final String NAME = "Name";          //排序key
    public static final String PAGENUMBER = "pageNumber";
    public static final String PAGESIZE = "pageSize";
    public static final String POLLUTIONTYPE = "PollutionType";
    public static final String STATUS = "Status";               //会话状态
    public static final String SYSTEM_STATUS = "SystemStatus";               //系统状态
    public static final String COUNTY_ID = "CountyID";               //区的ID
    public static final String ID = "ID";               //站点ID
    public static final String SETTING_TYPE = "SettingType";               //站点ID
    public static final String COMPONENT_ID = "ComponentID";               //站点ID

    public static final String TIME = "time";               //时间
    public static final String STARTIME = "StartTime";               //时间
    public static final String ENDTIME = "EndTime";               //时间
    public static final String COMPONENTTIME = "ComponentTime";
    public static final String ORDER = "order";   //监测项数
    public static final String AIR_MONTH_CALENDAR = "air-monthCalendar";   //月日历
    public static final String TIMELINESS = "timeliness";   //监测项数
    public static final String COMPONENTS = "components";   //组件名
    public static final String AIR_HOUR_RANK = "air-hourRank";   //组件名
    public static final String AIR = "air";
    public static final String POLLUTION_TYPE = "PollutionType";
    public static final String REPORT_TYPE = "reportType";
    public static final String DISPOSAL_ID = "DisposalID";
    public static final String WARNING_ID = "WarningID";
    public static final String DISPOSAL_DATE = "DisposalDate";
    public static final String DISPOSAL_USER = "DisposalUser";
    public static final String DISPOSAL_REMARK = "DisposalRemark";
    public static final String DISPOSAL_TYPE = "DisposalType";

    //获取站点
    public static final String GET_AIR_STATION_TREE = "GetAirStationTree";

    //获取站点信息
    public static final String GET_AREA_ALARM_DATA = "GetAreaAlarmData";

    //实时一览界面获取信息
    public static final String GET_TESTPOINT_INFO_WITH_USERRIGHTS = "GetTestPointInfoWithUserRights";

    //实时数据查询
    public static final String GET_REAL_TIME_DATA = "GetRealTimeData";

    //获取空气标准
    public static final String GET_STANDARD_INFO = "GetStandardInfo";

    //获取空气config
    public static final String GET_REPORT_CONFIG = "GetReportConfigItem";

    //获取组件
    public static final String GET_COMPONENT_INFO = "GetComponentInfo";

    //获取地图信息
    public static final String GET_MAP_LOCATION_INFO = "GetMapLocationInfo";

    //获取信息 包括日历 排行
    public static final String GENERATE_DATA_REPORT = "GenerateDataReport";


    //历史数据查询
    public static final String GET_HISTORICAL_DATA = "GetHistoricalData";
    //表头数据查询
    public static final String GET_TABLE_HEADER_DATA = "GetTableHeaderInfo";

    //获取对比分析表格数据
    public static final String GET_GRAPHS_REPORT_DATA = "GetGraphsReportData";

    //历史数据曲线数据查询
    public static final String GET_HISTORICAL_DATA_Curve = "GetHistoricalDataCurve";

    //历史数据曲线数据查询
    public static final String GET_AQI_DATA_Curve = "GetAQIDataCurve";

    //历史数据曲线数据查询
    public static final String INSERT_ALARM_DISPOSAL_INFO = "InsertAlarmDisposalInfo";

    /**
     * 会话状态
     * */
    public static final int ONLINE = 2;
    public static final String ONLINE_TEXT = "在线";
    public static final int UPLINE = 1;
    public static final String UPLINE_TEXT = "上线";
    public static final int OFFLINE = 0;
    public static final String OFFLINE_TEXT = "离线";

    /**
     * 系统状态
     * */
    public static final String NORMAL = "N";
    public static final String NORMAL_TEXT = "正常";
    public static final String SUPER_UPPER_LIMIT = "T";
    public static final String SUPER_UPPER_LIMIT_TEXT = "超上限";
    public static final String SUPER_LOWER_LIMIT = "L";
    public static final String SUPER_LOWER_LIMIT_TEXT = "超下限";
    public static final String INSTRUMENT_FAILURE = "D";
    public static final String INSTRUMENT_FAILURE_TEXT = "仪器故障";
    public static final String INSTRUMENT_COMMUNICATION_FAILURE = "F";
    public static final String INSTRUMENT_COMMUNICATION_FAILURE_TEXT = "仪器通信故障";
    public static final String INSTRUMENT_OFFLINE = "B";
    public static final String INSTRUMENT_OFFLINE_TEXT = "仪器离线";
    public static final String MAINTAIN_DEBUG_DATA = "M";
    public static final String MAINTAIN_DEBUG_DATA_TEXT = "维护调试数据";
    public static final String LACK_OF_REAGENTS = "lr";
    public static final String LACK_OF_REAGENTS_TEXT = "缺试剂";
    public static final String LACK_OF_WATER = "lp";
    public static final String LACK_OF_WATER_TEXT = "缺纯水";
    public static final String WATER_SHORTAGE = "lw";
    public static final String WATER_SHORTAGE_TEXT = "缺水样";
    public static final String LACK_OF_STANDARD_SAMPLE = "ls";
    public static final String LACK_OF_STANDARD_SAMPLE_TEXT = "缺标样";
    public static final String STANDARD_RECOVERY = "ra";
    public static final String STANDARD_RECOVERY_TEXT = "加标回收";
    public static final String PARALLEL_SAMPLE_TEST = "pt";
    public static final String PARALLEL_SAMPLE_TEST_TEXT = "平行样测试";

    public static final String NO_DATA_COLOR = "rgb(170,170,170)";

    public static final String EXCELLENT_TEXT = "优";
    public static final String EXCELLENT = "#00E400";
    public static final String GOOD_TEXT = "良";
    public static final String GOOD = "#FFFF00";
    public static final String SLIGHTLY_POLLUTED_TEXT = "轻度污染";
    public static final String SLIGHTLY_POLLUTED = "#FF7E00";
    public static final String MIDDLE_POLLUTED_TEXT = "中度污染";
    public static final String MIDDLE_POLLUTED = "#FF0000";
    public static final String HEAVY_POLLUTED_TEXT = "重度污染";
    public static final String HEAVY_POLLUTED = "#99004C";
    public static final String SERVE_POLLUTED_TEXT = "严重污染";
    public static final String SERVE_POLLUTED = "#7E0023";

    /**
     * 报警类型
     * */
    public static final String OVERPROOF_WARNING = "overproof";
    public static final String OVERPROOF_WARNING_TEXT = "超限数据报警";
    public static final String ABNORMAL_WARNING = "abnormal";
    public static final String ABNORMAL_WARNING_TEXT = "异常数据报警";
    public static final String OFFLINE_WARNING = "offline";
    public static final String OFFLINE_WARNING_TEXT = "离线报警";

    public static final String REPORT_TO_LEADER = "1";
    public static final String REPORT_TO_LEADER_TEXT = "报告领导";
    public static final String NOTICE_TO_LAWER = "2";
    public static final String NOTICE_TO_LAWER_TEXT = "通知执法人员";
    public static final String NOTICE_TO_OPERATION = "3";
    public static final String NOTICE_TO_OPERATION_TEXT = "通知运维人员";
    public static final String NOTICE_TO_STATION = "4";
    public static final String NOTICE_TO_STATION_TEXT = "通知监测站点";
    public static final String FREE_PROCESS = "5";
    public static final String FREE_PROCESS_TEXT = "自行处置";
    public static final String ANOTHER = "6";
    public static final String ANOTHER_TEXT = "其他";

}
