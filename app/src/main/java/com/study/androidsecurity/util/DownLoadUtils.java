package com.study.androidsecurity.util;

import java.io.File;

/**
 * Created by 46358_000 on 2016/8/5 0005.
 */
public class DownLoadUtils {
    public void downapk(String url, String targetFile, final MyCallBack myCallBack) {

    }


    interface MyCallBack {
        void onSuccess(ResponseInfo<File> arg0);

        void onFailure(HttpException arg0, String arg1);

        void onLoadding(long total, long current, boolean isUploading);
    }
}
