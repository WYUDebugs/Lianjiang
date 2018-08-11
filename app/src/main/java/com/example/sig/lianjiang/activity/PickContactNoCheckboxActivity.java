package com.example.sig.lianjiang.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.sig.lianjiang.Constant;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.R;
import com.hyphenate.easeui.adapter.EaseContactAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseSidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressLint("Registered")
public class PickContactNoCheckboxActivity extends BaseActivity {

    protected EaseContactAdapter contactAdapter;
    private List<EaseUser> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact_no_checkbox);
        ListView listView = (ListView) findViewById(R.id.list);
        EaseSidebar sidebar = (EaseSidebar) findViewById(R.id.sidebar);
        sidebar.setListView(listView);
        contactList = new ArrayList<EaseUser>();
        // get contactlist
        getContactList();
        // set adapter
        contactAdapter = new EaseContactAdapter(this, R.layout.ease_row_contact, contactList);
        listView.setAdapter(contactAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(position);
            }
        });

    }

    protected void onListItemClick(int position) {
        setResult(RESULT_OK, new Intent().putExtra("username", contactAdapter.getItem(position)
                .getUsername()));
        finish();
    }

    public void back(View view) {
        finish();
    }

    private void getContactList() {
        contactList.clear();
        Map<String, EaseUser> users = StarryHelper.getInstance().getContactList();
        for (Entry<String, EaseUser> entry : users.entrySet()) {
            if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(Constant.GROUP_USERNAME) && !entry.getKey().equals(Constant.CHAT_ROOM) && !entry.getKey().equals(Constant.CHAT_ROBOT))
                contactList.add(entry.getValue());
        }
        // sort
        Collections.sort(contactList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNick().compareTo(rhs.getNick());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });
    }

}
