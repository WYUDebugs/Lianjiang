package com.example.sig.lianjiang.view;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



import com.example.sig.lianjiang.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;

/**
 * Created by sig on 2018/7/26.
 */

public class NineGridTestLayout extends NineGridLayout {

    protected static final int MAX_W_H_RATIO = 3;

    public NineGridTestLayout(Context context) {
        super(context);
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {

        ImageLoaderUtil.displayImage(mContext, imageView, url, ImageLoaderUtil.getPhotoImageOption(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                int newW;
                int newH;
                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                setOneImageLayoutParams(imageView, newW, newH);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        ImageLoaderUtil.getImageLoader(mContext).displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
    }
    @Override
    protected void onClickImage(int i, String url, List<String> urlList) {
//        Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
        ImageInfo imageInfo;
        final List<ImageInfo> imageInfoList = new ArrayList<>();
        for (int j=0;j<urlList.size();j++) {
            imageInfo = new ImageInfo();
            imageInfo.setOriginUrl(urlList.get(j));// 原图
            imageInfo.setThumbnailUrl(urlList.get(j));// 缩略图，实际使用中，根据需求传入缩略图路径。如果没有缩略图url，可以将两项设置为一样，并隐藏查看原图按钮即可。
            imageInfoList.add(imageInfo);
//            imageInfo = null;
            Log.d("zxd1111",urlList.get(j));
        }
        ImagePreview
                .getInstance()
                .setContext(getContext())
                .setIndex(0)
                .setImageInfoList(imageInfoList)
                .setShowDownButton(true)
                .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
                .setFolderName("StarryDownload")
                .setScaleLevel(1, 3, 8)
                .setZoomTransitionDuration(300)
                .setShowCloseButton(true)
                .start();
//        ImagePreview
//                .getInstance()
//                .setContext(getContext())
//                .setIndex(0)
//                .setImageInfoList(imageInfoList)
//                .setShowDownButton(true)
//                .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysThumb)
//                .setFolderName("BigImageViewDownload")
//                .setScaleLevel(1, 3, 8)
//                .setZoomTransitionDuration(300)
//                .setShowCloseButton(false)
//                .start();
    }
}

