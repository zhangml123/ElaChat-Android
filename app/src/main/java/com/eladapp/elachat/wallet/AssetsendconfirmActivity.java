package com.eladapp.elachat.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.breadwallet.presenter.customviews.BRDialogView;
import com.breadwallet.tools.animation.BRDialog;
import com.breadwallet.tools.security.BRKeyStore;
import com.breadwallet.tools.util.BRConstants;
import com.breadwallet.wallet.wallets.CryptoTransaction;
import com.eladapp.elachat.R;
import com.eladapp.elachat.manager.WalletElaManager;
//import com.eladapp.elachat.application.CloudchatApp;
//import com.elastos.spvcore.IDidManager;
//import com.elastos.spvcore.IMasterWallet;
//import com.elastos.spvcore.ISubWallet;
//import com.elastos.spvcore.MasterWalletManager;

import java.math.BigDecimal;

public class AssetsendconfirmActivity extends Activity implements View.OnClickListener {
    private String assetname;
    private Button sendassetbtn;
    private EditText paypassword;
    private String toaddress;
    private String amount;
    private String momo;

    private String rootpath;
    private Boolean checkpwd = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assetname = this.getIntent().getStringExtra("assetname");
        toaddress = this.getIntent().getStringExtra("toaddress");
        amount = this.getIntent().getStringExtra("amount");
        momo = this.getIntent().getStringExtra("mono");
        rootpath = getApplicationContext().getFilesDir().getParent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pop);
        initView();
        sendassetbtn.setOnClickListener(this);
    }

    private void initView(){
        sendassetbtn = (Button)findViewById(R.id.sendassetbtn);
        paypassword = (EditText) findViewById(R.id.paypwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendassetbtn:

                //发送交易
                System.out.println("发送交易111111111111：");
                CheckPayPwd(paypassword.getText().toString());
                if(checkpwd){
                    transaction("ELA", paypassword.getText().toString(), toaddress,new BigDecimal(amount) , momo);
                }


            default:
                finish();
                break;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    //构建交易，直接发送
    public void transaction(String chainid, String paypassword, String toaddress, BigDecimal amount, String momo) {
        final Context context = this;
         new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    CryptoTransaction tx = null;

                    tx = WalletElaManager.getInstance(context).createTransaction(amount, toaddress, momo);
                    System.out.println("tx1111"+tx.getHash());
                    System.out.println("tx1111"+tx.getCoreTx());
                    System.out.println("tx1111"+tx.getElaTx());
                    System.out.println("tx1111"+tx.getTxSize());
                    System.out.println("tx1111"+tx.getTxStandardFee());
                    byte[] rawPhrase = null ;
                    try {
                        rawPhrase = BRKeyStore.getPhrase(context, BRConstants.PROVE_PHRASE_REQUEST);
                        System.out.println("rawPhrase"+rawPhrase.toString());
                    } catch (UserNotAuthenticatedException e) {

                    }
                    byte[] txHash = WalletElaManager.getInstance(context).signAndPublishTransaction(tx, rawPhrase);
                    System.out.println("txid1111111："+ txHash);
                    System.out.println("txid1111111："+ txHash.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}
         ).start();
    }
    public void CheckPayPwd(String curpaypwd){
        if(curpaypwd.equals("")){
            if(getlangconfig().equals("cn")){
                Toast.makeText(getApplicationContext(),"当前钱包支付密码不能为空.",Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(getApplicationContext(),"Current wallet payment password cannot be empty.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"当前钱包支付密码不能为空.",Toast.LENGTH_SHORT).show();
            }
            checkpwd = false;
        }
        String pin = BRKeyStore.getPinCode(getApplication());
        if(curpaypwd.equals(pin)){
            checkpwd = true;
        }else{
            Toast.makeText(getApplicationContext(),"支付密码错误.",Toast.LENGTH_SHORT).show();
            checkpwd = false;
        }
    }
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