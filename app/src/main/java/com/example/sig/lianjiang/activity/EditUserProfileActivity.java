package com.example.sig.lianjiang.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.bean.CardBean;
import com.example.sig.lianjiang.bean.JsonBean;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.CustomDatePicker;
import com.example.sig.lianjiang.utils.GetJsonDataUtil;
import com.example.sig.lianjiang.utils.ImageUtils;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.TimeUtil;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditUserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<CardBean> cardItem = new ArrayList<>();
    private OptionsPickerView pvCustomOptions;
    private final static int REQUEST_CODE = 0x123;
    public final int CROP_IMAGE_U = 5003;
    public final String USER_CROP_IMAGE_NAME = "temporary.png";
    public Uri cropImageUri;
    private LinearLayout mytopbar_back;
    private ImageView back;
    private LinearLayout changePhone;
    private LinearLayout changeNick;
    private LinearLayout changeSig; //个性签名
    private LinearLayout changePsw;
    private LinearLayout changeBirth;
    private LinearLayout changeAddress;
    private LinearLayout changeSex;
    private LinearLayout LLhead;
    private UserResultDto resultDto;
    private String userId; //用户id
    private ImageView headImage; //头像
    private TextView tv_nick; //昵称
    private TextView tv_signa; //签名
    private TextView tv_phone; //手机号码
    private TextView tvBirth;  //生日
    private TextView textView;  //性别
    private TextView tvlocation; //地址
    private TextView title;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private boolean isLoaded = false;


    public final static String LEVEL_NICK="nick";
    public final static String  LEVEL_SIGNA="signa";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

