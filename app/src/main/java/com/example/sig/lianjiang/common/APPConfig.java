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
//    public static String sendPublic=test_url+"publish/sendPublic";
//    public static String publishList=test_url+"publish/publishList";
//    public static String findMemoryBookById=test_url+"memoryBook/findMemoryBookById";
    public static String sendPublic=server_url+"publish/sendPublic";
    public static String publishList=server_url+"publish/publishList";
    public static String showSomeonePost=server_url+"publish/showSomeonePost";

    public static String IS_LOGIN = "is_login";
    public static String ACCOUNT = "account";
    public static String PSW = "password";
    public static String USERDATA = "userData";//获取当前用户的key
    public static String TYPE = "userType";
    /**
     * Hmp
     */
//    public static String changeUserById=test_url+"user/changeUserById";
//    public static String changePhoneById=test_url+"user/changePhoneById";
//    public static String checkPsw=test_url+"user/checkPsw";
//    public static String findUserByIdText=test_url+"user/findUserById";

    public static String changeUserById=server_url+"user/changeUserById";
    public static String changePhoneById=server_url+"user/changePhoneById";
    public static String checkPsw=server_url+"user/checkPsw";

//    public static String addComment=test_url+"comment/add";
//    public static String getCommentList=test_url+"comment/show";
//    public static String deleteComment=test_url+"comment/delete";
//    public static String addGood=test_url+"good/praise";
//    public static String getGoodList=test_url+"good/showPraiseMans";
    public static String addComment=server_url+"comment/add";
    public static String getCommentList=server_url+"comment/show";
    public static String deleteComment=server_url+"comment/delete";
    public static String addGood=server_url+"good/praise";
    public static String getGoodList=server_url+"good/showPraiseMans";


//    public static String findMemoryListByUserId=test_url+"memoryBook/findMemoryListByUserId";
//    public static String addBook=test_url+"memoryBook/addBook";
//    public static String findMemoryFriend=test_url+"memoryFriend/findMemoryFriend";
//    public static String addMemoryFriend=test_url+"memoryFriend/addMemoryFriend";
//    public static String sendMoment=test_url+"moment/sendMoment";
//    public static String momentList=test_url+"moment/momentList";
//    public static String deleteMemoryFriend=test_url+"memoryFriend/deleteMemoryFriend";
//    public static String changeMemoryBookTitle=test_url+"memoryBook/changeMemoryBookTitle";
//    public static String deleteMemoryBook=test_url+"memoryBook/deleteMemoryBook";
//    public static String publishNum=test_url+"publish/publishNum";
//    public static String bookNum=test_url+"memoryBook/bookNum";
//    public static String findBookDetailed=test_url+"memoryBook/findBookDetailed";
//    public static String changeMemoryBookCover=test_url+"memoryBook/changeMemoryBookCover";
//    public static String ShowOneMoment=test_url+"moment/ShowOneMoment";
//    public static String showMComments=test_url+"mComment/showMComments";
//    public static String addMomentComment=test_url+"mComment/addComment";

    public static String findMemoryListByUserId=server_url+"memoryBook/findMemoryListByUserId";
    public static String addBook=server_url+"memoryBook/addBook";
    public static String findMemoryFriend=server_url+"memoryFriend/findMemoryFriend";
    public static String addMemoryFriend=server_url+"memoryFriend/addMemoryFriend";
    public static String sendMoment=server_url+"moment/sendMoment";
    public static String momentList=server_url+"moment/momentList";
    public static String deleteMemoryFriend=server_url+"memoryFriend/deleteMemoryFriend";
    public static String changeMemoryBookTitle=server_url+"memoryBook/changeMemoryBookTitle";
    public static String deleteMemoryBook=server_url+"memoryBook/deleteMemoryBook";
    public static String publishNum=server_url+"publish/publishNum";
    public static String bookNum=server_url+"memoryBook/bookNum";
    public static String findBookDetailed=server_url+"memoryBook/findBookDetailed";
    public static String changeMemoryBookCover=server_url+"memoryBook/changeMemoryBookCover";
    public static String ShowOneMoment=server_url+"moment/ShowOneMoment";
    public static String showMComments=server_url+"mComment/showMComments";
    public static String addMomentComment=server_url+"mComment/addComment";
    public static String deleteMoment=server_url+"moment/deleteMoment";


}

