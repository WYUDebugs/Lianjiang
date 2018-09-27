package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/9/25.
 */

import java.util.List;

public class Moment {
    private int id;
    private int sender;
    private  String send_time;
    private  String location_time;
    private  String content;
    private List<MomentImage> imageList;
    private User user;

    public List<MomentImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<MomentImage> imageList) {
        this.imageList = imageList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getLocation_time() {
        return location_time;
    }

    public void setLocation_time(String location_time) {
        this.location_time = location_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

