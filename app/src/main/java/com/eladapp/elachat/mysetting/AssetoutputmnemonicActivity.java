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
//import com.eladapp.elachat.application.CloudchatApp;
//import com.elastos.spvcore.IMasterWallet;
//import com.elastos.spvcore.MasterWalletManager;

public class AssetoutputmnemonicActivity extends AppCompatActivity {
    private EditText mnemonicpaypwd;
    private Button outputmnemonicbtn;
    private RelativeLayout mnemoniccurpaypwdout;
    private RelativeLayout outputmnemonicbtnout;
    private RelativeLayout outputmnemonicout;
    private TextView outputmnemonic;
    private Boolean checkpwd = false;
    private String cleanPhrase;
    private LinearLayout copybtn;
    private boolean authenticated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outputmnemonic);
        initview();
        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                ClipData myClip;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", outputmnemonic.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                if(getlangconfig().equals("cn")){
                    Toast.makeText(AssetoutputmnemonicActivity.this,"助记词已复制.", Toast.LENGTH_SHORT).show();
                }else if(getlangconfig().equals("en")){
                    Toast.makeText(AssetoutputmnemonicActivity.this,"Mnemonic words have been copyed.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AssetoutputmnemonicActivity.this,"助记词已复制.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        outputmnemonicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPayPwd(mnemonicpaypwd.getText().toString());
                if(checkpwd){
                    try {
                        cleanPhrase = new String(BRKeyStore.getPhrase(getApplication(), BRConstants.PROVE_PHRASE_REQUEST));
                        System.out.println("cleanPhrase111"+cleanPhrase);
                        outputmnemonic.setText(cleanPhrase);
                        mnemoniccurpaypwdout.setVisibility(View.GONE);
                        outputmnemonicbtnout.setVisibility(View.GONE);
                        outputmnemonicout.setVisibility(View.VISIBLE);
                        authenticated = true;
                    } catch (UserNotAuthenticatedException e) {
                        authenticated = false;
                    }

                }
            }
        });
    }
    public void  initview(){
        mnemonicpaypwd = (EditText)findViewById(R.id.asset_mnemonic_paypwd);
        mnemoniccurpaypwdout = (RelativeLayout)findViewById(R.id.mnemonicpaypwdout);
        outputmnemonicbtnout = (RelativeLayout)findViewById(R.id.mnemonicoutbtn);
        outputmnemonicbtn = (Button) findViewById(R.id.output_mnemonic_btn);
        outputmnemonicout = (RelativeLayout) findViewById(R.id.mnemonic_c_out);
        outputmnemonic = (TextView)findViewById(R.id.mnemonic_c);
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