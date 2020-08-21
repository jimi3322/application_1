package com.app.common.data

open class MainData{
    companion object {
        val STARTING_TYPE = "1"         //启动中
        val REAL_TIME_LEAK_TYPE = "2"   //捡漏住界面
        val AWAITING_TYPE = "3"         //待机界面
        val CHOU_KONG_TYPE = "4"        //抽空中
        val ALARM_TIP_TYPE = "5"        //报警提示
        val ZK_BD_TYPE = "6"            //真空标定
        val XQ_BD_TYPE = "7"            //吸枪标定
    }
    //捡漏主界面
    var pageType:String? = ""
    var realLeakA:ArrayList<String> = ArrayList()           //漏率值格式： a×10n Pa.m3/s 这里是集合 最大20个值
    var realLeakN:ArrayList<String> = ArrayList()             //同样，这里改成int集合，最大20个值
    var leakUnit:String? = ""
    var leakProgress:Int? = 0           //进度条的值
    var leakProgressMax:Int? = 0        //进度条最大值
    var leakProgressMin:Int? = 0        //进度条最小值
    var minLeakA:Int? = 0
    var minLeakN:Int? = 0               //进度条左上方的值
    var maxLeakA:Int? = 0
    var maxLeakN:Int? = 0               //进图条右上方的值
    var dot:ArrayList<String?> = ArrayList()                //漏率的第二位小数,这里改成String集合，最大20个值
    var smallSingle:String? = ""        //是否展示小于号
    var leakPre:String? = ""            //检测口压力 这里不需要单位，单位用重新的key来接收
    var leakModel:String? = ""          //0:"真空模式";1:“吸枪模式”
    var zeroState:String? = ""          //0:"未调零";1:“调零中”
    var otherModel:String? = ""
    var limitChannel:String?= ""        //限值通道
    var preUnit:String? = ""            //真空标定 检测口压力单位


    var alarmVol:String? = ""
    var leakSignal:String? = ""
    var productNum:String? = ""
    var numLength:String? = ""
    var ventPre:String? = ""
    var alarmThreshold:String? = ""
    var progress:String? = "20"
    var realLeak:String? = ""

    var alarmNum:String? = "8"
    var alarmDesc:String? = "吸枪模式标定时，本地大于信号"
    var alarmReason:String?="121"
    var solution:String?="121"

    var zkRealLeakA:String? = ""
    var zkRealLeakN:String? = ""
    var startTime:String = ""
    var xqsStartTime:String = ""
    var zkStartTime:String = ""
    var lastBdValue:String? = ""
    var signalValue:String? = ""
    var gunPre:String? = ""
    var xqRealLeakA:String? = ""
    var xqRealLeakN:String? = ""
    var state:String = ""
    var leak:String? = ""
}

class HeMassLeakMainData {
    var realLeakA:ArrayList<String> = ArrayList()
    var realLeakN:ArrayList<String> = ArrayList()
    var leakUnit:String? = "Pa.m3/s"
    var leakProgress:Int? = 0
    var leakProgressMax:Int? = 0
    var leakProgressMin:Int? = 0
    var dot:ArrayList<String?> = ArrayList()
    var smallSingle:String? = "" //是否展示小于号
    var minLeakN:Int? = -8
    var minLeakA : Int? = 10
    var maxLeakN:Int? = -8
    var maxLeakA:Int? = 10
    var leakPre:String? = ""
    var leakModel:String? = "0"
    var zeroState:String? = "0"
    var otherModel:String? = "UTRL"
    var limitChannel:String?= ""
    var leak:String? = ""
}

class AwaitingMainData{
    var realLeak:String? = ""
    var leakPre:String? = ""
    var leakModel:String? = "0"
    var alarmVol:String? = "0"
    var leakSignal:String? = "6092"
    var productNum:String? = "wer1223"
    var numLength:String? = "5"
    var ventPre:String? = ""
    var alarmThreshold:String? = ""

}

class ChouKongMainData {
    var progress:Int? = 20
    var leakPre:String? = ""
}
class AlarmTipMainData{
    var alarmNum:String? = "8"
    var alarmDesc:String? = "0"
    var alarmReason:String?=""
    var solution:String?=""

}

//真空标定
class ZKMainData{
    var zkRealLeakA:String? = ""
    var zkRealLeakN:String? = ""
    var leakUnit:String? = "Pa.m3/s"
    var zkStartTime:String = ""
    var state:String = ""
    var leakPre:String? = ""
    var lastBdValue:String? = ""
    var signalValue:String? = ""
    var preUnit:String? = ""
    var leakModel:String? = ""
}

//吸枪标定
class XQMainData{
    var xqRealLeakA:String? = ""
    var xqRealLeakN:String? = ""
    var leakUnit:String? = "Pa.m3/s"
    var xqsStartTime:String = ""
    var gunPre:String? = ""
    var signalValue:String? = ""
    var preUnit:String? = ""
    var leakModel:String? = ""
}

class StartingData{
    var startTime:String = ""
    var leakPre:String? = ""
    var ventPre:String? = ""
    var alarmThreshold:String? = ""
    var progress:Int? = 0
}