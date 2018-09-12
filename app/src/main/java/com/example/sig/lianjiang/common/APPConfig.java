package com.example.sig.lianjiang.common;

/**
 * Created by wunaifu on 2018/8/13.
 */
public class APPConfig {

//    private static String base_url="http://10.0.2.2:8080/starry/";
    private static String server_url="http://wyusig.com:8080/starry/";
//    private static String server_url="http://10.0.2.2:8080/starry/";

//    private static String base_url="http://192.168.0.105:8080/starry/";
    private static String test_url="http://10.0.2.2:8080/starry/";
    private static String base_url="http://wyusig.com:8080/starry/";

    public static String img_url="http://wyusig.com:8080/starry/img/";
    public static String test_image_url="http://10.0.2.2:8080/starry/img/";


    public static String login = server_url + "user/Login";
    public static String register = server_url + "user/register";
    public static String findUserByPhone = server_url + "user/findUserByPhone";
    public static String findUserById=server_url+"user/findUserById";
    public static String findAllUser = server_url + "user/findAllUser";
    public static String deleteUserByPhone = server_url + "user/deleteUserByPhone";

    public static String updateHeadImage = base_url + "user/updateHeadImage";
    public static String addFriend=server_url+"friend/addFriend";
    public static String sendPublic=test_url+"publish/sendPublic";
    public static String publishList=test_url+"publish/publishList";
    public static String findMemoryBookById=test_url+"memoryBook/findMemoryBookById";

    public static String IS_LOGIN = "is_login";
    public static String ACCOUNT = "account";
    public static String PSW = "password";
    public static String USERDATA = "userData";//获取当前用户的key
    public static String TYPE = "userType";

    public static String changeUserById=test_url+"user/changeUserById";
}

