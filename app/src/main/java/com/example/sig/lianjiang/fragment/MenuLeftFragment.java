package com.example.sig.lianjiang.fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.activity.EditUserProfileActivity;
import com.example.sig.lianjiang.activity.LoginActivitysteup1;
import com.example.sig.lianjiang.activity.MainActivity;
import com.example.sig.lianjiang.activity.UserProfileActivity;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.event.HideButtonEvent;
import com.example.sig.lianjiang.event.ShowButtonEvent;
import com.example.sig.lianjiang.leftmenu.MenuFragment;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.view.CircleImageView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by sig on 2018/7/23.
 */

public class MenuLeftFragment extends MenuFragment implements View.OnClickListener{
    private UserResultDto resultDto;
    private CircleImageView head;
    private LinearLayout loginOut;
    private LinearLayout setUser;
    public static MenuLeftFragment newInstance() {
        Bundle args = new Bundle();

        MenuLeftFragment fragment = new MenuLeftFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private View leftMenu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        leftMenu = inflater.inflate(R.layout.fragment_leftmenu, container, false);
        init();
        return setupReveal(leftMenu);           /**坑点**/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        getHead();
    }
    private void init(){
        head = (CircleImageView) leftMenu.findViewById(R.id.head);
        loginOut=(LinearLayout)leftMenu.findViewById(R.id.login_out);
        setUser=(LinearLayout)leftMenu.findViewById(R.id.setUser);
        loginOut.setOnClickListener(this);
        head.setOnClickListener(this);
        setUser.setOnClickListener(this);
        getHead();
    }

    @Override
    public void onOpenMenu() {
        super.onOpenMenu();
        ShowButtonEvent showButtonEvent = new ShowButtonEvent();
        showButtonEvent.setShow(true);
        EventBus.getDefault().post(showButtonEvent);
    }

    @Override
    public void onCloseMenu() {
        super.onCloseMenu();
        HideButtonEvent hideButtonEvent = new HideButtonEvent();
        hideButtonEvent.setHide(true);
        EventBus.getDefault().post(hideButtonEvent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_out:
                logout();
                break;
            case R.id.head:
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtra("username",EMClient.getInstance().getCurrentUser());
                startActivity(intent);
                break;
            case R.id.setUser:
                Intent intent1=new Intent(getContext(), EditUserProfileActivity.class);
                startActivity(intent1);
                break;

        }
    }

    public void getHead() {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", EMClient.getInstance().getCurrentUser());
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
                            Picasso.with(getContext() ).load(APPConfig.img_url + resultDto.getData().getHeadimage())
                                    .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);


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
}

