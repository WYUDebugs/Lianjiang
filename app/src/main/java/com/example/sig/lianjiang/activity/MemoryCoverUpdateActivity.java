package com.example.sig.lianjiang.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.adapter.MemoryCoverListAdapter;
import com.example.sig.lianjiang.bean.CoverPictureBean;
import com.example.sig.lianjiang.bean.MemoryBook;
import com.example.sig.lianjiang.bean.MemoryBookResult;
import com.example.sig.lianjiang.bean.PublicResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.ImageUtils;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.StatusBarUtil;
import com.example.sig.lianjiang.view.ObservableListView;
import com.hyphenate.chat.EMClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryCoverUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgTopLeft;
    private TextView tvTopTitle;
    private ImageView imgTopRight;
    private TextView tvTopRight;
    private ListView recyclerView;
    private MemoryCoverListAdapter mAdapter;
    private List<CoverPictureBean> mDataList = new ArrayList<>();
    public final int GET_IMAGE_BY_CAMERA_U = 5001;
    public final int CROP_IMAGE_U = 5003;
    public final String USER_IMAGE_NAME = "image.png";
    public final String USER_CROP_IMAGE_NAME = "temporary.png";
    public Uri cropImageUri;
    private boolean progressShow;
    private MemoryBookResult memoryBookResult;
    private String memoryBookId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_memory_cover_update);
        memoryBookId=getIntent().getStringExtra("memoryBookId");
        initData();
        initView();

    }

    private void theme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        StatusBarUtil.StatusBarLightMode(this);  //把标题栏字体变黑色
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            CoverPictureBean coverPictureBean = new CoverPictureBean();
            if (i == 0) {
                coverPictureBean.setUrl1("coverAdd");
            } else {
                coverPictureBean.setUrl1("cover1" + i);
            }
            coverPictureBean.setUrl2("cover2" + i);
            mDataList.add(coverPictureBean);
        }
    }

    private void initView() {
        imgTopLeft = (ImageView) findViewById(R.id.img_top_left);
        tvTopTitle = (TextView) findViewById(R.id.tv_top_title);
        imgTopRight = (ImageView) findViewById(R.id.img_top_right);
        tvTopRight = (TextView) findViewById(R.id.tv_top_right);
        recyclerView = (ListView) findViewById(R.id.recyclerview);
        mAdapter = new MemoryCoverListAdapter(this,mDataList);
        recyclerView.setAdapter(mAdapter);

        imgTopLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_top_left:
                finish();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                cropImage(imageUri, 1024, 576, CROP_IMAGE_U);
//                updataHead(imageUri.toString());
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                if (resultCode == RESULT_CANCELED) {
                    ImageUtils.deleteImageUri(MemoryCoverUpdateActivity.this, ImageUtils.imageUriFromCamera);
                } else {
                    Uri imageUriCamera = ImageUtils.imageUriFromCamera;
                    //Toast.makeText(this, imageUriCamera.toString(), Toast.LENGTH_SHORT).show();
                    //Log.e("wnf", "imageUriCamera.getPath()------" + imageUriCamera.getPath());
                    //Log.e("wnf", "getImageAbsolutePath------" + ImageUtils.getImageAbsolutePath(this,imageUriCamera));
                    //updataHead(ImageUtils.getImageAbsolutePath(this,imageUriCamera));
                    // 开始对图片进行裁剪处理
                    cropImage(imageUriCamera, 1024, 576, CROP_IMAGE_U);
//                    updataHead(imageUriCamera.toString());
                }
                break;
            case CROP_IMAGE_U:
                final String s = MemoryCoverUpdateActivity.this.getExternalCacheDir() + "/" + USER_CROP_IMAGE_NAME;
                //Log.e("wnf", "path------" + s);
                // 裁剪头像图片后执行更新头像操作
//                updataHead(s);
                updateCoverPost(s,memoryBookId);
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
        File file = new File(MemoryCoverUpdateActivity.this.getExternalCacheDir(), USER_CROP_IMAGE_NAME);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //高版本一定要加上这两句话，做一下临时的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            FileProvider.getUriForFile(MemoryCoverUpdateActivity.this, "com.example.sig.lianjiang.fileProvider", file);
        }
        cropImageUri = Uri.fromFile(file);

        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", 1024);
        intent.putExtra("outputY", 576);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);

        startActivityForResult(intent, return_flag);
    }

    public void updateCoverPost(final String path,final String memoryBookId) {

        progressShow = true;
        final ProgressDialog pd= new ProgressDialog(MemoryCoverUpdateActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage("正在上传......");
        pd.show();
        //图片列表，后台图片文件对应的key值必须为 file，否则文件参数传递失败
        final List<File> fileList = new ArrayList<>();
        fileList.add(new File(path));
        //传递的非文件参数列表
        final Map<String, Object> map = new HashMap<>();
        map.put("id", memoryBookId);//当前用户的id
        //使用线程进行网络操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.postFile(APPConfig.changeMemoryBookCover, map, fileList, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryBookResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryBookResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryBookResult = MemoryBookResult.error("Exception:" + e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (memoryBookResult.getMsg().equals("success")) {
                            Toast.makeText(MemoryCoverUpdateActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                            if (!MemoryCoverUpdateActivity.this.isFinishing() && pd.isShowing()) {
                                pd.dismiss();
                            }
                            Intent intent=new Intent(MemoryCoverUpdateActivity.this,MemoryBookActivity.class);
                            intent.putExtra("memoryBookId",memoryBookId);
                            startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                    Toast.makeText(MemoryCoverUpdateActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                        Log.d("testRun", "请求失败------Exception:" + e.getMessage());
                        pd.dismiss();
                        Toast.makeText(MemoryCoverUpdateActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).start();
    }
}
