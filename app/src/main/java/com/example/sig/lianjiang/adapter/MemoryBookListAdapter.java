package com.example.sig.lianjiang.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.activity.MemoryBookActivity;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.activity.MemoryBookListActivity;
import com.example.sig.lianjiang.activity.MemoryCoverUpdateActivity;
import com.example.sig.lianjiang.activity.MemoryDeleteContactsActivity;
import com.example.sig.lianjiang.activity.MemoryPickContactsActivity;
import com.example.sig.lianjiang.bean.MemoryBookListResult;
import com.example.sig.lianjiang.bean.MemoryBookResult;
import com.example.sig.lianjiang.bean.MemoryFriendListResult;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.MemoryListModel;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.hyphenate.chat.EMClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sig on 2018/7/26.
 */

public class MemoryBookListAdapter extends RecyclerView.Adapter<MemoryBookListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private MemoryBookResult memoryBookResult;
    private List<MemoryListModel> list;
    private Context context;
    private View mHeaderView;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int KEY_MEMOYID = -2018926;
    public static final int KEY_TITLE = -2018927;
    private MemoryBookListResult memoryBookListResult;

    public MemoryBookListAdapter(List<MemoryListModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView!=null&&viewType == TYPE_HEADER) {
            View inflate = inflater.inflate(R.layout.layout_memory_book_list_header, parent, false);
            inflate.findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog1;
                    View diaView = View.inflate(context, R.layout.dialogui_center_add, null);
                    dialog1 = new Dialog(context, R.style.dialog);
                    dialog1.setContentView(diaView);
                    dialog1.show();
                    TextView tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
                    TextView tvSure = (TextView) diaView.findViewById(R.id.tv_sure);
                    TextView tvCancel = (TextView) diaView.findViewById(R.id.tv_cancel);
                    final EditText etInput = (EditText) diaView.findViewById(R.id.et_input);
                    tvSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            dialog1.cancel();
//                            Intent intent = new Intent(context, MemoryBookActivity.class);
//                            context.startActivity(intent);
                            final String title=etInput.getText().toString().trim();
                            if(title.length()>14){
                                Toast.makeText(context,"纪念册名称过长",Toast.LENGTH_SHORT).show();
                            }else if(title.equals("")){
                                Toast.makeText(context,"纪念册名称不能为空",Toast.LENGTH_SHORT).show();
                            }else{
                                addMemoryBookPost(EMClient.getInstance().getCurrentUser(),title,dialog1);
                            }
                        }
                    });
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.cancel();
                        }
                    });
                }
            });
            return new ViewHolder(inflate);
        }
//        View inflate = View.inflate(R.layout.item_memory_book,parent, false);
        View inflate = inflater.inflate(R.layout.item_memory_book, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position>0){
//            holder.tvMemoryTitle.setText(list.get(position));
            MemoryListModel memoryListModel=list.get(position-1);
            Picasso.with(context).load(APPConfig.test_image_url + memoryListModel.cover)
                    .placeholder(R.mipmap.memory1).error(R.mipmap.memory1).into(holder.imgCover);
            holder.tvMemoryTitle.setText(memoryListModel.title);
            holder.tvMemoryPersionNum.setText(Integer.toString(memoryListModel.friendCount));
            holder.tvMemoryMomentNum.setText(Integer.toString(memoryListModel.momentCount));
            holder.imgCover.setTag(KEY_MEMOYID,memoryListModel.memoryBookId);
            holder.imgCover.setTag(KEY_TITLE,memoryListModel.title);
            holder.tvMemoryPersionNum.setTag(KEY_MEMOYID,memoryListModel.memoryBookId);

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
    public int getItemCount() {
        return list.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgCover;
        TextView tvMemoryTitle;
        TextView tvMemoryPersionNum;
        TextView tvMemoryMomentNum;


        public ViewHolder(View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_cover);
            tvMemoryTitle = (TextView) itemView.findViewById(R.id.tv_memory_title);
            tvMemoryPersionNum = (TextView) itemView.findViewById(R.id.tv_memory_persion_num);
            tvMemoryMomentNum = (TextView) itemView.findViewById(R.id.tv_memory_moment_num);
            if(imgCover!=null&&tvMemoryTitle!=null&&tvMemoryPersionNum!=null&&tvMemoryMomentNum!=null){
                imgCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,MemoryBookActivity.class);
                        intent.putExtra("memoryBookId",view.getTag(KEY_MEMOYID).toString());
                        context.startActivity(intent);
                    }
                });
                imgCover.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        longClickItem(view.getTag(KEY_MEMOYID).toString(),view.getTag(KEY_TITLE).toString());
                        return true;
                    }
                });
            }

        }
    }



    public void addMemoryBookPost(final String owner,final String title,final Dialog dialog) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        Integer a=null;
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("owner", owner);
        OkHttpUtils.Param titleParam = new OkHttpUtils.Param("title", title);
        list.add(idParam);
        list.add(titleParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.addMemoryBook, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryBookResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryBookResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryBookResult = MemoryBookResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryBookResult.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
//                            initListData(memoryBookListResult.getData());
//                            mAdapter.notifyDataSetChanged();
                            Log.e("zxd","添加纪念册成功");
                            Toast.makeText(context,"添加纪念册成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MemoryBookActivity.class);
                            context.startActivity(intent);
                            dialog.cancel();
                        }else {
                            Log.e("zxd","添加纪念册失败");
                            Toast.makeText(context,"添加纪念册失败",Toast.LENGTH_SHORT).show();
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

    public void longClickItem(final String memoryBookId,String title) {
        View diaView= View.inflate(context, R.layout.dialogui_footer_setting, null);
        final Dialog dialog=new Dialog(context,R.style.dialogfooter);
        diaView.setMinimumWidth(R.dimen.width);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setContentView(diaView);
        dialog.show();
        TextView tvInfo;
        ImageView imgCancel;
        LinearLayout llMemorySettingPeople;
        LinearLayout llMemorySettingName;
        LinearLayout llMemorySettingCover;
        LinearLayout llMemorySettingDel;

        tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
        tvInfo.setText("设置\""+title+"\"的信息");
        imgCancel = (ImageView) diaView.findViewById(R.id.img_cancel);
        llMemorySettingPeople = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_people);
        llMemorySettingName = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_name);
        llMemorySettingCover = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_cover);
        llMemorySettingDel = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_del);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        llMemorySettingPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(context, MemoryDeleteContactsActivity.class);
                intent.putExtra("memoryBookId",memoryBookId);
                Log.e("zxd","适配器"+memoryBookId);
                context.startActivity(intent);
