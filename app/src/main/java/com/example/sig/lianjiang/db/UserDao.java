package com.example.sig.lianjiang.db;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.example.sig.lianjiang.domain.RobotUser;
import com.hyphenate.easeui.domain.EaseUser;
/**
 * Created by sig on 2018/8/7.
 */

public class UserDao {
    public static final String TABLE_NAME = "uers";
    public static final String COLUMN_NAME_ID = "username";
    public static final String COLUMN_NAME_NICK = "nick";
    public static final String COLUMN_NAME_AVATAR = "avatar";

    public static final String PREF_TABLE_NAME = "pref";
    public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
    public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";

    public static final String ROBOT_TABLE_NAME = "robots";
    public static final String ROBOT_COLUMN_NAME_ID = "username";
    public static final String ROBOT_COLUMN_NAME_NICK = "nick";
    public static final String ROBOT_COLUMN_NAME_AVATAR = "avatar";


    public UserDao(Context context) {
    }

    /**
     * save contact list
     *
     * @param contactList
     */
    public void saveContactList(List<EaseUser> contactList) {
        StarryDBManager.getInstance().saveContactList(contactList);
    }

    /**
     * get contact list
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {

        return StarryDBManager.getInstance().getContactList();
    }

    /**
     * delete a contact
     * @param username
     */
    public void deleteContact(String username){
        StarryDBManager.getInstance().deleteContact(username);
    }

    /**
     * save a contact
     * @param user
     */
    public void saveContact(EaseUser user){
        StarryDBManager.getInstance().saveContact(user);
    }

    public void setDisabledGroups(List<String> groups){
        StarryDBManager.getInstance().setDisabledGroups(groups);
    }

    public List<String>  getDisabledGroups(){
        return StarryDBManager.getInstance().getDisabledGroups();
    }

    public void setDisabledIds(List<String> ids){
        StarryDBManager.getInstance().setDisabledIds(ids);
    }

    public List<String> getDisabledIds(){
        return StarryDBManager.getInstance().getDisabledIds();
    }

    public Map<String, RobotUser> getRobotUser(){
        return StarryDBManager.getInstance().getRobotList();
    }

    public void saveRobotUser(List<RobotUser> robotList){
        StarryDBManager.getInstance().saveRobotList(robotList);
    }
}

