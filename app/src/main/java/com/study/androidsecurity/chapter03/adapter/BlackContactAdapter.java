package com.study.androidsecurity.chapter03.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.StaticLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.study.androidsecurity.R;
import com.study.androidsecurity.chapter03.db.BlackNumberDao;
import com.study.androidsecurity.chapter03.entity.BlackContactInfo;

import java.util.List;

/**
 * Created by 46358_000 on 2016/9/20 0020.
 */
public class BlackContactAdapter extends BaseAdapter {
    private List<BlackContactInfo> contactInfos;
    private Context context;
    private BlackNumberDao dao;
    private BlackContactCallBack callBack;

    public void setCallBack(BlackContactCallBack callBack) {
        this.callBack = callBack;

    }

    public BlackContactAdapter(List<BlackContactInfo> systemContacts, Context context) {
        super();
        this.contactInfos = systemContacts;
        this.context = context;
        dao = new BlackNumberDao(context);

    }


    @Override
    public int getCount() {

        return contactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return contactInfos.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewHolder holder=null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_blackcontact, null);
            holder = new viewHolder();
            holder.mNameTV = (TextView) convertView.findViewById(R.id.tv_black_name);
            holder.mModeTV = (TextView) convertView.findViewById(R.id.tv_black_mode);
            holder.mContactImgv = convertView.findViewById(R.id.view_black_icon);
            holder.mDeleteView = convertView.findViewById(R.id.view_black_delete);
            convertView.setTag(holder);

        }else {
            holder = (viewHolder) convertView.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(position).contactName + "(" + contactInfos.get(position).phoneNumber + ")");
        holder.mModeTV.setText(contactInfos.get(position).getModeString(contactInfos.get(position).mode));
        holder.mNameTV.setTextColor(context.getResources().getColor(R.color.purple));
        holder.mModeTV.setTextColor(context.getResources().getColor(R.color.purple));
        holder.mContactImgv.setBackgroundResource(R.drawable.brightpurple_contact_icon);
        holder.mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean delete = dao.delete(contactInfos.get(position));
                if (delete) {
                    contactInfos.remove(contactInfos.get(position));
                    BlackContactAdapter.this.notifyDataSetChanged();
                    if (dao.getTotalNumber() == 0) {
                        callBack.DataSizeChanged();

                    }
                    }else {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;

    }

    static class viewHolder {
        TextView mNameTV;
        TextView mModeTV;
        View mContactImgv;
        View mDeleteView;
    }

    public interface BlackContactCallBack {
        void DataSizeChanged();
    }

}
