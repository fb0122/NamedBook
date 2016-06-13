package com.example.fb0122.namedbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fb0122 on 2016/6/14.
 */
public class InfoAdapter extends BaseAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private static Context context;

    public InfoAdapter(Context context,ArrayList<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.stuinfo_item,parent,false);
            holder.info_name = (TextView)convertView.findViewById(R.id.info_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.info_name.setText(list.get(position));
        return convertView;
    }
    class ViewHolder{
        TextView info_name;
    }
}
