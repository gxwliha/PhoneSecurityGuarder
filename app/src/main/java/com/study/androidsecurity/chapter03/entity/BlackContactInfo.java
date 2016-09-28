package com.study.androidsecurity.chapter03.entity;

/**
 * Created by 46358_000 on 2016/9/18 0018.
 */
public class BlackContactInfo {
    public String phoneNumber;
    public String contactName;
    public int mode;

    public String getModeString(int mode) {
        switch (mode) {
            case 1:
                return "电话拦截";
            case 2:
                return "短信拦截";
            case 3:
                return "电话,短信拦截";
        }

        return "";
    }
}
