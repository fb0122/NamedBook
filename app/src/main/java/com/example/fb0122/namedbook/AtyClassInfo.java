package com.example.fb0122.namedbook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;

import java.util.ArrayList;

/**
 * Created by fb0122 on 2016/6/15.
 */
public class AtyClassInfo extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private final static String TAG = "AtyClassInfo";

    private static DbNameBook dbNameBook;
    private SQLiteDatabase dbRead;
    ListView lv_class;
    private static Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_classinfo);

        context = getApplicationContext();

        dbNameBook = new DbNameBook(context,"namebook");
        Toolbar toolbar = (Toolbar)findViewById(R.id.namedToolbar);
        toolbar.setTitle("课程信息");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        initView();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (toolbar.getNavigationIcon() != null){
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        lv_class.setOnItemClickListener(this);
        getData();
        ClassInfoAdapter adapter = new ClassInfoAdapter(context,getData());
        lv_class.setAdapter(adapter);

    }

    private void initView(){
        lv_class = (ListView)findViewById(R.id.lv_class);
    }

    private ArrayList<String> getData(){
        ArrayList<String> list = new ArrayList<>();
        dbRead = dbNameBook.getReadableDatabase();
        Cursor c = dbRead.rawQuery("select course from course",null);
        if (c.moveToFirst()){
            do {

                list.add(c.getString(c.getColumnIndex("course")));

            }while (c.moveToNext());
        }

        return list;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG,"click item is:" + getData().get(position));
        Intent i = new Intent(AtyClassInfo.this,StuInfo.class);
        Bundle b = new Bundle();
        b.putString("course",getData().get(position));
        i.putExtras(b);
        startActivity(i);
    }
}
