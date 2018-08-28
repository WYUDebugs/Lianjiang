package com.example.sig.lianjiang.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.activity.UserProfileActivity;
import com.example.sig.lianjiang.event.HideButtonEvent;
import com.example.sig.lianjiang.event.ShowButtonEvent;
import com.example.sig.lianjiang.leftmenu.MenuFragment;
import com.example.sig.lianjiang.view.CircleImageView;

import de.greenrobot.event.EventBus;

/**
 * Created by sig on 2018/7/23.
 */

public class MenuLeftFragment extends MenuFragment {

    private CircleImageView head;
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
}

