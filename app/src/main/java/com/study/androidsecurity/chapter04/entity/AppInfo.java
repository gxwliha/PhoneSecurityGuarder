package com.study.androidsecurity.chapter04.entity;

import android.graphics.drawable.Drawable;


public class AppInfo {
    public String packageName;
    public Drawable icon;
    public String appName;
    public String apkpath;
    public boolean isLock;
    public long appSize;
    public boolean isInRoom;
    public boolean isUserApp;
    public boolean isSelected = false;

    public String getAppLocation(boolean isInRoom){
        if(isInRoom){
            return "手机内存";
        }else {
            return "外部存储";
        }
    }
}
