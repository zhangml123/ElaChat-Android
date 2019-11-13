package com.eladapp.elachat.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.eladapp.elachat.ElachatActivity;
import com.eladapp.elachat.R;


public class WalletActivity extends AppCompatActivity {
    private Button btnCreateWallet;
    private Button btnImportWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_init);
        btnCreateWallet = (Button)findViewById(R.id.btn_create_wallet);
        btnImportWallet = (Button)findViewById(R.id.btn_import_wallet);


        btnCreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getApplication(), WalletcreateonestepActivity.class));
                finish();
            }
        });
        btnImportWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getApplication(), WalletImportActivity.class));
                finish();

            }
        });

    }
    // 捕获返回键的方法1
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下BACK，同时没有重复
            Log.d("WalletActivity", "onKeyDown()");
            startActivity(new Intent().setClass(this, ElachatActivity.class).putExtra("id",0));
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }


}
