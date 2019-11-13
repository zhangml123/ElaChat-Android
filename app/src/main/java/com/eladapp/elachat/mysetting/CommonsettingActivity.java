package com.eladapp.elachat.mysetting;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.eladapp.elachat.ElachatActivity;
import com.eladapp.elachat.R;
import com.eladapp.elachat.utils.LanguageUtil;

import java.util.Locale;

public class CommonsettingActivity extends AppCompatActivity{
    private Spinner setlang_spinner;
    private ArrayAdapter<String> mAdapter ;
    private String [] mStringArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonsetting);
        initview();
    }
    public void initview(){
        setlang_spinner = (Spinner)findViewById(R.id.setlang_spinner);
        mStringArray=getResources().getStringArray(R.array.lang_spinngarr);
        //使用自定义的ArrayAdapter
        mAdapter = new LangAdapter(CommonsettingActivity.this,mStringArray);
        //设置下拉列表风格(这句不些也行)
        //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setlang_spinner.setAdapter(mAdapter);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        String langs = String.valueOf(config.locale);
        String cnn = String.valueOf(Locale.CHINA);
        String enn = String.valueOf(Locale.ENGLISH);
        if(langs.equals(cnn)){
            setlang_spinner.setSelection(1,true);
        }else if(langs.equals(enn)){
            setlang_spinner.setSelection(2,true);
        }else{
            setlang_spinner.setSelection(0,true);
        }
        //监听Item选中事件
        setlang_spinner.setOnItemSelectedListener(new ItemSelectedListenerImpl());
    }
    private class ItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position,long arg3) {
            if(position==0){
                setlang_spinner.setSelection(0);
            }else if(position==1){
                setlang_spinner.setSelection(1);
                switchLanguage("cn");
            }else if(position==2){
                setlang_spinner.setSelection(2);
                switchLanguage("en");
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }
    private void switchLanguage(String language){

        if (language.equals("cn"))

            LanguageUtil.changeAppLanguage(this,Locale.SIMPLIFIED_CHINESE,true);
        else
            LanguageUtil.changeAppLanguage(this,Locale.ENGLISH,true);

        finish();
        startActivity(new Intent(CommonsettingActivity.this, ElachatActivity.class));
    }
    public void back(View view){
        finish();
    }
}