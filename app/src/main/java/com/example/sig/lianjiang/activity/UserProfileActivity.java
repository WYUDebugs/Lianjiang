package com.example.sig.lianjiang.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.sig.lianjiang.bean.ResultDto;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.ImageUtils;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.view.ObservableScrollView;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileActivity extends BaseActivity implements OnClickListener, ObservableScrollView.ScrollViewListener {

    public Uri cropImageUri;
    public final int GET_IMAGE_BY_CAMERA_U = 5001;
    public final int CROP_IMAGE_U = 5003;
    public final String USER_IMAGE_NAME = "image.png";
    public final String USER_CROP_IMAGE_NAME = "temporary.png";
    private UserResultDto resultDto;

    private UserResultDto userResultDto;

    private ObservableScrollView mScrollView;
    private LinearLayout topbar;
    private TextView topbarText;
    private int imageHeight=200;
    private ImageView back;
    private TextView userName;
    private TextView userSignature;
    private TextView userSex;
    private TextView userDay;
    private ImageView head_background;
    private ImageView head;
    private String userId;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        theme();
        setContentView(R.layout.activity_user_profile);
        initView();
        //图片加载框架，如果图片加载出错或者没加载出来，显示默认图片
        Picasso.with(UserProfileActivity.this).load(APPConfig.img_url + "3a5f0819-d925-47e8-a6a2-2c71a403f07d.png")
                .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
    }

    public void initView() {
        Intent intent = getIntent();
        userId= intent.getStringExtra("username");
//        Toast.makeText(this,userId,Toast.LENGTH_SHORT).show();
        back = (ImageView) findViewById(R.id.top_left);
        back.setOnClickListener(this);
        topbarText = (TextView) findViewById(R.id.top_center);
        topbar = (LinearLayout) findViewById(R.id.mytopbar);
        mScrollView = findViewById(R.id.sv_main_content);
        mScrollView.setScrollViewListener(this);
        userName = findViewById(R.id.user_name);
        userSignature=findViewById(R.id.user_signature);
        head = findViewById(R.id.head);
        head.setOnClickListener(this);
        getHead();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head:
                ImageUtils.showImagePickDialog(this, "修改头像");
                break;
            case R.id.top_left:
                finish();
                break;


        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            topbar.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));//AGB由相关工具获得，或者美工提供
            topbarText.setText("");
        } else if (y > 0 && y <= imageHeight - topbar.getHeight()) {
            // 只是layout背景透明(仿知乎滑动效果)
            topbar.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
            topbarText.setText("");
        } else {
            topbar.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
            topbarText.setText("个人信息");
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
            FileProvider.getUriForFile(UserProfileActivity.this, "com.example.sig.lianjiang.fileProvider", file);
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

    /**
     * 裁剪图片，自行裁剪宽高
     * @param imageUri
     * @param aspectX
     * @param aspectY
     * @param return_flag
     */
    public void cropImageBig(Uri imageUri, int aspectX, int aspectY,
                          int return_flag) {
        File file = new File(this.getExternalCacheDir(), USER_CROP_IMAGE_NAME);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //高版本一定要加上这两句话，做一下临时的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            FileProvider.getUriForFile(UserProfileActivity.this, "com.example.sig.lianjiang.fileProvider", file);
        }
        cropImageUri = Uri.fromFile(file);

        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "false");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);

        startActivityForResult(intent, return_flag);
    }

    /**
     * 将图片设置固定宽高
     * @param path
     * @param w
     * @param h
     * @return
     */
    public Bitmap GetBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
                BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    public void updataHead(final String path) {
        //图片列表，后台图片文件对应的key值必须为 file，否则文件参数传递失败
        final List<File> fileList = new ArrayList<>();
        fileList.add(new File(path));
        Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
        //传递的非文件参数列表
        final Map<String, Object> map = new HashMap<>();
//        map.put("id", String.valueOf(3));//当前用户的id
        Toast.makeText(this,userId,Toast.LENGTH_SHORT).show();
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
                            userResultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            userResultDto = UserResultDto.error("Exception:" + e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (userResultDto.getMsg().equals("success")) {
                            if (userResultDto.getData() != null) {
                                //图片加载框架，如果图片加载出错或者没加载出来，显示默认图片
                                Picasso.with(UserProfileActivity.this).load(APPConfig.img_url + userResultDto.getData().getHeadimage())
                                        .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
                            }
                            Toast.makeText(UserProfileActivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "头像更新失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                        Log.d("testRun", "请求失败------Exception:" + e.getMessage());
                        Toast.makeText(UserProfileActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).start();
    }

    public void getHead() {
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
                            Toast.makeText(UserProfileActivity.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            Picasso.with(UserProfileActivity.this).load(APPConfig.img_url + resultDto.getData().getHeadimage())
                                    .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
                            userName.setText(resultDto.getData().getName());
                            if (resultDto.getData().getSignature()!=null){
                                userSignature.setText(resultDto.getData().getSignature());
                            }else {
                                userSignature.setVisibility(View.INVISIBLE);
                            }
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(UserProfileActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }
}