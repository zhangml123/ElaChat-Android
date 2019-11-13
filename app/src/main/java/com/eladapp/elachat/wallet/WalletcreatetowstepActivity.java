package com.eladapp.elachat.wallet;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.breadwallet.tools.security.BRKeyStore;
import com.eladapp.elachat.ElachatActivity;
import com.eladapp.elachat.R;
/*import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.chat.ChatActivity;
import com.eladapp.elachat.chat.Fragmentwallet;
import com.elastos.spvcore.ElastosWalletUtils;
import com.elastos.spvcore.IDidManager;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.ISubWallet;
import com.elastos.spvcore.MasterWalletManager;*/

public class WalletcreatetowstepActivity extends AppCompatActivity {
    private EditText paypwd;
    private EditText cpaypwd;
    private Button confirmbtn;

    private String rootpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletcreatetowstep);

        rootpath = getApplicationContext().getFilesDir().getParent();
        final String phrasewords = this.getIntent().getStringExtra("phrasewords");
        paypwd = (EditText)findViewById(R.id.paypwd);
        cpaypwd = (EditText)findViewById(R.id.cpaypwd);
        confirmbtn = (Button)findViewById(R.id.btn_add_wallet_step_confirm);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("confirmbtn1111：");
                boolean rs = checkpwd( paypwd.getText().toString(), cpaypwd.getText().toString());
                if(rs){
                    boolean result =  BRKeyStore.putPinCode(paypwd.getText().toString(),getApplication());
                    startActivity(new Intent(WalletcreatetowstepActivity.this, ElachatActivity.class).putExtra("id",2));
                    finish();
                }
            }
        });
    }
    //检测支付密码
    public boolean checkpwd(String paypwd,String cpaypwd){

        System.out.println("checkpwd1111：");
        boolean paypwdrs = true;
        if(!isLetterDigit(paypwd)){
            if(getlangconfig().equals("cn")){
                 Toast.makeText(WalletcreatetowstepActivity.this,"密码必须包含字母和数字且长度8位到20位.",Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(WalletcreatetowstepActivity.this,"Passwords must contain letters and numbers and be 8 to 20 bits long.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(WalletcreatetowstepActivity.this,"密码必须包含字母和数字且长度8位到20位.",Toast.LENGTH_SHORT).show();
            }
            paypwdrs = false;
        }
        if(!paypwd.equals(cpaypwd)){
            if(getlangconfig().equals("cn")){
                 Toast.makeText(WalletcreatetowstepActivity.this,"支付密码与确认密码不一致.",Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(WalletcreatetowstepActivity.this,"Payment password is inconsistent with confirmation password.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(WalletcreatetowstepActivity.this,"支付密码与确认密码不一致.",Toast.LENGTH_SHORT).show();
            }
            paypwdrs = false;
        }
        return paypwdrs;
    }
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;
        boolean isLetter = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{8,20}$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
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

    // 捕获返回键的方法1
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下BACK，同时没有重复
            startActivity(new Intent().setClass(this, ElachatActivity.class).putExtra("id",0));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}