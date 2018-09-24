package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/9/22.
 */

public class PublicGoodResultDto {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private PublishGood data;

    public PublicGoodResultDto(Integer status, String msg, PublishGood data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public PublicGoodResultDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public PublicGoodResultDto(PublishGood data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public PublicGoodResultDto() {

    }

    public static PublicGoodResultDto ok(PublishGood data) {
        return new PublicGoodResultDto(data);
    }

    public static PublicGoodResultDto ok() {
        return new PublicGoodResultDto(null);
    }

    public static PublicGoodResultDto error(String msg){
        return new PublicGoodResultDto(500, "error："+msg);
    }
    public static PublicGoodResultDto error(){
        return new PublicGoodResultDto(500, "error");
    }
    public static PublicGoodResultDto failure(){
        return new PublicGoodResultDto(500, "failure");
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

    public PublishGood getData() {
        return data;
    }

    public void setData(PublishGood data) {
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
