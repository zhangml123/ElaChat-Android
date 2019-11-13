package com.eladapp.elachat.db;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.elastos.carrier.Carrier;
import org.elastos.carrier.ConnectionStatus;
import org.elastos.carrier.FriendInfo;
import org.elastos.carrier.UserInfo;
import org.elastos.carrier.exceptions.CarrierException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ela.Carrier.Chatcarrier;

public class Db {
    private static String dbpath ="/data/data/com.eladapp.elachat/chat.db";

    List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    Chatcarrier chatcarrier = new Chatcarrier();
    Boolean curn = false;
    public void initdb(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        String sqla="create table if not exists messagelist ("
                + "id integer primary key autoincrement,"
                + "sender text, "
                + "content text, "
                + "yn integer,"
                + "time text,"
                + "voicepath text,"
                + "voiceurl text,"
                + "imagepath text,"
                + "thumbPath text,"
                + "imageurl text,"
                + "mtype integer not null,"
                + "reciver text)";
        String sqlb="create table if not exists friend_list("
                + "id integer primary key autoincrement, "
                + "userId text, "
                + "name text,"
                + "description text,"
                + "email text,"
                + "gender text,"
                + "phone text,"
                + "connectionStatus integer,"
                + "region text)";
        String sqlc ="create table if not exists newfriendlist("
                + "id integer primary key autoincrement,"
                + "userid text,"
                + "yn integer,"
                + "hello text,"
                + "nickname text)";
        String sqld ="create table if not exists newmessagelast("
                + "id integer primary key autoincrement,"
                + "userid text,"
                + "content text,"
                + "yn integer,"
                + "time text,"
                + "mtype integer not null,"
                + "num integer)";
        String sqle ="create table if not exists didinfo("
                + "id integer primary key autoincrement,"
                + "did text,"
                + "pubkey text,"
                + "prvkey text,"
                + "useradr text,"
                + "nickname text,"
                + "walletadr text)";
        String sqlf ="create table if not exists dappinfo("
                + "id integer primary key autoincrement,"
                + "appid text,"
                + "appname text,"
                + "did text,"
                + "cate char(5),"
                + "pubkey text,"
                + "appshortname text,"
                + "remark text,"
                + "images text,"
                + "menujson text)";
        db.execSQL(sqla);
        db.execSQL(sqlb);
        db.execSQL(sqlc);
        db.execSQL(sqld);
        db.execSQL(sqle);
        db.execSQL(sqlf);
        db.execSQL("create table if not exists message_list_new("
                + "id integer primary key autoincrement,"
                + "friendUid text,"
                + "isReceive text,"
                + "content text,"
                + "time text,"
                + "voicePath text,"
                + "imagePath text,"
                + "thumbPath text,"
                + "mType text)");
        db.close();
    }
    /**
    *
    * 新增DID信息到DIDinfo表
    * */
    public boolean adddid(String did,String prvkey,String pubkey,String useradr,String nickname,String walletadr){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("did", did);
        values.put("pubkey", pubkey);
        values.put("prvkey", prvkey);
        values.put("useradr",useradr);
        values.put("nickname", nickname);
        values.put("walletadr", walletadr);
        long rs = dba.insert("didinfo", null, values);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    /**
    *
    * 读取did信息列表
    * */
    public JSONArray getdidinfo(){
            SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
            Cursor result = dba.query ("didinfo",new String[]{"id,did,pubkey,prvkey,useradr,nickname,walletadr"},"id=? ",new String[]{"1"},null,null,null);
            JSONArray json = new JSONArray();
            if(result.moveToFirst()){
                while(!result.isAfterLast()){
                    String did = result.getString(result.getColumnIndex("did"));
                    String pubkey = result.getString(result.getColumnIndex("pubkey"));
                    String prvkey = result.getString(result.getColumnIndex("prvkey"));
                    String useradr = result.getString(result.getColumnIndex("useradr"));
                    String nickname = result.getString(result.getColumnIndex("nickname"));
                    String walletadr = result.getString(result.getColumnIndex("walletadr"));
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("did", did);
                        obj.put("useradr",useradr);
                        obj.put("prvkey",prvkey);
                        obj.put("pubkey",pubkey);
                        obj.put("nickname",nickname);
                        obj.put("walletadr",walletadr);
                        json.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    result.moveToNext();
                }
            }
            dba.close();
            return json;

    }
    /**
     *  新朋友操作
     */
    //新增好友到新好友列表
    public boolean addnewfriend(String uid,String remark){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("userid", uid);
        values.put("yn", 0);
        values.put("hello", remark);
        long rs = dba.insert("newfriendlist", null, values);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    //更新新增好友的列表状态
    public boolean updatenewfriend(String uid){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("yn", 1);
        long rs = dba.update("newfriendlist", values, "userid=?", new String[] { uid});
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    //获取新增的好友列表
    public JSONArray newfriendlist(){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newfriendlist",new String[]{"userid,yn,hello"},null,null,null,null,null);
        JSONArray json = new JSONArray();
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String fuserid = result.getString(result.getColumnIndex("userid"));
                String hello = result.getString(result.getColumnIndex("hello"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                JSONObject obj = new JSONObject();
                try {
                    obj.put("userid", fuserid);
                    obj.put("yn",yn.toString());
                    obj.put("message",hello);
                    json.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result.moveToNext();
            }
        }
        dba.close();
        return json;
    }
    //获取好友列表
    public List<Map<String, String>>  getfriendlist(){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Chatcarrier chatcarrier = new Chatcarrier();
        Cursor result = dba.query ("newfriendlist",new String[]{"userid,yn,hello"},null,null,null,null,null);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String fuserid = result.getString(result.getColumnIndex("userid"));
                String hello = result.getString(result.getColumnIndex("hello"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                Map<String, String> maps = new HashMap<String, String>();
                FriendInfo friendinfo = chatcarrier.friendinfo(fuserid);
                if(yn.equals(1)){
                    maps.put("userid", fuserid);
                    maps.put("remark",friendinfo.getName().toString());
                    list.add(maps);
                }
                result.moveToNext();
            }
        }
        dba.close();
        return list;
    }
    //获取好友列表
    public List<Map<String, String>> getfriendlistlist(){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newfriendlist",new String[]{"userid,yn,hello"},null,null,null,null,null);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String fuserid = result.getString(result.getColumnIndex("userid"));
                String hello = result.getString(result.getColumnIndex("hello"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("userid", fuserid);
                maps.put("yn",yn.toString());
                maps.put("message",hello);
                list.add(maps);
                result.moveToNext();
            }
        }
        dba.close();
        return list;
    }
    //新增消息表
    public boolean addfriendmessage(String fuid,String content,String voicepath,String imgpath,String thumbPath,int cate,String reciver){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        long time = new Date().getTime();
        ContentValues values = new ContentValues();
        values.put("sender", fuid);
        values.put("content", content);
        values.put("yn", 0);
        values.put("time", String.valueOf(time));
        values.put("voicepath", voicepath);
        values.put("imagepath", imgpath);
        values.put("thumbPath", thumbPath);
        values.put("mtype", cate);
        values.put("reciver", reciver);
        long rs = dba.insert("messagelist", null, values);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    //获取消息列表
    public List<Map<String, String>> getfriendmessagelist(String uid,int start,int limit){
        List<Map<String, String>> msgList=new ArrayList<Map<String,String>>();
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("messagelist",new String[]{"sender,content,yn,time,voicepath,imagepath,thumbPath,mtype,reciver"},"sender=? or reciver=? ",new String[]{uid,uid},null,null,"id DESC",start +","+limit);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String sender = result.getString(result.getColumnIndex("sender"));
                String content = result.getString(result.getColumnIndex("content"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                String time = result.getString(result.getColumnIndex("time"));
                String voceipath = result.getString(result.getColumnIndex("voicepath"));
                String imagepath = result.getString(result.getColumnIndex("imagepath"));
                String thumbPath = result.getString(result.getColumnIndex("thumbPath"));
                Integer mtype = result.getInt(result.getColumnIndex("mtype"));
                String reciver = result.getString(result.getColumnIndex("reciver"));
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("sender", sender);
                maps.put("content",content);
                maps.put("yn",yn.toString());
                maps.put("time",time);
                maps.put("voicepath",voceipath);
                maps.put("imagePath",imagepath);
                maps.put("thumbPath",thumbPath);
                maps.put("mtype",mtype.toString());
                maps.put("reciver",reciver);
                msgList.add(maps);
                result.moveToNext();
            }
        }
        dba.close();
        return msgList;
    }
    /**
     *  添加消息列表
     *
     *
     **/
    public boolean putMsgListNew(String friendUid, String isReceive, String content, String voicePath, String imagePath, String thumbPath, int mType){
        Log.d("Db","putMsgListNew");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result  = db.query("message_list_new",new String[]{"friendUid"},"friendUid=? ",new String[]{friendUid},null,null,null);
        long rs = 0;
        long time = new Date().getTime();
        ContentValues values = new ContentValues();
        values.put("friendUid", friendUid);
        values.put("isReceive", isReceive);
        values.put("content", content);
        values.put("time", String.valueOf(time));
        values.put("voicePath", voicePath);
        values.put("imagePath", imagePath);
        values.put("thumbPath", thumbPath);
        values.put("mType", mType);
        if(result.getCount() != 0){
            rs=db.update("message_list_new",values,"friendUid=?",new String[]{friendUid});
        }else{
            rs = db.insert("message_list_new", null, values);
        }
        db.close();
        if (rs != -1) {
            return true;
        }else{
            return false;
        }
    }
    /**
     *  获取消息列表
     *
     *
     **/
    public List<Map<String, Object>> getMsgListNew(){
        Log.d("Db","getMsgListNew");
        List<Map<String, Object>> msgList=new ArrayList<Map<String,Object>>();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = db.query ("message_list_new",new String[]{"friendUid,isReceive,content,time,voicePath,imagePath,thumbPath,mType"},null,null,null,null,"time DESC");
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String friendUid = result.getString(result.getColumnIndex("friendUid"));
                String isReceive = result.getString(result.getColumnIndex("isReceive"));
                String content = result.getString(result.getColumnIndex("content"));
                long time = Long.parseLong(result.getString(result.getColumnIndex("time")));
                String voicePath = result.getString(result.getColumnIndex("voicePath"));
                String imagePath = result.getString(result.getColumnIndex("imagePath"));
                String thumbPath = result.getString(result.getColumnIndex("thumbPath"));
                String mType = result.getString(result.getColumnIndex("mType"));
                Map<String, Object> maps = new HashMap<String, Object>();
                maps.put("friendUid", friendUid);
                maps.put("isReceive", isReceive);
                maps.put("content", content);
                maps.put("time",time);
                maps.put("voicePath",voicePath);
                maps.put("imagePath",imagePath);
                maps.put("thumbPath",thumbPath);
                maps.put("mType",mType);
                msgList.add(maps);
                result.moveToNext();
            }
        }
        db.close();
        return msgList;
    }

    //获取好友列表
    public List<Map<String, String>> getFriendList(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        List<Map<String, String>> friendList = new ArrayList<Map<String,String>>();
        Cursor result = db.query ("friend_list",new String[]{"userId,name,description,email,gender,phone,region,connectionStatus"},null,null,null,null,null);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String userId = result.getString(result.getColumnIndex("userId"));
                String name = result.getString(result.getColumnIndex("name"));
                String description = result.getString(result.getColumnIndex("description"));
                String email = result.getString(result.getColumnIndex("email"));
                String gender = result.getString(result.getColumnIndex("gender"));
                String phone = result.getString(result.getColumnIndex("phone"));
                String region = result.getString(result.getColumnIndex("region"));
                int connectionStatus = result.getInt(result.getColumnIndex("connectionStatus"));
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("userId", userId);
                maps.put("name",name);
                maps.put("description",description);
                maps.put("email",email);
                maps.put("gender",gender);
                maps.put("phone",phone);
                maps.put("region",region);
                maps.put("connectionStatus",String.valueOf(connectionStatus));
                friendList.add(maps);
                result.moveToNext();
            }
        }
        db.close();
        return friendList;
    }


    //更新好友列表
    public boolean updateFriendList(){
        Carrier mycarrier = chatcarrier.carrierinstance();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        long rs = 0;
        try {
            List<FriendInfo> friendlist = mycarrier.getFriends();
            for(int i=0;i<friendlist.size();i++){
                ContentValues values = new ContentValues();
                Cursor result  = db.query("friend_list",new String[]{"userId"},"userId=? ",new String[]{friendlist.get(i).getUserId()},null,null,null);
                if(result.getCount() != 0 && friendlist.get(i).getConnectionStatus().value() == 0){
                    values.put("userId", friendlist.get(i).getUserId());
                    values.put("name", friendlist.get(i).getName());
                    values.put("description", friendlist.get(i).getDescription());
                    values.put("email", friendlist.get(i).getEmail());
                    values.put("gender", friendlist.get(i).getGender());
                    values.put("phone", friendlist.get(i).getPhone());
                    values.put("region", friendlist.get(i).getRegion());
                    values.put("connectionStatus", friendlist.get(i).getConnectionStatus().value());
                    rs = db.update("friend_list",values,"userId=?",new String[]{friendlist.get(i).getUserId()});
                }else if(result.getCount() != 0 && friendlist.get(i).getConnectionStatus().value() == 1){
                    values.put("connectionStatus", friendlist.get(i).getConnectionStatus().value());
                    rs = db.update("friend_list",values,"userId=?",new String[]{friendlist.get(i).getUserId()});
                }else{
                    values.put("userId", friendlist.get(i).getUserId());
                    values.put("name", friendlist.get(i).getName());
                    values.put("description", friendlist.get(i).getDescription());
                    values.put("email", friendlist.get(i).getEmail());
                    values.put("gender", friendlist.get(i).getGender());
                    values.put("phone", friendlist.get(i).getPhone());
                    values.put("region", friendlist.get(i).getRegion());
                    values.put("connectionStatus", friendlist.get(i).getConnectionStatus().value());
                    rs = db.insert("friend_list", null, values);
                }

            }
            db.close();
        } catch (CarrierException e) {
            e.printStackTrace();
        }
        if (rs != -1) {
            return true;
        }else{
            return false;
        }
    }
    //更新好友信息
    public boolean updateFriendInfo(String friendUid, FriendInfo friendInfo ){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        long rs = 0;
        ContentValues values = new ContentValues();
        values.put("userId", friendInfo.getUserId());
        values.put("name", friendInfo.getName());
        values.put("description", friendInfo.getDescription());
        values.put("email", friendInfo.getEmail());
        values.put("gender", friendInfo.getGender());
        values.put("phone", friendInfo.getPhone());
        values.put("region", friendInfo.getRegion());
        values.put("connectionStatus",friendInfo.getConnectionStatus().value());
        if(friendInfo.getConnectionStatus().value() == 0){
            rs = db.update("friend_list",values,"userId=?",new String[]{friendUid});
        }
        db.close();
        if (rs != -1) {
            return true;
        }else{
            return false;
        }
    }

    private FriendInfo friendInfo;
    public FriendInfo getFriendInfo(String friendUid){
        try{
            Carrier myCarrier = chatcarrier.carrierinstance();
            friendInfo = myCarrier.getFriend(friendUid);
            if(friendInfo.getConnectionStatus().value() == 1){
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
                List<Map<String, String>> friendList = new ArrayList<Map<String,String>>();
                Cursor result = db.query ("friend_list",new String[]{"userId,name,description,email,gender,phone,region,connectionStatus"},"userId=? ",new String[]{friendUid},null,null,null,"1");
                if(result.moveToFirst()){
                    while(!result.isAfterLast()){
                        String name = result.getString(result.getColumnIndex("name"));
                        String description = result.getString(result.getColumnIndex("description"));
                        String email = result.getString(result.getColumnIndex("email"));
                        String gender = result.getString(result.getColumnIndex("gender"));
                        String phone = result.getString(result.getColumnIndex("phone"));
                        String region = result.getString(result.getColumnIndex("region"));
                        friendInfo.setName(name);
                        friendInfo.setDescription(description);
                        friendInfo.setEmail(email);
                        friendInfo.setGender(gender);
                        friendInfo.setPhone(phone);
                        friendInfo.setRegion(region);
                        friendInfo.setConnectionStatus(ConnectionStatus.valueOf(result.getInt(result.getColumnIndex("connectionStatus"))));
                        result.moveToNext();
                    }
                }
                db.close();
            }

        }catch(CarrierException e){
            e.printStackTrace();
        }
        return friendInfo;
    }
    //检测是否存在好友
    public boolean checkfriend(String uid){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newfriendlist",new String[]{"userid"},"userid=? ",new String[]{uid},null,null,null);
        int i = 0;
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                i = 1;
            }
        }
        if(i==1){
            return true;
        }else{
            return false;
        }
    }
    //新增好友到新好友
    public boolean addnewfriends(String uid,String remark){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("userid", uid);
        values.put("yn", 1);
        values.put("hello", remark);
        long rs = dba.insert("newfriendlist", null, values);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    //新增或更新到最后一条记录
    public boolean addorupdatenewmessagelast(String uid,String content,Integer mtype,Integer num){
            SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
            ContentValues values = new ContentValues();
            long time = new Date().getTime();
            System.out.println("加入最后一条记录时间："+time);
            if(checkmessagelast(uid)){
                values.put("content", content);
                values.put("mtype", mtype);
                values.put("time", String.valueOf(time));
                values.put("num", num);
                long rs = dba.update("newmessagelast", values, "userid=?", new String[] {uid});
                System.out.println("更新新消息："+rs);
                dba.close();
                if (rs!=-1) {
                    return false;
                }else{
                    return true;
                }
            }else{
                values.put("userid", uid);
                values.put("content", content);
                values.put("mtype", mtype);
                values.put("yn", 0);
                values.put("time", String.valueOf(time));
                values.put("num", num);
                long rs = dba.insert("newmessagelast", null, values);
                System.out.println("加入新消息："+rs);
                dba.close();
                if (rs!=-1) {
                    return false;
                }else{
                    return true;
                }
            }
    }
    //查询newmessagelast是否存在记录
    public boolean checkmessagelast(String uid){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newmessagelast",new String[]{"userid"},null,null,null,null,null);
        if(result.moveToFirst()){
            curn = true;
        }
        System.out.println("判断是否存在："+curn.toString());
        dba.close();
        return curn;
    }
    //更新指定消息记录为已读状态
    public boolean updatemessagelast(String uid){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("yn", 1);
        long rs = dba.update("newmessagelast", values, "userid=?", new String[] {uid});
        System.out.println("更新新消息："+rs);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }

    //获取消息列表
    List<Map<String, Object>> listo=new ArrayList<Map<String,Object>>();
    public List<Map<String, Object>> getfriendmessagelistnoread(){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newmessagelast",new String[]{"userid,content,mtype,num,yn,time"},null,null,null,null,null);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String sender = result.getString(result.getColumnIndex("userid"));
                String content = result.getString(result.getColumnIndex("content"));
                Integer mtype = result.getInt(result.getColumnIndex("mtype"));
                Integer num = result.getInt(result.getColumnIndex("num"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                long time = Long.parseLong(result.getString(result.getColumnIndex("time")));
                Map<String, Object> maps = new HashMap<String, Object>();
                maps.put("sender", sender);
                maps.put("content",content);
                maps.put("yn",yn.toString());
                maps.put("time",time);
                maps.put("mtype",mtype.toString());
                maps.put("num",num.toString());
                listo.add(maps);
                result.moveToNext();
            }
        }
        dba.close();
        return listo;
    }
    /**
     *
     * 新增信息到DAPP信息表
     * */
    public boolean adddappinfo(String appid,String did,String appname,String cate,String pubkey,String appshortname,String remark,String images,String menujson){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("appid", appid);
        values.put("did", did);
        values.put("appname", appname);
        values.put("cate", cate);
        values.put("pubkey",pubkey);
        values.put("appshortname", appshortname);
        values.put("remark", remark);
        values.put("images", images);
        values.put("menujson", menujson);
        long rs = dba.insert("dappinfo", null, values);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    /**
     *
     * 更新信息到DAPP信息表
     * */
    public boolean updatedappinfo(String id,String appname,String cate,String appshortname,String remark,String images,String menujson){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("appname", appname);
        values.put("cate", cate);
        values.put("appshortname", appshortname);
        values.put("remark", remark);
        values.put("images", images);
        values.put("menujson", menujson);
        long rs = dba.update("dappinfo", values, "id=?", new String[] {id});
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }

    /**
     *
     * 获取DAPP信息表
     * */
    public List<Map<String, String>> dappinfolist(){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("dappinfo",new String[]{"appid,did,appname,appshortname,cate,pubkey,remark,images,menujson"},null,null,null,null,null);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String appid = result.getString(result.getColumnIndex("appid"));
                String did = result.getString(result.getColumnIndex("did"));
                String appname = result.getString(result.getColumnIndex("appname"));
                String appshortname = result.getString(result.getColumnIndex("cate"));
                String cate = result.getString(result.getColumnIndex("appshortname"));
                String pubkey = result.getString(result.getColumnIndex("pubkey"));
                String remark = result.getString(result.getColumnIndex("remark"));
                String images = result.getString(result.getColumnIndex("images"));
                String menujson = result.getString(result.getColumnIndex("menujson"));
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("appid", appid);
                maps.put("did",did);
                maps.put("appname",appname);
                maps.put("cate",cate);
                maps.put("appshortname",appshortname);
                maps.put("pubkey",pubkey);
                maps.put("remark",remark);
                maps.put("images",images);
                maps.put("menujson",menujson);
                list.add(maps);
                result.moveToNext();
            }
        }
        dba.close();
        return list;
    }

}