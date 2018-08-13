package com.example.sig.lianjiang.bean;

public class UserResultDto{

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private User data;

    public UserResultDto(Integer status, String msg, User data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public UserResultDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public UserResultDto(User data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public UserResultDto() {

    }

    public static UserResultDto ok(User data) {
        return new UserResultDto(data);
    }

    public static UserResultDto ok() {
        return new UserResultDto(null);
    }

    public static UserResultDto error(String msg){
        return new UserResultDto(500, "error："+msg);
    }
    public static UserResultDto error(){
        return new UserResultDto(500, "error");
    }
    public static UserResultDto failure(){
        return new UserResultDto(500, "failure");
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

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserResultDto{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
