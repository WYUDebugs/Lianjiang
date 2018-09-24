package com.example.sig.lianjiang.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.activity.SquareActivity;
import com.example.sig.lianjiang.activity.UserProfileActivity;
import com.example.sig.lianjiang.bean.PublicCommentResultDto;
import com.example.sig.lianjiang.bean.PublicGoodResultDto;
import com.example.sig.lianjiang.bean.PublicListResultDto;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.Comment;
import com.example.sig.lianjiang.view.PraiseTextView;

import java.util.ArrayList;
import java.util.List;

import czh.library.LikeView;

/**
 * Created by sig on 2018/9/20.
 */

public class GoodFun {
    public static final int KEY_GOOD_LIST = -2018921;
    public static final int KEY_PUBLISHID_LIST = -2018922;
    public static PublicGoodResultDto publicGoodResultDto;

    /**
     * 在页面中显示评论列表
     *
     * @param context
     * @param mPraiseInfos
     * @param mPraiseTextView
     * @param lv_good
     */
    public static void parseGoodList(final Context context, List<PraiseTextView.PraiseInfo> mPraiseInfos, String id, PraiseTextView mPraiseTextView, View lv_good, ImageView iv_like) {
        if (lv_good != null) {
            lv_good.setTag(KEY_GOOD_LIST, mPraiseInfos);
            lv_good.setTag(KEY_PUBLISHID_LIST,id);
        }
        if(mPraiseInfos.size()==0){
            mPraiseTextView.setVisibility(View.GONE);
            iv_like.setVisibility(View.GONE);
        }else {
            mPraiseTextView.setVisibility(View.VISIBLE);
            mPraiseTextView.setNameTextColor(0xff0099cc);
            mPraiseTextView.setData (mPraiseInfos);
            mPraiseTextView.setMiddleStr ("、");
            mPraiseTextView.setIconSize(new Rect());
            iv_like.setVisibility(View.VISIBLE);
            mPraiseTextView.setonPraiseListener (new PraiseTextView.onPraiseClickListener () {
                @Override
                public void onClick (final int position, final PraiseTextView.PraiseInfo mPraiseInfo) {
//                    mTextView.append ("position = [" + position + "], mPraiseInfo = [" + mPraiseInfo + "]"+"\r\n");
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra("username",Integer.toString(mPraiseInfo.getId()));
                    context.startActivity(intent);
                }

                @Override
                public void onOtherClick () {

                }
            });
        }

    }

    public static  void addGood(List<PraiseTextView.PraiseInfo> mPraiseInfos,  final View lv_good, final addGoodListener listener){
        List<PraiseTextView.PraiseInfo> mPraiseInfos1 = (List) lv_good.getTag(KEY_GOOD_LIST);

        mPraiseInfos1.addAll(mPraiseInfos);
        listener.onCommitComment();


    }

    public static class addGoodListener {
        //　评论成功时调用
        public void onCommitComment() {

        }
    }

    public static  void addGood(final Context context,final View lv_good,final PraiseTextView mPraiseTextView,boolean flag,ImageView iv_like){
        List<PraiseTextView.PraiseInfo> mPraiseInfos1 = (List) lv_good.getTag(KEY_GOOD_LIST);

//        List<PraiseTextView.PraiseInfo> mPraiseInfos = new ArrayList<> ();

//        mPraiseInfos.add (new PraiseTextView.PraiseInfo ().setId (SquareActivity.sUser.mId).setNickname (SquareActivity.sUser.mName));



        String id=lv_good.getTag(KEY_PUBLISHID_LIST).toString();

        addGoodPost(context,id,mPraiseInfos1,flag,mPraiseTextView,(LikeView) lv_good,iv_like);



    }


    public static void addGoodPost(final Context context,final String pId,final List<PraiseTextView.PraiseInfo> mPraiseInfos1,final boolean flag,final PraiseTextView mPraiseTextView,final LikeView lv_good,final ImageView iv_like) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param pidParam = new OkHttpUtils.Param("pId", pId);
        OkHttpUtils.Param midParam = new OkHttpUtils.Param("mId", Integer.toString(SquareActivity.sUser.mId));
        list.add(pidParam);
        list.add(midParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.addGood, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            publicGoodResultDto = OkHttpUtils.getObjectFromJson(response.toString(), PublicGoodResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            publicGoodResultDto = PublicGoodResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(publicGoodResultDto.getMsg().equals("success")){
                            if(flag){
                                mPraiseInfos1.add(new PraiseTextView.PraiseInfo ().setId (SquareActivity.sUser.mId).setNickname (SquareActivity.sUser.mName));
                                lv_good.setTag(KEY_GOOD_LIST, mPraiseInfos1);
                            }else{
                                for(int i=0;i<mPraiseInfos1.size();i++){
                                    if(mPraiseInfos1.get(i).getId()==SquareActivity.sUser.mId){
                                        mPraiseInfos1.remove(i);
                                    }
                                }
                                lv_good.setTag(KEY_GOOD_LIST, mPraiseInfos1);
                            }
                            if(mPraiseInfos1.size()!=0){
                                mPraiseTextView.setVisibility(View.VISIBLE);
                                iv_like.setVisibility(View.VISIBLE);
                                mPraiseTextView.setNameTextColor(0xff0099cc);
                                mPraiseTextView.setData(mPraiseInfos1);
                                mPraiseTextView.setMiddleStr ("、");
                                mPraiseTextView.setIconSize(new Rect());
                                mPraiseTextView.setonPraiseListener (new PraiseTextView.onPraiseClickListener () {
                                    @Override
                                    public void onClick (final int position, final PraiseTextView.PraiseInfo mPraiseInfo) {
                                        Intent intent = new Intent(context, UserProfileActivity.class);
                                        intent.putExtra("username",Integer.toString(mPraiseInfo.getId()));
                                        context.startActivity(intent);
                                    }

                                    @Override
                                    public void onOtherClick () {

                                    }
                                });
                            }else {
                                mPraiseTextView.setVisibility(View.GONE);
                                iv_like.setVisibility(View.GONE);
                            }
                            Log.d("zxd","点赞成功");
                        }else {
                            if (lv_good.isChecked()) {
                                lv_good.selectLike(false);
                            } else {
                                lv_good.selectLike(true);
                            };
                            Log.d("zxd","点赞失败");
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (lv_good.isChecked()) {
                            lv_good.selectLike(false);
                        } else {
                            lv_good.selectLike(true);
                        };
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                    }
                }, list);
            }

        }).start();
    }

}
