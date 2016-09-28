package com.study.androidsecurity.chapter06.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.androidsecurity.R;
import com.study.androidsecurity.chapter06.adapter.CacheCleanAdapter;
import com.study.androidsecurity.chapter06.entity.CacheInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Created by 46358_000 on 2016/8/14 0014.
 */
public class CacheClearListActivity extends Activity implements View.OnClickListener {
    protected static final int SCANNING = 100;
    protected static final int FINISH = 101;
    private AnimationDrawable animation;
    private TextView mRecomandTV;
    private TextView mCanCleanTV;
    private long cacheMemory;
    private List<CacheInfo> cacheInfos = new ArrayList<CacheInfo>();
    private List<CacheInfo> mCacheInfos = new ArrayList<CacheInfo>();
    private PackageManager pm;
    private CacheCleanAdapter adapter;

    private ListView mCacheLV;
    private Button mCacheBtn;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCANNING:
                    PackageInfo info=(PackageInfo) msg.obj;
                    mRecomandTV.setText("正在扫描：" + info.packageName);
                    mCanCleanTV.setText("已经扫描缓存：" + android.text.format.Formatter.formatFileSize(CacheClearListActivity.this, cacheMemory));
                    mCacheInfos.clear();
                    mCacheInfos.addAll(cacheInfos);
                   adapter.notifyDataSetChanged();
                    mCacheLV.setSelection(mCacheInfos.size());
                    break;
                case FINISH:
                    animation.stop();
                    if (cacheMemory > 0) {
                        mCacheBtn.setEnabled(true);
                    } else {
                        mCacheBtn.setEnabled(false);
                        Toast.makeText(CacheClearListActivity.this, "您的手机洁净如新", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cacheclearlist);
        pm=getPackageManager();
        initView();

    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#EE3B3B"));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        ((TextView) findViewById(R.id.tv_title)).setText("缓存扫描");
        mRecomandTV = (TextView) findViewById(R.id.tv_recommend_clean);
        mCanCleanTV = (TextView) findViewById(R.id.tv_can_clean);
        mCacheLV = (ListView) findViewById(R.id.lv_scancache);
        mCacheBtn = (Button) findViewById(R.id.btn_cleanall);
        mCacheBtn.setOnClickListener(this);
        animation = (AnimationDrawable) findViewById(R.id.imgv_broom).getBackground();
        animation.setOneShot(false);
        animation.start();
        adapter = new CacheCleanAdapter(this, mCacheInfos);
        mCacheLV.setAdapter(adapter);
        fillData();

    }

    private void fillData() {
        thread = new Thread(){
            public void run() {
                cacheInfos.clear();
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                for (PackageInfo info : infos) {
                    getCacheSize(info);
                    try {
                        Thread.sleep(500);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    msg.obj = info;
                    msg.what = SCANNING;
                    handler.sendMessage(msg);

                }
                Message msg = Message.obtain();
                msg.what = FINISH;
                handler.sendMessage(msg);

            }
        };
        thread.start();

    }


    public void getCacheSize(PackageInfo info) {
        try {
            Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(pm, info.packageName, new MyPackObserver(info));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animation.stop();
        if (thread != null) {
            thread.interrupt();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_cleanall:
                if (cacheMemory > 0) {
                    Intent intent = new Intent(this, CleanCacheActivity.class);
                    intent.putExtra("cacheMemory", cacheMemory);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private class MyPackObserver extends android.content.pm.IPackageStatsObserver.Stub{
        private PackageInfo info;

        public MyPackObserver(PackageInfo info) {
            this.info = info;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats,boolean succeeded)
            throws RemoteException {
            long cachesize=pStats.cacheSize;
            if (cachesize >= 0) {
                CacheInfo cacheInfo = new CacheInfo();
                cacheInfo.cacheSize=cachesize;
                cacheInfo.packagename = info.packageName;
                cacheInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                cacheInfo.appIcon = info.applicationInfo.loadIcon(pm);
                cacheInfos.add(cacheInfo);
                cacheMemory += cachesize;
            }

        }

    }

}
