package com.example.sig.lianjiang.domain;
import com.hyphenate.easeui.domain.EaseUser;
/**
 * Created by sig on 2018/8/7.
 */

public class RobotUser extends EaseUser{
    public RobotUser(String username) {
        super(username.toLowerCase());
    }
}


