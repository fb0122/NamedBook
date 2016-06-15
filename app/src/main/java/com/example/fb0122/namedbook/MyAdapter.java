package com.example.fb0122.namedbook;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fb0122.namedbook.NameBook_DB.DbNameBook;
import com.example.fb0122.namedbook.utils.GetTime;
import com.google.android.gms.common.api.BooleanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by fb0122 on 2016/5/31.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {

    private final static String TAG = "MyAdapter";

    private static Context context;
    private ArrayList<String> list = new ArrayList<String>();
    private Cursor c_stu,c_course;
    private DbNameBook dbNameBook;
    private RecyclerView lv_student;
    static View v;
    boolean change = false;
    LinearLayoutManager layoutManager;
    private static boolean STU_ABSENCE  = false;
    private static boolean STU_ATTEND = false;
    private static boolean STU_LEAVE = false;
    SQLiteDatabase dbWrite;
    int firstPosition;
    private static boolean colorChange = false;
    Vector<State> vector = new Vector<>();
    static ViewHolder viewHolder;
    Looper looper = Looper.myLooper();
    private static int isScroll;
    private GetTime getTime = new GetTime();
    private static TextView tv;
    private static String course;

    public MyAdapter(Context context, DbNameBook dbNameBook, ArrayList<String> list, RecyclerView lv_student,LinearLayoutManager layoutManager,String course){
        this.context = context;
        this.dbNameBook = dbNameBook;
        this.list = list;
        this.lv_student = lv_student;
        this.layoutManager = layoutManager;
        this.course = course;
        for (String s : list){
            vector.add(State.NORMAL);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        v = LayoutInflater.from(context).inflate(R.layout.named_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String,Integer> map = new HashMap<>();
        Log.e(TAG,"onBindViewHolder");
        if (change){
            setStuState(position,holder.tv_count);
        }
        holder.tv_name.setText(list.get(position));
        map = getStuState(holder.tv_name.getText().toString());
        tv = holder.tv_count;
        holder.tv_count.setText("本学期缺勤" + map.get("absence") + "次，" + "出勤" + map.get("attend") + "次");

        //setTag为当前view添加状态，之后直接点击调用getTag便可获取其position
//        if (holder.btn_attend.getTag()== null || holder.btn_absence.getTag()== null || holder.btn_leave.getTag()== null){
            Log.e(TAG,"position" + position);
            holder.btn_attend.setTag(position);
            holder.btn_attend.setOnClickListener(this);
            holder.btn_absence.setTag(position);
            holder.btn_absence.setOnClickListener(this);
            holder.btn_leave.setTag(position);
            holder.btn_leave.setOnClickListener(this);
//        }
        if (vector.get(position) !=null ){
            switch (vector.get(position)){
                case ABSENCE:
                    if (colorChange) {
                        holder.btn_absence.setBackgroundColor(context.getResources().getColor(R.color.named_absence));
                        holder.btn_absence.setTextColor(context.getResources().getColor(R.color.white));
                        holder.btn_attend.setBackground(context.getResources().getDrawable(R.drawable.named_click_tv));
                        holder.btn_attend.setTextColor(context.getResources().getColor(R.color.textColor));
                        holder.btn_leave.setBackground(context.getResources().getDrawable(R.drawable.named_click_tv));
                        holder.btn_leave.setTextColor(context.getResources().getColor(R.color.textColor));
                    }
                    break;
                case ATTEND:
                    if (colorChange) {
                        holder.btn_attend.setBackgroundColor(context.getResources().getColor(R.color.named_attend));
                        holder.btn_attend.setTextColor(context.getResources().getColor(R.color.white));
                        holder.btn_absence.setBackground(context.getResources().getDrawable(R.drawable.named_click_tv));
                        holder.btn_absence.setTextColor(context.getResources().getColor(R.color.textColor));
                        holder.btn_leave.setBackground(context.getResources().getDrawable(R.drawable.named_click_tv));
                        holder.btn_leave.setTextColor(context.getResources().getColor(R.color.textColor));
                    }
                    break;
                case LEAVE:
                    if (colorChange) {
                        holder.btn_leave.setBackgroundColor(context.getResources().getColor(R.color.named_leave));
                        holder.btn_leave.setTextColor(context.getResources().getColor(R.color.white));
                        holder.btn_attend.setBackground(context.getResources().getDrawable(R.drawable.named_click_tv));
                        holder.btn_attend.setTextColor(context.getResources().getColor(R.color.textColor));
                        holder.btn_absence.setBackground(context.getResources().getDrawable(R.drawable.named_click_tv));
                        holder.btn_absence.setTextColor(context.getResources().getColor(R.color.textColor));
                    }
                    break;
                case NORMAL:
                    holder.btn_attend.setBackground(context.getResources().getDrawable(R.drawable.named_click_tv));
                    holder.btn_attend.setTextColor(context.getResources().getColor(R.color.textColor));
                    holder.btn_absence.setBackground(context.getResources().getDrawable(R.drawable.named_click_tv));
                    holder.btn_absence.setTextColor(context.getResources().getColor(R.color.textColor));
                    holder.btn_leave.setBackground(context.getResources().getDrawable(R.drawable.named_click_tv));
                    holder.btn_leave.setTextColor(context.getResources().getColor(R.color.textColor));
                    break;
            }
        }
    }

    public enum State{
        LEAVE,ATTEND,ABSENCE,NORMAL;
    }

    /*
        获取点击的item的viewHolder，以便通过取得学生姓名存储和获取学生点名状态
     */

    public void setStuState(int position,TextView tv){
        ContentValues cv = new ContentValues();
        dbWrite = dbNameBook.getWritableDatabase();
        firstPosition = layoutManager.findFirstVisibleItemPosition();
        viewHolder = (ViewHolder)lv_student.getChildViewHolder(lv_student.getChildAt(Math.abs(position - firstPosition)));
        Log.e(TAG,"the text is: " + viewHolder.tv_name.getText());
        if (STU_ABSENCE){
            cv.put("absence","1");
        }else{
            cv.put("absence","0");
        }
        if (STU_ATTEND){
            cv.put("attend","1");
        }else {
            cv.put("attend","0");
        }
        if (STU_LEAVE){
            cv.put("late","1");
        }else {
            cv.put("late","0");
        }

        dbWrite.update("student",cv,"name = ?",new String[]{viewHolder.tv_name.getText().toString()});
        Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show();
        notifyItemChanged(Math.abs(position));
//        map.clear();
    }

    public void getChange(int position, int textRes, int rId, int bgRes, Drawable drawable){


        firstPosition = layoutManager.findFirstVisibleItemPosition();
            viewHolder = (ViewHolder) lv_student.getChildViewHolder(lv_student.getChildAt(Math.abs(position - firstPosition)));
//            viewHolder = (ViewHolder)lv_student.findViewHolderForAdapterPosition(position);
            if (viewHolder == null){
            }else {
                if (colorChange) {
                    switch (rId) {
                        case R.id.btn_absence:
                            viewHolder.btn_absence.setTextColor(textRes);
                            viewHolder.btn_absence.setBackgroundColor(bgRes);
                            break;
                        case R.id.btn_attend:
                            viewHolder.btn_attend.setTextColor(textRes);
                            viewHolder.btn_attend.setBackgroundColor(bgRes);
                            break;
                        case R.id.btn_leave:
                            viewHolder.btn_leave.setTextColor(textRes);
                            viewHolder.btn_leave.setBackgroundColor(bgRes);
                            break;
                    }
                } else {
                    switch (rId) {
                        case R.id.btn_absence:
                            viewHolder.btn_absence.setTextColor(textRes);
                            viewHolder.btn_absence.setBackground(drawable);
                            break;
                        case R.id.btn_attend:
                            viewHolder.btn_attend.setTextColor(textRes);
                            viewHolder.btn_attend.setBackground(drawable);
                            break;
                        case R.id.btn_leave:
                            viewHolder.btn_leave.setTextColor(textRes);
                            viewHolder.btn_leave.setBackground(drawable);
                            break;
                    }
                }
//            }
        }
        lv_student.invalidate();

    }

    public void clearColor(int position, int textRes, int rId,Drawable drawable){

        firstPosition = layoutManager.findFirstVisibleItemPosition();
        Log.e(TAG,"getChanged position is " + position);
        Log.e(TAG,"firstPosition " + firstPosition);
        viewHolder = (ViewHolder) lv_student.getChildViewHolder(lv_student.getChildAt(Math.abs(position - firstPosition)));
        switch (rId){
            case R.id.btn_attend:
                viewHolder.btn_attend.setTextColor(textRes);
                viewHolder.btn_attend.setBackground(drawable);
                break;
            case R.id.btn_absence:
                viewHolder.btn_absence.setTextColor(textRes);
                viewHolder.btn_absence.setBackground(drawable);
                break;
            case R.id.btn_leave:
                viewHolder.btn_leave.setTextColor(textRes);
                viewHolder.btn_leave.setBackground(drawable);
                break;
        }
        lv_student.invalidate();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //按钮点击事件  点击按钮变背景颜色与字体颜色
    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        Log.e(TAG,"click position is: " + position);
        switch (v.getId()){
            case R.id.btn_absence:
                STU_ATTEND = false;
                STU_LEAVE = false;
                clearColor(position,context.getResources().getColor(R.color.textColor),R.id.btn_leave,context.getResources().getDrawable(R.drawable.named_click_tv));
                clearColor(position,context.getResources().getColor(R.color.textColor),R.id.btn_attend,context.getResources().getDrawable(R.drawable.named_click_tv));
                //通过判断button内字体的颜色判断点击选中或者取消
                if ((Boolean)(((Button)v).getTextColors().getDefaultColor() == context.getResources().getColor(R.color.textColor))) {
                    colorChange = true;
                    getChange(position,context.getResources().getColor(R.color.white),v.getId(),context.getResources().getColor(R.color.named_absence),context.getResources().getDrawable(R.drawable.named_click_tv));
                    STU_ABSENCE = true;
                    vector.add(position,State.ABSENCE);
                }else if (((Button)v).getTextColors().getDefaultColor() == context.getResources().getColor(R.color.white)){
                    colorChange = false;
                    getChange(position,context.getResources().getColor(R.color.textColor),v.getId(),context.getResources().getColor(R.color.named_absence),context.getResources().getDrawable(R.drawable.named_click_tv));
                    STU_ABSENCE = false;
                    vector.add(position,State.NORMAL);
                }

                break;
            case R.id.btn_attend:
                STU_LEAVE = false;
                STU_ABSENCE = false;
                clearColor(position,context.getResources().getColor(R.color.textColor),R.id.btn_leave,context.getResources().getDrawable(R.drawable.named_click_tv));
                clearColor(position,context.getResources().getColor(R.color.textColor),R.id.btn_absence,context.getResources().getDrawable(R.drawable.named_click_tv));
                if ((Boolean)(((Button)v).getTextColors().getDefaultColor() == context.getResources().getColor(R.color.textColor))) {
                    colorChange = true;
                    getChange(position,context.getResources().getColor(R.color.white),v.getId(),context.getResources().getColor(R.color.named_attend),context.getResources().getDrawable(R.drawable.named_click_tv));
                    STU_ATTEND = true;
                    vector.add(position,State.ATTEND);
                }else if (((Button)v).getTextColors().getDefaultColor() == context.getResources().getColor(R.color.white)){
                    colorChange = false;
                    getChange(position,context.getResources().getColor(R.color.textColor),v.getId(),context.getResources().getColor(R.color.named_attend),context.getResources().getDrawable(R.drawable.named_click_tv));
                    STU_ATTEND = false;
                    vector.add(position,State.NORMAL);
                }

                break;
            case R.id.btn_leave:
                STU_ABSENCE = false;
                STU_ATTEND = false;
                clearColor(position,context.getResources().getColor(R.color.textColor),R.id.btn_attend,context.getResources().getDrawable(R.drawable.named_click_tv));
                clearColor(position,context.getResources().getColor(R.color.textColor),R.id.btn_absence,context.getResources().getDrawable(R.drawable.named_click_tv));
                if ((Boolean)(((Button)v).getTextColors().getDefaultColor() == context.getResources().getColor(R.color.textColor))) {
                    colorChange = true;
                    getChange(position,context.getResources().getColor(R.color.white),v.getId(),context.getResources().getColor(R.color.named_leave),context.getResources().getDrawable(R.drawable.named_click_tv));
                    STU_LEAVE = true;
                    vector.add(position,State.LEAVE);
                }else if (((Button)v).getTextColors().getDefaultColor() == context.getResources().getColor(R.color.white)){
                    colorChange = false;
                    getChange(position,context.getResources().getColor(R.color.textColor),v.getId(),context.getResources().getColor(R.color.named_leave),context.getResources().getDrawable(R.drawable.named_click_tv));
                    STU_LEAVE = false;
                    vector.add(position,State.NORMAL);
                }

                break;
        }
        Log.d(TAG, "default" + v.getTag() + "");
        setStuState((Integer) v.getTag(),tv);
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_count;
        Button btn_attend,btn_leave,btn_absence;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_count = (TextView)itemView.findViewById(R.id.tv_count);
            btn_absence = (Button)itemView.findViewById(R.id.btn_absence);
            btn_attend = (Button)itemView.findViewById(R.id.btn_attend);
            btn_leave = (Button)itemView.findViewById(R.id.btn_leave);

        }
    }

    class updateView extends Handler{
        int position;

        public updateView(int position,Looper looper){
            super(looper);
            this.position = position;

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    viewHolder = (ViewHolder)lv_student.getChildViewHolder(lv_student.getChildAt(position));
                    break;
            }
        }
    }

    //获取数据统计该学生缺勤与出勤次数
    private HashMap<String,Integer> getStuState(String name){
        HashMap<String,Integer> map = new HashMap<>();
        SQLiteDatabase dbRead = dbNameBook.getReadableDatabase();
        String s = getTime.getCurTime();
        Cursor attendCur = dbRead.rawQuery(" select attend from student where course = ? and name = ?",new String[]{course,name});
        map.put("attend",count(attendCur,"attend"));
        Cursor absenceCur = dbRead.rawQuery(" select absence from student where course = ? and name = ?",new String[]{course,name});
        map.put("absence",count(absenceCur,"absence"));
        Cursor lateCur = dbRead.rawQuery(" select late from student where course = ? and name = ?",new String[]{course,name});
        map.put("late",count(lateCur,"late"));
        return map;
    }

    //统计学生状态
    private int count(Cursor c,String columnName){
        int count = 0;
        if (c.moveToFirst()){
            for (int i = 0;i<c.getCount();i++){
                if (Integer.parseInt(c.getString(c.getColumnIndex(columnName))) == 1){
                    count++;
                }
            }
        }
        return count;
    }
}
