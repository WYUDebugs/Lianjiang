package com.example.sig.lianjiang.bean;

import java.util.List;

/**
 * Created by sig on 2018/10/7.
 */

public class MomentResult {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Moment data;

    public MomentResult(Integer status, String msg, Moment data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public MomentResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public MomentResult(Moment data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public MomentResult() {

    }

    public static MomentResult ok(Moment data) {
        return new MomentResult(data);
    }

    public static MomentResult ok() {
        return new MomentResult(null);
    }

    public static MomentResult error(String msg){
        return new MomentResult(500, "error："+msg);
    }
    public static MomentResult error(){
        return new MomentResult(500, "error");
    }
    public static MomentResult failure(){
        return new MomentResult(500, "failure");
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

    public Moment getData() {
        return data;
    }

    public void setData(Moment data) {
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
