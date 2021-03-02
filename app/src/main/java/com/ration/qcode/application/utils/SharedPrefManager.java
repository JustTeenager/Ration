package com.ration.qcode.application.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREFS_STORAGE_NAME="prefs_storage_name";
    private static final String URL_KEY="url_key";

    private static SharedPrefManager sManager;
    private final SharedPreferences settings;

    public static SharedPrefManager getManager(Context context){
        if (sManager==null) sManager=new SharedPrefManager(context);
        return sManager;
    }

    private SharedPrefManager (Context context){
        settings=context.getSharedPreferences(PREFS_STORAGE_NAME, Context.MODE_PRIVATE);
    }

    public void setUrl(String str){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString( URL_KEY, str );
        editor.apply();
    }

    public String getUrl(){
        return settings.getString(URL_KEY,null);
    }
}
