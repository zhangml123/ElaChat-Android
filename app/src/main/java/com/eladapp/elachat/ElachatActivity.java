package com.eladapp.elachat;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.breadwallet.presenter.customviews.BRDialogView;
import com.breadwallet.tools.animation.BRDialog;
import com.breadwallet.tools.security.BRKeyStore;
import com.breadwallet.tools.util.BRConstants;
import com.eladapp.elachat.chat.Fragmentapp;
import com.eladapp.elachat.chat.Fragmentapps;
import com.eladapp.elachat.chat.Fragmentmessage;
import com.eladapp.elachat.chat.Fragmentprofile;
import com.eladapp.elachat.chat.Fragmentwallet;
import com.eladapp.elachat.utils.LanguageUtil;

import java.util.Locale;


public class ElachatActivity extends AppCompatActivity {
    private FragmentTransaction mFragmentTransaction;//fragment事务
    private FragmentManager mFragmentManager;//fragment管理者
    private Fragmentmessage fragmentmessage;
    private Fragmentprofile fragmentprofile;
    private Fragmentwallet fragmentwallet;
    private Fragmentapp fragmentapp;
    private int id;
    private BottomNavigationBar  bottomNavigationBar;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.initLanguage(this);
        setContentView(R.layout.activity_elachat);
        //Intent intent = getIntent();
        id = getIntent().getIntExtra("id", 0);
        System.out.println("ElachatActivity id "+id);
        barinit(id);
        changeFragment(id);
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Activity.KEYGUARD_SERVICE);
        if (!keyguardManager.isKeyguardSecure()) {
            sayNoScreenLock(this);
        }else{
            try {
                BRKeyStore.getCanary(this, BRConstants.CANARY_REQUEST_CODE);
            } catch (UserNotAuthenticatedException e) {
                return;
            }
        }

        String lang = getlangconfig();
        System.out.println("ElaChatActivity lang = "+lang);
        System.out.println("ElaChatActivity lang_message = "+ getResources().getString(R.string.message));
    }
    private Configuration config;
    public String getlangconfig(){
        String lang = "";
        Resources resources = getResources();
        config = resources.getConfiguration();
        if(config.locale.equals(Locale.CHINA)){
            lang = "cn";
        }else if(config.equals(Locale.ENGLISH)){
            lang =  "en";
        }
        return lang;
    }
    private void sayNoScreenLock(final Activity app) {
        BRDialog.showCustomDialog(app,
                "",
                "Prompts_NoScreenLock_body_android",
                "AccessibilityLabels_close",
                null,
                new BRDialogView.BROnClickListener() {
                    @Override
                    public void onClick(BRDialogView brDialogView) {
                        app.finish();
                    }
                }, null, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        app.finish();
                    }
                }, 0);
    }
    private void barinit(int ids){
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .setActiveColor("#2e2e2e")//选中颜色 图标和文字
                .setInActiveColor("#8e8e8e")//默认未选择颜色
                .setBarBackgroundColor("#ECECEC");//默认背景色
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.bottom_message_checked,getResources().getString(R.string.message)).setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.bottom_message_unchecked)))
                .addItem(new BottomNavigationItem(R.drawable.bottom_app_checked,getResources().getString(R.string.dapps)).setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.bottom_app_unchecked)))
                .addItem(new BottomNavigationItem(R.drawable.bottom_asset_checked,getResources().getString(R.string.assets)).setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.bottom_asset_unchecked)))
                .addItem(new BottomNavigationItem(R.drawable.bottom_me_checked,getResources().getString(R.string.profile)).setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.bottom_me_unchecked)))
                .setFirstSelectedPosition(ids)
                .initialise();
        mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransactiona = mFragmentManager.beginTransaction();
        fragmentmessage = new Fragmentmessage();
        mFragmentTransactiona.add(R.id.splash_root, fragmentmessage);
        mFragmentTransactiona.commitAllowingStateLoss();
        bottomNavigationBar
                .setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        changeFragment(position);
                    }
                    @Override
                    public void onTabUnselected(int position) {

                    }

                    @Override
                    public void onTabReselected(int position) {

                    }
                });
    }
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        //如果此fragment不为空的话就隐藏起来
        if (fragmentapp != null) {
            fragmentTransaction.hide(fragmentapp);
        }
        if(fragmentmessage != null) {
            fragmentTransaction.hide(fragmentmessage);
        }
        if (fragmentwallet!= null) {
            fragmentTransaction.hide(fragmentwallet);
        }
        if (fragmentprofile != null) {
            fragmentTransaction.hide(fragmentprofile);
        }
    }
    private void changeFragment(int position){
        mFragmentManager = getFragmentManager();
        //开启事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        //显示之前将所有的fragment都隐藏起来,在去显示我们想要显示的fragment
        hideFragment(mFragmentTransaction);
        bottomNavigationBar.setFirstSelectedPosition(position);
        switch (position){
            case 0:
                System.out.println("changeFragment_message");
                //Toast.makeText(ElawalletaActivity.this,"这是新消息",Toast.LENGTH_SHORT).show();
                if (fragmentmessage == null) {
                    System.out.println("fragmentmessage == null");
                    fragmentmessage = new Fragmentmessage();
                    mFragmentTransaction.add(R.id.splash_root, fragmentmessage);

                } else {
                    System.out.println("fragmentmessage != null");
                    mFragmentTransaction.show(fragmentmessage);
                    // mFragmentTransaction.replace(R.id.splash_root,fragmentmessage).commit();
                }
                break;
            case 1:
                // Toast.makeText(ElawalletaActivity.this,"这是联系人",Toast.LENGTH_SHORT).show();
                if (fragmentapp == null) {
                    fragmentapp = new Fragmentapp();
                    mFragmentTransaction.add(R.id.splash_root, fragmentapp);
                } else {
                    mFragmentTransaction.show(fragmentapp);
                    // mFragmentTransaction.replace(R.id.splash_root,fragmentcontact).commit();
                }
                break;
            case 2:
                //Toast.makeText(ElawalletaActivity.this,"行情研发中",Toast.LENGTH_SHORT).show();
                System.out.println("changeFragment fragmentwallet");
                if (fragmentwallet == null) {
                    fragmentwallet = new Fragmentwallet();
                    mFragmentTransaction.add(R.id.splash_root, fragmentwallet);
                } else {
                    mFragmentTransaction.show(fragmentwallet);
                    //mFragmentTransaction.add(R.id.splash_root, fragmentwallet);
                    //mFragmentTransaction.replace(R.id.splash_root,fragmentwallet).commit();
                }

                break;
            case 3:
                //Toast.makeText(ElachatActivity.this,"应用研发中",Toast.LENGTH_SHORT).show();
                System.out.println("changeFragment fragmentprofile");
                if (fragmentprofile == null) {
                    System.out.println("fragmentprofile == null");
                    fragmentprofile = new Fragmentprofile();
                    mFragmentTransaction.add(R.id.splash_root, fragmentprofile);
                } else {
                    System.out.println("fragmentprofile != null");
                    mFragmentTransaction.show(fragmentprofile);
                    //mFragmentTransaction.replace(R.id.splash_root,fragmentdapps).commit();
                }
                break;
        }

        mFragmentTransaction.commitAllowingStateLoss();
    }
    public void getPermission() {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
