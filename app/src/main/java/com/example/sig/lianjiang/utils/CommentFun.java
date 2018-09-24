package com.example.sig.lianjiang.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;

import com.example.sig.lianjiang.activity.SquareActivity;
import com.example.sig.lianjiang.bean.PublicComment;
import com.example.sig.lianjiang.bean.PublicCommentResultDto;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.Comment;
import com.example.sig.lianjiang.model.CommentUser;
import com.hyphenate.chat.EMClient;
import com.sch.rfview.AnimRFRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sig on 2018/8/31.
 */

public class CommentFun {
    public static final int KEY_COMMENT_SOURCE_COMMENT_LIST = -200162;
    public static final int KEY_PUBLISHID_LIST = -2018923;
    public static String content="";
    public static PublicCommentResultDto publicCommentResultDto;

    /**
     * 在页面中显示评论列表
     *
     * @param context
     * @param mCommentList
     * @param commentList
     * @param tagHandler
     */
    public static void parseCommentList(Context context, List<Comment> mCommentList,String id, LinearLayout commentList,View btnComment,
                                         Html.TagHandler tagHandler) {
        if (btnComment != null) {
            btnComment.setTag(KEY_COMMENT_SOURCE_COMMENT_LIST, mCommentList);
            Log.e("comment",id);
            btnComment.setTag(KEY_PUBLISHID_LIST, id);
        }
        TextView textView;
        Comment comment;
        int i;
        String content;
        for (i = 0; i < mCommentList.size(); i++) {
            comment = mCommentList.get(i);
            textView = (TextView) commentList.getChildAt(i);
            if (textView == null) { // 创建评论Videw
                textView = (TextView) View.inflate(context, R.layout.view_comment_list_item, null);
                commentList.addView(textView);
            }
            textView.setVisibility(View.VISIBLE);
            if (comment.mReceiver == null) { // 没有评论接受者
                content = String.format("<html><%s>%s</%s>: <%s>%s</%s></html>", CustomTagHandler.TAG_COMMENTATOR,
                        comment.mCommentator.mName, CustomTagHandler.TAG_COMMENTATOR,
                        CustomTagHandler.TAG_CONTENT, comment.mContent, CustomTagHandler.TAG_CONTENT);
            } else {
                content = String.format("<html><%s>%s</%s> 回复 <%s>%s</%s>: <%s>%s</%s><html>",
                        CustomTagHandler.TAG_COMMENTATOR, comment.mCommentator.mName, CustomTagHandler.TAG_COMMENTATOR,
                        CustomTagHandler.TAG_RECEIVER, comment.mReceiver.mName, CustomTagHandler.TAG_RECEIVER,
                        CustomTagHandler.TAG_CONTENT, comment.mContent, CustomTagHandler.TAG_CONTENT);
            }
            textView.setText(Html.fromHtml(content, null, tagHandler)); // 解析标签
            textView.setClickable(true);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setTag(CustomTagHandler.KEY_COMMENTATOR, comment.mCommentator);
            textView.setTag(CustomTagHandler.KEY_RECEIVER, comment.mReceiver);

            textView.setTag(KEY_COMMENT_SOURCE_COMMENT_LIST, mCommentList);
        }
        for (; i < commentList.getChildCount(); i++) {
            commentList.getChildAt(i).setVisibility(View.GONE);
        }
        if (mCommentList.size() > 0) {
            commentList.setVisibility(View.VISIBLE);
        } else {
            commentList.setVisibility(View.GONE);
        }
    }


