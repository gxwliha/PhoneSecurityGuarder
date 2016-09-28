package com.study.androidsecurity.chapter03.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.study.androidsecurity.chapter03.db.BlackNumberDao;

import java.lang.reflect.Method;

/**
 * Created by 46358_000 on 2016/9/22 0022.
 */
public class InterceptCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean BlackNumStatus = mSP.getBoolean("BlackNumStatus", true);
        if (!BlackNumStatus) {
            return;
        }
        BlackNumberDao dao = new BlackNumberDao(context);
        if (!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String mIncomingNumber = "";
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (telephonyManager.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:

                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    int blackContactMode = dao.getBlackContactMode(mIncomingNumber);
                    if (blackContactMode == 1 || blackContactMode == 3) {
                        Uri uri = Uri.parse("content://call_log/calls");
                        context.getContentResolver().registerContentObserver(uri, true, new CallLogObserver(new Handler(), mIncomingNumber, context));

                    }
                    break;
            }
        }

    }

    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;
        private Context context;
        public CallLogObserver(Handler handler, String incomingNumber, Context context) {
            super(handler);
            this.incomingNumber=incomingNumber;
            this.context=context;
        }
        @Override
        public void onChange(boolean selfChange) {
            Log.i("CallLogObserver", "呼叫记录数据库的内容变化了");
            context.getContentResolver().unregisterContentObserver(this);
            deleCallLog(incomingNumber, context);
            super.onChange(selfChange);
        }
    }

    public void deleCallLog(String incomingNumber, Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, "number=?", new String[]{incomingNumber}, "_id desc limit 1");
        if (cursor.moveToNext()) {
            String id = cursor.getString(0);
            resolver.delete(uri, "_id=?", new String[]{id});
        }
    }

    public void endCall(Context context) {
        try {
            Class clazz = context.getClassLoader().loadClass("android.os.Service Manager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
