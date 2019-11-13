package com.eladapp.elachat.chat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.breadwallet.tools.security.BRKeyStore;
import com.breadwallet.tools.util.BRConstants;
import com.breadwallet.wallet.abstracts.OnBalanceChangedListener;
import com.eladapp.elachat.R;
import com.eladapp.elachat.manager.WalletElaManager;
import com.eladapp.elachat.wallet.AssetdetailActivity;
import com.eladapp.elachat.wallet.AssetmenurActivity;
import com.eladapp.elachat.wallet.WalletActivity;
import com.eladapp.elachat.wallet.WalletcreateonestepActivity;
//import com.elastos.spvcore.IMasterWallet;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import com.breadwallet.wallet.WalletsMaster;
import com.eladapp.elachat.wallet.WalletcreatetowstepActivity;

import static android.app.Activity.RESULT_OK;

public class Fragmentwallet extends Fragment  {
    //Button addwalletbtn;
    LinearLayout asset_bg;
    LinearLayout asset_sum_line;
    TextView assetsum;
    TextView token_ela_sum;
    ImageView menupopmainasset;
    TextView elasum;
    LinearLayout asset_ela;
    Handler handler;
    private String curprice=null;
    private boolean started = false;
    private boolean authenticated;
    //private CloudchatApp cloudchatapp;
   // private  ArrayList<IMasterWallet> mainmasterwallet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wallet, container,false);
        handler=new Handler();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCanaryCheck();
        initView();
        if(checkWallet() && !started) {
            if(checkPIN()){
                if(authenticated){
                    refreshWalletinfoThread.start();
                }
            }else{
                startActivity(new Intent().setClass(getActivity(), WalletcreatetowstepActivity.class));
                getActivity().finish();
            }

        }else{
            started = true;
            System.out.println("startwalletactivity 11111");
            startActivity(new Intent().setClass(getActivity(), WalletActivity.class));
            getActivity().finish();
        }
        menupopmainasset = (ImageView)getView().findViewById(R.id.menupopmainasset);
        menupopmainasset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopDialog(v);
            }
        });
        asset_ela.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AssetdetailActivity.class).putExtra("assetname","ELA"));
            }
        });
        WalletElaManager.getInstance(getActivity()).addBalanceChangedListener(
                new OnBalanceChangedListener() {
                    @Override
                    public void onBalanceChanged(BigDecimal newBalance) {
                        initView();
                    }
                }
        );
    }
    protected void initView() {
        asset_bg = (LinearLayout)getView().findViewById(R.id.asset_bg);
        asset_sum_line = (LinearLayout)getView().findViewById(R.id.asset_sum_line);
        assetsum = (TextView)getView().findViewById(R.id.assetsum);
        asset_ela = (LinearLayout)getView().findViewById(R.id.asset_ela);
        token_ela_sum = (TextView)getView().findViewById(R.id.token_ela_sum);
        elasum = (TextView)getView().findViewById(R.id.elasum);
        asset_bg.setVisibility(View.VISIBLE);
        asset_sum_line.setVisibility(View.VISIBLE);
        if(getlangconfig().equals("cn")){
            assetsum.setText("总资产：≈ "+getblance());
        }else if(getlangconfig().equals("en")){
            assetsum.setText("Assets：≈ "+getblance());
        }else{
            assetsum.setText("总资产：≈ "+getblance());
        }
        asset_ela.setVisibility(View.VISIBLE);
        token_ela_sum.setText(getblance());



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
    public boolean checkWallet(){
        final WalletsMaster m = WalletsMaster.getInstance(getActivity());
        boolean nowallet = m.noWallet(getActivity());
        System.out.println("noWallet:"+nowallet);
        //判断是否有钱包
        if(nowallet){//创建钱包
            return false;
        }else{
            return true;
        }
    }
    public boolean checkPIN(){
        String result =  BRKeyStore.getPinCode(getContext());
        System.out.println("PIN = "+result);
        if(result == ""){
            return false;
        }else{
            return true;
        }
    }
    public void openPopDialog(View view) {
        Intent intent01= new Intent();
        intent01.setClass(getActivity(),AssetmenurActivity.class);
        startActivity(intent01);
    }
    Thread refreshWalletinfoThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try  {
                WalletElaManager.getInstance(getActivity()).refreshCachedBalance(getActivity());
                WalletElaManager.getInstance(getActivity()).updateTxHistory();
                //initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }}
    );
    //获取钱余额
    public String getblance(){
        BigDecimal balance =  WalletElaManager.getInstance(getActivity()).getCachedBalance(getActivity());
        System.out.println("balance11111111："+balance);
        return String.valueOf(balance);
    }

    private String getwebinfo(URL url) {
        String price = "";
        try {
            //2,开水闸--openConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
                price =  jsonobjb.get("close").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            //System.out.println("链接错误："+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("异常错误："+e.getMessage());
        }
        return price;
    }
    // 构建Runnable对象，在runnable中更新界面
    Runnable  runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            String aa = "0.22222";
            aa.format("asdf");

          assetsum.setText("≈ $"+String.format("%.2f",Double.valueOf(curprice)*Double.valueOf(getblance())));
          elasum.setText("$"+String.format("%.2f",Double.valueOf(curprice)*Double.valueOf(getblance()))+"(huobi)");
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
    public void onCanaryCheck() {
        String canary = null;
        try {
            canary = BRKeyStore.getCanary(getActivity(), BRConstants.CANARY_REQUEST_CODE);
            authenticated = true;
        } catch (UserNotAuthenticatedException e) {
            authenticated = false;
            return;
        }
        if (canary == null || !canary.equalsIgnoreCase(BRConstants.CANARY_STRING)) {
            byte[] phrase;
            try {
                phrase = BRKeyStore.getPhrase(getActivity(), BRConstants.CANARY_REQUEST_CODE);
                authenticated = true;
            } catch (UserNotAuthenticatedException e) {
                authenticated = false;
                return;
            }

            String strPhrase = new String((phrase == null) ? new byte[0] : phrase);
            if (strPhrase.isEmpty()) {
                WalletsMaster m = WalletsMaster.getInstance(getActivity());
                m.wipeKeyStore(getActivity());
                m.wipeWalletButKeystore(getActivity());
            } else {
               // Log.e(TAG, "onCanaryCheck: Canary wasn't there, but the phrase persists, adding canary to keystore.");
                try {
                    BRKeyStore.putCanary(BRConstants.CANARY_STRING, getActivity(), 0);
                    authenticated = true;
                } catch (UserNotAuthenticatedException e) {
                    authenticated = false;
                    return;
                }
            }
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("fragment wallet onResume");
        if(!checkWallet() && !started){
            System.out.println("startwalletactivity 222222");
            started = true;
            startActivity(new Intent().setClass(getActivity(), WalletActivity.class));
            getActivity().finish();
        }
        initView();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult requestCode ="+requestCode);
        if (requestCode == BRConstants.PUT_PHRASE_NEW_WALLET_REQUEST_CODE) {


            if (resultCode == RESULT_OK) {

            } else {


            }
        }
    }
}