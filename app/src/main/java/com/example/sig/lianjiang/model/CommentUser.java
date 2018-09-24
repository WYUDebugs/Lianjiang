package com.example.sig.lianjiang.model;

/**
 * Created by sig on 2018/8/31.
 */

public class CommentUser {
    public int mId; // id
    public String mName; // 用户名


    public CommentUser(int mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }
    public void setmName(String mName){
        this.mName=mName;
    }
}
