package com.example.fb0122.namedbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;
import com.example.fb0122.namedbook.utils.GetTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by fb0122 on 2016/5/31.
 */
public class AnalogData {
    private final static String TAG = "AanalogData";

    private ArrayList<String> list = new ArrayList<>();
    Random random = new Random();
    String course,time_lesson,week,date;
    int count;
    private static Context context;
    private static DbNameBook dbNameBook;
    private static ContentValues stu_cv;
    private SQLiteDatabase dbWriter,dbRead;
    static GetTime getTime;

    public AnalogData(Context context,DbNameBook dbNameBook,ContentValues contentValues,String course,String week,String time_lesson,int count,String date){
        this.context = context;
        this.dbNameBook = dbNameBook;
        this.stu_cv = contentValues;
        this.course = course;
        this.week = week;
        this.time_lesson = time_lesson;
        this.count = count;
        this.date = date;
        Simulation(count);
    }

    public void Simulation(int count){

        int f_random,l_random;
        for (int i = 0;i< count; i++){
            f_random = random.nextInt(6);
            l_random = random.nextInt(6);
            list.add(FristName(f_random) + LastName(l_random));
        }
    }

    public void addData(){
        dbRead = dbNameBook.getReadableDatabase();
        dbWriter = dbNameBook.getWritableDatabase();
        Cursor  c = dbRead.rawQuery("select * from student where course = ? ",new String[]{course});
        Log.e(TAG,"cursor is :" + c.getCount());
        if (c.getCount() != 0){
            if (c.moveToFirst()){
                do {
                    stu_cv.put("name",c.getString(c.getColumnIndex("name")));
                    stu_cv.put("course", c.getString(c.getColumnIndex("course")));
                    stu_cv.put("time_lesson", c.getString(c.getColumnIndex("time_lesson")));
                    stu_cv.put("time_week", c.getString(c.getColumnIndex("time_week")));
                    stu_cv.put("date",getTime.getCurTime());
                    dbWriter.insert(dbNameBook.getStuTableName(), null, stu_cv);

                }while (c.moveToNext());
            }
            Toast.makeText(context,"学生信息已存在",Toast.LENGTH_SHORT).show();
        }else {
            for (int i = 0; i < list.size(); i++) {

                stu_cv.put("name", list.get(i));
                stu_cv.put("course", course);
                stu_cv.put("time_lesson", time_lesson);
                stu_cv.put("time_week", week);
                stu_cv.put("date",date);
                dbWriter.insert(dbNameBook.getStuTableName(), null, stu_cv);
                Toast.makeText(context,"学生信息已添加",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String FristName(int random){
        String s = "";
        switch (random){
            case 0:
                s = "周";
                break;
            case 1:
                s = "李";
                break;
            case 2:
                s = "张";
                break;
            case 3:
                s = "王";
                break;
            case 4:
                s = "赵";
                break;
            case 5:
                s = "孙";
                break;
        }
        return s;
    }

    private String LastName(int random){
        String s = "";
        switch (random){
            case 0:
                s = "哥";
                break;
            case 1:
                s = "伟";
                break;
            case 2:
                s = "刚";
                break;
            case 3:
                s = "明";
                break;
            case 4:
                s = "杨";
                break;
            case 5:
                s = "少爷";
                break;
        }
        return s;
    }

}
