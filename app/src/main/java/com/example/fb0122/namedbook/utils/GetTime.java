package com.example.fb0122.namedbook.utils;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by fb0122 on 2016/6/13.
 */
public class GetTime {

    private static String TAG = "GetTime";

    public String getCurTime(){
        Calendar calendar = Calendar.getInstance();
        String time = "";
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.e(TAG,"the week is:  " + month + week + day);
        time =  String.valueOf(month) + "-" +  String.valueOf(week) + "-" + String.valueOf(day);
        return time;
    }
}
