package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/9/26.
 */

public class MemoryFriend {
    private int id;
    private int friend_id; //好友id（User的id约束）
    private int memory_book_id; //纪念册id（MemoryBook的id约束）
    private String add_time; //添加时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriendId() {
        return friend_id;
    }

    public void setFriendId(int friend_id) {
        this.friend_id = friend_id;
    }

    public int getMemoryBookId() {
        return memory_book_id;
    }

    public void setMemoryBookId(int memory_book_id) {
        this.memory_book_id = memory_book_id;
    }

    public String getAddTime() {
        return add_time;
    }

    public void setAddTime(String add_time) {
        this.add_time = add_time;
    }
}

