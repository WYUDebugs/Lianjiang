package com.example.sig.lianjiang.bean;
import java.util.Date;
/**
 * Created by sig on 2018/9/4.
 */

public class PublishConcern {
    private int id;
    private int concern_people;
    private int concern_public;
    private Date concern_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConcern_people() {
        return concern_people;
    }

    public void setConcern_people(int concern_people) {
        this.concern_people = concern_people;
    }

    public int getConcern_public() {
        return concern_public;
    }

    public void setConcern_public(int concern_public) {
        this.concern_public = concern_public;
    }

    public Date getConcern_time() {
        return concern_time;
    }

    public void setConcern_time(Date concern_time) {
        this.concern_time = concern_time;
    }
}
