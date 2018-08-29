package com.example.sig.lianjiang.fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.activity.LoginActivitysteup1;
import com.example.sig.lianjiang.activity.MainActivity;
import com.example.sig.lianjiang.activity.UserProfileActivity;
import com.example.sig.lianjiang.event.HideButtonEvent;
import com.example.sig.lianjiang.event.ShowButtonEvent;
import com.example.sig.lianjiang.leftmenu.MenuFragment;
import com.example.sig.lianjiang.view.CircleImageView;
import com.hyphenate.EMCallBack;

import de.greenrobot.event.EventBus;

/**
 * Created by sig on 2018/7/23.
 */

public class MenuLeftFragment extends MenuFragment implements View.OnClickListener{

    private CircleImageView head;
    private LinearLayout loginOut;
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

    private void init(){
        head = (CircleImageView) leftMenu.findViewById(R.id.head);
        loginOut=(LinearLayout)leftMenu.findViewById(R.id.login_out);
        loginOut.setOnClickListener(this);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        });
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
    void logout() {
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
                        Toast.makeText(getActivity(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
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

        }
    }
}

