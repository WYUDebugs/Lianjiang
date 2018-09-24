package com.example.sig.lianjiang.bean;

import java.util.List;

/**
 * Created by sig on 2018/9/19.
 */

public class PublicCommentListResultDto {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private List<PublicComment> data;

    public PublicCommentListResultDto(Integer status, String msg, List<PublicComment> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public PublicCommentListResultDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public PublicCommentListResultDto(List<PublicComment> data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public PublicCommentListResultDto() {

    }

    public static PublicCommentListResultDto ok(List<PublicComment> data) {
        return new PublicCommentListResultDto(data);
    }

    public static PublicCommentListResultDto ok() {
        return new PublicCommentListResultDto(null);
    }

    public static PublicCommentListResultDto error(String msg){
        return new PublicCommentListResultDto(500, "error："+msg);
    }
    public static PublicCommentListResultDto error(){
        return new PublicCommentListResultDto(500, "error");
    }
    public static PublicCommentListResultDto failure(){
        return new PublicCommentListResultDto(500, "failure");
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

    public List<PublicComment> getData() {
        return data;
    }

    public void setData(List<PublicComment> data) {
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
