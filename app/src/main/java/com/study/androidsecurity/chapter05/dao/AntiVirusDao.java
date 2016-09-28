package com.study.androidsecurity.chapter05.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.study.androidsecurity.chapter01.util.DownLoadUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 46358_000 on 2016/8/25 0025.
 */
public class AntiVirusDao {
    public static String fullPath;


    public static String checkVirus(String md5,Context context) {
        String desc=null;
        File f = context.getDatabasePath("antivirus.db").getParentFile();
        //context.getFilesDir().getAbsolutePath().replace("files")
        if (f.exists() == false) {
            f.mkdir();
        }
        fullPath = f.getPath() + "/antivirus.db";
        
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath("antivirus.db").toString(),null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});
        if (cursor.moveToNext()) {
            desc = cursor.getString(0);
        }
        cursor.close();
       // db.endTransaction();
        db.close();
        return desc;
    }

}
