package com.example.sig.lianjiang.bean;

import java.util.List;

/**
 * Created by sig on 2018/9/28.
 */

public class MemoryBookListResult {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private List<MemoryBook> data;

    public MemoryBookListResult(Integer status, String msg, List<MemoryBook> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public MemoryBookListResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public MemoryBookListResult(List<MemoryBook> data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public MemoryBookListResult() {

    }

    public static MemoryBookListResult ok(List<MemoryBook> data) {
        return new MemoryBookListResult(data);
    }

    public static MemoryBookListResult ok() {
        return new MemoryBookListResult(null);
    }

    public static MemoryBookListResult error(String msg){
        return new MemoryBookListResult(500, "error："+msg);
    }
    public static MemoryBookListResult error(){
        return new MemoryBookListResult(500, "error");
    }
    public static MemoryBookListResult failure(){
        return new MemoryBookListResult(500, "failure");
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

    public List<MemoryBook> getData() {
        return data;
    }

    public void setData(List<MemoryBook > data) {
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
