package com.eladapp.elachat.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.eladapp.elachat.R;
import com.eladapp.elachat.application.ElachatApp;
import com.eladapp.elachat.db.Db;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import android.os.Messenger;

public class NewFriendsActivity extends AppCompatActivity {
    private ListView listView;
    private NewFriendsListAdapter newFriendsListAdapter;
    private Messenger messenger;
    private Db db = new Db();
    private List<Map<String, String>> list = new ArrayList<Map<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);
        listView = (ListView) findViewById(R.id.listview_list);

        list = db.getfriendlistlist();
        newFriendsListAdapter = new NewFriendsListAdapter(this, list);
        listView.setAdapter(newFriendsListAdapter);
        IntentFilter filterNewFriend = new IntentFilter(ElachatApp.newFriendAction);
        registerReceiver(newFriendReceiver, filterNewFriend);
    }
    BroadcastReceiver newFriendReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String friendUid = intent.getExtras().getString("friendUid");
            String hello = intent.getExtras().getString("hello");
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("userid", friendUid);
            maps.put("yn","0");
            maps.put("message",hello);
            list.add(maps);
            newFriendsListAdapter.notifyDataSetChanged();
        }
    };
    public void back(View view) {
        finish();
    }
}
