package com.example.sig.lianjiang.bean;
import java.util.Date;
/**
 * Created by sig on 2018/9/4.
 */

public class PublishGood {
    private int id;
    private int publishId;
    private int manOfPraise;
    private Date timeOfPraise;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublishId() {
        return publishId;
    }

    public void setPublishId(int publishId) {
        this.publishId = publishId;
    }

    public int getManOfPraise() {
        return manOfPraise;
    }

    public void setManOfPraise(int manOfPraise) {
        this.manOfPraise = manOfPraise;
    }

    public Date getTimeOfPraise() {
        return timeOfPraise;
    }

    public void setTimeOfPraise(Date timeOfPraise) {
        this.timeOfPraise = timeOfPraise;
    }
}
