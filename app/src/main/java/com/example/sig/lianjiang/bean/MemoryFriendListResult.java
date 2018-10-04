package com.example.sig.lianjiang.bean;

import java.util.List;

/**
 * Created by sig on 2018/9/30.
 */

public class MemoryFriendListResult {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private List<MemoryFriend> data;

    public MemoryFriendListResult(Integer status, String msg, List<MemoryFriend> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public MemoryFriendListResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public MemoryFriendListResult(List<MemoryFriend> data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public MemoryFriendListResult() {

    }

    public static MemoryFriendListResult ok(List<MemoryFriend> data) {
        return new MemoryFriendListResult(data);
    }

    public static MemoryFriendListResult ok() {
        return new MemoryFriendListResult(null);
    }

    public static MemoryFriendListResult error(String msg){
        return new MemoryFriendListResult(500, "error："+msg);
    }
    public static MemoryFriendListResult error(){
        return new MemoryFriendListResult(500, "error");
    }
    public static MemoryFriendListResult failure(){
        return new MemoryFriendListResult(500, "failure");
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MemoryFriend> getData() {
        return data;
    }

    public void setData(List<MemoryFriend > data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserListResultDto{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
