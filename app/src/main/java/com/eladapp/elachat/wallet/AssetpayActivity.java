package com.eladapp.elachat.wallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.eladapp.elachat.application.CloudchatApp;
//import com.elastos.spvcore.IMasterWallet;
//import com.elastos.spvcore.ISubWallet;

import com.breadwallet.tools.manager.BRReportsManager;
import com.breadwallet.tools.security.BRKeyStore;
import com.breadwallet.wallet.WalletsMaster;
import com.breadwallet.wallet.abstracts.BaseWalletManager;
import com.eladapp.elachat.R;
import com.eladapp.elachat.manager.WalletElaManager;
import com.eladapp.elachat.utils.QRCodeUtil;

import java.util.ArrayList;
import java.util.List;


public class AssetpayActivity extends AppCompatActivity{
    private static final int IMAGE_HALFWIDTH = 40;
    private Bitmap logo;
    private ImageView asset_qcord;
    private String assetname;
    //private CloudchatApp cloudchatapp;
    private TextView asset_adr;
    //private Map<String, ISubWallet> mSubWalletMap = new HashMap<String, ISubWallet>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assetname = this.getIntent().getStringExtra("assetname");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_receive);
        logo= BitmapFactory.decodeResource(super.getResources(),R.drawable.eladefalut);
        asset_qcord = (ImageView)findViewById(R.id.asset_qcord);
        asset_adr = (TextView)findViewById(R.id.asset_adr);

        //获取地址
        String adr = getassetadr();
        asset_adr.setText(adr);
        asset_adr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                ClipData myClip;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", asset_adr.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                if(getlangconfig().equals("cn")){
                    Toast.makeText(AssetpayActivity.this,"已复制地址.", Toast.LENGTH_SHORT).show();
                }else if(getlangconfig().equals("en")){
                    Toast.makeText(AssetpayActivity.this,"Copied address.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AssetpayActivity.this,"已复制地址.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //生成二维码
        try {
            Bitmap bm = QRCodeUtil.createQRCodeBitmap(adr, 250, 250);
            asset_qcord.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取地址
    public String getassetadr(){
       /* try {
            byte[] address = BRKeyStore.getMasterPublicKey(this);
            System.out.println("addresssss "+new String(address));
        } catch (Exception e) {
            e.printStackTrace();
            BRReportsManager.reportBug(e);
        }*/
        /*WalletsMaster.getInstance(this).updateWallets(this);
        String address = "";
        final BaseWalletManager wm = WalletsMaster.getInstance(this).getCurrentWallet(this);
        if(wm == null) {
            System.out.println("getCurrentWallet "+wm);

        }else{
            address =wm.getAddress();
        }
        System.out.println("address =  "+address);*/

        String address = WalletElaManager.getInstance(this).getAddress();
        System.out.println("address =  "+address);
       // String address = WalletsMaster.getInstance(this).getCurrentWallet(this).getAddress();
        //cloudchatapp = new CloudchatApp();
       // ArrayList<IMasterWallet> Fmastwallet = cloudchatapp.getwalletlist();
       // String adrinfo = Fmastwallet.get(0).GetSubWallet("ELA").GetAllAddress(0,1).toString();
       // JSONObject jsonobj = JSONObject.fromObject(adrinfo);
       // JSONArray jsonobja = JSONArray.fromObject(jsonobj.get("Addresses"));
        return address;
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