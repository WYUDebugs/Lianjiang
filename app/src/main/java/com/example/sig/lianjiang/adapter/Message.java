package com.example.sig.lianjiang.adapter;

/**
 * Created by sig on 2018/7/11.
 */

public class Message {
    private int imageId;
    private String name;
    private String mainMean;
    private String time;
    private int messageNum;

    public Message(int imageId,String name,String mainMean,String time,int messageNum){
        this.imageId=imageId;
        this.name=name;
        this.mainMean=mainMean;
        this.time=time;
        this.messageNum=messageNum;
    }
    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getMainMean() {
        return mainMean;
    }

    public String getTime() {
        return time;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMainMean(String mainMean) {
        this.mainMean = mainMean;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
    }
}
