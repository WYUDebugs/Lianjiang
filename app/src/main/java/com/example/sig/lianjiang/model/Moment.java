package com.example.sig.lianjiang.model;

import java.util.ArrayList;

/**
 * Created by sig on 2018/8/31.
 */

public class Moment {

    public String mContent;
    public ArrayList<Comment> mComment; // 评论列表

    public Moment(String mContent,ArrayList<Comment> mComment) {
        this.mComment = mComment;
        this.mContent = mContent;
    }
}
