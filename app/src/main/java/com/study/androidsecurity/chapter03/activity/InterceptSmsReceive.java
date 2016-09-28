package com.study.androidsecurity.chapter03.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ObbInfo;
import android.telephony.SmsMessage;

import com.study.androidsecurity.chapter03.db.BlackNumberDao;

/**
 * Created by 46358_000 on 2016/9/22 0022.
 */
public class InterceptSmsReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean BlackNumStatus = mSP.getBoolean("BlackNumStatus", true);
        if (!BlackNumStatus) {
            return;
        }
        BlackNumberDao dao = new BlackNumberDao(context);
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for (Object object : objects) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
            String sender = smsMessage.getOriginatingAddress();
            String body = smsMessage.getMessageBody();
            if (sender.startsWith("+86")) {
                sender = sender.substring(3, sender.length());

            }
            int mode = dao.getBlackContactMode(sender);
            if (mode == 2 || mode == 3) {
                abortBroadcast();
            }
        }
    }
}
