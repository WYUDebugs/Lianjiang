package com.example.sig.lianjiang.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.activity.ChangePswActivity;
import com.example.sig.lianjiang.activity.EditUserProfileActivity;
import com.example.sig.lianjiang.activity.LoginActivitysteup1;
import com.example.sig.lianjiang.activity.MainActivity;
import com.example.sig.lianjiang.activity.MemoryBookListActivity;
import com.example.sig.lianjiang.activity.SquareActivity;
import com.example.sig.lianjiang.activity.UserProfileActivity;
import com.example.sig.lianjiang.adapter.CursorTagsAdapter;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.ImageUtils;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.view.ObservableScrollView;
import com.example.sig.lianjiang.view.SecurityCodeView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by sig on 2018/7/9.
 */

public class StarFragment extends Fragment implements View.OnClickListener, ObservableScrollView.ScrollViewListener{
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
    private TextView userName;
    private TextView userSignature;
    private TextView userSex;
    private TextView userDay;
    private ImageView head_background;
    private ImageView head;
    private String userId;
    private String birthday;//生日
    private TextView date;
    private String address;//地址
    private TextView dizi;
    private ImageView sex;
    private LinearLayout llInfo;
    private LinearLayout llBook;
    private LinearLayout llSquare;
    private LinearLayout llUser;
    private String name;
    private LinearLayout logout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_star, container, false);
        initView(view);
        return view;
    }
    public void initView(View view) {
//        Intent intent = getIntent();
        userId= EMClient.getInstance().getCurrentUser();
//        Toast.makeText(this,userId,Toast.LENGTH_SHORT).show();
        topbarText = (TextView) view.findViewById(R.id.top_center);
        topbar = (LinearLayout) view.findViewById(R.id.mytopbar);
        mScrollView = view.findViewById(R.id.sv_main_content);
        mScrollView.setScrollViewListener(this);
        userName = view.findViewById(R.id.user_name);
        userSignature=view.findViewById(R.id.user_signature);
        head = view.findViewById(R.id.head);
        head.setOnClickListener(this);
        sex=view.findViewById(R.id.sex);
        dizi=view.findViewById(R.id.address);
        date=view.findViewById(R.id.date);
        llInfo=(LinearLayout)view.findViewById(R.id.llInfo);
        llInfo.setOnClickListener(this);
        llBook=(LinearLayout)view.findViewById(R.id.ll_book);
        llBook.setOnClickListener(this);
        llSquare=(LinearLayout)view.findViewById(R.id.ll_square);
        llSquare.setOnClickListener(this);
        llUser=(LinearLayout)view.findViewById(R.id.ll_user);
        llUser.setOnClickListener(this);
        logout=(LinearLayout)view.findViewById(R.id.ll_logout);
        logout.setOnClickListener(this);
        getHead();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head:
//                ImageUtils.showImagePickDialog(getActivity(), "修改头像");
                break;
            case R.id.llInfo:
                Intent intent=new Intent(getContext(),EditUserProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_book:
                Intent intent1=new Intent(getContext(),MemoryBookListActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_square:
                Intent intent2=new Intent(getContext(),SquareActivity.class);
                intent2.putExtra("userId",EMClient.getInstance().getCurrentUser());
                startActivity(intent2);
//                Toast.makeText(getContext(),"开发中",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_user:
                Intent intent3=new Intent(getContext(),UserProfileActivity.class);
                intent3.putExtra("username",EMClient.getInstance().getCurrentUser());
                startActivity(intent3);
                break;
            case R.id.ll_logout:
                logout();
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
            if(name!=null){
                topbarText.setText(name);
            }
        }
    }

//    private void theme() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//    }

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
                cropImage(imageUri, 1, 1, CROP_IMAGE_U);
//                updataHead(imageUri.toString());
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                if (resultCode == RESULT_CANCELED) {
                    ImageUtils.deleteImageUri(getActivity(), ImageUtils.imageUriFromCamera);
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
                final String s = getActivity().getExternalCacheDir() + "/" + USER_CROP_IMAGE_NAME;
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
        File file = new File(getActivity().getExternalCacheDir(), USER_CROP_IMAGE_NAME);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //高版本一定要加上这两句话，做一下临时的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            FileProvider.getUriForFile(getContext(), "com.example.sig.lianjiang.fileProvider", file);
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
        File file = new File(getActivity().getExternalCacheDir(), USER_CROP_IMAGE_NAME);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //高版本一定要加上这两句话，做一下临时的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            FileProvider.getUriForFile(getContext(), "com.example.sig.lianjiang.fileProvider", file);
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
                                Picasso.with(getContext()).load(APPConfig.img_url + userResultDto.getData().getHeadimage())
                                        .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
                            }
                            Toast.makeText(getContext(), "头像更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "头像更新失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                        Log.d("testRun", "请求失败------Exception:" + e.getMessage());
                        Toast.makeText(getContext(), "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).start();
    }
    private void logout() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        StarryHelper.getInstance().logout(true,new EMCallBack() {

            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // show login screen
                        ((MainActivity) getActivity()).finish();
                        startActivity(new Intent(getActivity(), LoginActivitysteup1.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(getActivity(), "退出登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
                            Toast.makeText(getContext(),"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            Picasso.with(getContext()).load(APPConfig.img_url + resultDto.getData().getHeadimage())
                                    .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
                            userName.setText(resultDto.getData().getName());
                            name=resultDto.getData().getName();
                            int sexId=resultDto.getData().getGender();
                            if (sexId == 1) {
                                sex.setImageResource(R.mipmap.icon_female);
                            }
                            birthday=resultDto.getData().getBirthday();
                            if (birthday != null) {
                                date.setText(birthday);
                            }
                            address=resultDto.getData().getAddress();
                            if (address!=null) {
                                dizi.setText(address);
                            }
                            if (resultDto.getData().getSignature()!=null){

                                userSignature.setText(resultDto.getData().getSignature());
                            }else {
                                userSignature.setText("这个人很懒，什么也没有留下...");
                            }
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(getContext(), "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }


    @Override
    public void onResume() {
        super.onResume();
        getHead();
    }

}
