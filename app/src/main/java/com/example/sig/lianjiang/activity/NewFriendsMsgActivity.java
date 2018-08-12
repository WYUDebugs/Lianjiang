package com.example.sig.lianjiang.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.adapter.NewFriendsMsgAdapter;
import com.example.sig.lianjiang.db.InviteMessgeDao;
import com.example.sig.lianjiang.domain.InviteMessage;

import java.util.Collections;
import java.util.List;

public class NewFriendsMsgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_msg);
        ListView listView = (ListView) findViewById(R.id.list);
        InviteMessgeDao dao = new InviteMessgeDao(this);
        List<InviteMessage> msgs = dao.getMessagesList();
        Collections.reverse(msgs);

        NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
        listView.setAdapter(adapter);
        dao.saveUnreadMessageCount(0);
    }
    public void back(View view) {
        finish();
    }
}
