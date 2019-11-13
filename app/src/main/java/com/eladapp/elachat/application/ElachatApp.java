package com.eladapp.elachat.application;

import android.app.Application;
import org.ela.Carrier.Synchronizer;
import org.ela.Carrier.TestOptions;
import android.content.Context;

import org.ela.Elaspv.Elaspvapi;
import org.elastos.carrier.AbstractCarrierHandler;
import org.elastos.carrier.Carrier;
import org.elastos.carrier.ConnectionStatus;
import org.elastos.carrier.FriendInfo;
import org.elastos.carrier.UserInfo;
import org.elastos.carrier.exceptions.CarrierException;
import org.elastos.carrier.session.AbstractStreamHandler;
import org.elastos.carrier.session.Manager;
import org.elastos.carrier.session.ManagerHandler;
import org.elastos.carrier.session.Session;
import org.elastos.carrier.session.Stream;
import org.elastos.carrier.session.StreamState;
import org.elastos.carrier.session.StreamType;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.LocaleList;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.widget.ImageView;

import com.breadwallet.BreadApp;
import com.breadwallet.tools.util.BRConstants;
import com.eladapp.elachat.R;
import com.eladapp.elachat.chat.MsgEntity;
import com.eladapp.elachat.db.Db;
/*
import com.elastos.spvcore.DIDManagerSupervisor;
import com.elastos.spvcore.ElastosWalletUtils;
import com.elastos.spvcore.IDid;
import com.elastos.spvcore.IDidManager;
import com.elastos.spvcore.IMainchainSubWallet;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.ISubWallet;
import com.elastos.spvcore.MasterWalletManager;
import com.elastos.spvcore.WalletException;
*/
import com.eladapp.elachat.utils.LanguageUtil;
import com.lqr.emoji.IImageLoader;
import com.lqr.emoji.LQREmotionKit;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.sf.json.JSONArray;

import static com.eladapp.elachat.chat.ChatActivity.revitionImageSize;

public class ElachatApp extends Application{
    Carrier carrierInst = null;
    String carrierAddr = null;
    String carrierUserID = null;
    private static final String TAG = "ElachatApp";
    static String sender = "";
    static String contents = "";
    static byte[] filebyte;
    static int filenum;
    static int filetranid;
    Manager sessionMgra;
    Session activsession;
    String sessionRequestSdp;
    String curfuid;
    public static final String friendMessageAction = "friendMessage";
    public static final String friendStatusAction = "friendStatus";
    public static final String newFriendAction = "newFriendAction";
    private static Map receivefiletype = new HashMap();
    private Db db = new Db();
    private Configuration config;

    private static final String PACKAGE_NAME = BreadApp.getBreadContext() == null ? null : BreadApp.getBreadContext().getApplicationContext().getPackageName();

    static {
        try {
            System.loadLibrary(BRConstants.NATIVE_LIB_NAME);
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.d(TAG, "Native code library failed to load.\\n\" + " + e);
            Log.d(TAG, "Installer Package Name -> " + (PACKAGE_NAME == null ? "null" : BreadApp.getBreadContext().getPackageManager().getInstallerPackageName(PACKAGE_NAME)));
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LQREmotionKit.init(this, new IImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
        });
        Db db = new Db();
        db.initdb();
        TestOptions options = new TestOptions(getAppPath());
        Myhandler handler = new Myhandler();
        try {

            //1.1获得Carrier的实例
            Carrier.initializeInstance(options, handler);
            carrierInst = Carrier.getInstance();
            //1.2获得Carrier的地址
            carrierAddr = carrierInst.getAddress();
            Log.i(TAG,"address: " + carrierAddr);
            //1.3获得Carrier的用户ID
            carrierUserID = carrierInst.getUserId();
            Log.i(TAG,"userID: " + carrierUserID);
            List<FriendInfo> friendlist = carrierInst.getFriends();
            Log.i(TAG,"friendlist.size: " + friendlist.size());
            for(int i=0;i<friendlist.size();i++){
                Log.i(TAG,"friendlist: " + friendlist.get(i).toString());
            }

            //1.4启动网络
            carrierInst.start(1000);
            handler.synch.await();
           // Log.i(TAG,"carrier client is ready now");
            setUp(carrierInst);
        } catch (CarrierException e) {
            System.out.println("ElachatApp CarrierException1111"+e);
            e.printStackTrace();
        }
       /* Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.locale = Locale.SIMPLIFIED_CHINESE;
        resources.updateConfiguration(config, resources.getDisplayMetrics());

/*
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            //Application这种方式适用于8.0之前(不包括8.0)的版本
            LanguageUtil.initAppLanguage(getApplicationContext(), "zh");
        }*/


        String lang = getlangconfig();
        System.out.println("ElaChatApp lang = "+lang);
        System.out.println("ElaChatApp lang_message = "+ getResources().getString(R.string.message));
    }
    public String getlangconfig(){
        String lang = "";
        Resources resources = getResources();
        config = resources.getConfiguration();
        if(config.locale.equals(Locale.CHINA)){
            lang = "cn";
        }else if(config.equals(Locale.ENGLISH)){
            lang =  "en";
        }
        return lang;
    }

