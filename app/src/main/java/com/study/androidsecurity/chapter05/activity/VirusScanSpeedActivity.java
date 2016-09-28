package com.study.androidsecurity.chapter05.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.study.androidsecurity.R;
import com.study.androidsecurity.chapter05.adapter.ScanVirusAdapter;
import com.study.androidsecurity.chapter05.dao.AntiVirusDao;
import com.study.androidsecurity.chapter05.entity.ScanAppInfo;
import com.study.androidsecurity.chapter05.utils.MD5Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by 46358_000 on 2016/8/25 0025.
 */
public class VirusScanSpeedActivity extends Activity implements View.OnClickListener {
    protected static final int SCAN_BEGIN=100;
    protected static final int SCANNING=101;
    protected static final int  SCAN_FINISH=102;
    private int total;
    private int process;
    private TextView mProcessTV;
    private boolean flag;
    private PackageManager pm;
    private boolean isStop;
    private TextView mScanAppTV;
    private Button mCancleBtn;
    private ImageView mScanningIcon;
    private RotateAnimation rotateAnimation;
    private ListView mScanListView;
    private ScanVirusAdapter adapter;
    private List<ScanAppInfo> mScanAppInfos = new ArrayList<ScanAppInfo>();
    private SharedPreferences mSP;
    private Context mContext;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SCAN_BEGIN:
                    mScanAppTV.setText("初始化杀毒引擎中...");
                    break;
                case SCANNING:
                    ScanAppInfo info = (ScanAppInfo) msg.obj;
                    mScanAppTV.setText("正在扫描：" + info.appName);
                    int speed=msg.arg1;
                    mProcessTV.setText((speed * 100 / total) + "%");
                    mScanAppInfos.add(info);
                    adapter.notifyDataSetChanged();
                    mScanListView.setSelection(mScanAppInfos.size());
                    break;
                case SCAN_FINISH:
                    mScanAppTV.setText("扫描完成！");
                    mScanningIcon.clearAnimation();
                    mCancleBtn.setBackgroundResource(R.drawable.scan_complete);
                    saveScanTime();
                    break;
            }
        }

        private void saveScanTime() {
            SharedPreferences.Editor editor = mSP.edit();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            currentTime = "上次查杀：" + currentTime;
            editor.putString("lastVirusScan", currentTime);
            editor.commit();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virusscanspeed);
        mContext=this.getApplicationContext();
        pm = getPackageManager();
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        initView();
        scanVirus();

    }

    private void scanVirus() {
        flag = true;
        isStop = false;
        process=0;
        mScanAppInfos.clear();
        new Thread(){
            public void run() {
                Message msg = Message.obtain();
                msg.what = SCAN_BEGIN;
                mHandler.sendMessage(msg);
                List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
                total = installedPackages.size();
                for (PackageInfo info : installedPackages) {
                    if (!flag) {
                        isStop = true;
                        return;
                    }
                    String apkpath = info.applicationInfo.sourceDir;
                    String md5info = MD5Utils.getFileMd5(apkpath);
                    String result = AntiVirusDao.checkVirus(md5info,mContext);
                    msg.what = SCANNING;
                    ScanAppInfo scanInfo = new ScanAppInfo();
                    if (result == null) {
                        scanInfo.description = "扫描安全";
                        scanInfo.isVirus = true;
                    } else {
                        scanInfo.description = result;
                        scanInfo.isVirus = true;

                    }
                    process++;
                    scanInfo.packageName=info.packageName;
                    scanInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                    scanInfo.appicon = info.applicationInfo.loadIcon(pm);
                    msg.obj = scanInfo;
                    msg.arg1 = process;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(300);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                msg = Message.obtain();
                msg.what = SCAN_FINISH;
                mHandler.sendMessage(msg);

            }
        }.start();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#436EEE"));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("病毒查杀进度");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mProcessTV = (TextView) findViewById(R.id.tv_scanprocess);
        mScanAppTV = (TextView) findViewById(R.id.tv_scansapp);
        mCancleBtn = (Button) findViewById(R.id.btn_canclescan);
        mCancleBtn.setOnClickListener(this);
        mScanListView = (ListView) findViewById(R.id.lv_scanapps);
        adapter = new ScanVirusAdapter(mScanAppInfos, this);
        mScanListView.setAdapter(adapter);
        mScanningIcon = (ImageView) findViewById(R.id.imgv_scanningicon);
        startAnim();
    }

    private void startAnim() {
        if (rotateAnimation == null) {
            rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        }
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(2000);
        mScanningIcon.startAnimation(rotateAnimation);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_canclescan:
                if (process == total & process > 0) {
                    finish();
                } else if (process > 0 & process < total & isStop == false) {
                    mScanningIcon.clearAnimation();
                    flag = false;
                    mCancleBtn.setBackgroundResource(R.drawable.restart_scan_btn);

                } else if (isStop){
                    startAnim();
                    scanVirus();
                    mCancleBtn.setBackgroundResource(R.drawable.cancle_scan_btn_selector);

                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        flag=false;
        super.onDestroy();

    }

}
