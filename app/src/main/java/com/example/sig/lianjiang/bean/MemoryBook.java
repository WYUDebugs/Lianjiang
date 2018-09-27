package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/9/26.
 */

public class MemoryBook {

    private int id;//自增
    private int owner;//创建者id
    private String title;//标题
    private String cover;//封面图片文件路径
    private String creatTime;//创建时间
    private int friendCount;//人数
    private int momentCount;//片段数

    public void setId(int id) {
        this.id = id;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCreateTime(String createTime) {
        this.creatTime = createTime;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public void setMomentCount(int momentCount) {
        this.momentCount = momentCount;
    }

    public int getId() {
        return id;
    }

    public int getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getCover() {
        return cover;
    }

    public String getCreateTime() {
        return creatTime;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public int getMomentCount() {
        return momentCount;
    }
}
