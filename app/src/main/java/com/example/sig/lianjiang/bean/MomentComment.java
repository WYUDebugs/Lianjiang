package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/10/7.
 */

public class MomentComment {
    private int mcId;
    private int commentator;
    private User user;
    private int momentId;
    private String commentTime;
    private String commentContent;
    private int state;

    public int getMcId() {
        return mcId;
    }

    public void setMcId(int mcId) {
        this.mcId = mcId;
    }

    public int getCommentator() {
        return commentator;
    }

    public void setCommentator(int commentator) {
        this.commentator = commentator;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getMomentId() {
        return momentId;
    }

    public void setMomentId(int momentId) {
        this.momentId = momentId;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MomentComment{" +
                "mcId=" + mcId +
                ", commentator=" + commentator +
                ", user=" + user +
                ", momentId=" + momentId +
                ", commentTime='" + commentTime + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", state=" + state +
                '}';
    }
}
