package com.study.androidsecurity.chapter03.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.study.androidsecurity.R;
import com.study.androidsecurity.chapter03.adapter.ContactAdapter;
import com.study.androidsecurity.chapter03.db.ContactInfoParser;
import com.study.androidsecurity.chapter03.entity.ContactInfo;

import java.util.List;

/**
 * Created by 46358_000 on 2016/9/22 0022.
 */
public class ContactSelectActivity extends Activity implements View.OnClickListener {
    private ListView mListView;
    private ContactAdapter adapter;
    private List<ContactInfo> systemContacts;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (systemContacts != null) {
                        adapter = new ContactAdapter(systemContacts, ContactSelectActivity.this);
                        mListView.setAdapter(adapter);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact_select);
        initView();

    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("选择联系人");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        mListView = (ListView) findViewById(R.id.lv_contact);
        new Thread(){
            public void run() {
                systemContacts = ContactInfoParser.getSystemContact(ContactSelectActivity.this);
                //systemContacts.addAll(ContactInfoParser.getSimContacts())
                mHandler.sendEmptyMessage(10);
            }
        }.start();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactInfo item = (ContactInfo) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("phone", item.phone);
                intent.putExtra("name", item.name);
                setResult(0, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            //lalalala

        }
    }
}
