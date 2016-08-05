package com.study.androidsecurity.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.study.androidsecurity.R;

import java.security.PrivateKey;

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

    }

    private void initView() {
        mVersionTV = (TextView) findViewById(R.id.current_version);
        mVersionTV.setText("版本号 "+mVersion);
    }
}
