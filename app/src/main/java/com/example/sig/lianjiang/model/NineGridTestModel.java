package com.example.sig.lianjiang.model;
import com.example.sig.lianjiang.view.PraiseTextView;

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

//    public List<String> mGood= new ArrayList<>();

    public List<Comment> mComment; // 评论列表

    public String publishId;
    public List<PraiseTextView.PraiseInfo> mPraiseInfos = new ArrayList<> ();

    public String head;
    public String name;
    public String address;
    public String time;
    public String userId;
    public boolean likeFlag;


    public NineGridTestModel(String mContent,List<String> urlList,boolean isShowAll,List<Comment> mComment,String publishId,List<PraiseTextView.PraiseInfo> mPraiseInfos,String head,String name,String address ,String time,String userId,boolean likeFlag){
        this.mContent=mContent;
        this.urlList=urlList;
        this.isShowAll=isShowAll;
        this.mComment=mComment;
        this.publishId=publishId;
        this.mPraiseInfos=mPraiseInfos;
        this.head=head;
        this.name=name;
        this.address=address;
        this.time=time;
        this.userId=userId;
        this.likeFlag=likeFlag;
    }
}

