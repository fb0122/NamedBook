package com.example.fb0122.namedbook;

/**
 * Created by fb0122 on 2016/5/17.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;
import com.example.fb0122.namedbook.utils.Config;
import com.example.fb0122.namedbook.utils.GetTime;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";

    private static Context context;
    private ScheduleView scheduleView;
    private static ArrayList<ClassInfo> classList;
    private static DbNameBook dbNameBook;
    private GetTime getTime = new GetTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        dbNameBook = new DbNameBook(context,"namebook");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("点到本");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout =(DrawerLayout)findViewById(R.id.drawlayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.draw_open,R.string.draw_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.stuInfo:
                            Intent i = new Intent(MainActivity.this,StuInfo.class);
                            startActivity(i);
                            break;
                    }
                    return true;
                }
            });
        }

        scheduleView = (ScheduleView) this.findViewById(R.id.scheduleView);
        getClassData();
        scheduleView.setClassList(classList);// 将课程信息导入到课表中
        scheduleView
                .setOnItemClassClickListener(new ScheduleView.OnItemClassClickListener() {

                    @Override
                    public void onClick(ClassInfo classInfo) {
                        Toast.makeText(MainActivity.this,
                                "您点击的课程是：" + classInfo.getClassname(),
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this,AtyNamed.class);
                        Bundle b = new Bundle();
                        b.putString("course",classInfo.getClassname());
                        b.putString("week",String.valueOf(classInfo.getWeekday()));
                        i.putExtras(b);
                        startActivity(i);

                    }
                });
    }

    private int setClass(String timeCur){
        int from = 0;
        switch (timeCur){
            case "第一大节":
                from = 1;
                break;
            case "第二大节":
                from = 3;
                break;
            case "第三大节":
                from = 5;
                break;
            case "第四大节":
                from = 7;
                break;
        }
        return from;
    }

    private void getClassData() {
        classList = new ArrayList<ClassInfo>();
        SQLiteDatabase dbRead = dbNameBook.getReadableDatabase();
        String time = getTime.getCurTime();
        Cursor lessonCur = dbRead.rawQuery("select * from course where time_week = ?" ,new String[]{time});
//        Cursor timeCur = dbRead.rawQuery("select time_lesson from course where time_week = ?",new String[]{time});
        if (lessonCur.moveToFirst()){
            do {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassname(lessonCur.getString(lessonCur.getColumnIndex("course")));
                Log.e(TAG,"course is "+lessonCur.getString(lessonCur.getColumnIndex("course")));
                classInfo.setFromClassNum(setClass(lessonCur.getString(lessonCur.getColumnIndex("time_lesson"))));
                classInfo.setClassNumLen(2);
                classInfo.setClassRoom(lessonCur.getString(lessonCur.getColumnIndex("place")));
                classInfo.setWeekday(Integer.parseInt((lessonCur.getString(lessonCur.getColumnIndex("time_week"))).split("-")[1]));
                classList.add(classInfo);
            }while (lessonCur.moveToNext());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add_course:
                Log.e(TAG,"this is ItemSeclected");
                Intent i = new Intent(this,AddCourse.class);
                startActivityForResult(i,0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case 0:
                    if (resultCode == Config.ADD_COURSE) {
                        getClassData();
                        scheduleView.setClassList(classList);
                    }
            }
    }
}
