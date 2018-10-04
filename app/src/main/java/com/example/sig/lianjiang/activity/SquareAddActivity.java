package com.example.sig.lianjiang.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.bean.PublicResultDto;
import com.example.sig.lianjiang.bean.PublishDto;
import com.example.sig.lianjiang.bean.ResultDto;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.CustomDatePicker;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.StatusBarUtil;
import com.example.sig.lianjiang.utils.TimeUtil;
import com.hyphenate.chat.EMClient;
import com.lidong.photopicker.ImageCaptureManager;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;

import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SquareAddActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgTopLeft;
    private TextView tv_send;
    private EditText etMomentContent;
    private LinearLayout llLocationTime;
    private TextView tv_location;
    private final static int REQUEST_CODE = 0x123;
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private String TAG =SquareAddActivity.class.getSimpleName();
    private String depp;
    private String location;
    private PublicResultDto ResultDto;
    private GridView gridView;
    private GridAdapter gridAdapter;
    private boolean progressShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_square_add);
        initData();
        initView();
    }
    private void initData() {

    }

    private void initView() {
        imgTopLeft = (ImageView) findViewById(R.id.img_top_left);

        etMomentContent = (EditText) findViewById(R.id.et_moment_content);
        llLocationTime = (LinearLayout) findViewById(R.id.ll_location_time);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_send=findViewById(R.id.tv_send);
        imgTopLeft.setOnClickListener(this);
        llLocationTime.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);
        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if ("000000".equals(imgs) ){
                    PhotoPickerIntent intent = new PhotoPickerIntent(SquareAddActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(9); // 最多选择照片数量，默认为6
                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                }else{
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(SquareAddActivity.this);
                    intent.setCurrentItem(position);
                    ArrayList<String> imagePaths_temp = new ArrayList<>();
                    for(int i=0;i<imagePaths.size();i++){
                        if(imagePaths.get(i).equals("000000")){

                        }else {
                            imagePaths_temp.add(imagePaths.get(i));
                        }
                    }
                    intent.setPhotoPaths(imagePaths_temp);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });
        imagePaths.add("000000");
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
        tv_send.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_top_left:
                finish();
                break;
            case R.id.ll_location_time:
                getLocation();
                break;
            case R.id.tv_send:
                upLoad();
                break;

        }
    }
    public void upLoad(){
        depp =etMomentContent.getText().toString().trim()!=null?etMomentContent.getText().toString().trim():"";
        location=tv_location.getText().toString().trim()!=null?tv_location.getText().toString().trim():"中国";
        if(location.equals("添加地点")){
            location="";
        }
//        for(int i=0;i<imagePaths.size();i++){
//            Log.d("zxd",imagePaths.get(i));
//        }
        sendPublicPost(imagePaths,depp,location);
    }
    private void theme(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        StatusBarUtil.StatusBarLightMode(this);  //把标题栏字体变黑色
    }

    public void getLocation(){
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 返回成功
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            String position = data.getStringExtra("position");
            tv_location.setText(position);
        }

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d(TAG, "list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    Log.d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths){
        if (imagePaths!=null&& imagePaths.size()>0){
            imagePaths.clear();
        }
        if (paths.contains("000000")){
            paths.remove("000000");
        }
        paths.add("000000");
        imagePaths.addAll(paths);
        gridAdapter  = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
        try{
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class GridAdapter extends BaseAdapter{
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;
        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if(listUrls.size() == 7){
                listUrls.remove(listUrls.size()-1);
            }
            inflater = LayoutInflater.from(SquareAddActivity.this);
        }

        public int getCount(){
            return  listUrls.size();
        }
        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image, parent,false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            final String path=listUrls.get(position);
            if (path.equals("000000")){
                holder.image.setImageResource(R.drawable.addpic);
            }else {
                Glide.with(SquareAddActivity.this)
                        .load(path)
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.default_error)
                        .centerCrop()
                        .crossFade()
                        .into(holder.image);
            }
            return convertView;
        }
        class ViewHolder {
            ImageView image;
        }
    }
    public void sendPublicPost(final ArrayList<String> imagePaths,final String content,final String address) {

        progressShow = true;
        final ProgressDialog pd= new ProgressDialog(SquareAddActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage("正在发布......");
        pd.show();

        //图片列表，后台图片文件对应的key值必须为 file，否则文件参数传递失败
        final List<File> fileList = new ArrayList<>();
        for(int i=0;i<imagePaths.size();i++){
            if(imagePaths.get(i).equals("000000")){

            }else{
                fileList.add(new File(imagePaths.get(i)));
            }
        }
//        fileList.add(new File(path));
//        Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
        //传递的非文件参数列表
        final Map<String, Object> map = new HashMap<>();
//        map.put("id", String.valueOf(3));//当前用户的id
//        Toast.makeText(this,userId,Toast.LENGTH_SHORT).show();
        map.put("publisher", EMClient.getInstance().getCurrentUser());//当前用户的id
        map.put("content",content);
        map.put("address",address);
        map.put("type",0);
        //使用线程进行网络操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.postFile(APPConfig.sendPublic, map, fileList, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            ResultDto = OkHttpUtils.getObjectFromJson(response.toString(), PublicResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            ResultDto = ResultDto.error("Exception:" + e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (ResultDto.getMsg().equals("publish_success")) {
                            Toast.makeText(SquareAddActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                            if (!SquareAddActivity.this.isFinishing() && pd.isShowing()) {
                                pd.dismiss();
                            }
                            Intent intent=new Intent(SquareAddActivity.this,SquareActivity.class);
                            startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                    Toast.makeText(SquareAddActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                        Log.d("testRun", "请求失败------Exception:" + e.getMessage());
                        pd.dismiss();
                        Toast.makeText(SquareAddActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).start();
    }
}
