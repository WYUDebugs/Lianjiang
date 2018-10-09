package com.example.sig.lianjiang.bean;

import java.util.List;

/**
 * Created by sig on 2018/10/8.
 */

public class MomentCommentListResult {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private List<MomentComment> data;

    public MomentCommentListResult(Integer status, String msg, List<MomentComment> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public MomentCommentListResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public MomentCommentListResult(List<MomentComment> data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public MomentCommentListResult() {

    }

    public static MomentCommentListResult ok(List<MomentComment> data) {
        return new MomentCommentListResult(data);
    }

    public static MomentCommentListResult ok() {
        return new MomentCommentListResult(null);
    }

    public static MomentCommentListResult error(String msg){
        return new MomentCommentListResult(500, "error："+msg);
    }
    public static MomentCommentListResult error(){
        return new MomentCommentListResult(500, "error");
    }
    public static MomentCommentListResult failure(){
        return new MomentCommentListResult(500, "failure");
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

    public List<MomentComment> getData() {
        return data;
    }

    public void setData(List<MomentComment > data) {
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
