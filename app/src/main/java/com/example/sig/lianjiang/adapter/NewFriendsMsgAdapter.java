package com.example.sig.lianjiang.adapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.activity.AddContactActivity;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.hyphenate.chat.EMClient;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.db.InviteMessgeDao;
import com.example.sig.lianjiang.domain.InviteMessage;
import com.example.sig.lianjiang.domain.InviteMessage.InviteMessageStatus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by sig on 2018/8/8.
 */

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

    private Context context;
    private InviteMessgeDao messgeDao;
    private UserResultDto resultDto;

    public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        messgeDao = new InviteMessgeDao(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.em_row_invite_msg, null);
            holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.agreeBtn = (Button) convertView.findViewById(R.id.agree);
            holder.refuseBtn = (Button) convertView.findViewById(R.id.refuse);
            // holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final InviteMessage msg = getItem(position);
        if (msg != null) {
            holder.agreeBtn.setVisibility(View.GONE);
            holder.refuseBtn.setVisibility(View.GONE);


            holder.message.setText(msg.getReason());
            holder.name.setText(msg.getFrom());
            getHead(holder.avator,holder.name,msg.getFrom());
            // holder.time.setText(DateUtils.getTimestampString(new
            // Date(msg.getTime())));
            if (msg.getStatus() == InviteMessageStatus.BEAGREED) {
                holder.agreeBtn.setVisibility(View.GONE);
                holder.refuseBtn.setVisibility(View.GONE);
                holder.message.setText(context.getResources().getString(R.string.Has_agreed_to_your_friend_request));
            } else if (msg.getStatus() == InviteMessageStatus.BEINVITEED || msg.getStatus() == InviteMessageStatus.BEAPPLYED ||
                    msg.getStatus() == InviteMessageStatus.GROUPINVITATION) {
                holder.agreeBtn.setVisibility(View.VISIBLE);
                holder.refuseBtn.setVisibility(View.VISIBLE);
                if(msg.getStatus() == InviteMessageStatus.BEINVITEED){
                    if (msg.getReason() == null) {
                        // use default text
                        holder.message.setText(context.getResources().getString(R.string.Request_to_add_you_as_a_friend));
                    }
                }else if (msg.getStatus() == InviteMessageStatus.BEAPPLYED) { //application to join group
                    if (TextUtils.isEmpty(msg.getReason())) {
                        holder.message.setText(context.getResources().getString(R.string.Apply_to_the_group_of) + msg.getGroupName());
                    }
                } else if (msg.getStatus() == InviteMessageStatus.GROUPINVITATION) {
                    if (TextUtils.isEmpty(msg.getReason())) {
                        holder.message.setText(context.getResources().getString(R.string.invite_join_group) + msg.getGroupName());
                    }
                }

                // set click listener
                holder.agreeBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // accept invitation
                        acceptInvitation(holder.agreeBtn, holder.refuseBtn, msg);
                    }
                });
                holder.refuseBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // decline invitation
                        refuseInvitation(holder.agreeBtn, holder.refuseBtn, msg);
                    }
                });
            } else {
                String str = "";
                InviteMessageStatus status = msg.getStatus();
                switch (status) {
                    case AGREED:
                        str = context.getResources().getString(R.string.Has_agreed_to);
                        break;
                    case REFUSED:
                        str = context.getResources().getString(R.string.Has_refused_to);
                        break;
                    case GROUPINVITATION_ACCEPTED:
                        str = context.getResources().getString(R.string.accept_join_group);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case GROUPINVITATION_DECLINED:
                        str = context.getResources().getString(R.string.refuse_join_group);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_CONTACT_ADD:
                        str = context.getResources().getString(R.string.multi_device_contact_add);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_CONTACT_BAN:
                        str = context.getResources().getString(R.string.multi_device_contact_ban);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_CONTACT_ALLOW:
                        str = context.getResources().getString(R.string.multi_device_contact_allow);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_CONTACT_ACCEPT:
                        str = context.getResources().getString(R.string.multi_device_contact_accept);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_CONTACT_DECLINE:
                        str = context.getResources().getString(R.string.multi_device_contact_decline);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_GROUP_CREATE:
                        str = context.getResources().getString(R.string.multi_device_group_create);
                        break;
                    case MULTI_DEVICE_GROUP_DESTROY:
                        str = context.getResources().getString(R.string.multi_device_group_destroy);
                        break;
                    case MULTI_DEVICE_GROUP_JOIN:
                        str = context.getResources().getString(R.string.multi_device_group_join);
                        break;
                    case MULTI_DEVICE_GROUP_LEAVE:
                        str = context.getResources().getString(R.string.multi_device_group_leave);
                        break;
                    case MULTI_DEVICE_GROUP_APPLY:
                        str = context.getResources().getString(R.string.multi_device_group_apply);
                        break;
                    case MULTI_DEVICE_GROUP_APPLY_ACCEPT:
                        str = context.getResources().getString(R.string.multi_device_group_apply_accept);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_APPLY_DECLINE:
                        str = context.getResources().getString(R.string.multi_device_group_apply_decline);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_INVITE:
                        str = context.getResources().getString(R.string.multi_device_group_invite);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_INVITE_ACCEPT:
                        str = context.getResources().getString(R.string.multi_device_group_invite_accept);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_INVITE_DECLINE:
                        str = context.getResources().getString(R.string.multi_device_group_invite_decline);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_KICK:
                        str = context.getResources().getString(R.string.multi_device_group_kick);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_BAN:
                        str = context.getResources().getString(R.string.multi_device_group_ban);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_ALLOW:
                        str = context.getResources().getString(R.string.multi_device_group_allow);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_BLOCK:
                        str = context.getResources().getString(R.string.multi_device_group_block);
                        break;
                    case MULTI_DEVICE_GROUP_UNBLOCK:
                        str = context.getResources().getString(R.string.multi_device_group_unblock);
                        break;
                    case MULTI_DEVICE_GROUP_ASSIGN_OWNER:
                        str = context.getResources().getString(R.string.multi_device_group_assign_owner);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_ADD_ADMIN:
                        str = context.getResources().getString(R.string.multi_device_group_add_admin);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_REMOVE_ADMIN:
                        str = context.getResources().getString(R.string.multi_device_group_remove_admin);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_ADD_MUTE:
                        str = context.getResources().getString(R.string.multi_device_group_add_mute);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_REMOVE_MUTE:
                        str = context.getResources().getString(R.string.multi_device_group_remove_mute);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    default:
                        break;
                }
                holder.message.setText(str);
            }
        }

        return convertView;
    }

    /**
     * accept invitation
     *
     * @param buttonAgree
     * @param buttonRefuse
     * @param msg
     */
    private void acceptInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources().getString(R.string.Are_agree_with);
        final String str2 = context.getResources().getString(R.string.Has_agreed_to);
        final String str3 = context.getResources().getString(R.string.Agree_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // call api
                try {
                    if (msg.getStatus() == InviteMessageStatus.BEINVITEED) {//accept be friends
                        EMClient.getInstance().contactManager().acceptInvitation(msg.getFrom());//环信后台
                        addFriendPost(EMClient.getInstance().getCurrentUser(),msg.getFrom());//自家后台
                    } else if (msg.getStatus() == InviteMessageStatus.BEAPPLYED) { //accept application to join group
                        EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
                    } else if (msg.getStatus() == InviteMessageStatus.GROUPINVITATION) {
                        EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getGroupInviter());
                    }
                    msg.setStatus(InviteMessageStatus.AGREED);
                    // update database
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonAgree.setText(str2);
                            buttonAgree.setBackgroundDrawable(null);
                            buttonAgree.setEnabled(false);

                            buttonRefuse.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        }).start();
    }

    public void addFriendPost(final String userId,final String friendId) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param userIdParam = new OkHttpUtils.Param("userId", userId);
        OkHttpUtils.Param friendIdParam = new OkHttpUtils.Param("friendId", friendId);
        list.add(userIdParam);
        list.add(friendIdParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.addFriend, new OkHttpUtils.ResultCallback() {
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
                            Toast.makeText(getContext(),"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){


                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(getContext(), "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }

    /**
     * decline invitation
     *
     * @param buttonAgree
     * @param buttonRefuse
     * @param msg
     */
    private void refuseInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources().getString(R.string.Are_refuse_with);
        final String str2 = context.getResources().getString(R.string.Has_refused_to);
        final String str3 = context.getResources().getString(R.string.Refuse_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // call api
                try {
                    if (msg.getStatus() == InviteMessageStatus.BEINVITEED) {//decline the invitation
                        EMClient.getInstance().contactManager().declineInvitation(msg.getFrom());
                    } else if (msg.getStatus() == InviteMessageStatus.BEAPPLYED) { //decline application to join group
                        EMClient.getInstance().groupManager().declineApplication(msg.getFrom(), msg.getGroupId(), "");
                    } else if (msg.getStatus() == InviteMessageStatus.GROUPINVITATION) {
                        EMClient.getInstance().groupManager().declineInvitation(msg.getGroupId(), msg.getGroupInviter(), "");
                    }
                    msg.setStatus(InviteMessageStatus.REFUSED);
                    // update database
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonRefuse.setText(str2);
                            buttonRefuse.setBackgroundDrawable(null);
                            buttonRefuse.setEnabled(false);

                            buttonAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();
    }

    private static class ViewHolder {
        ImageView avator;
        TextView name;
        TextView message;
        Button agreeBtn;
        Button refuseBtn;
    }

    public void getHead(final ImageView head,final TextView name,final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", id);
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
                            Toast.makeText(getContext(),"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            Picasso.with(getContext()).load(APPConfig.img_url + resultDto.getData().getHeadimage())
                                    .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
                            name.setText(resultDto.getData().getName());

                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(getContext(), "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }
}
