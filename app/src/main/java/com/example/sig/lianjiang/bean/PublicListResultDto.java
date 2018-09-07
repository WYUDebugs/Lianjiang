package com.example.sig.lianjiang.bean;
import java.util.List;
/**
 * Created by sig on 2018/9/6.
 */

public class PublicListResultDto {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private List<PublishDto> data;

    public PublicListResultDto(Integer status, String msg, List<PublishDto> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public PublicListResultDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.data = null;
    }

    public PublicListResultDto(List<PublishDto> data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public PublicListResultDto() {

    }

    public static PublicListResultDto ok(List<PublishDto> data) {
        return new PublicListResultDto(data);
    }

    public static PublicListResultDto ok() {
        return new PublicListResultDto(null);
    }

    public static PublicListResultDto error(String msg){
        return new PublicListResultDto(500, "error："+msg);
    }
    public static PublicListResultDto error(){
        return new PublicListResultDto(500, "error");
    }
    public static PublicListResultDto failure(){
        return new PublicListResultDto(500, "failure");
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

    public List<PublishDto> getData() {
        return data;
    }

    public void setData(List<PublishDto> data) {
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