//                ((Activity)context).startActivityForResult(new Intent(context, MemoryPickContactsActivity.class).putExtra("memoryBookId", memoryBookId), 0);
            }
        });
        llMemorySettingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                updateMemoryName(memoryBookId);
            }
        });
        llMemorySettingCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(context, MemoryCoverUpdateActivity.class);
                context.startActivity(intent);
            }
        });
        llMemorySettingDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                clickDelMemory(memoryBookId);
            }
        });
    }

    public void updateMemoryName(final String memoryBookId) {
        final Dialog dialog1;
        View diaView = View.inflate(context, R.layout.dialogui_center_add, null);
        dialog1 = new Dialog(context, R.style.dialog);
        dialog1.setContentView(diaView);
        dialog1.show();
        TextView tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
        TextView tvSure = (TextView) diaView.findViewById(R.id.tv_sure);
        TextView tvCancel = (TextView) diaView.findViewById(R.id.tv_cancel);
        tvInfo.setText("请输入更改后的纪念册名称");
        final EditText etInput = (EditText) diaView.findViewById(R.id.et_input);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMemoryBookTitle(memoryBookId,etInput.getText().toString().trim());
                dialog1.cancel();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
    }

    public void clickDelMemory(final String memoryBookId) {
        View diaView1= View.inflate(context, R.layout.dialogui_center_sure_or_cancel, null);
        final Dialog dialog1=new Dialog(context,R.style.dialog);
        dialog1.setContentView(diaView1);
        dialog1.show();
        TextView textInfo1 = (TextView) diaView1.findViewById(R.id.tv_info);
        TextView btSure1 = (TextView) diaView1.findViewById(R.id.tv_sure);
        TextView btCancel1 = (TextView) diaView1.findViewById(R.id.tv_cancel);
        textInfo1.setText("是否解散该纪念册？");
        btSure1.setTextColor(context.getResources().getColor(R.color.grey));
        btCancel1.setTextColor(context.getResources().getColor(R.color.red));
        btSure1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "退出", Toast.LENGTH_SHORT).show();
                deleteMemoryBook(memoryBookId);
                dialog1.cancel();
            }
        });
        btCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
    }

    public void changeMemoryBookTitle(final String id,final String title) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", id);
        OkHttpUtils.Param titleParam = new OkHttpUtils.Param("title", title);
        list.add(idParam);
        list.add(titleParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.changeMemoryBookTitle, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryBookListResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryBookListResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryBookListResult = MemoryBookListResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryBookListResult.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
//                            initListData(memoryBookListResult.getData());
//                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                            changeTitle(id,title);
                        }else {
                            Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
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

    public void deleteMemoryBook(final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.deleteMemoryBook, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryBookListResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryBookListResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryBookListResult = MemoryBookListResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryBookListResult.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
//                            initListData(memoryBookListResult.getData());
//                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                            deleteBook(id);
                        }else {
                            Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
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

    private void changeTitle(String id,String title){
        for(int i=0;i<list.size();i++){
            MemoryListModel memoryListModel=list.get(i);
            if(id.equals(memoryListModel.memoryBookId)){
                list.get(i).setTitle(title);
                break;
            }
        }
        this.notifyDataSetChanged();
    }
    private void deleteBook(String id){
        for(int i=0;i<list.size();i++){
            MemoryListModel memoryListModel=list.get(i);
            if(id.equals(memoryListModel.memoryBookId)){
                list.remove(i);
                break;
            }
        }
        this.notifyDataSetChanged();
    }
}
