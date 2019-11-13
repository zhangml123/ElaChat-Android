package com.eladapp.elachat.mysetting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import com.eladapp.elachat.R;

public class AssetsettingActivity extends AppCompatActivity{
    private RelativeLayout editpaypwd_show;
    private RelativeLayout export_phrase_show;
    private RelativeLayout export_keystore_show;
    private RelativeLayout delete_wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assetsetting);
        initview();
        editpaypwd_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(AssetsettingActivity.this, AsseteditpaypwdActivity.class));
            }
        });
        export_keystore_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(AssetsettingActivity.this, AssetOutPutPrivateKeyActivity.class));
            }
        });
        export_phrase_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(AssetsettingActivity.this, AssetoutputmnemonicActivity.class));
            }
        });
        delete_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(AssetsettingActivity.this, AssetDeletWalletActivity.class));
            }
        });
    }
    public void initview(){
        export_phrase_show = (RelativeLayout)findViewById(R.id.export_phrase_show);
        editpaypwd_show = (RelativeLayout)findViewById(R.id.editpaypwd_show);
        export_keystore_show = (RelativeLayout)findViewById(R.id.export_keystore_show);
        delete_wallet = (RelativeLayout)findViewById(R.id.delete_wallet);
    }
    public void back(View view){
        finish();
    }

}