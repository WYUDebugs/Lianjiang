package com.example.sig.lianjiang.adapter;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sig on 2018/8/23.
 */

public class UserProfile {
    private int imageView;
    private String textView;
    private boolean flag;
    public UserProfile(int imageView,String textView,boolean flag){
        this.imageView=imageView;
        this.textView=textView;
        this.flag=flag;
    }
    public int getImageView() {
        return imageView;
    }

    public String getTextView() {
        return textView;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public void setTextView(String textView) {
        this.textView = textView;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
