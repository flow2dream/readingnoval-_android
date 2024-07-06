package com.homework.mynoval;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.List;

public class MyApplication extends Application {

    private List<String> chpaters;
    private int chapter_id=0;
    private List<String> titles;
    public static MyApplication instance;
    private String user_id;

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences("share", MODE_PRIVATE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public List<String> getChpaters() {
        return chpaters;
    }

    public void setChpaters(List<String> chpaters) {
        this.chpaters = chpaters;
    }

    public void addID() {
        this.chapter_id = this.chapter_id !=chpaters.size()?this.chapter_id+1:this.chapter_id;
    }
    public void desID() {
        this.chapter_id = this.chapter_id == 0?0:this.chapter_id-1;
    }


    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