//                        Toast.makeText(EditUserProfileActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(EditUserProfileActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(EditUserProfileActivity.this, "加载城市失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_edit_user_profile);
        initView();
       /* Picasso.with(EditUserProfileActivity.this).load(APPConfig.img_url + "3a5f0819-d925-47e8-a6a2-2c71a403f07d.png")
                .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(headImage);*/
        getCardData();   //加载男女选择器内容
        mHandler.sendEmptyMessage(MSG_LOAD_DATA); //加载城市
    }

    private void initView(){
        title=(TextView)findViewById(R.id.top_center);
        title.setText("修改资料");
        userId= EMClient.getInstance().getCurrentUser();
        headImage=(ImageView)findViewById(R.id.imv_avatar);
        LLhead=(LinearLayout)findViewById(R.id.llHead);
        LLhead.setOnClickListener(this);
        mytopbar_back=(LinearLayout)findViewById(R.id.mytopbar);
        mytopbar_back.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
        back = (ImageView) findViewById(R.id.top_left);
        back.setOnClickListener(this);
        changePhone=(LinearLayout)findViewById(R.id.change_phone);
        changePhone.setOnClickListener(this);
        changeNick=(LinearLayout)findViewById(R.id.llNickname);
        changeNick.setOnClickListener(this);
        changeSex=(LinearLayout)findViewById(R.id.set_sex);
        changeSex.setOnClickListener(this);
        changeSig=(LinearLayout)findViewById(R.id.llMotto);
        changeSig.setOnClickListener(this);
        changePsw=(LinearLayout)findViewById(R.id.llChangePwd);
        changePsw.setOnClickListener(this);
        changeBirth=(LinearLayout)findViewById(R.id.llDirth);
        changeBirth.setOnClickListener(this);
        changeAddress=(LinearLayout)findViewById(R.id.llAdress);
        changeAddress.setOnClickListener(this);
        tvlocation=(TextView)findViewById(R.id.tvlocation);
        tvBirth=(TextView)findViewById(R.id.tvBirth);
        tvBirth.setOnClickListener(this);
        textView=(TextView)findViewById(R.id.tvCard);
        tv_nick=(TextView)findViewById(R.id.tvNickname);
        tv_signa=(TextView)findViewById(R.id.tvMotto);
        tv_phone=(TextView)findViewById(R.id.phone_num) ;
         getUserDate();
    }

    private void theme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_left:
                finish();
                break;
            case R.id.llHead:
                ImageUtils.showImagePickDialog(this, "修改头像");
                break;
            case R.id.change_phone:
                Intent intent=new Intent(EditUserProfileActivity.this,ChangePhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.llNickname:
                Intent intent1=new Intent(EditUserProfileActivity.this,ChangeInfoActivity.class);
                intent1.putExtra("level",LEVEL_NICK);
                String nickdate=tv_nick.getText().toString();
                startActivity(intent1);
                break;
            case R.id.set_sex:
                initCustomOptionPicker();
                pvCustomOptions.show(); //弹出性别选择器
                break;
            case R.id.llMotto:
                Intent intent2=new Intent(EditUserProfileActivity.this,ChangeInfoActivity.class);
                String signaDate=tv_signa.getText().toString();
                intent2.putExtra("level",LEVEL_SIGNA);
                startActivity(intent2);
                break;
            case R.id.llChangePwd:
                Intent intent3=new Intent(EditUserProfileActivity.this,ChangePswActivity.class);
                startActivity(intent3);
                break;
            case R.id.llDirth:
                alterDatePicker();
                break;
            case R.id.llAdress:
//                getLocation();
                if (isLoaded) {
                    showPickerView();
                } else {
                    Toast.makeText(EditUserProfileActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            String position = data.getStringExtra("position");
            tvlocation.setText(position);
            submitChangeAddress(position);
        }
     switch (requestCode) {
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                //这里得到图片后做相应操作
                Uri imageUri = data.getData();
                //Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();
                // 开始对图片进行裁剪处理
                cropImage(imageUri, 1, 1, CROP_IMAGE_U);
//                updataHead(imageUri.toString());
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                if (resultCode == RESULT_CANCELED) {
                    ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
                } else {
                    Uri imageUriCamera = ImageUtils.imageUriFromCamera;
                    //Toast.makeText(this, imageUriCamera.toString(), Toast.LENGTH_SHORT).show();
                    //Log.e("wnf", "imageUriCamera.getPath()------" + imageUriCamera.getPath());
                    //Log.e("wnf", "getImageAbsolutePath------" + ImageUtils.getImageAbsolutePath(this,imageUriCamera));
                    //updataHead(ImageUtils.getImageAbsolutePath(this,imageUriCamera));
                    // 开始对图片进行裁剪处理
                    cropImage(imageUriCamera, 1, 1, CROP_IMAGE_U);
//                    updataHead(imageUriCamera.toString());
                }
                break;
            case CROP_IMAGE_U:
                final String s = getExternalCacheDir() + "/" + USER_CROP_IMAGE_NAME;
                //Log.e("wnf", "path------" + s);
                // 裁剪头像图片后执行更新头像操作
                updataHead(s);
                break;
            default:
                break;
        }
    }
    /**
     * 裁剪图片，固定宽高320
     * @param imageUri
     * @param aspectX
     * @param aspectY
     * @param return_flag
     */
    public void cropImage(Uri imageUri, int aspectX, int aspectY,
                          int return_flag) {
        File file = new File(this.getExternalCacheDir(), USER_CROP_IMAGE_NAME);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //高版本一定要加上这两句话，做一下临时的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            FileProvider.getUriForFile(EditUserProfileActivity.this, "com.example.sig.lianjiang.fileProvider", file);
        }
        cropImageUri = Uri.fromFile(file);

        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);

        startActivityForResult(intent, return_flag);
    }
    public void updataHead(final String path) {
        //图片列表，后台图片文件对应的key值必须为 file，否则文件参数传递失败
        final List<File> fileList = new ArrayList<>();
        fileList.add(new File(path));
        //Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
        //传递的非文件参数列表
        final Map<String, Object> map = new HashMap<>();
//        map.put("id", String.valueOf(3));//当前用户的id
        //Toast.makeText(this,userId,Toast.LENGTH_SHORT).show();
        map.put("id", userId);//当前用户的id
        //使用线程进行网络操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.postFile(APPConfig.updateHeadImage, map, fileList, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto = UserResultDto.error("Exception:" + e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            if (resultDto.getData() != null) {
                                //图片加载框架，如果图片加载出错或者没加载出来，显示默认图片
                                Picasso.with(EditUserProfileActivity.this).load(APPConfig.img_url + resultDto.getData().getHeadimage())
                                        .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(headImage);
                            }
                            Toast.makeText(EditUserProfileActivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditUserProfileActivity.this, "头像更新失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                        Log.d("testRun", "请求失败------Exception:" + e.getMessage());
                        Toast.makeText(EditUserProfileActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).start();
    }
    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = cardItem.get(options1).getPickerViewText();
                if (tx.equals("女")) {
                    String genderId = "1";
                    submitChangeGender(genderId);
                }
                if (tx.equals("男")){
                    String genderId="0";
                    submitChangeGender(genderId);
                }
