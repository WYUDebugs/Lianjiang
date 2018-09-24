package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/9/19.
 */

public class PublicCommentResultDto {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private PublicComment data;

    public PublicCommentResultDto(Integer status, String msg, PublicComment data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public PublicCommentResultDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public PublicCommentResultDto(PublicComment data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public PublicCommentResultDto() {

    }

    public static PublicCommentResultDto ok(PublicComment data) {
        return new PublicCommentResultDto(data);
    }

    public static PublicCommentResultDto ok() {
        return new PublicCommentResultDto(null);
    }

    public static PublicCommentResultDto error(String msg){
        return new PublicCommentResultDto(500, "error："+msg);
    }
    public static PublicCommentResultDto error(){
        return new PublicCommentResultDto(500, "error");
    }
    public static PublicCommentResultDto failure(){
        return new PublicCommentResultDto(500, "failure");
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

    public PublicComment getData() {
        return data;
    }

    public void setData(PublicComment data) {
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
