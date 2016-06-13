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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

/**
 * Created by fb0122 on 2016/6/14.
 */
public class StuInfo extends AppCompatActivity {

    private final static String TAG = "StuInfo";

    private MaterialSearchView searchView;
    static DbNameBook dbNameBook;
    private static Context context;
    private static SQLiteDatabase dbRead;
    private static ArrayList<String> list = new ArrayList<>();
    InfoAdapter infoAdapter;
    private ListView lv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_stuinfo);

        context = getApplicationContext();

        lv_info = (ListView)findViewById(R.id.lv_info);
        dbNameBook = new DbNameBook(context,"namebook");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if(toolbar.getNavigationIcon() != null){
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        searchView = (MaterialSearchView)findViewById(R.id.searchView);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.color_cursor_white);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestion));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        infoAdapter = new InfoAdapter(context,getData());
        lv_info.setAdapter(infoAdapter);

    }
    private ArrayList<String> getData(){
        String name = "";
        ArrayList<String> stu_list = new ArrayList<>();
        dbRead = dbNameBook.getReadableDatabase();
        Cursor c = dbRead.rawQuery("select * from student ",null);
        if (c.moveToFirst()){
            do {
                stu_list.add(c.getString(c.getColumnIndex("name")));
                Log.e(TAG,c.getString(c.getColumnIndex("name")));
            }while (c.moveToNext());
        }

       return stu_list;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info,menu);
        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()){
            searchView.closeSearch();
        }else {
            super.onBackPressed();
        }
    }
}

