package com.study.androidsecurity.chapter04.Utils;

import android.content.Context;


public class DensityUtil {
    /**
     * dip 转 px
     */
    public static int dip2px(Context context,float dpValue){
        try{
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue*scale+0.5f);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int) dpValue;
    }
    /**
     * px 转 dip
     */
    public static int px2dip(Context context,float pxValue){
        try{
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue/scale+0.5f);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int) pxValue;
    }
}
