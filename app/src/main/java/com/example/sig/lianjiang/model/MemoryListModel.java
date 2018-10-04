package com.example.sig.lianjiang.model;

/**
 * Created by sig on 2018/9/28.
 */

public class MemoryListModel {
    public String memoryBookId;
    public String cover;
    public String title;
    public int friendCount;
    public int momentCount;//片段数

    public MemoryListModel(String memoryBookId,String cover,String title,int friendCount,int momentCount){
        this.memoryBookId=memoryBookId;
        this.cover=cover;
        this.title=title;
        this.friendCount=friendCount;
        this.momentCount=momentCount;
    }

    public void setTitle(String title){
        this.title=title;
    }

}
