package com.study.androidsecurity.chapter05.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.androidsecurity.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by 46358_000 on 2016/8/25 0025.
 */
public class VirusScanActivity extends Activity implements View.OnClickListener {
    private TextView mLastTimeTV;
    private SharedPreferences mSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virusscan);
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        copyDB("antivirus.db");
        initView();
    }

    @Override
    protected void onResume() {
        String string = mSP.getString("lastVirusScan", "您现在还没有查杀病毒！");
        mLastTimeTV.setText(string);
        super.onResume();
    }

    private void copyDB(final String dbname) {
        new Thread(){
            public void run() {
                try {
                    File file = new File(getFilesDir(), dbname);
                    if (file.exists() && file.length() > 10) {
                        Log.i("VirusScanActivity", "数据库已存在！");
                        return;
                    }
                    InputStream inputStream = getAssets().open(dbname);
                    FileOutputStream fileOutputStream = openFileOutput(dbname, MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len=0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);

                    }
                    inputStream.close();
                    fileOutputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(Color.parseColor("#FFBBFF"));
        ImageView mleftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("病毒查杀");
        mleftImgv.setOnClickListener(this);
        mleftImgv.setImageResource(R.drawable.back);
        mLastTimeTV = (TextView) findViewById(R.id.tv_lastscantime);
        findViewById(R.id.rl_allscanvirus).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.rl_allscanvirus:
                startActivity(new Intent(this, VirusScanSpeedActivity.class));
                break;
        }
    }
}
