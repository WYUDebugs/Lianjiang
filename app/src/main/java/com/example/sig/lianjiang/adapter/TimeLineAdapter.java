package com.example.sig.lianjiang.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.activity.ChatActivity;
import com.example.sig.lianjiang.activity.UserProfileActivity;
import com.example.sig.lianjiang.bean.ResultDto;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.TimeLineModel;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.view.NineGridTestLayout;
import com.example.sig.lianjiang.view.TimeLineMarker;
import com.flyco.roundview.RoundTextView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sig.lianjiang.utils.TimeUtil.beforeToday;
import static com.example.sig.lianjiang.utils.TimeUtil.stampToDate;

/**
 * Created by sig on 2018/10/3.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder>{
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private LayoutInflater inflater;
    private Context context;
    private List<TimeLineModel> list;
    private UserResultDto resultDto;
    private ResultDto resultDto1;
    private String userId;
    private ProgressDialog progressDialog;
    public TimeLineAdapter(List<TimeLineModel> list,Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    private View mHeaderView;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView!=null&&viewType == TYPE_HEADER){
            View inflate = inflater.inflate(R.layout.userprofile_head, parent, false);
            CircleImageView head=inflate.findViewById(R.id.avatar);
            final TextView name=inflate.findViewById(R.id.nickname);
            ImageView sex=inflate.findViewById(R.id.sex);
            TextView sig=inflate.findViewById(R.id.signature);
            TextView square=inflate.findViewById(R.id.square);
            TextView memory=inflate.findViewById(R.id.memory);
            RoundTextView sendMsg=inflate.findViewById(R.id.attention);
            sendMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId", userId));
                }
            });
            RoundTextView addFriend=inflate.findViewById(R.id.addFriend);
            addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addContact(userId,name);
                }
            });
            getUser(head,name,sex,sig);
            getSquareNum(square);
            getMemoryBookNum(memory);
            return new ViewHolder(inflate);
        }
        View inflate = inflater.inflate(R.layout.item_time_line, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position>0){
            TimeLineModel timeLineModel=list.get(position-1);
            if(holder.content.equals("")){
                holder.content.setVisibility(View.GONE);
            }else {
                holder.content.setVisibility(View.VISIBLE);
                holder.content.setText(timeLineModel.mContent);
            }
            holder.layout.setIsShowAll(false);
            holder.layout.setUrlList(timeLineModel.urlList);
            holder.time.setText(stampToDate(timeLineModel.time,"yyyy-MM-dd"));
//            if(beforeToday(timeLineModel.time)){
//                holder.time.setText(stampToDate(timeLineModel.time,"yyyy-MM-dd"));
//                Log.d("shijian",stampToDate(timeLineModel.time,"yyyy-MM-dd HH:mm:ss"));
//            }else{
//                holder.time.setText(stampToDate(timeLineModel.time,"HH:mm"));
//                Log.d("shijian",stampToDate(timeLineModel.time,"yyyy-MM-dd HH:mm:ss"));
//            }
            if(timeLineModel.flag){
                holder.delete.setVisibility(View.VISIBLE);
            }else {
                holder.delete.setVisibility(View.GONE);
            }
            if (position==1){
                holder.marker.setBeginLine(null);
            }
            if(position==list.size()){
                holder.marker.setEndLine(null);
            }
            Resources resources = context.getResources();
            Drawable drawable = resources.getDrawable(R.mipmap.has_pic);
            Drawable drawable1 = resources.getDrawable(R.mipmap.only_word);
            if (timeLineModel.urlList.size()==0){
                holder.marker.setMarkerDrawable(drawable1);
            }else {
                holder.marker.setMarkerDrawable(drawable);
            }
        }

    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }
    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }
    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public void setUserId(String userId){
        this.userId=userId;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView delete;
        NineGridTestLayout layout;
        TextView time;
        TimeLineMarker marker;
        public ViewHolder(View itemView) {
            super(itemView);
            content=itemView.findViewById(R.id.tv_content);
            delete=itemView.findViewById(R.id.tv_delete);
            layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);
            time=itemView.findViewById(R.id.tv_time);
            marker=(TimeLineMarker)itemView.findViewById(R.id.item_time_line_mark);
        }


    }

    public void addContact(final String id,final TextView name){
        if(EMClient.getInstance().getCurrentUser().equals(id)){
//            new EaseAlertDialog(context, R.string.not_add_myself).show();
            Toast.makeText(context,"不能添加自己",Toast.LENGTH_SHORT).show();
            return;
        }

        if(StarryHelper.getInstance().getContactList().containsKey(id)){
            //let the user know the contact already in your contact list
            if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(name.getText().toString())){
//                new EaseAlertDialog(context, R.string.user_already_in_contactlist).show();
                Toast.makeText(context,"此用户已是你好友（被拉黑状态）",Toast.LENGTH_SHORT).show();
                return;
            }
//            new EaseAlertDialog(context, R.string.This_user_is_already_your_friend).show();
            Toast.makeText(context,"此用户已是你好友",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(context);
        String stri = context.getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = context.getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(id, s);
                    progressDialog.dismiss();
                    String s1 = context.getResources().getString(R.string.send_successful);
                    Toast.makeText(context, s1, Toast.LENGTH_LONG).show();
                } catch (final Exception e) {
                    progressDialog.dismiss();
                    String s2 = context.getResources().getString(R.string.Request_add_buddy_failure);
                    Toast.makeText(context, s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }

    public void getUser(final CircleImageView head, final TextView name,final ImageView sex,final TextView sig) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", userId);
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
                            Toast.makeText(context,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            Picasso.with(context).load(APPConfig.img_url + resultDto.getData().getHeadimage())
                                    .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
                            name.setText(resultDto.getData().getName());
                            int sexId=resultDto.getData().getGender();
                            if (sexId == 1) {
                                sex.setImageResource(R.mipmap.icon_female);
                            }
                            sig.setText(resultDto.getData().getSignature());
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(context, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }
    public void getSquareNum( final TextView square) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("uId", userId);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.publishNum, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto1 = OkHttpUtils.getObjectFromJson(response.toString(), ResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto1 = ResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(context,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto1.getData()!=null){
                            String number=resultDto1.getData().toString();
                            String intNumber = number.substring(0,number.indexOf("."));
                            square.setText("动态 "+intNumber);
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(context, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }
    public void getMemoryBookNum(final TextView memoryBook) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("uId", userId);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.bookNum, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto1 = OkHttpUtils.getObjectFromJson(response.toString(), ResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto1 = ResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(context,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto1.getData()!=null){
                            String number=resultDto1.getData().toString();
                            String intNumber = number.substring(0,number.indexOf("."));
                            memoryBook.setText("相册 "+intNumber);
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(context, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }
}
