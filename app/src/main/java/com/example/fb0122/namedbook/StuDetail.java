package com.example.fb0122.namedbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;
import com.example.fb0122.namedbook.circlePrograssBar.TasksCompletedView;

import java.util.HashMap;

/**
 * Created by fb0122 on 2016/6/14.
 */
public class StuDetail extends AppCompatActivity {

    private final static String TAG = "StuDetail";

    private static Context context;
    private TextView detail_name;
    private TasksCompletedView attend_tasksCompletedView,leave_tasksCompletedView,absence_tasksCompletedView;
    private int mFinishProgress;
    private int mToatalProgress;
    private DbNameBook dbNameBook;
    static SQLiteDatabase dbRead;

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

        attend_tasksCompletedView.setProgress(80);
        absence_tasksCompletedView.setProgress(5);
        leave_tasksCompletedView.setProgress(15);
        dbNameBook = new DbNameBook(context,"namebook");
        detail_name.setText(getIntent().getStringExtra("stuname"));
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (toolbar.getNavigationIcon() != null){
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void initProgress(){
        mToatalProgress = 100;
        mFinishProgress = 0;
    }

    private void initView(){
        detail_name = (TextView)findViewById(R.id.detail_name);
        attend_tasksCompletedView = (TasksCompletedView)findViewById(R.id.atttend_progress);
        leave_tasksCompletedView = (TasksCompletedView)findViewById(R.id.leave_progress);
        absence_tasksCompletedView = (TasksCompletedView)findViewById(R.id.absence_progress);
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

        }
    }

    public int getmFinishProgress() {
        int attend_count = 0,absence_count = 0,leave_count = 0;
        HashMap<String,Integer> map = new HashMap<>();
        dbRead = dbNameBook.getReadableDatabase();
        Cursor c = dbRead.rawQuery("select * from student where name = ?", new String[]{getIntent().getStringExtra("stuname")});
        if (c.moveToFirst()){
            do {
                if (c.getString(c.getColumnIndex("attend")) == "1"){
                    attend_count++;
                }
                if (c.getString(c.getColumnIndex("absence")) == "1"){
                    absence_count++;
                }
                if (c.getString(c.getColumnIndex("late")) == "1"){
                    leave_count++;
                }

            }while (c.moveToNext());
        }
        return mFinishProgress;
    }
}
