package com.study.androidsecurity.chapter03.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import com.study.androidsecurity.R;
import com.study.androidsecurity.chapter03.entity.ContactInfo;

import java.security.PolicySpi;
import java.util.List;

/**
 * Created by 46358_000 on 2016/9/22 0022.
 */
public class ContactAdapter extends BaseAdapter {
    private List<ContactInfo> contactInfos;
    private Context context;

    public ContactAdapter(List<ContactInfo> contactInfos, Context context) {
        super();
        this.contactInfos=contactInfos;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list_contact_select, null);
            holder = new ViewHolder();
            holder.mNameTV = (TextView) convertView.findViewById(R.id.tv_contact_name);
            holder.mPhoneTV = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.mContactImgv = convertView.findViewById(R.id.view1);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(position).name);
        holder.mPhoneTV.setText(contactInfos.get(position).phone);
        holder.mContactImgv.setBackgroundResource(R.drawable.brightpurple_contact_icon);

        return convertView;
    }

    class ViewHolder {
        TextView mNameTV;
        TextView mPhoneTV;
        View mContactImgv;
    }
}
