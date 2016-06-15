package com.example.fb0122.namedbook;

import android.provider.ContactsContract;

/**
 * Created by fb0122 on 2016/5/17.
 */
public class ClassInfo {

        private int fromX;
        private int fromY;
        private int toX;
        private int toY;

        private int classid;
        private String classname;
        private int fromClassNum;
        private int classNumLen;
        private int weekday;
        private String classRoom;
        private String time_lesson;

        public void setPoint(int fromX, int fromY, int toX, int toY) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }

        public int getFromX() {
            return fromX;
        }

        public void setFromX(int fromX) {
            this.fromX = fromX;
        }

        public int getFromY() {
            return fromY;
        }

        public void setFromY(int fromY) {
            this.fromY = fromY;
        }

        public int getToX() {
            return toX;
        }

        public void setToX(int toX) {
            this.toX = toX;
        }

        public int getToY() {
            return toY;
        }

        public void setToY(int toY) {
            this.toY = toY;
        }

        public int getClassid() {
            return classid;
        }

        public void setClassid(int classid) {
            this.classid = classid;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public int getFromClassNum() {
            return fromClassNum;
        }

        public void setFromClassNum(int fromClassNum) {
            this.fromClassNum = fromClassNum;
        }

        public int getClassNumLen() {
            return classNumLen;
        }

        public void setClassNumLen(int classNumLen) {
            this.classNumLen = classNumLen;
        }

        public int getWeekday() {
            return weekday;
        }

        public void setWeekday(int weekday) {
            this.weekday = weekday;
        }

        public String getClassRoom() {
            return classRoom;
        }

        public void setClassRoom(String classRoom) {
            this.classRoom = classRoom;
        }

    public void setTime_lesson(String time_lesson) {
        this.time_lesson = time_lesson;
    }

    public String getTime_lesson() {
        return time_lesson;
    }
}

