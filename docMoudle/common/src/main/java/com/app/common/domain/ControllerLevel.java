package com.app.common.domain;

public enum ControllerLevel {
    NATION("nation","国控"), PROVINCE("province","省控"),CITY("city","市控");
    String key;
    String value;
    private ControllerLevel(String index, String value){
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

//    public static String getValueByKey(String key){
//    }
}
