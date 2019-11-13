package com.breadwallet.presenter.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.breadwallet.core.BRCoreKey;
import com.eladapp.elachat.R;

public class TestActivity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);



        boolean key = BRCoreKey.isValidBitcoinPrivateKey("asdfadfadfad");
        System.out.println("key1111:"+key);
    }
}
