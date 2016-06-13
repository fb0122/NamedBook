package com.example.fb0122.namedbook.NameBook_DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by fb0122 on 2016/5/18.
 */
public class DbNameBook extends SQLiteOpenHelper{

    public static String TAG = "DbNameBook";

    public static Context context;
    private static String course_table_name = "course";
    private static String student_table_name = "student";
    private String name;

    private  String CREATE;
    private static String SQL_BEFORE = "create table ";
    private static String CREATE_COURSE ="create table " + course_table_name + " ("
            + "id integer primary key autoincrement, "
            + "course text, "
            + "place text, "
            + "time_week text, "
            + "time_lesson text, "
            + "student_count integer) ";
    private static String CREATE_STU = "create table " + student_table_name + " ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "absence text default '0', "
            + "attend text default '0', "
            + "late text default '0', "
            + "course text, "
            + "time_week, "
            + "time_lesson) "
            ;

    private SQLiteDatabase dbr;

    public DbNameBook(Context context, String name){
        this(context,name,null,1);
    }

    public DbNameBook(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "namebook", factory, 1);
        this.context  = context;
        Log.e(TAG,"DbNameBook");
    }

    public void setCourseTablename(String table_name) {
        Log.e(TAG,"setTable_name");
        this.course_table_name = table_name;
    }

    public static String getCourseTablename() {
        Log.e(TAG,"getTable_name");
        return course_table_name;
    }

    public static String getStuTableName(){
        return student_table_name;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG,"onCreate()");
        db.execSQL(CREATE_COURSE);
        db.execSQL(CREATE_STU);
        Toast.makeText(context,"create database successed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists course");
        db.execSQL("drop table if exists student");
        onCreate(db);
    }

    public Cursor getStuCursor(){
        dbr = getReadableDatabase();
        Cursor c = dbr.rawQuery(" select * from " + "course",null);
        return c;
    }

    public Cursor getCourseCursor(){
        dbr = getReadableDatabase();
        Cursor c1 = dbr.rawQuery(" select * from " + "student",null);
        return c1;
    }
}
