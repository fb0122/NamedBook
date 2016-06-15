package com.example.fb0122.namedbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;
import com.example.fb0122.namedbook.utils.Config;
import com.example.fb0122.namedbook.utils.GetTime;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by fb0122 on 2016/5/29.
 */

public class AddCourse extends AppCompatActivity implements Spinner.OnItemSelectedListener,View.OnClickListener {

    private final static String TAG = "AddCourse";

    Spinner sp_course_time,sp_week,week_times;
    private static Context context;
    static DbNameBook dbNameBook;
    EditText et_student, et_course, et_campus, et_building, et_floor, et_classroom;
    private GetTime getTime = new GetTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course);
        context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        //toolbar添加返回按钮并将其变为白色
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        initView();
        dbNameBook = new DbNameBook(context, "namebook");
    }

    private void initView() {
        et_course = (EditText) findViewById(R.id.et_course);
        et_student = (EditText) findViewById(R.id.et_student);
        et_campus = (EditText) findViewById(R.id.et_campus);
        et_building = (EditText) findViewById(R.id.et_building);
        et_classroom = (EditText) findViewById(R.id.et_classroom);
        sp_course_time = (Spinner) findViewById(R.id.sp_course_time);
        sp_week = (Spinner)findViewById((R.id.sp_week));
        week_times = (Spinner)findViewById(R.id.week_time);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] array;
        switch (parent.getId()) {
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getData(String s) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //处理字符串，将地点中包含有阿拉伯数字的字符串将数字替换为中文数字形式

    private String dealPlace() {
        String campus, building,classroom, place;
        String pattern = "\\d+";
        Pattern r = Pattern.compile(pattern);
        campus = et_campus.getText().toString();
        building = et_building.getText().toString();
        Matcher m = r.matcher(building);
        while (m.find()){
            int index = building.indexOf(m.group(0));
            building = building.substring(0,index) + translateNum(m.group(0)) + building.substring(index + m.group(0).length(),building.length());
        }
        Log.e(TAG,"campus is :  " + building );
        classroom = et_classroom.getText().toString();
        place = campus +" - " +  building + " - " + classroom;

        return place;
    }

    //替换数字方法
    private String translateNum(String s){

        String replace = "";
        char[] numArray = s.toCharArray();
        for (int i = 0;i<numArray.length;i++){
            switch (numArray[i]){
                case '1':
                    replace += "一";
                    break;
                case '2':
                    replace += "二";
                    break;
                case '3':
                    replace += "三";
                    break;
                case '4':
                    replace += "四";
                    break;
                case '5':
                    replace += "五";
                    break;
                case '6':
                    replace += "六";
                    break;
                case '7':
                    replace += "七";
                    break;
                case '8':
                    replace += "八";
                    break;
                case '9':
                    replace += "九";
                    break;
            }
        }

        Log.e(TAG,"replace: " + replace);
        return replace;
    }

    //处理toolbar确定按钮
    public void done(View v) {
        dealPlace();
        Log.e(TAG, "done");
        if (et_building.getText().equals("") || et_classroom.getText().equals("") || et_campus.getText().equals("") || et_course.getText().equals("") || et_student.getText().equals("")){
            Toast.makeText(context,"请完善课程信息",Toast.LENGTH_SHORT).show();
        }else {
            ContentValues course_cv = new ContentValues();
            ContentValues stu_cv = new ContentValues();
            SQLiteDatabase dbWriter = dbNameBook.getWritableDatabase();
            course_cv.put("course", et_course.getText().toString());
            course_cv.put("place",dealPlace());
            course_cv.put("time_lesson", sp_course_time.getSelectedItem().toString());
            course_cv.put("student_count", Integer.parseInt(et_student.getText().toString()));
            course_cv.put("time_week",sp_week.getSelectedItem().toString());
            course_cv.put("date", getTime.getCurTime());
            dbWriter.insert(dbNameBook.getCourseTablename(),null,course_cv);
            Toast.makeText(context,"课程已添加",Toast.LENGTH_SHORT).show();
            AnalogData analogData = new AnalogData(context,dbNameBook,stu_cv,et_course.getText().toString(),sp_week.getSelectedItem().toString(),sp_course_time.getSelectedItem().toString(),Integer.parseInt(et_student.getText().toString()),getTime.getCurTime());
            analogData.addData();
            Log.e(TAG, "create database successed and insert data");
        }
        setResult(Config.ADD_COURSE);
        finish();
    }

    private void setClassinfo(String name){
        ClassInfo classInfo = new ClassInfo();
    }

    public String getTime(){

        Calendar calendar = Calendar.getInstance();
        String time = "";
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        Log.e(TAG,"the week is:  " + month + week);
        time =  String.valueOf(month) + "-" +  String.valueOf(week);
        return time;
    }

    public void editTextClick(View v){
        switch (v.getId()){
            case R.id.et_campus:
                et_campus.setHint(null);
                break;
            case R.id.et_building:
                et_building.setHint(null);
                break;
            case R.id.et_classroom:
                et_classroom.setHint(null);
                break;
        }
    }


    @Override
    public void onClick(View v) {

    }
}
