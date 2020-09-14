package com.app.common.domain;

public enum SiteNetStatusFilter {
    OFFLINE(0,"离线"), ONLINE(2,"在线"),ALL(3,"全部"),ONLINE_NORMAL(0,"正常"),ONLINE_OVER(1,"超标");
    int index;
    String value;
    private SiteNetStatusFilter(int index, String value){
        this.index = index;
        this.value = value;
    }
    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public static SiteNetStatusFilter getSiteStatusFilter(int status) {
        switch (status) {
            case 0:
                return OFFLINE;
            case 2:
                return ONLINE;
            default:
                return ALL;
        }
    }

    public static String getVlueByIndex(int status) {
        switch (status) {
            case 0:
                return OFFLINE.value;
            case 2:
                return ONLINE.value;
            default:
                return ALL.value;
        }
    }
}
