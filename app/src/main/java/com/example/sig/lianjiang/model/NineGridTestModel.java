package com.example.sig.lianjiang.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sig on 2018/7/26.
 */

public class NineGridTestModel implements Serializable {
    private static final long serialVersionUID = 2189052605715370758L;

    public String mContent;

    public List<String> urlList = new ArrayList<>();

    public boolean isShowAll = false;

    public ArrayList<Comment> mComment; // 评论列表

    public NineGridTestModel(String mContent,List<String> urlList,boolean isShowAll,ArrayList<Comment> mComment){
        this.mContent=mContent;
        this.urlList=urlList;
        this.isShowAll=isShowAll;
        this.mComment=mComment;
    }
}

