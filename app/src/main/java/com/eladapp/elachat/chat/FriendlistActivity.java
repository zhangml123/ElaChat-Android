package com.eladapp.elachat.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.eladapp.elachat.R;
//import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.db.Db;

import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.Carrier;
import org.elastos.carrier.FriendInfo;
import org.elastos.carrier.exceptions.CarrierException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eladapp.elachat.application.ElachatApp;

public class FriendlistActivity extends AppCompatActivity {
    private RelativeLayout myinfo;
    private RelativeLayout newfriend;
    private Db db;
    private ListView listView;
    private ContactListAdapter contactListAdapter;
    private ImageView addFriend;
    private Handler handler;
    private List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);
        IntentFilter filter = new IntentFilter(ElachatApp.friendStatusAction);
        registerReceiver(broadcastReceiver, filter);
        initView();
        db = new Db();
        db.updateFriendList();
        refresh();
    }
    @Override
    public void onResume() {
        super.onResume();
        contactListAdapter.notifyDataSetChanged();
    }
    protected void refresh(){
        list = db.getFriendList();
        contactListAdapter = new ContactListAdapter(this, list);
        listView.setAdapter(contactListAdapter);
    }
    public void updateFriendInfo(String uid,String status){
        Map<String, String> mpa = new HashMap<String, String>();
        String fnickname = null;
        int j = -1;
        for(int w=0;w<list.size();w++){
            if(list.get(w).get("userId").equals(uid)){
                j = w;
                break;
            }
        }
        if(j!=-1){
            if(status.equals("1")){
                Chatcarrier chatcarrier = new Chatcarrier();
                Carrier mycarrier = chatcarrier.carrierinstance();
                try {
                    FriendInfo fuinfo = mycarrier.getFriend(uid);
                    fnickname = fuinfo.getName();
                   /* if(fnickname.equals("")){
                        if(getlangconfig().equals("cn")){
                            fnickname="在线";
                        }else if(getlangconfig().equals("en")){
                            fnickname="Online";
                        }else{
                            fnickname="在线";
                        }

                    }*/
                }catch (CarrierException e){
                    e.getCode();
                }
            }else if(status.equals("0")){
                /*if(getlangconfig().equals("cn")){
                    fnickname = "离线";
                }else if(getlangconfig().equals("en")){
                    fnickname = "Offline";
                }else{
                    fnickname = "离线";
                }*/
            }
            mpa.put("userId",uid);
            mpa.put("name",fnickname);
            list.set(j,mpa);
            contactListAdapter.notifyDataSetChanged();
        }
    }
    public void updateFriendStatus(String uid,String status){
        Map<String, String> map = new HashMap<String, String>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).get("userId").equals(uid)){
                map.put("userId",uid);
                map.put("name",list.get(i).get("name"));
                map.put("connectionStatus",status);
                list.set(i,map);
                contactListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
    //初始化控件
    private void initView() {
        myinfo = (RelativeLayout)findViewById(R.id.myinfo);
        newfriend = (RelativeLayout)findViewById(R.id.newFriends);
        listView = (ListView)findViewById(R.id.id_friend_list);
        addFriend = (ImageView)findViewById(R.id.addFriend);
        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendlistActivity.this, MyInfoActivity.class));
            }
        });
        newfriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendlistActivity.this, NewFriendsActivity.class));
            }
        });
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(FriendlistActivity.this,AddFriendsActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Map<String, String> friendInfo = ((Map<String, String>) listView.getItemAtPosition(position));
                    startActivity(new Intent(FriendlistActivity.this, UserInfoActivity.class).putExtra("friendId", friendInfo.get("userId")).putExtra("name",friendInfo.get("name")));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void back(View view){
        finish();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String friendid = intent.getExtras().getString("friendid");
            String status = intent.getExtras().getString("status");
            //updateFriendInfo(friendid,status);
            updateFriendStatus(friendid,status);
        }
    };
    public String getlangconfig(){
        String lang = "";
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        String langs = String.valueOf(config.locale);
        if(langs.equals("cn")){
            lang = "cn";
        }else if(langs.equals("en")){
            lang = "en";
        }else{
            lang = "cn";
        }
        return lang;
    }
}