package com.example.sig.lianjiang.bean;


/**
 * Created by sig on 2018/9/28.
 */

public class MemoryBookResult {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private MemoryBook data;

    public MemoryBookResult(Integer status, String msg, MemoryBook data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public MemoryBookResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public MemoryBookResult(MemoryBook data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public MemoryBookResult() {

    }

    public static MemoryBookResult ok(MemoryBook data) {
        return new MemoryBookResult(data);
    }

    public static MemoryBookResult ok() {
        return new MemoryBookResult(null);
    }

    public static MemoryBookResult error(String msg){
        return new MemoryBookResult(500, "error："+msg);
    }
    public static MemoryBookResult error(){
        return new MemoryBookResult(500, "error");
    }
    public static MemoryBookResult failure(){
        return new MemoryBookResult(500, "failure");
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

    public MemoryBook getData() {
        return data;
    }

    public void setData(MemoryBook data) {
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
