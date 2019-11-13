package com.eladapp.elachat.chat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.FriendInfo;
import org.w3c.dom.Text;
import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;

public class ListViewAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private Chatcarrier chatarrier = new Chatcarrier();
    private Db db = new Db();

   // private DrawerLayout dwl;
    private ListView lv;
    List<String> list=new ArrayList<>();

    public ListViewAdapter(Context context,List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应list.xml中的控件
     * @author Administrator
     */
    public final class Zujian{
        public ImageView image;
        public TextView friendname;
        public TextView curtime;
        public TextView info;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Zujian zujian=null;
        if(convertView==null){
            zujian=new Zujian();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.activity_messagelist,parent,false);
            zujian.image=(ImageView)convertView.findViewById(R.id.image);
            zujian.friendname=(TextView)convertView.findViewById(R.id.firendname);
            //zujian.view=(Button)convertView.findViewById(R.id.view);
            zujian.curtime=(TextView)convertView.findViewById(R.id.curtime);
            zujian.info=(TextView)convertView.findViewById(R.id.info);
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }
        TextView redbtn = (TextView)convertView.findViewById(R.id.unread_msg_number);

        //绑定数据


        //FriendInfo finfo = chatarrier.friendinfo((String)data.get(position).get("friendUid"));
        FriendInfo friendInfo = db.getFriendInfo((String)data.get(position).get("friendUid"));
        String name = friendInfo.getName();
        System.out.println(" name= "+name);

        if(name.equals("") || name.isEmpty()){
            zujian.friendname.setText((String)data.get(position).get("friendUid"));
        }else{
            zujian.friendname.setText(name);
        }
        String messagetime="";

        Date sqlday = new Date((long)data.get(position).get("time"));
        Date curday = new Date();
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat currentDayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm:ss");
        sqlDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        currentDayFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        currentTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        if(sqlDateFormat.format(sqlday).equals(currentDayFormat.format(curday))){
             messagetime = currentTimeFormat.format(sqlday);
        }else{
             messagetime = sqlDateFormat.format(sqlday);
        }

        zujian.curtime.setText(messagetime);

        System.out.println("dataType = "+data.get(position).get("mType") );
        if(data.get(position).get("mType").toString().equals("2")){
            System.out.println("dataType = 2 " );
            zujian.info.setText("[图片]");
        }else{
            System.out.println("dataType = != 2");
            zujian.info.setText((String)data.get(position).get("content"));
        }

        return convertView;
    }

}
