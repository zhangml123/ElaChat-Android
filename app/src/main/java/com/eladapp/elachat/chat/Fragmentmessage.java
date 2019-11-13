package com.eladapp.elachat.chat;

import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.AdapterView;

import org.elastos.carrier.Carrier;
import org.elastos.carrier.FriendInfo;
import org.elastos.carrier.exceptions.CarrierException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;

public class Fragmentmessage extends Fragment {
    public static String TAG = "MessageFragment";
    private ListView listView;
    private List<FriendInfo> lista;
    private SQLiteDatabase sqldb;
    private ImageView menupopdialog;
    private Db db = new Db();
    private  List<Map<String, Object>> lists;
    private  ListViewAdapter adapters;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message, container,false);
        listView = (ListView)view.findViewById(R.id.lv);
        //lists = db.getfriendmessagelistnoread();
        lists = db.getMsgListNew();
        System.out.println("Fragmentmessage message_list"+lists.size());
        adapters = new ListViewAdapter(getActivity(), lists);
        listView.setAdapter(adapters);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("Fragmentmessage onActivityCreated");
        menupopdialog = (ImageView) getActivity().findViewById(R.id.menupopdialog);
        ImageView friendlist = (ImageView) getActivity().findViewById(R.id.friendlist);
        friendlist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent02=new Intent();
                intent02.setClass(getActivity(),FriendlistActivity.class);
                startActivity(intent02);
            }
        });
        menupopdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopDialog(v);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> parent, View view, int position, long id) {
                try {
                    HashMap friendInfo = ((HashMap) listView.getItemAtPosition(position));
                    startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("friendId", friendInfo.get("friendUid").toString()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Map<String, String> friendInfo = ((Map<String, String>) listView.getItemAtPosition(position));
                    startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("friendId", friendInfo.get("sender")));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }
    @Override
    public void onResume() {
        super.onResume();
        refresh();
        System.out.println("Fragmentmessage onResume");
        Log.d(TAG,"onResume");
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            System.out.println("Fragmentmessage onHiddenChanged !hidden");
           refresh();
        } else {
            System.out.println("Fragmentmessage onHiddenChanged hidden");
       }
    }
    protected void refresh(){
        System.out.println("refresh");
        lists.clear();
        List<Map<String, Object>> list = db.getMsgListNew();
        lists.addAll(list);
        adapters.notifyDataSetChanged();
    }
    public void openPopDialog(View view) {
        Intent intent01=new Intent();
        intent01.setClass(getActivity(),ChatmenurActivity.class);
        startActivity(intent01);
    }
}