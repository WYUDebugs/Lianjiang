package com.example.sig.lianjiang.bean;
import java.util.Date;
import java.util.List;
/**
 * Created by sig on 2018/9/4.
 */

public class PublishDto {
    private int id;
    private int publisher; //发布人id
    private Date time;  //发布的时间
    private String content;  //发布的内容
    private int times_of_browse; //浏览数
    private int times_of_praise;  //点赞数
    private int number_of_comments; //评论数
    private int collection_number;  //关注收藏数
    private String address;  //定位的地址
    private int type;  //动态的类型，默认为0为发布的动态，1为分享过来的动态
    private List<PublishImage> imageList;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublisher() {
        return publisher;
    }

    public void setPublisher(int publisher) {
        this.publisher = publisher;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTimes_of_browse() {
        return times_of_browse;
    }

    public void setTimes_of_browse(int times_of_browse) {
        this.times_of_browse = times_of_browse;
    }

    public int getTimes_of_praise() {
        return times_of_praise;
    }

    public void setTimes_of_praise(int times_of_praise) {
        this.times_of_praise = times_of_praise;
    }

    public int getNumber_of_comments() {
        return number_of_comments;
    }

    public void setNumber_of_comments(int number_of_comments) {
        this.number_of_comments = number_of_comments;
    }

    public int getCollection_number() {
        return collection_number;
    }

    public void setCollection_number(int collection_number) {
        this.collection_number = collection_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<PublishImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<PublishImage> imageList) {
        this.imageList = imageList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Publish{" +
                "id=" + id +
                ", publisher=" + publisher +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", times_of_browse=" + times_of_browse +
                ", times_of_praise=" + times_of_praise +
                ", number_of_comments=" + number_of_comments +
                ", collection_number=" + collection_number +
                ", address='" + address + '\'' +
                ", type=" + type +
                ", imageList=" + imageList +
                ", user=" + user +
                '}';
    }
}
