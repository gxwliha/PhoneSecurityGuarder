package com.study.androidsecurity.chapter03.test;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.util.TimeUtils;
import android.test.AndroidTestCase;
import android.util.Log;

import com.study.androidsecurity.chapter03.db.BlackNumberDao;
import com.study.androidsecurity.chapter03.entity.BlackContactInfo;

import java.util.List;
import java.util.Random;

/**
 * Created by 46358_000 on 2016/9/19 0019.
 */
public class TestBlackNumberDao extends AndroidTestCase {
    private Context context;
    @Override
    protected void setUp() throws Exception {
        context=getContext();
        super.setUp();
    }

    public void testAdd() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        Random random = new Random(8799);
        for (long i=0;i<30;i++) {
            BlackContactInfo info = new BlackContactInfo();
            info.phoneNumber = 1350000001 + i + "";
            info.contactName = "zhangsan" + i;
            info.mode = random.nextInt(3) + 1;
            dao.add(info);

        }
    }
    public void testDelete()throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        BlackContactInfo info = new BlackContactInfo();
        for (long i=1;i<5;i++) {
            info.phoneNumber = 1350000001 + i + "";
            dao.delete(info);
        }
    }
    public void testGetPageBlackNumber()throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        List<BlackContactInfo> list = dao.getPageBlackNumber(2, 5);
        for (int i=0;i<list.size();i++) {
            Log.i("TestBlackNumberDao", list.get(i).phoneNumber);

        }
    }
    public void testGetBlackContactMode()throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        int mode = dao.getBlackContactMode(1350000081 + "");
        Log.i("TestBlackNumberDao", mode + "");
    }
    public void testGetTotalNumber()throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        int total = dao.getTotalNumber();
        Log.i("TestBlaceNumberDao", "数据总条目：" + total);
    }
    public void testIsNumberExist()throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        boolean isExist = dao.isNumberExist(1350000081 + "");
        if (isExist) {
            Log.i("TestBlackNumberDao", "该号码在数据库中");
        }else {
            Log.i("TestBlackNumberDao", "该号码不在数据库中");
        }
    }
}
