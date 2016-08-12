package com.study.androidsecurity.chapter01.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.study.androidsecurity.R;
import com.study.androidsecurity.util.MyUtils;
import com.study.androidsecurity.util.VersionUpdateUtils;

/**
 * Created by 46358_000 on 2016/8/4 0004.
 */
public class WelcomeActivity extends Activity {
    private TextView mVersionTV;
    private String mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_page);
        mVersion = MyUtils.getVersion(getApplicationContext());
        initView();
        final VersionUpdateUtils updateUtils = new VersionUpdateUtils(mVersion, WelcomeActivity.this);
        new Thread(){
            public void run() {
                updateUtils.getCloudVersion();
            }
        }.start();
    }

    private void initView() {
        mVersionTV = (TextView) findViewById(R.id.current_version);
        mVersionTV.setText("版本号 "+mVersion);
    }
}
