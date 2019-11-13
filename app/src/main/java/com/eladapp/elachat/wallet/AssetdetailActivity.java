package com.eladapp.elachat.wallet;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.breadwallet.presenter.entities.TxUiHolder;
import com.breadwallet.tools.adapter.TransactionListAdapter;
import com.breadwallet.wallet.WalletsMaster;
import com.breadwallet.wallet.abstracts.BaseWalletManager;
import com.eladapp.elachat.R;
import com.eladapp.elachat.manager.WalletElaManager;
//import com.eladapp.elachat.application.CloudchatApp;
//import com.elastos.spvcore.IMasterWallet;
//import com.elastos.spvcore.ISubWallet;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetdetailActivity extends AppCompatActivity{
    private LinearLayout assetskbtn;
    private LinearLayout assetzzbtn;
    private TextView token_sum;
    private String assetname;
    private TextView asset_sum;
    //private CloudchatApp cloudchatapp;
    //private IMasterWallet mCurrentMasterWallet;
    //private ArrayList<IMasterWallet> mMasterWalletList = new ArrayList<IMasterWallet>();
    private ListView txlist;
    Handler handler;
    private String curprice="";
    List<Map<String, String>> listmap=new ArrayList<Map<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assetname = this.getIntent().getStringExtra("assetname");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);
        handler=new Handler();
        assetzzbtn = (LinearLayout)findViewById(R.id.asset_zz_btn);
        assetskbtn = (LinearLayout)findViewById(R.id.asset_sk_btn);
        token_sum = (TextView)findViewById(R.id.token_sum);
        txlist = (ListView) findViewById(R.id.txlist);
        asset_sum = (TextView)findViewById(R.id.asset_sum);
        token_sum.setText(getblance());
        assetzzbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AssetdetailActivity.this, AssetsendActivity.class).putExtra("assetname","ELA"));
            }
        });
        assetskbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AssetdetailActivity.this, AssetpayActivity.class).putExtra("assetname","ELA"));
            }
        });
        //System.out.println("交易记录列表："+transactionlist().toString());
        List<TxUiHolder> list = transactionlist();
        TxlistAdapter txlistadapter = new TxlistAdapter(this, list);
        txlist.setAdapter(txlistadapter);


        final String path = "http://ela.chat/quota/op.php?parm=detail&maincoin=usdt&subcoin=ela";
        URL urls = null;
        try {
             urls = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final URL finalUrls = urls;

        new Thread(){
            public void run(){
                curprice = getwebinfo(finalUrls);
                handler.post(runnableUi);
            }
        }.start();
    }
    private String getwebinfo(URL url) {
        String price = "";
        try {
            //1,找水源--创建URL
            //URL url = new URL("https://www.baidu.com/");//放网站
            //2,开水闸--openConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            System.out.println("链接信息："+httpURLConnection.toString());
            System.out.println("链接信息："+httpURLConnection.getResponseMessage());
            //httpURLConnection.getResponseMessage();
            //3，建管道--InputStream
            InputStream inputStream = null;
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                 inputStream = httpURLConnection.getInputStream();
            } else {
                 inputStream = httpURLConnection.getErrorStream(); // 得到网络返回的输入流
            }

            //4，建蓄水池蓄水-InputStreamReader
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            //5，水桶盛水--BufferedReader
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer buffer = new StringBuffer();
            String temp = null;

            while ((temp = bufferedReader.readLine()) != null) {
                //取水--如果不为空就一直取
                buffer.append(temp);
            }
            bufferedReader.close();//记得关闭
            reader.close();
            inputStream.close();
            try {
                org.json .JSONObject jsonobja = new  org.json .JSONObject(buffer.toString());
                org.json .JSONObject jsonobjb = new  org.json .JSONObject(jsonobja.get("tick").toString());
                System.out.println("当前价格："+jsonobjb.get("close"));
                //asset_sum.setText("≈ $"+String.valueOf(Double.valueOf(jsonobjb.get("close").toString())*Double.valueOf(getblance()))+"(huobi)");
                price =  jsonobjb.get("close").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("MAIN",buffer.toString());//打印结果

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("链接错误："+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("异常错误："+e.getMessage());
        }
        return price;
    }


    //获取钱余额
    public String getblance(){
        BigDecimal balance =  WalletElaManager.getInstance(this).getCachedBalance(this);

        System.out.println("balance11111111："+balance);
        return String.valueOf(balance);
    }
    //获取交易记录

    //获得指定钱包地址的的交易记录
    public List<TxUiHolder>  transactionlist() {
        WalletElaManager wallet = WalletElaManager.getInstance(this);
        if (wallet == null) {
            //Log.e(TAG, "updateTxList: wallet is null");
            return null;
        }
        final List<TxUiHolder> items = wallet.getTxUiHolders(this);
        System.out.println(items);
        System.out.println(items.size());
        return items;
    }

    public void back(View view){
        finish();
    }
    // 构建Runnable对象，在runnable中更新界面
    Runnable  runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            //asset_sum.setText("≈ $"+String.valueOf(Double.valueOf(curprice)*Double.valueOf(getblance())));
           // asset_sum.setText("$"+String.valueOf(Double.valueOf(curprice)*Double.valueOf(getblance()))+"(huobi)");
        }

    };
}