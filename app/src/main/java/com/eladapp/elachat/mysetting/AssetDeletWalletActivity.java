package com.eladapp.elachat.mysetting;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.breadwallet.tools.security.BRKeyStore;
import com.breadwallet.wallet.WalletsMaster;
import com.eladapp.elachat.ElachatActivity;
import com.eladapp.elachat.R;
import com.eladapp.elachat.manager.WalletElaManager;
import com.eladapp.elachat.wallet.WalletcreatetowstepActivity;

public class AssetDeletWalletActivity extends AppCompatActivity {
    private EditText assetDeletePayPwd;
    private Button deleteWalletBtn;
    private Boolean checkpwd = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_wallet);
        initview();
        deleteWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPayPwd(assetDeletePayPwd.getText().toString());
                if(checkpwd){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                WalletsMaster m = WalletsMaster.getInstance(getApplication());
                                m.wipeAll(getApplication());
                                WalletElaManager.getInstance(getApplication()).wipeData(getApplication());
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }}
                    ).start();

                    Toast.makeText(getApplicationContext(),"删除成功.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void  initview(){
        assetDeletePayPwd = (EditText)findViewById(R.id.asset_delete_pay_pwd);
        deleteWalletBtn = (Button) findViewById(R.id.delete_wallet_btn);

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
    public void back(View view){
        finish();
    }

}