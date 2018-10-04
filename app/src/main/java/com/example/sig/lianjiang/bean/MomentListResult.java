package com.example.sig.lianjiang.bean;

import java.util.List;

/**
 * Created by sig on 2018/10/1.
 */

public class MomentListResult {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private List<Moment> data;

    public MomentListResult(Integer status, String msg, List<Moment> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public MomentListResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public MomentListResult(List<Moment> data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public MomentListResult() {

    }

    public static MomentListResult ok(List<Moment> data) {
        return new MomentListResult(data);
    }

    public static MomentListResult ok() {
        return new MomentListResult(null);
    }

    public static MomentListResult error(String msg){
        return new MomentListResult(500, "error："+msg);
    }
    public static MomentListResult error(){
        return new MomentListResult(500, "error");
    }
    public static MomentListResult failure(){
        return new MomentListResult(500, "failure");
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

    public List<Moment> getData() {
        return data;
    }

    public void setData(List<Moment > data) {
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
