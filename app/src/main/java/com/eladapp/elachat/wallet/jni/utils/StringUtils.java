package com.eladapp.elachat.wallet.jni.utils;

public class StringUtils {

    public static boolean isNullOrEmpty(String value){
        if(value==null || value.isEmpty()) return true;
        return false;
    }
}
