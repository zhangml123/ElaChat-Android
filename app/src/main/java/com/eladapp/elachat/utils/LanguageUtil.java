package com.eladapp.elachat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;


public class LanguageUtil {
    private static final String COUNTRY = "COUNTRY";
    private static final String LANGUAGE = "LANGUAGE";
    public static void initLanguage(Context context){
        String name = context.getPackageName() + "_" + LANGUAGE;

        SharedPreferences preferences =

                context.getSharedPreferences(name, Context.MODE_PRIVATE);
        System.out.println("LanguageUtil preferences.getAll().get(\"LANGUAGE\") = "+preferences.getAll().get("LANGUAGE"));
        if(preferences.getAll().get("LANGUAGE") != null && preferences.getAll().get("LANGUAGE").equals("en")){
            changeAppLanguage(context, Locale.ENGLISH, true);
        }else {
            changeAppLanguage(context, Locale.SIMPLIFIED_CHINESE, true);
        }

    }

    /**

     * 更改应用语言

     *

     * @param context

     * @param locale 语言地区

     * @param persistence 是否持久化

     */
    public static void changeAppLanguage(Context context, Locale locale,

                                         boolean persistence) {

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        Configuration configuration = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            configuration.setLocale(locale);

        } else {

            configuration.locale = locale;

        }

        resources.updateConfiguration(configuration, metrics);

        if (persistence) {

            saveLanguageSetting(context, locale);

        }

    }
    private static void saveLanguageSetting(Context context, Locale locale) {

        String name = context.getPackageName() + "_" + LANGUAGE;

        SharedPreferences preferences =

                context.getSharedPreferences(name, Context.MODE_PRIVATE);

        preferences.edit().putString(LANGUAGE, locale.getLanguage()).apply();

        preferences.edit().putString(COUNTRY, locale.getCountry()).apply();

    }

}