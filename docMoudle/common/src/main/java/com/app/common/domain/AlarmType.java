package com.app.common.domain;

public enum AlarmType {
    OVERPROOF("overproof","超限数据报警"), ABNORMAL("abnormal","异常数据报警"),OFFLINE("offline","离线报警");
    String key;
    String value;
    private AlarmType(String index, String value){
        this.key = index;
        this.value = value;
    }
    public String getKey()
    {
        return key;
    }

    public void setKey(int String)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    public static String getValueByKey(String key){
        if(OVERPROOF.key.equals(key)){
            return OVERPROOF.value;
        }
        if(ABNORMAL.key.equals(key)){
            return ABNORMAL.value;
        }
        if(OFFLINE.key.equals(key)){
            return OFFLINE.value;
        }
        return  "";
    }
}