    /**
     * 弹出评论对话框
     */
    public static void inputComment(final Activity activity, final AnimRFRecyclerView recyclerView,
                                    final View btnComment, final CommentUser receiver,
                                    final InputCommentListener listener) {

        final List<Comment> commentList = (List) btnComment.getTag(KEY_COMMENT_SOURCE_COMMENT_LIST);
        final String id=(String)btnComment.getTag(KEY_PUBLISHID_LIST);
        Log.e("comment","弹出"+id);


        String hint;
        if (receiver != null) {
            if (receiver.mId == SquareActivity.sUser.mId) {
                hint = "我也说一句";
            } else {
                hint = "回复 " + receiver.mName;
            }
        } else {
            hint = "我也说一句";
        }
        // 获取评论的位置,不要在CommentDialogListener.onShow()中获取，onShow在输入法弹出后才调用，
        // 此时btnComment所属的父布局可能已经被ListView回收
        final int[] coord = new int[2];
        if (recyclerView != null) {
            btnComment.getLocationOnScreen(coord);
        }

        //弹出评论对话框
        showInputComment(activity, hint, new CommentDialogListener() {
            @Override
            public void onClickPublish(final Dialog dialog, EditText input, final TextView btn) {
                content = input.getText().toString();
                if (content.trim().equals("")) {
                    Toast.makeText(activity, "评论不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                btn.setClickable(false);
                final long receiverId = receiver == null ? -1 : receiver.mId;
                Comment comment = new Comment(SquareActivity.sUser, content, receiver);
                if(comment==null){
                    Log.e("111","kong");

                }else{
                    Log.e("111",comment.mContent);
                    addCommentPost(Integer.toString(SquareActivity.sUser.mId),"0",id,content);
                }
                commentList.add(comment);
                if (listener != null) {
                    listener.onCommitComment();
                }
                dialog.dismiss();
                Toast.makeText(activity, "评论成功", Toast.LENGTH_SHORT).show();
            }

            /**
             * @see CommentDialogListener
             * @param inputViewCoordinatesInScreen [left,top]
             */
            @Override
            public void onShow(int[] inputViewCoordinatesInScreen) {
                if (recyclerView != null) {
                    // 点击某条评论则这条评论刚好在输入框上面，点击评论按钮则输入框刚好挡住按钮
                    int span = btnComment.getId() == R.id.img_input_comment ? 0 : btnComment.getHeight();
//                    listView.smoothScrollBy(coord[1] + span - inputViewCoordinatesInScreen[1], 200);
                }
            }

            @Override
            public void onDismiss() {

            }
        });

    }

    public static class InputCommentListener {
        //　评论成功时调用
        public void onCommitComment() {

        }
    }


    /**
     * 弹出评论对话框
     */
    private static Dialog showInputComment(Activity activity, CharSequence hint, final CommentDialogListener listener) {
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.view_input_comment);
        dialog.findViewById(R.id.input_comment_dialog_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onDismiss();
                }
            }
        });
        final EditText input = (EditText) dialog.findViewById(R.id.input_comment);
        input.setHint(hint);
        final TextView btn = (TextView) dialog.findViewById(R.id.btn_publish_comment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickPublish(dialog, input, btn);
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    int[] coord = new int[2];
                    dialog.findViewById(R.id.input_comment_container).getLocationOnScreen(coord);
                    // 传入 输入框距离屏幕顶部（不包括状态栏）的位置
                    listener.onShow(coord);
                }
            }
        }, 300);
        return dialog;
    }

    /**
     * 评论对话框相关监听
     */
    public interface CommentDialogListener {
        void onClickPublish(Dialog dialog, EditText input, TextView btn);

        /**
         * onShow在输入法弹出后才调用
         * @param inputViewCoordinatesOnScreen 输入框距离屏幕顶部（不包括状态栏）的位置[left,top]
         */
        void onShow(int[] inputViewCoordinatesOnScreen);

        void onDismiss();
    }

    public static  void addComment(final List<PublicComment> comments, final View btnComment, final addCommentListener listener){
        final List<Comment> commentList = (List) btnComment.getTag(KEY_COMMENT_SOURCE_COMMENT_LIST);

        for(int i=0;i<comments.size();i++){

//            Comment comment = new Comment(comments.get(i).getCommentator(), comments.get(i).getCommentContent(), comments.get(i).getReceiveId());
        }

    }

    public static class addCommentListener {
        //　评论成功时调用
        public void onCommitComment() {

        }
    }

    public static void addCommentPost(final String cId,final String rId,final String pId,final String cContent) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param pidParam = new OkHttpUtils.Param("pId", pId);
        OkHttpUtils.Param cidParam = new OkHttpUtils.Param("cId", cId);
        OkHttpUtils.Param contentParam = new OkHttpUtils.Param("cContent", cContent);
//        OkHttpUtils.Param ridParam = new OkHttpUtils.Param("rId", "0");

        list.add(pidParam);
        list.add(cidParam);
        list.add(contentParam);
//        list.add(ridParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.addComment, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            publicCommentResultDto = OkHttpUtils.getObjectFromJson(response.toString(), PublicCommentResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            publicCommentResultDto = PublicCommentResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(publicCommentResultDto.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
//                            initListData(publicListResultDto.getData());
//                            mAdapter.notifyDataSetChanged();
                            Log.e("zxd","评论成功");

                        }else {
                            Log.e("zxd","失败");
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                    }
                }, list);
            }

        }).start();
    }
}