    public String getsender(){
        //System.out.println("获取到的发送者："+sender);
        return sender;
    }
    public  String getcontents(){
        return contents;
    }
    public void setsender(){
        //System.out.println("初始化发送者");
        sender="";
    }

    //第一次设置
    public void setfilebyteone(byte[] bt){
            filebyte = bt;
    }

    //获取文件传输正文
    public byte[] getfilebyte(){
        return  filebyte;
    }
    private String getAppPath() {
        Context context=this;
        File file=context.getFilesDir();
        String path=file.getAbsolutePath();
        return path;
    }

     class Myhandler extends AbstractCarrierHandler {
        Synchronizer synch = new Synchronizer();
        String from;
        ConnectionStatus friendStatus;
        String CALLBACK="call back";
        public void onReady(Carrier carrier) {
            synch.wakeup();
        }
        public void onConnection(Carrier carrier, ConnectionStatus status) {
            System.out.println("onConnection");
        }
        public void onFriendConnection(Carrier carrier, String friendId, ConnectionStatus status) {
            from = friendId;
            friendStatus = status;
            System.out.println("From:"+friendId+";状态："+status);
            Intent intent0 = new Intent(friendStatusAction);
            intent0.putExtra("friendid", friendId);
            if (friendStatus == ConnectionStatus.Connected) {
                intent0.putExtra("status", "0");
                sendBroadcast(intent0);
                synch.wakeup();
            }else if(friendStatus == ConnectionStatus.Disconnected){
                intent0.putExtra("status", "1");
                sendBroadcast(intent0);
            }
        }
         public void onFriendInfoChanged(Carrier carrier, String friendId, FriendInfo info) {
                System.out.println("onFriendInfoChanged 11111111111111"+ friendId + info.toString());
             db.updateFriendInfo(friendId,info);
         }
        //2.2 通过好友验证
        public void onFriendRequest(Carrier carrier, String userId, UserInfo info, String hello) {
            try{
                System.out.println("好友请求信息："+userId+",备注:"+hello);
                db.addnewfriend(userId,hello);
                Intent intent = new Intent(newFriendAction);
                intent.putExtra("friendUid", userId);
                intent.putExtra("hello", hello);
                sendBroadcast(intent);
                startAlarm();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
         public void onFriendAdded(Carrier carrier, FriendInfo info) {
         }
         public void onFriendRemoved(Carrier carrier, String friendId) {
         }
         public void onFriendMessage(Carrier carrier, String from, byte[] message, boolean isOffline) {
             try {
                 String reveiverid = carrier.getUserId().toString();
                 String receivemessage = new String(message);
                 String [] wltadr = receivemessage.split(":");
                 if(receivemessage.length()>11 && receivemessage.substring(0,11).equals("extendfile|")){
                     String[] narr = receivemessage.split("\\|");
                     receivefiletype.put(from,narr[1]);
                 }else if(receivemessage.equals("new&&&|friend|&&&")){
                     //加入到好友列表
                     //db.addnewfriend(from.toString(),new String(message));
                 }else if(receivemessage.equals("getassetmessage:")) {
                     System.out.println("获取钱包地址消息："+receivemessage);
                     /*if (getwalletlist() == null || getwalletlist().toString().equals("[]")) {
                         carrierInst.sendFriendMessage(from, "Myassetadr:ERRONOWALLETADR");
                     } else {
                         String adr = getassetadr();
                         carrierInst.sendFriendMessage(from, "Myassetadr:" + adr);
                     }*/
                 }else if(wltadr[0].equals("Myassetadr")){
                     System.out.println("获取钱包地址消息111："+receivemessage);
                     Intent intent = new Intent(friendMessageAction);
                     intent.putExtra("fromid", from.toString());
                     intent.putExtra("message", new String(message));
                     intent.putExtra("msgcate", 1);
                     sendBroadcast(intent);
                 }else{
                     db.addfriendmessage(from,receivemessage,"","","",1,reveiverid);
                     db.putMsgListNew(from,"1",receivemessage,"","","",1);
                     db.addorupdatenewmessagelast(from,receivemessage,1,1);
                     startAlarm();
                     Intent intent = new Intent(friendMessageAction);
                     intent.putExtra("fromid", from.toString());
                     intent.putExtra("message", new String(message));
                     intent.putExtra("msgcate", 1);
                     sendBroadcast(intent);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
    }

    /* 以下是文件传输功能 */
    private  final SessionManagerHandler sessionHandler = new SessionManagerHandler();
     class SessionManagerHandler implements ManagerHandler {
        @Override
        public void onSessionRequest(Carrier carrier, String from, String sdp) {
            sessionRequestSdp = sdp;
            curfuid = from;
            createsessionjoinstream(from);
        }
    }
    //准备接受数据，建立session
    public void setUp(Carrier curcarrier) {
        System.out.println("准备建立Session!");
        try {
            Manager.initializeInstance(curcarrier, sessionHandler);
            sessionMgra = Manager.getInstance();
        } catch (CarrierException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * 以下功能是接受数据
     *
     * */
    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
    public void createsessionjoinstream(String s){
        try{
            activsession = sessionMgra.newSession(s);
            activsession.addStream(StreamType.Text,Stream.PROPERTY_RELIABLE,new AbstractStreamHandler() {
                byte[] receivedData1 = null;
                @Override
                public void onStateChanged(Stream stream, StreamState state) {
                    System.out.println("onStateChanged"+state.name());
                    try {
                        switch (state.name()) {
                            case "Initialized":
                                System.out.println("session111_receive state Initialized");
                                System.out.println("初始化");
                                activsession.replyRequest(0, null);
                                break;
                            case "TransportReady":
                                System.out.println("session111_receive state TransportReady");
                                System.out.println("准备传输");
                                activsession.start(sessionRequestSdp);
                                break;
                            case "Connected":
                                System.out.println("session111_receive state Connected");
                                //datas = null;
                                System.out.println("建立连接");
                                break;
                            case "Closed":
                                System.out.println("session111_receive state Closed");
                                System.out.println("断开连接");
                                final String reveiverid = carrierInst.getUserId().toString();
                                Thread threadb = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Date date = new Date();
                                        SimpleDateFormat curtime = new SimpleDateFormat("yyyyMMddHHmmss");
                                        curtime.format(date);
                                        String filepath = "/storage/emulated/0/Download/";
                                        String filename = curtime.format(date).toString()+'.'+String.valueOf(receivefiletype.get(curfuid));

                                        if(!getFile(receivedData1,filepath,filename)) return;
                                        //加入到数据库
                                        Intent intent = new Intent(friendMessageAction);
                                        intent.putExtra("fromid", curfuid.toString());
                                        String extensionname = String.valueOf(receivefiletype.get(curfuid));
                                        if(extensionname.equals("mp4")){
                                            db.addfriendmessage(curfuid, "", "", filepath + "" + filename, "", 3, reveiverid);
                                            db.putMsgListNew(curfuid,"1","","",filepath+ "" + filename,"",3);
                                            intent.putExtra("message", filepath + "" + filename);
                                            intent.putExtra("msgcate", 3);
                                        }else if(extensionname.equals("m4a")) {
                                            db.addfriendmessage(curfuid, "", filepath + "" + filename,"","",  4, reveiverid);
                                            db.putMsgListNew(curfuid,"1","",filepath+ "" + filename,"","",4);
                                            intent.putExtra("message", filepath + "" + filename);
                                            intent.putExtra("msgcate", 4);
                                        }else{
                                            String thumbPath = createThumb(filepath + "" + filename);
                                            db.addfriendmessage(curfuid, "", "", filepath + "" + filename,thumbPath, 2, reveiverid);
                                            db.putMsgListNew(curfuid,"1","","",filepath+ "" + filename,"",2);

                                            try{
                                                org.json.JSONObject obj = new JSONObject();
                                                obj.put("imagePath",filepath + "" + filename);
                                                obj.put("thumbPath",thumbPath);
                                                intent.putExtra("message", obj.toString());
                                                intent.putExtra("msgcate", 2);
                                            }catch(JSONException e){
                                                e.printStackTrace();
                                            }
                                        }
                                        sendBroadcast(intent);
                                    }
                                });
                                threadb.start();
                                try {
                                    threadb.join();
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (CarrierException e) {
                        e.printStackTrace();
                    }
                    //synch1.wakeup();
                }
                @Override
                public void onStreamData(Stream stream, byte[] data) {
                    Log.d(TAG, "onStreamData data="+(new String(data)));
                    System.out.println("session111_receive onStreamData "+ data.length);
                    if(receivedData1==null) {
                        receivedData1 =data;
                    }else{
                        receivedData1 =byteMerger(receivedData1,data);
                    }
                    System.out.println("session111_receive receivedData "+ receivedData1.length);
                    System.out.println("\n\nsession111_receive receivedData "+ receivedData1.toString());
                }
            });
        }catch (CarrierException e){
            e.getMessage();
        }
    }
    public String createThumb(String path){
        System.out.println("path111111111111 ="+path);
        String[] arr = path.split("\\/");
        String fileName = arr[arr.length -1 ];
        System.out.println("path111111111111  filename："+arr[arr.length -1 ]);
        FileOutputStream b = null;
        String filePath =  this.getFilesDir().getPath();
        String fileDir = filePath + "/images";
        String thumbPath = filePath + "/images/thumb_"+fileName;
        System.out.println("path111111122222  path："+thumbPath);
        try{
            File newFolder = new File(fileDir);
            if(!newFolder.exists()){
                boolean isSuccess = newFolder.mkdirs();
                if(isSuccess){
                    File file = new File(thumbPath);
                    file.createNewFile();
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            Bitmap bitmap = revitionImageSize(path,300,300);
            b = new FileOutputStream(thumbPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return thumbPath;
    }
    /**
     *
     *
     *
     * 根据byte数组，生成文件
     */
    public static boolean getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        boolean check = true;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"//"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
            check =  false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    check =  false;
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    check =  false;
                }
            }
        }
        return check;
    }
    private void startAlarm() throws IOException {
        File file = new File("/data/data/com.eladapp.elachat/voice.txt");
        if (file.exists()) {

            FileInputStream fis = new FileInputStream(file);
            int length = fis.available();
            byte [] buffer = new byte[length];
            fis.read(buffer);
            String res = new String(buffer, "UTF-8");
            fis.close();
            if(res.equals("0")){

            }else{
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (notification == null) return;
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            }
        }else{
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (notification == null) return;
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
    }
    //读取指定文件
    public String getmvsinfo () throws IOException {
        File file = new File("/data/data/com.eladapp.elachat/voice.txt");
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte [] buffer = new byte[length];
        fis.read(buffer);
        String res = new String(buffer, "UTF-8");
        fis.close();
        return res;
    }

}