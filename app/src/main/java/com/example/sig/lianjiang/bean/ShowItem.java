package com.example.sig.lianjiang.bean;
import java.io.Serializable;
/**
 * Created by sig on 2018/9/14.
 */

public class ShowItem implements Serializable{
    private int position;
    private int img;
    private String tip;
    private boolean isSelected;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
