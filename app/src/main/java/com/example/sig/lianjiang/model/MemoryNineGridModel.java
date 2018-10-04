package com.example.sig.lianjiang.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by sig on 2018/9/1.
 */

public class MemoryNineGridModel {
    private static final long serialVersionUID = 2189052605715370758L;

    public String mContent;

    public List<String> urlList = new ArrayList<>();

    public boolean isShowAll = false;


    public String momentId;

    public String head;
    public String name;
    public String time;
    public String userId;


    public MemoryNineGridModel(String mContent,List<String> urlList,boolean isShowAll,String momentId,String head,String name,String time,String userId){
        this.mContent=mContent;
        this.urlList=urlList;
        this.isShowAll=isShowAll;
        this.momentId=momentId;
        this.head=head;
        this.name=name;
        this.time=time;
        this.userId=userId;
    }

}
