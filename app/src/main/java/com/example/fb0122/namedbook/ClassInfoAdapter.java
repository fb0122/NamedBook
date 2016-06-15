package com.example.fb0122.namedbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;

import java.util.ArrayList;

/**
 * Created by fb0122 on 2016/6/15.
 */
public class ClassInfoAdapter extends BaseAdapter {

    private ArrayList<String> list = new ArrayList<>();
    private Context context;
    private static DbNameBook dbNameBook;
    private SQLiteDatabase dbRead;

    public ClassInfoAdapter(Context context,ArrayList<String> list){
        this.list = list;
        this.context = context;
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
        dbNameBook = new DbNameBook(context,"namebook");
        dbRead = dbNameBook.getReadableDatabase();
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.classinfo_item,parent,false);
            holder.classinfo_name = (TextView)convertView.findViewById(R.id.classinfo_name);
            holder.classinfo_Week = (TextView)convertView.findViewById(R.id.classinfo_week);
            holder.classinfo_stucount = (TextView)convertView.findViewById(R.id.classinfo_stucount);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        Cursor c_week = dbRead.rawQuery("select time_week from course where course = ?",new String[]{list.get(position)});
        Cursor c_lesson = dbRead.rawQuery("select time_lesson from course where course = ?",new String[]{list.get(position)});
        Cursor c_stucount = dbRead.rawQuery("select student_count from course where course = ?",new String[]{list.get(position)});
        c_week.moveToFirst();
        c_lesson.moveToFirst();
        c_stucount.moveToFirst();
        holder.classinfo_name.setText(list.get(position).toString());
        holder.classinfo_Week.setText("上课时间：    " + c_week.getString(c_week.getColumnIndex("time_week")).toString() + "  " + c_lesson.getString(c_lesson.getColumnIndex("time_lesson")).toString());
        holder.classinfo_stucount.setText("上课学生人数：  " + c_stucount.getString(c_stucount.getColumnIndex("student_count")));
        return convertView;
    }

    class ViewHolder{
        TextView classinfo_name;
        TextView classinfo_Week;
        TextView classinfo_stucount;
    }
}
