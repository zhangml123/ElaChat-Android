package com.eladapp.elachat.wallet;


import android.content.Intent;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.breadwallet.core.BRCoreKey;
import com.breadwallet.core.BRCoreMasterPubKey;
import com.breadwallet.tools.manager.BRReportsManager;
import com.breadwallet.tools.manager.BRSharedPrefs;
import com.breadwallet.tools.security.BRKeyStore;
import com.breadwallet.tools.security.SmartValidator;
import com.breadwallet.tools.util.BRConstants;
import com.breadwallet.tools.util.Utils;
import com.breadwallet.wallet.WalletsMaster;
import com.eladapp.elachat.ElachatActivity;
import com.eladapp.elachat.R;

public class WalletImportActivity extends AppCompatActivity {
    private static  String TAG = "WalletImportActivity";
    private Button confirmButton ;
    private EditText phrase;
    private static final int NUMBER_OF_WORDS = 12;
    private static final int LAST_WORD_INDEX = 11;
    private String mCachedPaperKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_import);
        confirmButton = (Button) findViewById(R.id.btn_wallet_import_confirm);
        phrase = (EditText) findViewById(R.id.phrase);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phraseToCheck = getPhrase();
                if (phraseToCheck == null) {
                    Log.e(TAG, "phraseToCheck == null");
                    return;
                }
                String cleanPhrase = SmartValidator.cleanPaperKey(getApplication(), phraseToCheck);
                if (Utils.isNullOrEmpty(cleanPhrase)) {
                    BRReportsManager.reportBug(new NullPointerException("cleanPhrase is null or empty!"));
                    return;
                }
                if (SmartValidator.isPaperKeyValid(getApplication(), cleanPhrase)) {
                    mCachedPaperKey = cleanPhrase;
                    importWallet();
                }
            }
        });
    }

    public void importWallet(){
        if (Utils.isNullOrEmpty(mCachedPaperKey)) {
            Log.e(TAG, "onRecoverWalletAuth: phraseForKeyStore is null or empty");
            BRReportsManager.reportBug(new NullPointerException("onRecoverWalletAuth: phraseForKeyStore is or empty"));
            return;
        }

        try {
            boolean success = false;
            try {
                success = BRKeyStore.putPhrase(mCachedPaperKey.getBytes(),
                        this, BRConstants.PUT_PHRASE_RECOVERY_WALLET_REQUEST_CODE);
            } catch (UserNotAuthenticatedException e) {
            }

            if (!success) {
                    Log.e(TAG, "onRecoverWalletAuth, !success && authAsked");
            } else {
                if (mCachedPaperKey.length() != 0) {
                    BRSharedPrefs.putPhraseWroteDown(this, true);
                    byte[] seed = BRCoreKey.getSeedFromPhrase(mCachedPaperKey.getBytes());
                    byte[] authKey = BRCoreKey.getAuthPrivKeyForAPI(seed);
                    BRKeyStore.putAuthKey(authKey, this);
                    BRCoreMasterPubKey mpk = new BRCoreMasterPubKey(mCachedPaperKey.getBytes(), true);
                    BRKeyStore.putMasterPublicKey(mpk.serialize(), this);
                    mCachedPaperKey = null;
                    startActivity(new Intent(WalletImportActivity.this, WalletcreatetowstepActivity.class).putExtra("id",2));
                    finish();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            BRReportsManager.reportBug(e);
        }

    }

    private String getPhrase() {
        boolean success = true;

        StringBuilder paperKeyStringBuilder = new StringBuilder();
        String parseString = phrase.getText().toString().toLowerCase();
        String[] parseArr = parseString.split(" ");
        for (String parseItem : parseArr  ) {
            String cleanedWords = clean(parseItem);
            if (Utils.isNullOrEmpty(cleanedWords)) {
                success = false;
            } else {
                paperKeyStringBuilder.append(cleanedWords);
                paperKeyStringBuilder.append(' ');
            }
        }
        Log.d(TAG, "getPhrase");
        if (!success) {
            Log.e(TAG, "!success");
            return null;
        }

        // Ensure the paper key is 12 words.
        String paperKey = paperKeyStringBuilder.toString().trim();
        int numberOfWords = paperKey.split(" ").length;
        if (numberOfWords != NUMBER_OF_WORDS) {
            BRReportsManager.reportBug(new IllegalArgumentException("Paper key contains " + numberOfWords + " words"));
            return null;
        }

        return paperKey;
    }
    private String clean(String word) {
        return word.trim().replaceAll(" ", "");
    }

}
