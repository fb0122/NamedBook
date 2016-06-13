package com.example.fb0122.namedbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;

import java.util.ArrayList;

/**
 * Created by fb0122 on 2016/5/31.
 */
public class AtyNamed extends AppCompatActivity {

    private static Context context;
    RecyclerView lv_student;
    MyAdapter myAdapter;
    DbNameBook dbNameBook;
    private Cursor c_course,c_stu;
    private ArrayList<String> list = new ArrayList<>();
    SQLiteDatabase dbRead;
    LinearLayoutManager layoutManager;
    static int firstPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_named);
        context = getApplicationContext();

        layoutManager = new LinearLayoutManager(context);
        lv_student = (RecyclerView)findViewById(R.id.lv_student);
        lv_student.setLayoutManager(layoutManager);
        lv_student.setItemAnimator(new DefaultItemAnimator());
        lv_student.setHasFixedSize(true);

        Toolbar toolbar = (Toolbar)findViewById(R.id.namedToolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (toolbar.getNavigationIcon() != null){
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        dbNameBook = new DbNameBook(context,"namebook",null,1);
        c_course = dbNameBook.getCourseCursor();
        c_stu = dbNameBook.getStuCursor();
        list = getData(c_stu);
        myAdapter = new MyAdapter(context,dbNameBook,list,lv_student,layoutManager);
        lv_student.setAdapter(myAdapter);
        lv_student.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                firstPosition = layoutManager.findFirstVisibleItemPosition();
            }
        });

    }

    public ArrayList<String> getData(Cursor c_stu){
        ArrayList<String> name_list = new ArrayList<>();
        dbRead = dbNameBook.getReadableDatabase();
         c_stu = dbRead.rawQuery(" select name from " + "student",null);
        if (c_stu.moveToFirst()){
            do {

                name_list.add(c_stu.getString(c_stu.getColumnIndex("name")));

            }while (c_stu.moveToNext());
        }
        return  name_list;
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
    public void done(View v){
        finish();
    }
}
