package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/9/26.
 */

public class MemoryFriend {
    private int id;
    private int friendId; //好友id（User的id约束）
    private int memoryBookId; //纪念册id（MemoryBook的id约束）
    private String addTime; //添加时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friend_id) {
        this.friendId = friend_id;
    }

    public int getMemoryBookId() {
        return memoryBookId;
    }

    public void setMemoryBookId(int memory_book_id) {
        this.memoryBookId = memory_book_id;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String add_time) {
        this.addTime = add_time;
    }
}

