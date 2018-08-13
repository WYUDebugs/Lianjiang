package com.example.sig.lianjiang.bean;

import java.util.List;

public class UserListResultDto{
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private List<User> data;

    public UserListResultDto(Integer status, String msg, List<User> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public UserListResultDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public UserListResultDto(List<User> data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public UserListResultDto() {

    }

    public static UserListResultDto ok(List<User> data) {
        return new UserListResultDto(data);
    }

    public static UserListResultDto ok() {
        return new UserListResultDto(null);
    }

    public static UserListResultDto error(String msg){
        return new UserListResultDto(500, "error："+msg);
    }
    public static UserListResultDto error(){
        return new UserListResultDto(500, "error");
    }
    public static UserListResultDto failure(){
        return new UserListResultDto(500, "failure");
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

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
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