//                btn_CustomOptions.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });


                    }
                })

                .isDialog(true)
                .build();

        pvCustomOptions.setPicker(cardItem);//添加数据


    }
    private void getCardData() {

        cardItem.add(new CardBean(0, "男" ));
        cardItem.add(new CardBean(1, "女" ));
    }
    public void getLocation(){
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //自定义弹框3
    public void alterDatePicker(){
        String now = TimeUtil.dateToStringNoS(new Date());
        String startTime = "1950-01-01 00:00";
        String endTime = now;
        String seletTime="1997-01-01 00:00";
        CustomDatePicker customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                String date = time.substring(0, 10);
                submitChangeBirth(date);
            }
        }, startTime, endTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        customDatePicker.show(seletTime);

    }

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);

//                Toast.makeText(EditUserProfileActivity.this, tx, Toast.LENGTH_SHORT).show();
                tvlocation.setText(tx);
                submitChangeAddress(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                if(CityName.equals("其他")){

                }else {
                    CityList.add(CityName);//添加城市
                }
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    List<String> temp=jsonBean.get(i).getCityList().get(c).getArea();
                    for(int j=0;j<temp.size();j++){
                        if(temp.get(j).equals("其他")){

                        }else {
                            City_AreaList.add(temp.get(j));
                        }
                    }
//                    if(!(jsonBean.get(i).getCityList().get(c).getArea().equals("其他"))){
//                        City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
//                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }
    public void submitChangeGender(final String gender) {
        Log.d("zxd111","提交性别");
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param genderParam = new OkHttpUtils.Param("gender", gender);
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id",userId);
        list.add(genderParam);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.changeUserById, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto = UserResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(EditUserProfileActivity.this,"服务器出错,修改失败",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            if (gender.equals("1")) {
                                textView.setText("女");
                            } else {
                                textView.setText("男");
                            }
                            Toast.makeText(EditUserProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(EditUserProfileActivity.this, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(EditUserProfileActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }
    public void submitChangeBirth(final String birthday) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param birthParam = new OkHttpUtils.Param("birthday", birthday);
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id",userId);
        list.add(birthParam);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.changeUserById, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto = UserResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(EditUserProfileActivity.this,"服务器出错,修改失败",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            tvBirth.setText(birthday);
                            Toast.makeText(EditUserProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(EditUserProfileActivity.this, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(EditUserProfileActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }
    public void getUserDate() {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", userId);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.findUserById, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto = UserResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(EditUserProfileActivity.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            Picasso.with(EditUserProfileActivity.this).load(APPConfig.img_url + resultDto.getData().getHeadimage())
                                    .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(headImage);
                           tv_nick.setText(resultDto.getData().getName());
                           tv_signa.setText(resultDto.getData().getSignature());
                           tv_phone.setText(resultDto.getData().getPhone());
                            if (resultDto.getData().getBirthday()!=null) {
                                tvBirth.setText(resultDto.getData().getBirthday());
                            }
                            int sexId=resultDto.getData().getGender();
                            if (sexId==1) {
                                textView.setText("女");
                            }else {
                                textView.setText("男");
                            }
                            if(resultDto.getData().getAddress()!=null){
                                tvlocation.setText(resultDto.getData().getAddress());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(EditUserProfileActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }

    public void submitChangeAddress(final String address) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param addressParam = new OkHttpUtils.Param("address", address);
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id",userId);
        list.add(addressParam);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.changeUserById, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto = UserResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(EditUserProfileActivity.this,"服务器出错,修改失败",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            Toast.makeText(EditUserProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditUserProfileActivity.this, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                            getUserDate();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(EditUserProfileActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
         getUserDate();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserDate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
