package com.eladapp.elachat.mysetting;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.breadwallet.tools.security.BRKeyStore;
import com.breadwallet.tools.util.BRConstants;
import com.eladapp.elachat.R;


public class AssetOutPutPrivateKeyActivity extends AppCompatActivity {
    private EditText privatepaypwd;
    private Button outputprivatebtn;
    private RelativeLayout privatecurpaypwdout;
    private RelativeLayout outputprivatebtnout;
    private RelativeLayout outputprivateout;
    private TextView outputprivate;
    private Boolean checkpwd = false;
    private String privateKey;
    private LinearLayout copybtn;
    private boolean authenticated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_private_key);
        initview();
        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                ClipData myClip;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", outputprivate.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                if(getlangconfig().equals("cn")){
                    Toast.makeText(AssetOutPutPrivateKeyActivity.this,"私钥已复制.", Toast.LENGTH_SHORT).show();
                }else if(getlangconfig().equals("en")){
                    Toast.makeText(AssetOutPutPrivateKeyActivity.this,"PrivateKey words have been copyed.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AssetOutPutPrivateKeyActivity.this,"私钥已复制.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        outputprivatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPayPwd(privatepaypwd.getText().toString());
                if(checkpwd){

                        privateKey = new String(BRKeyStore.getAuthKey(getApplication()));
                        System.out.println("privateKey111"+privateKey);
                        outputprivate.setText(privateKey);
                        privatecurpaypwdout.setVisibility(View.GONE);
                        outputprivatebtnout.setVisibility(View.GONE);
                        outputprivateout.setVisibility(View.VISIBLE);
                        authenticated = true;


                }
            }
        });
    }
    public void  initview(){
        privatepaypwd = (EditText)findViewById(R.id.asset_privatekey_paypwd);
        privatecurpaypwdout = (RelativeLayout)findViewById(R.id.privatekeypaypwdout);
        outputprivatebtnout = (RelativeLayout)findViewById(R.id.privatekeyoutbtn);
        outputprivatebtn = (Button) findViewById(R.id.output_privatekey_btn);
        outputprivateout = (RelativeLayout) findViewById(R.id.privatekey_c_out);
        outputprivate = (TextView)findViewById(R.id.privatekey_c);
        copybtn = (LinearLayout)findViewById(R.id.copybtn);
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
    public void back(View view){
        finish();
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