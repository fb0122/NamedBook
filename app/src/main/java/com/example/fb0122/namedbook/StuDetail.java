package com.example.fb0122.namedbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;
import com.example.fb0122.namedbook.circlePrograssBar.TasksCompletedView;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by fb0122 on 2016/6/14.
 */
public class StuDetail extends AppCompatActivity {

    private final static String TAG = "StuDetail";

    private static Context context;
    private TextView detail_name;
    private TasksCompletedView attend_tasksCompletedView,leave_tasksCompletedView,absence_tasksCompletedView;
    private int mAttendProgress,mAbsenceProgress,mLeaveProgress;
    private int mToatalProgress;
    private static DbNameBook dbNameBook;
    static SQLiteDatabase dbRead;
    static HashMap<String,Float> map = new HashMap<>();
    private TextView detail_attend_times,detail_absence_times,detail_leave_times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_detail);

        context = getApplicationContext();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        initView();
        initProgress();

        dbNameBook = new DbNameBook(context,"namebook");
        map = getmFinishProgress();
        detail_name.setText(getIntent().getStringExtra("stuname"));
        detail_absence_times.setText(map.get("absence_count").toString());
        detail_attend_times.setText(map.get("attend_count").toString());
        detail_leave_times.setText(map.get("leave_count").toString());
        Log.e(TAG,"percent" + map.get("attend_percent"));
        attend_tasksCompletedView.setProgress((int)(float)map.get("attend_percent"));
        absence_tasksCompletedView.setProgress((int)(float)map.get("absence_percent"));
        leave_tasksCompletedView.setProgress((int)(float)map.get("leave_percent"));
//        new Thread(new FinishPro(10)).start();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (toolbar.getNavigationIcon() != null){
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void initProgress(){
        mToatalProgress = 100;
        mAttendProgress = 0;
        mAbsenceProgress = 0;
        mLeaveProgress = 0;
    }

    private void initView(){
        detail_name = (TextView)findViewById(R.id.detail_name);
        attend_tasksCompletedView = (TasksCompletedView)findViewById(R.id.atttend_progress);
        leave_tasksCompletedView = (TasksCompletedView)findViewById(R.id.leave_progress);
        absence_tasksCompletedView = (TasksCompletedView)findViewById(R.id.absence_progress);
        detail_absence_times = (TextView) findViewById(R.id.detail_absence_times);
        detail_attend_times = (TextView)findViewById(R.id.detail_attend_times);
        detail_leave_times = (TextView)findViewById(R.id.detail_leave_times);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class FinishPro implements Runnable{
        int progress;

        public FinishPro(int progress){
            this.progress = progress;
        }

        @Override
        public void run() {
            try {
                while (mAttendProgress < map.get("attend_percent")) {
                    mAttendProgress += 1;
                    attend_tasksCompletedView.setProgress(mAttendProgress);
                    Thread.sleep(100);
                }
                while (mAbsenceProgress < map.get("absence_percent")){
                    mAbsenceProgress += 1;
                    absence_tasksCompletedView.setProgress(mAbsenceProgress);
                    Thread.sleep(100);
                }
                while (mLeaveProgress < map.get("leave_percent")){
                    mLeaveProgress += 1;
                    leave_tasksCompletedView.setProgress(mLeaveProgress);
                    Thread.sleep(100);
                }
                }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public HashMap<String,Float> getmFinishProgress() {
        int attend_count = 0,absence_count = 0,leave_count = 0,total_count = 1;
        HashMap<String,Float> map = new HashMap<>();
        dbRead = dbNameBook.getReadableDatabase();
        Cursor c = dbRead.rawQuery("select * from student where name = ?", new String[]{getIntent().getStringExtra("stuname")});
        Log.e(TAG,"------------" + c.getCount());
        if (c.moveToFirst()){
            do {

                if (c.getString(c.getColumnIndex("attend")).equals("1")){
                    attend_count++;
                }
                if (c.getString(c.getColumnIndex("absence")).equals("1")){
                    absence_count++;
                }
                if (c.getString(c.getColumnIndex("late")).equals("1")){
                    leave_count++;
                }

            }while (c.moveToNext());
        }
        Log.e(TAG,"attend + " + attend_count + "absence + " + absence_count + "leave + " + leave_count + "total_count: " + c.getCount() + "result is: " + (float)attend_count/c.getCount() * 100);
        map.put("attend_count",(float)attend_count);
        map.put("absence_count",(float)absence_count);
        map.put("leave_count",(float)leave_count);
        map.put("attend_percent",(float)attend_count/c.getCount() * 100);
        map.put("absence_percent",(float)absence_count/c.getCount() * 100);
        map.put("leave_percent",(float)leave_count/c.getCount() * 100);
        return map;
    }
}
