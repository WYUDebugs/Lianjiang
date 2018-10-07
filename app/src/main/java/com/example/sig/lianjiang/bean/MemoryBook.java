package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/9/26.
 */

public class MemoryBook {

    private int id;//自增
    private int owner;//创建者id
    private User user;
    private String title;//标题
    private String cover;//封面图片文件路径
    private String creatTime;//创建时间
    private int friendCount;//人数
    private int momentCount;//片段数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public int getMomentCount() {
        return momentCount;
    }

    public void setMomentCount(int momentCount) {
        this.momentCount = momentCount;
    }

    @Override
    public String toString() {
        return "MemoryBookDto{" +
                "id=" + id +
                ", owner=" + owner +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", creatTime='" + creatTime + '\'' +
                ", friendCount=" + friendCount +
                ", momentCount=" + momentCount +
                '}';
    }
}
