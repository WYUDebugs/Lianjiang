package com.example.sig.lianjiang.adapter;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dalong.zwlviewpager.PagerAdapter;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.bean.ShowItem;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by sig on 2018/9/14.
 */

public class CardPagerAdapter extends PagerAdapter implements CardAdapter{
    private List<CardView> mViews;
    public Context mContext;

    public List<ShowItem> mData;
    private float mBaseElevation;
    public CardPagerAdapter(Context context, List<ShowItem> list) {
        mContext = context;
        mViews = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mViews.add(null);
        }
        this.mData = list;
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_fancycoverflow, container, false);
        container.addView(view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
//        CircleImageView mIcon=(CircleImageView)view.findViewById(R.id.profile_image);
        LinearLayout picture=(LinearLayout)view.findViewById(R.id.picture) ;
        TextView mTip= (TextView) view.findViewById(R.id.tip);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)mOnItemClickListener.onClick(position);
            }
        });
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);

        final ShowItem item=mData.get(position);
        picture.setBackgroundResource(item.getImg());
        mTip.setText(item.getTip());
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }
    OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }
    public interface OnItemClickListener{
        void onClick(int position);
    }
}
