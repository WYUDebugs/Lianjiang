package com.example.sig.lianjiang.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by sig on 2018/10/3.
 */

public class TimeLineModel {
    public String mContent;
    public List<String> urlList = new ArrayList<>();
    public String publishId;
    public String time;
    public boolean flag;


    public TimeLineModel(String mContent,List<String> urlList,String publishId,String time,boolean flag){
        this.mContent=mContent;
        this.urlList=urlList;
        this.publishId=publishId;
        this.time=time;
        this.flag=flag;
    }
}
