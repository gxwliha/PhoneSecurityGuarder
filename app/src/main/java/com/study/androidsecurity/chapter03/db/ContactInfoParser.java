package com.study.androidsecurity.chapter03.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.study.androidsecurity.chapter03.entity.ContactInfo;

import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 46358_000 on 2016/9/22 0022.
 */
public class ContactInfoParser {

    public static List<ContactInfo> getSystemContact(Context context) {

        ContentResolver resolver=context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            if (id != null) {

                System.out.println("联系人id:" + id);
                ContactInfo info = new ContactInfo();
                info.id = id;
                Cursor datacursor = resolver.query(datauri, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);
                while (datacursor.moveToNext()) {
                    String data1 = datacursor.getString(0);
                    String mimetype = datacursor.getString(1);
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        System.out.println("姓名=" + data1);
                        info.name = data1;
                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        System.out.println("电话=" + data1);
                        info.phone = data1;
                    }
                }
                infos.add(info);
                datacursor.close();
            }

        }

        cursor.close();
        return infos;
    }
}
