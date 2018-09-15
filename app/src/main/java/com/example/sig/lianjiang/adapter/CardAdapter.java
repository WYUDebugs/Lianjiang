package com.example.sig.lianjiang.adapter;

/**
 * Created by sig on 2018/9/14.
 */

import android.support.v7.widget.CardView;

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
