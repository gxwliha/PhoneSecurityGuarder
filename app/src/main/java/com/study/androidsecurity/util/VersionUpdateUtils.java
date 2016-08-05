package com.study.androidsecurity.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

import com.study.androidsecurity.R;
import com.study.androidsecurity.entity.VersionEntity;

/**
 * Created by 46358_000 on 2016/8/5 0005.
 */
public class VersionUpdateUtils {
    private static final int MESSAGE_NET_EEOR = 101;
    private static final int MESSAGE_IO_EEOR = 102;
    private static final int MESSAGE_JSON_EEOR = 103;
    private static final int MESSAGE_SHOEW_DIALOG = 104;
    protected static final int MESSAGE_ENTERHOME = 105;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_IO_EEOR:
                    Toast.makeText(MyApplication.getContext(),"IO异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_JSON_EEOR:
                    Toast.makeText(MyApplication.getContext(), "JSON解析异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_NET_EEOR:
                    Toast.makeText(MyApplication.getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_SHOEW_DIALOG:
                    showU
            }
        }
    };


    private void showUpdateDialog(final VersionEntity versionEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getContext());
        builder.setTitle("检测到新版本：" + versionEntity.versioncode);
        builder.setMessage(versionEntity.description);
        builder.setCancelable(false);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initProgressDialog();

            }
        })
    }

    private String mVersion;
    private ProgressDialog mprogressDialog;

    private VersionEntity versionEntity;

    public VersionUpdateUtils(String version, Activity activity) {
        mVersion = version;

    }


    private void initProgressDialog() {
        mprogressDialog = new ProgressDialog(MyApplication.getContext());
        mprogressDialog.setMessage("准备下载...");
        mprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mprogressDialog.show();
    }


    protected void downloadNewApk(String apkurl) {

    }

    private void enterHome() {
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME, 2000);
    }
}
