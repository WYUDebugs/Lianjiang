package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/9/5.
 */

public class PublicResultDto {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private PublishDto data;

    public PublicResultDto(Integer status, String msg, PublishDto data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public PublicResultDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public PublicResultDto(PublishDto data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public PublicResultDto() {

    }

    public static PublicResultDto ok(PublishDto data) {
        return new PublicResultDto(data);
    }

    public static PublicResultDto ok() {
        return new PublicResultDto(null);
    }

    public static PublicResultDto error(String msg){
        return new PublicResultDto(500, "error："+msg);
    }
    public static PublicResultDto error(){
        return new PublicResultDto(500, "error");
    }
    public static PublicResultDto failure(){
        return new PublicResultDto(500, "failure");
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

    public PublishDto getData() {
        return data;
    }

    public void setData(PublishDto data) {
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
