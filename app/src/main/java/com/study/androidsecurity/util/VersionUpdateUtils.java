package com.study.androidsecurity.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.study.androidsecurity.R;
import com.study.androidsecurity.entity.VersionEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                    Toast.makeText(context,"IO异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_JSON_EEOR:
                    Toast.makeText(context, "JSON解析异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_NET_EEOR:
                    Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_SHOEW_DIALOG:
                    showUpdateDialog(versionEntity);
                    break;
//                case MESSAGE_ENTERHOME:
//                    Intent intent = new Intent(context, HomeActivity.class);
//                    context.startActivity(intent);
//                    break;
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
                downloadNewApk(versionEntity.apkurl);

            }
        });

        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    private String mVersion;
    private ProgressDialog mprogressDialog;
    private Activity context;

    private VersionEntity versionEntity;

    public VersionUpdateUtils(String version, Activity activity) {
        mVersion = version;
        context = activity;

    }

    public void getCloudVersion() {
        try {
            HttpURLConnection connection;
            URL url = new URL("http://202.113.76.210/updateinfo.json");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream inputStream=connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);

            }

            String result = response.toString();
            JSONObject jsonObject = new JSONObject(result);
            versionEntity = new VersionEntity();
            String code = jsonObject.getString("code");
            versionEntity.versioncode=code;
            String des = jsonObject.getString("des");
            versionEntity.description = des;
            String apkurl = jsonObject.getString("apkurl");
            versionEntity.apkurl = apkurl;
            if (!mVersion.equals(versionEntity.versioncode)) {
                handler.sendEmptyMessage(MESSAGE_SHOEW_DIALOG);

            }
        } catch (IOException e) {
            handler.sendEmptyMessage(MESSAGE_IO_EEOR);
            e.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(MESSAGE_JSON_EEOR);
            e.printStackTrace();
        }
    }


    private void initProgressDialog() {
        mprogressDialog = new ProgressDialog(MyApplication.getContext());
        mprogressDialog.setMessage("准备下载...");
        mprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mprogressDialog.show();
    }


    protected void downloadNewApk(String apkurl) {
        DownLoadUtils downLoadUtils = new DownLoadUtils();
        downLoadUtils.downapk(apkurl, "/mnt.sdcard/mobilesafe2.0.apk", new DownLoadUtils.MyCallBack() {
            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                mprogressDialog.dismiss();
                MyUtils.installApk(context);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                mprogressDialog.setMessage("下载失败");
                mprogressDialog.dismiss();
                enterHome();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                mprogressDialog.setMax((int) total);
                mprogressDialog.setMessage("正在下载...");
                mprogressDialog.setProgress((int) current);

            }
        });
    }

    private void enterHome() {
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME, 2000);
    }
}
