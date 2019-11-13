package com.eladapp.elachat.wallet;

import android.app.Activity;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.os.Looper;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.eladapp.elachat.application.CloudchatApp;

import org.apache.commons.lang.StringUtils;
import org.ela.Elaspv.Elaspvapi;
//import com.elastos.spvcore.ElastosWalletUtils;
//import com.elastos.spvcore.MasterWalletManager;

import com.breadwallet.tools.security.BRKeyStore;
import com.breadwallet.tools.security.FingerprintUiHelper;
import com.breadwallet.tools.util.BRConstants;
import com.breadwallet.wallet.WalletsMaster;
import com.eladapp.elachat.R;
import com.eladapp.elachat.manager.WalletElaManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;





public class WalletcreateonestepActivity extends AppCompatActivity {
    private EditText phrasewrds;
    private Button prvbtn;
    private Button nextbtn;
    private Elaspvapi elaspvapi;
    private String [] phrasearr;
    private TextView prhasewordcurnum;
    //private  MasterWalletManager mWalletManager;
    private Button btn_add_wallet_step_next;
    private String cleanPhrase;
    private boolean authenticated;
    private String [] phrasewordstring;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletcreateonestep);
        prvbtn = (Button)findViewById(R.id.btn_add_wallet_pre_step);
        nextbtn = (Button)findViewById(R.id.btn_add_wallet_next_step);
        phrasewrds = (EditText)findViewById(R.id.prhaseword);
        prhasewordcurnum = (TextView)findViewById(R.id.prhasewordcurnum);
        btn_add_wallet_step_next = (Button)findViewById(R.id.btn_add_wallet_step_next);
        phrasewrds.setEnabled(false);
        phrasewordstring = createprhase();
        if(authenticated){
            phrasewrds.setText(phrasewordstring[0]);
        }
        prvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wordsnum = Integer.parseInt(prhasewordcurnum.getText().toString());
                if(wordsnum>1){
                    nextbtn.setBackgroundColor(Color.parseColor("#438de2"));
                    phrasewrds.setText(phrasewordstring[wordsnum-2]);
                    prhasewordcurnum.setText(String.valueOf(wordsnum-1));
                   // btn_add_wallet_step_next.setVisibility(View.GONE);
                    btn_add_wallet_step_next.setBackgroundColor(Color.parseColor("#e5e5e5"));
                    btn_add_wallet_step_next.setEnabled(false);
                }
                if(wordsnum==1){
                    prvbtn.setBackgroundColor(Color.parseColor("#e5e5e5"));
                    btn_add_wallet_step_next.setEnabled(false);
                }
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int wordsnum = Integer.parseInt(prhasewordcurnum.getText().toString());
                if(wordsnum<12){
                    prvbtn.setBackgroundColor(Color.parseColor("#438de2"));
                    phrasewrds.setText(phrasewordstring[wordsnum]);
                    prhasewordcurnum.setText(String.valueOf(wordsnum+1));
                }
                if(wordsnum==12){
                    nextbtn.setBackgroundColor(Color.parseColor("#e5e5e5"));
                    btn_add_wallet_step_next.setBackgroundColor(Color.parseColor("#0070C9"));
                    //btn_add_wallet_step_next.setVisibility(View.VISIBLE);
                    btn_add_wallet_step_next.setEnabled(true);
                }
            }
        });
        btn_add_wallet_step_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String phrasewordss = StringUtils.join(phrasewordstring, " ");
                startActivity(new Intent(WalletcreateonestepActivity.this, WalletcreatetowstepActivity.class).putExtra("phrasewords",phrasewordss.toString()));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BRConstants.PUT_PHRASE_NEW_WALLET_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                phrasewordstring = createprhase();
                if(authenticated){
                    phrasewrds.setText(phrasewordstring[0]);
                }
            } else {


            }
        }
    }
    //创建钱包、助记词
    public String [] createprhase() {
        System.out.println("createprhase11111");
        boolean success = WalletsMaster.getInstance(this).generateRandomSeed(this);

        System.out.println("success11111"+success);
        if (success) {
            try {
                cleanPhrase = new String(BRKeyStore.getPhrase(this, BRConstants.PROVE_PHRASE_REQUEST));
                System.out.println("cleanPhrase111"+cleanPhrase);
                authenticated = true;
            } catch (UserNotAuthenticatedException e) {
                authenticated = false;
            }
        }
        if(cleanPhrase == null){
            return null;
        }else{
            return cleanPhrase.split(" ");
        }
    }
    public void back(View view){
        finish();
    }
}
