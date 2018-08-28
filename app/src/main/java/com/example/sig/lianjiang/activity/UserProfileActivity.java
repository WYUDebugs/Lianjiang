package com.example.sig.lianjiang.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.example.sig.lianjiang.bean.ResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.ImageUtils;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.OnBooleanListener;
import com.example.sig.lianjiang.utils.TimeUtil;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.view.ObservableScrollView;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import static java.lang.String.valueOf;

public class UserProfileActivity extends BaseActivity implements OnClickListener, ObservableScrollView.ScrollViewListener {

    private OnBooleanListener onPermissionListener;
    public Uri cropImageUri;
    public final int GET_IMAGE_BY_CAMERA_U = 5001;
    public final int CROP_IMAGE_U = 5003;
    public final String USER_IMAGE_NAME = "image.png";
    public final String USER_CROP_IMAGE_NAME = "temporary.png";

    private ObservableScrollView mScrollView;
    private LinearLayout topbar;
    private TextView topbarText;
    private int imageHeight;
    private ImageView back;
    private TextView userName;
    private TextView userSex;
    private TextView userDay;
    private ImageView head_background;
    private ImageView head;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        theme();
        setContentView(R.layout.activity_user_profile);
        initView();
        initListeners();
        //图片加载框架，如果图片加载出错或者没加载出来，显示默认图片
        Picasso.with(UserProfileActivity.this).load(APPConfig.img_url + "3a5f0819-d925-47e8-a6a2-2c71a403f07d.png")
                .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
    }

    public void initView() {
        back = (ImageView) findViewById(R.id.top_left);
        back.setOnClickListener(this);
        topbarText = (TextView) findViewById(R.id.top_center);
        topbar = (LinearLayout) findViewById(R.id.mytopbar_square);
        mScrollView = findViewById(R.id.lv_bbs);
//        mScrollView.setScrollViewListener(this);
        userName = findViewById(R.id.user_name);
        userSex = findViewById(R.id.user_sex);
        userDay = findViewById(R.id.user_day);
        head = findViewById(R.id.head);
        head_background = findViewById(R.id.head_background);
        head.setOnClickListener(this);
        head_background.setOnClickListener(this);
    }

    /*
    *  获取顶部图片(也可以任意一控件)高度后，设置滚动监听
     */
    private void initListeners() {
        ViewTreeObserver vto = head_background.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                head_background.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = head_background.getHeight();

                mScrollView.setScrollViewListener(UserProfileActivity.this);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_background:
                ImageUtils.showImagePickDialog(this, "修改背景图片");
                break;
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

                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                if (resultCode == RESULT_CANCELED) {
                    ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
                } else {
                    Uri imageUriCamera = ImageUtils.imageUriFromCamera;
                    //Toast.makeText(this, imageUriCamera.toString(), Toast.LENGTH_SHORT).show();
                    // 开始对图片进行裁剪处理
                    cropImage(imageUriCamera, 1, 1, CROP_IMAGE_U);
                }
                break;
            case CROP_IMAGE_U:
                final String s = getExternalCacheDir() + "/" + USER_CROP_IMAGE_NAME;
                // 裁剪头像图片后执行更新头像操作
                updataHead(s);
                break;
            default:
                break;
        }
    }

    /**
     * 裁剪图片
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
        //传递的非文件参数列表
        final Map<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(3));//用户的id
        //使用线程进行网络操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.postFile(APPConfig.updateHeadImage, map, fileList, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        ResultDto resultDto;
                        try {
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), ResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto = ResultDto.error("Exception:" + e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            Bitmap imageBitmap = GetBitmap(path, 320, 320);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
                            head.setImageBitmap(imageBitmap);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(true);
                }
            } else {
                //权限拒绝
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(false);
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
